package mg.utils.ssx4j;

import org.junit.Test;

public class E2ETest {

	@Test
	public void test() throws Exception {
		
		Sender sender = new Sender();
		sender.setHostResolver(new SingleServerSocketResolver());
		sender.setPoster(new SocketPoster());
		sender.init();
		Receiver receiver = new Receiver();		
		receiver.registerCallback("test", new TestCallback());
		receiver.init();
		SocketReceiver socketServer = new SocketReceiver();
		socketServer.setReceiver(receiver);
		socketServer.init();

		Thread.sleep(2000);

		sender.send("hello world");
		sender.send("hello world");
		
		Thread.sleep(6000);
	}

}
