package mg.utils.ssx4j;

public interface ConfigInterface {
	String SERVER_SOCKET_PORT = "serverSocketPort";
	String SOCKET_POOL_SIZE = "socketPoolSize";
	
	public String getString(String name);
	public Integer getInteger(String name);
}
