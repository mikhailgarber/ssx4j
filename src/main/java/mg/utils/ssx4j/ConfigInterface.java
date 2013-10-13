package mg.utils.ssx4j;

public interface ConfigInterface {
	String SERVER_SOCKET_PORT = "serverSocketPort";
	String SOCKET_POOL_SIZE = "socketPoolSize";
	String SENDER_POOL_SIZE = "senderPoolSize";
	String UPDATE_ENDPOINTS_INTERVAL_SECONDS = "senderUpdateEndpointsIntervalSeconds";
	String RECEIVER_POOL_SIZE = "receiverPoolSize";
	
	public String getString(String name);
	public Integer getInteger(String name);
}
