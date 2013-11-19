package net.nunnerycode.bukkit.libraries.config;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;

public interface ConventConfiguration {

	boolean load();

	boolean save();

	File getFile();

	boolean needToUpdate();

	boolean backup();

	boolean reload();

	String getName();

	FileConfiguration getFileConfiguration();

	String getVersion();

}
