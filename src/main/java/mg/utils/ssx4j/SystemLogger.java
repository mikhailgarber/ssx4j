package mg.utils.ssx4j;

import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;

public class SystemLogger implements LoggingInterface {

	public void info(String msg) {
		System.out.println(String.format("%s,INFO,%s", new Date(), msg));
	}

	public void warn(String msg, Throwable th) {
		System.out.println(String.format("%s,WARN,%s,%s", new Date(), msg, ExceptionUtils.getStackTrace(th)));
	}

	public void warn(String msg) {
		System.out.println(String.format("%s,WARN,%s", new Date(), msg));
	}

	public void error(String msg, Throwable th) {
		System.err.println(String.format("%s,ERROR,%s,%s", new Date(), msg, ExceptionUtils.getStackTrace(th)));
	}

	public void error(String msg) {
		System.err.println(String.format("%s,ERROR,%s", new Date(), msg));
	}

}
