package mg.utils.ssx4j;

public class TestCallback implements ReceiverCallbackInterface {

	private int cntHello = 0;

	@Override
	public void received(String data) {
		System.out.println("GOT:" + data);
		if (data.compareToIgnoreCase("hello world") == 0) {
			cntHello++;
		}
	}

	public int getHelloCount() {
		return cntHello;
	}

}
