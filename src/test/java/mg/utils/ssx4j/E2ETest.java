package mg.utils.ssx4j;

import org.junit.Test;

public class E2ETest {

	@Test
	public void test() throws Exception {
		InMemoryTestConnector connector = new InMemoryTestConnector();
		Sender sender = new Sender();
		sender.setHostResolver(connector);
		sender.setPoster(connector);
		sender.init();
		Receiver receiver = new Receiver();
		receiver.setGetter(connector);
		receiver.registerCallback("test", connector);
		receiver.init();

		Thread.sleep(2000);

		sender.send("hello world");
		Thread.sleep(6000);
	}

}
