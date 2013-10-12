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
		return null;
	}

}
