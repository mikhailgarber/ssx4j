package mg.utils.ssx4j;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.IOUtils;

public class PostingStream implements LifecycleInterface, Runnable {

	private Thread worker;
	private BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	private final DataOutputStream stream;
	private boolean valid = true;
	private LoggingInterface logger = new Log4JLogger(getClass());
	

	public PostingStream(DataOutputStream stream) {
		super();
		this.stream = stream;
		worker = new Thread(this);
		worker.start();
	}

	public void init() {

	}

	public void destroy() {
		IOUtils.closeQuietly(stream);
		if (!worker.isInterrupted()) {
			worker.interrupt();
		}
	}

	public void run() {
		try {
			while (true) {
				logger.info("waiting on queue");
				String data = queue.take();
				try {
					stream.writeUTF(data);
					stream.flush();
					logger.info("wrote to stream");
				} catch (IOException ioe) {
					queue.add(data);
					throw ioe;
				}
			}
		} catch (Exception ex) {
			destroy();
			valid = false;
		}

	}

	public void post(String data) {
		if (!valid) {
			throw new IllegalStateException("poster is not valid");
		}
		this.queue.add(data);
		logger.info("posted");
	}

	public boolean isValid() {
		return valid;
	}

	@Override
	public String toString() {
		return "PostingStream [valid=" + valid + "]";
	}

	
}
