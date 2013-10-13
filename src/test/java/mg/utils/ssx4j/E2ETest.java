package mg.utils.ssx4j;

import org.junit.Assert;
import org.junit.Test;

public class E2ETest {

	@Test
	public void test() throws Exception {
		TestCallback callback = new TestCallback();
		Sender sender = new Sender();
		sender.setHostResolver(new SingleServerSocketResolver());
		sender.setPoster(new SocketPoster());
		sender.init();
		Receiver receiver = new Receiver();
		receiver.registerCallback("test", callback);
		receiver.init();
		SocketReceiver socketServer = new SocketReceiver();
		socketServer.setReceiver(receiver);
		socketServer.init();

		Thread.sleep(100);

		for (int i = 0; i < 4096; i++)
			sender.send("hello world");

		Thread.sleep(1000);
		System.out.println("stats:" + sender.getStats());
		Assert.assertEquals("got:" + callback.getHelloCount(), 4096, callback.getHelloCount());
	}

}
