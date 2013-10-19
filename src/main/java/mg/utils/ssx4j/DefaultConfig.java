package mg.utils.ssx4j;

public class DefaultConfig implements ConfigInterface {

	@Override
	public String getString(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getInteger(String name) {
		if(ConfigInterface.SERVER_SOCKET_PORT.equals(name)) {
			return 2203;
		}
		if(ConfigInterface.SOCKET_POOL_SIZE.equals(name)) {
			return 20;
		}
		if(ConfigInterface.SENDER_POOL_SIZE.equals(name)) {
			return 20;
		}
		if(ConfigInterface.RECEIVER_POOL_SIZE.equals(name)) {
			return 20;
		}
		if(ConfigInterface.URL_RELOAD_SECONDS.equals(name)) {
			return 5;
		}
		if(ConfigInterface.UPDATE_ENDPOINTS_INTERVAL_SECONDS.equals(name)) {
			return 1;
		}
		return null;
	}

}
