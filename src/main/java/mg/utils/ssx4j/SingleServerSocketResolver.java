package mg.utils.ssx4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SingleServerSocketResolver extends AbstractConfigurable implements HostResolverInterface {

	
	
	@Override
	public List<URL> resolve() {
		ArrayList<URL> result = new ArrayList<URL>();
		try {
			result.add(new URL("http://localhost:" + config.getInteger(ConfigInterface.SERVER_SOCKET_PORT)));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

}
