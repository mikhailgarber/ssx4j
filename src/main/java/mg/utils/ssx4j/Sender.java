package mg.utils.ssx4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Sender implements SenderInterface, ReportingInterface, LifecycleInterface {

	private static final int UPDATE_ENDPOINTS_INTERVAL_SECONDS = 1;
	private static final int INITIAL_DELAY_SECONDS = 1;
	
	// collaborators
	private HostResolverInterface hostResolver;
	private LoggingInterface logger = new SystemLogger();
	private PosterInterface poster;

	// internal
	private List<URL> endpoints = new ArrayList<URL>();
	private ScheduledThreadPoolExecutor endpointUpdater;
	private ExecutorService senderQueueReader;

	public void setHostResolver(HostResolverInterface hostResolver) {
		this.hostResolver = hostResolver;
	}

	public void setLogger(LoggingInterface logger) {
		this.logger = logger;
	}
	
	

	public void setPoster(PosterInterface poster) {
		this.poster = poster;
	}

	public void send(final String data) {

		logger.info("sending:" + data);
		senderQueueReader.submit(new Runnable() {

			public void run() {

				logger.info("going thru endpoints:" + endpoints);

				synchronized (endpoints) {
					for (URL endpoint : endpoints) {
						PostingStream stream = streams.get(endpoint);
						if (stream == null) {
							continue;
						}
						try {
							stream.post(data);
						} catch (Exception ex) {
							logger.warn("posting to stream", ex);
						}

					}
				}

			}
		});
	}

	public Map<String, Long> getStats() {
		Map<String, Long> stats = new HashMap<String, Long>();
		stats.put("SENDER_QUEUE_SIZE", getSenderQueueSize());
		return stats;
	}

	private Long getSenderQueueSize() {
		return new Long(((ThreadPoolExecutor) this.endpointUpdater).getQueue().size());
	}

	public void init() {
		if (this.hostResolver == null || this.logger == null || this.poster == null) {
			throw new IllegalStateException("incomplete configuration");
		}
		endpointUpdater = new ScheduledThreadPoolExecutor(8);
		endpointUpdater.scheduleWithFixedDelay(new Runnable() {

			public void run() {
				updateEndpointsPeriodically();
			}
		}, INITIAL_DELAY_SECONDS, UPDATE_ENDPOINTS_INTERVAL_SECONDS, TimeUnit.SECONDS);
		senderQueueReader = Executors.newFixedThreadPool(20);
		logger.info("inited");
	}

	protected void updateEndpointsPeriodically() {
		logger.info("updating endpoints");
		try {
			synchronized (endpoints) {
				endpoints.clear();
				endpoints.addAll(hostResolver.resolve(null));
				configurePosters();
			}
		} catch (Exception ex) {
			logger.error("updatign endpoints", ex);
		}
	}

	private Map<URL, PostingStream> streams = new HashMap<URL, PostingStream>();

	private void configurePosters() {
		for (URL endpoint : endpoints) {
			PostingStream stream = streams.get(endpoint);
			if (stream == null || !stream.isValid()) {
				streams.remove(endpoint);
				stream = poster.createStream(endpoint);
				streams.put(endpoint, stream);
			}
		}
	}

	
	public void destroy() {
		endpointUpdater.shutdownNow();
		senderQueueReader.shutdownNow();
	}

}
