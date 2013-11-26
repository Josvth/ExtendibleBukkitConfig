package net.nunnerycode.bukkit.libraries.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

public class ConventYamlConfigurationOptions extends YamlConfigurationOptions {
	private boolean updateOnLoad = true;
	private boolean backupOnUpdate = false;
	private boolean createDefaultFile = true;
	private boolean autoReload = false;

	protected ConventYamlConfigurationOptions(YamlConfiguration configuration) {
		super(configuration);
	}

	public boolean updateOnLoad() {
		return updateOnLoad;
	}

	public boolean backupOnUpdate() {
		return backupOnUpdate;
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

	public void backupOnUpdate(boolean b) {
		backupOnUpdate = b;
	}

	public void createDefaultFile(boolean b) {
		createDefaultFile = b;
	}

	public void autoReload(boolean b) {
		autoReload = b;
	}
}
