package net.nunnerycode.bukkit.libraries.config;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public interface ConventConfiguration {

	boolean load();

	boolean save();

	File getFile();

	boolean needToUpdate();

	boolean backup();

	String getName();

	FileConfiguration getFileConfiguration();

	String getVersion();

	long getLastModified();

	void set(String path, Object value);

	Object get(String path, Object def);

}
