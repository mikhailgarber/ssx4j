package mg.utils.ssx4j;

import java.io.DataOutputStream;
import java.net.Socket;
import java.net.URL;

public class SocketPoster implements PosterInterface {

	@Override
	public PostingStream createStream(URL endpoint) {
		try {
			return new PostingStream(new DataOutputStream(new Socket(endpoint.getHost(), endpoint.getPort()).getOutputStream()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
