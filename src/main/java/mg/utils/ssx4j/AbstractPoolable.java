package mg.utils.ssx4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AbstractPoolable  extends AbstractConfigurable implements ReportingInterface {

	protected ExecutorService pool;

	protected LoggingInterface logger = new SystemLogger();
	
	

	public void setLogger(LoggingInterface logger) {
		this.logger = logger;
	}

	@Override
	public Map<String, Long> getStats() {
		// TODO Auto-generated method stub
		return null;
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
