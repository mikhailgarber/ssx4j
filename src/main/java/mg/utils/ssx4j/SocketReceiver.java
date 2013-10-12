package mg.utils.ssx4j;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.IOUtils;

public class SocketReceiver extends AbstractPoolable {

	ServerSocket serverSocket;
	Thread serverThread;

	ReceiverInterface receiver;
	
	
	public void setReceiver(ReceiverInterface receiver) {
		this.receiver = receiver;
	}

	@Override
	public void init() {
		super.init();
		if(this.receiver == null) {
			throw new IllegalStateException("receiver is missing");
		}
		try {
			serverSocket = new ServerSocket(config.getInteger(ConfigInterface.SERVER_SOCKET_PORT));
			serverThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					pool.execute(new Runnable() {
						
						@Override
						public void run() {
							try {
								handleClient(serverSocket.accept());
							} catch (IOException e) {
								logger.warn("serving client", e);
							}							
						}

						
					});
					
				}
			});
			serverThread.start();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
	}

	private void handleClient(Socket socket) throws IOException {
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(socket.getInputStream());
			String data = null;
			while(true) {
				data = dis.readUTF();
				receiver.receive(data);
			}
		} finally {
			IOUtils.closeQuietly(dis);
		}
	}
	
	@Override
	public void destroy() {
		try {
			serverSocket.close();
			serverThread.interrupt();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		super.destroy();
	}

	@Override
	public int getPoolSize() {
		return config.getInteger(ConfigInterface.SOCKET_POOL_SIZE);
	}

}
