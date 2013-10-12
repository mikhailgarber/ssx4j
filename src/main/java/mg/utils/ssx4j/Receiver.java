package mg.utils.ssx4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver implements ReportingInterface,
		LifecycleInterface {

	// collaborators
	private LoggingInterface logger = new SystemLogger();
	private GetterInterface getter;
	
	
	
	public void setGetter(GetterInterface getter) {
		this.getter = getter;
	}

	public void setLogger(LoggingInterface logger) {
		this.logger = logger;
	}
	
	

	private Map<String, ReceiverCallbackInterface> callbacks = new HashMap<String, ReceiverCallbackInterface>();

	public void registerCallback(String id, ReceiverCallbackInterface callback) {
		callbacks.put(id, callback);
	}

	public void removeCallback(String id) {
		callbacks.remove(id);
	}

	

	private Thread worker;

	public void init() {

		worker = new Thread(new Runnable() {

			public void run() {
				while (true) {
					DataInputStream stream = getter.getStream();
					try {
						String s = stream.readUTF();
						for (Entry<String, ReceiverCallbackInterface> entry : callbacks
								.entrySet()) {
							submitWork(entry.getValue(), s);
						}
					} catch (Exception e) {
						logger.warn("receiving", e);
						stream = getter.getStream();
					}

				}
			}
		});
		worker.start();

		receiverQueueReader = Executors.newFixedThreadPool(20);
		logger.info("inited");

	}

	private ExecutorService receiverQueueReader;

	protected void submitWork(final ReceiverCallbackInterface callback, final String s) {
		receiverQueueReader.submit(new Runnable() {

			public void run() {
				callback.received(s);				
			}});

	}

	public void destroy() {
		worker.interrupt();

	}

	public Map<String, Long> getStats() {
		// TODO Auto-generated method stub
		return null;
	}

}
