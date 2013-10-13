package mg.utils.ssx4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Receiver extends AbstractPoolable implements ReceiverInterface {

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

			for (Entry<String, ReceiverCallbackInterface> entry : callbacks.entrySet()) {
				submitWork(entry.getValue(), data);
			}
		} catch (Exception e) {
			logger.warn("receiving", e);

		}
	}

	public void init() {

		super.init();
		logger.info("inited");

	}

	protected void submitWork(final ReceiverCallbackInterface callback, final String s) {
		pool.execute(new Runnable() {

			public void run() {
				callback.received(s);
			}
		});

	}

	public void destroy() {
		super.destroy();

	}

	@Override
	public int getPoolSize() {
		return config.getInteger(ConfigInterface.RECEIVER_POOL_SIZE);
	}

}
