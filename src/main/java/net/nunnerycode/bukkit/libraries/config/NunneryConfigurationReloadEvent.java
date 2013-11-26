package net.nunnerycode.bukkit.libraries.config;

public class NunneryConfigurationReloadEvent extends NunneryConfigurationEvent {

	private final NunneryConfiguration configuration;

	public NunneryConfigurationReloadEvent(NunneryConfiguration configuration) {
		this.configuration = configuration;
	}

	public NunneryConfiguration getConfiguration() {
		return configuration;
	}

}
