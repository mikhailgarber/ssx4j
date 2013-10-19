package mg.utils.ssx4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;



import org.junit.Assert;
import org.junit.Test;

public class URLPropertiesResolverTest {

	@Test
	public void test() throws IOException {
		HostResolverInterface resolver = new PropertiesURLHostResolver();
		
		List<URL> urls = resolver.resolve("file:///" + new File(".").getCanonicalPath() + "/src/test/java/url_host_resolver_test.properties");
		System.out.println(urls);
		Assert.assertEquals(1, urls.size());
	}

}
