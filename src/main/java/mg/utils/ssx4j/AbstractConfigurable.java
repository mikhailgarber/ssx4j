package mg.utils.ssx4j;


public abstract class AbstractConfigurable implements LifecycleInterface {

	protected ConfigInterface config = new DefaultConfig();

	public void setConfig(ConfigInterface config) {
		this.config = config;
	}

	@Override
	public void init() {
		
		if (this.config == null) {
			throw new IllegalStateException("incomplete configuration");
		}
		
		
	}
	
	@Override
	public void destroy() {
		
	}

}
