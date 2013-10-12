package mg.utils.ssx4j;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.exception.ExceptionUtils;

public class SystemLogger implements LoggingInterface {

	private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
	
	public void info(String msg) {
		System.out.println(String.format("%s,INFO,%s", getTimestamp(), msg));
	}

	private String getTimestamp() {
		return format.format(new Date()); 
	}

	public void warn(String msg, Throwable th) {
		System.out.println(String.format("%s,WARN,%s,%s", getTimestamp(), msg, ExceptionUtils.getStackTrace(th)));
	}

	public void warn(String msg) {
		System.out.println(String.format("%s,WARN,%s", getTimestamp(), msg));
	}

	public void error(String msg, Throwable th) {
		System.err.println(String.format("%s,ERROR,%s,%s", getTimestamp(), msg, ExceptionUtils.getStackTrace(th)));
	}

	public void error(String msg) {
		System.err.println(String.format("%s,ERROR,%s", getTimestamp(), msg));
	}

}
