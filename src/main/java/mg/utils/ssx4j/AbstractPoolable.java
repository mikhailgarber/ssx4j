package mg.utils.ssx4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractPoolable  extends AbstractConfigurable implements ReportingInterface {

	protected ExecutorService pool;

	protected LoggingInterface logger = new Log4JLogger(getClass());
	
	

	public void setLogger(LoggingInterface logger) {
		this.logger = logger;
	}

	@Override
	public Map<String, Long> getStats() {
		Long pooSize = new Long(((ThreadPoolExecutor) this.pool).getQueue().size());
		Map<String, Long> result = new HashMap<String, Long>();
		result.put("POOL_QUEUE_SIZE", pooSize);
		return result;
	}

	@Override
	public void init() {
		
		if (this.config == null || this.logger == null) {
			throw new IllegalStateException("incomplete configuration");
		}
		
		try {

			pool = Executors.newFixedThreadPool(getPoolSize());
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		logger.info("inited");
	}

	@Override
	public void destroy() {

		pool.shutdownNow();
		logger.info("destroyed");
	}

	public abstract int getPoolSize();

}
