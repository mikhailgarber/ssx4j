package mg.utils.ssx4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver implements ReportingInterface,
		LifecycleInterface, ReceiverInterface {

	// collaborators
	private LoggingInterface logger = new Log4JLogger(getClass());
	
	

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

	@Override
	public void receive(String data) {
		try {
			
			for (Entry<String, ReceiverCallbackInterface> entry : callbacks
					.entrySet()) {
				submitWork(entry.getValue(), data);
			}
		} catch (Exception e) {
			logger.warn("receiving", e);
			
		}
	}
	

	public void init() {


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
		

	}

	public Map<String, Long> getStats() {
		// TODO Auto-generated method stub
		return null;
	}

}
