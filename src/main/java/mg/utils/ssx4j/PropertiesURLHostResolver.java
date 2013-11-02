package mg.utils.ssx4j;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * 
 * format of property file:
 * 
 * url_count = X
 * url_0 = url
 * url_1 = url
 * url_X-1 = url
 * 
 * @author mgarber
 *
 */
public class PropertiesURLHostResolver extends AbstractConfigurable implements HostResolverInterface {

	
	private List<URL> cachedList = null;
	private long lastLoaded = 0L;
	
	private String defaultPropertiesUrl = null;
	
	
	
	public void setDefaultPropertiesUrl(String defaultPropertiesUrl) {
		this.defaultPropertiesUrl = defaultPropertiesUrl;
	}


	@Override
	public List<URL> resolve(String givenPropertiesUrl) { 
		
		String propertiesUrl = (givenPropertiesUrl == null)? this.defaultPropertiesUrl : givenPropertiesUrl;
		
		if(cacheExpired()) {
			cachedList = null;
		}
		if(cachedList != null) {
			return cachedList;
		}
		InputStream is = null;
		List<URL> result = new ArrayList<URL>();
		try {
			URLConnection connection = new URL(propertiesUrl).openConnection();
			connection.connect();
			Properties p = new Properties();
			is = connection.getInputStream();
			p.load(is);
			
			int cnt = Integer.parseInt(p.getProperty("url_count"));
			for(int i=0; i<cnt; i++) {
				result.add(new URL(p.getProperty("url_" + i)));
			}
			lastLoaded = System.currentTimeMillis();
			cachedList = result;
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}


	private boolean cacheExpired() {
		return (System.currentTimeMillis() - lastLoaded > config.getInteger(ConfigInterface.URL_RELOAD_SECONDS));
	}

}
