package mg.utils.ssx4j;

import org.junit.Assert;
import org.junit.Test;

public class E2ETest {

	@Test
	public void testSimple() throws Exception {
		
		final int NUMBER_OF_STRINGS_TO_WRITE = 4096;
		
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

		Thread.sleep(1000);

		
		for (int i = 0; i < NUMBER_OF_STRINGS_TO_WRITE; i++)
			sender.send("hello world");
		System.out.println("stats:" + sender.getStats());
		Thread.sleep(3000);
		
		Assert.assertEquals("got:" + callback.getHelloCount(), NUMBER_OF_STRINGS_TO_WRITE, callback.getHelloCount());
	}

}
