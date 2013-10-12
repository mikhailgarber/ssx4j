package mg.utils.ssx4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTestConnector implements HostResolverInterface, PosterInterface, GetterInterface, ReceiverCallbackInterface {

	PipedOutputStream pos = new PipedOutputStream();
	
	@Override
	public DataInputStream getStream() {
		try {
			return new DataInputStream(new PipedInputStream(pos));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public PostingStream createStream(URL endpoint) {
		DataOutputStream daos = new DataOutputStream(pos);
		PostingStream stream = new PostingStream(daos);
		return stream;
	}

	@Override
	public List<URL> resolve(String context) {
		List<URL> list = new ArrayList<URL>();
		try {
			list.add(new URL("file://"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void received(String data) {
		System.out.println("GOT:" + data);
		
	}

}
