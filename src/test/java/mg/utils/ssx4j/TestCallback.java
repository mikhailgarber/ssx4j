package mg.utils.ssx4j;

public class TestCallback implements ReceiverCallbackInterface {

	@Override
	public void received(String data) {
		System.out.println("GOT:" + data);

	}

}
