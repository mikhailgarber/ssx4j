package mg.utils.ssx4j;

import org.apache.log4j.Logger;

public class Log4JLogger implements LoggingInterface {

	
	private Logger logger;
	
	public Log4JLogger(Class<?> clazz) {
		super();
		logger = Logger.getLogger(clazz);
	}

	@Override
	public void info(String msg) {
			logger.info(msg);
	}

	@Override
	public void warn(String msg, Throwable th) {
		logger.warn(msg, th);

	}

	@Override
	public void warn(String msg) {
		logger.warn(msg);

	}

	@Override
	public void error(String msg, Throwable th) {
		logger.error(msg, th);

	}

	@Override
	public void error(String msg) {
		logger.error(msg);
	}

}
