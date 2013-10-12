package mg.utils.ssx4j;

public interface LoggingInterface {
	public void info(String msg);
	public void warn(String msg, Throwable th);
	public void warn(String msg);
	public void error(String msg, Throwable th);
	public void error(String msg);
	
}
