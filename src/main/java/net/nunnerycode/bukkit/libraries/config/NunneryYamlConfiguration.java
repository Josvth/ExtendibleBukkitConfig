package net.nunnerycode.bukkit.libraries.config;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class NunneryYamlConfiguration extends YamlConfiguration implements NunneryConfiguration {

	private final File file;
	private final String version;
	private NunneryYamlConfigurationOptions options;
	private long lastModified = 0;

	public NunneryYamlConfiguration(File file) {
		this(file, null);
	}

	public NunneryYamlConfiguration(File file, String version) {
		super();

		this.file = file;
		this.version = version;
		this.lastModified = this.file.lastModified();
	}

	@Override
	public boolean load() {
		return load(options().updateOnLoad(), options().createDefaultFile());
	}

	public boolean load(boolean update, boolean createDefaultFile) {
		try {
			if (file.exists()) {
				if (needToUpdate() && update) {
					update();
				}
				load(file);
			} else if (createDefaultFile) {
				options().copyDefaults(true);
				return save();
			}
		} catch (Exception e) {
		 	// do nothing
		}
		return true;
	}

	public boolean update() {
		if (options().backupOnUpdate()) {
			if (!backup()) {
				return false;
			}
		}

		options().copyDefaults(true);

		return save();
	}

	@Override
	public NunneryYamlConfigurationOptions options() {
		if (options == null) {
			options = new NunneryYamlConfigurationOptions(this);
		}
		return options;
	}

	@Override
	public boolean save() {
		try {
			file.getParentFile().mkdirs();
			save(file);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public boolean needToUpdate() {
		return getString("version") == null || (version != null && !version.equalsIgnoreCase(getString("version")));
	}

	@Override
	public boolean backup() {
		File backup = new File(file.getParent(), file.getName().replace(".yml", "_old.yml"));

		try {
			backup.getParentFile().mkdirs();
			save(backup);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void set(String path, Object value) {
		super.set(path, value);
		lastModified = System.currentTimeMillis();
	}

	@Override
	public Object get(String path, Object def) {
		if (lastModified <= this.file.lastModified() && options().autoReload()) {
			load();
		}
		return super.get(path, def);
	}

	@Override
	public FileConfiguration getFileConfiguration() {
		return this;
	}

	@Override
	public String getVersion() {
		return version != null ? version : "";
	}

	@Override
	public long getLastModified() {
		return lastModified;
	}

	@Override
	public String getName() {
		return file != null ? file.getName() : "";
	}

	public static NunneryYamlConfiguration loadConfiguration(File file) {
		Validate.notNull(file, "File cannot be null");

		NunneryYamlConfiguration c = new NunneryYamlConfiguration(file);
		c.load();

		return c;
	}

}
