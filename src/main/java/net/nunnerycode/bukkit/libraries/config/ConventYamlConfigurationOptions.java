package net.nunnerycode.bukkit.libraries.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class ConventYamlConfigurationOptions extends YamlConfigurationOptions {
	private boolean updateOnLoad = true;
	private boolean backupOnLoad = false;
	private boolean createDefaultFile = true;
	private boolean autoReload = false;

	protected ConventYamlConfigurationOptions(YamlConfiguration configuration) {
		super(configuration);
	}

	public boolean updateOnLoad() {
		return updateOnLoad;
	}

	public boolean backupOnLoad() {
		return backupOnLoad;
	}

	public boolean createDefaultFile() {
		return createDefaultFile;
	}

	public boolean autoReload() {
		return autoReload;
	}

	public void updateOnLoad(boolean b) {
		updateOnLoad = b;
	}

	public void backupOnLoad(boolean b) {
		backupOnLoad = b;
	}

	public void createDefaultFile(boolean b) {
		createDefaultFile = b;
	}

	public void autoReload(boolean b) {
		autoReload = b;
	}
}
