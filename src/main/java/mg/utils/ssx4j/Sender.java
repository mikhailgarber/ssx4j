package mg.utils.ssx4j;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Sender extends AbstractPoolable implements SenderInterface {

	
	
	
	// collaborators
	private HostResolverInterface hostResolver;
	
	private PosterInterface poster;

	// internal
	private List<URL> endpoints = new ArrayList<URL>();
	private ScheduledThreadPoolExecutor endpointUpdater;
	
	private long cntSent = 0L;
	private long startup = System.currentTimeMillis();

	public void setHostResolver(HostResolverInterface hostResolver) {
		this.hostResolver = hostResolver;
	}

	

	public void setPoster(PosterInterface poster) {
		this.poster = poster;
	}

	public void send(final String data) {

		logger.info("sending:" + data);
		pool.execute(new Runnable() {

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
							cntSent++;
						} catch (Exception ex) {
							logger.warn("posting to stream", ex);
						}

					}
				}

			}
		});
	}

	public Map<String, Long> getStats() {
		Map<String, Long> stats = super.getStats();
		stats.put("SENDER_RATE", cntSent * 1000 / (System.currentTimeMillis() - startup));
		return stats;
	}

	

	public void init() {
		super.init();
		if (this.hostResolver == null || this.logger == null || this.poster == null) {
			throw new IllegalStateException("incomplete configuration");
		}
		endpointUpdater = new ScheduledThreadPoolExecutor(1);
		endpointUpdater.scheduleWithFixedDelay(new Runnable() {

			public void run() {
				updateEndpointsPeriodically();
			}
		}, 0L, config.getInteger(ConfigInterface.UPDATE_ENDPOINTS_INTERVAL_SECONDS), TimeUnit.SECONDS);
		
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
		super.destroy();
		endpointUpdater.shutdownNow();
		for(Entry<URL, PostingStream> entry : streams.entrySet()) {
			entry.getValue().destroy();
		}
	}

	@Override
	public int getPoolSize() {
		return config.getInteger(ConfigInterface.SENDER_POOL_SIZE);
	}

}
