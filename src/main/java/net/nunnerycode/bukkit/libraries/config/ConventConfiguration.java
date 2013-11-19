package net.nunnerycode.bukkit.libraries.config;

import java.io.InputStream;
import org.bukkit.configuration.file.FileConfiguration;

public interface ConventConfiguration {

    boolean load();

    boolean save();

    void setDefaults(InputStream inputStream);

	@Deprecated
	void saveDefaults(InputStream inputStream);

	boolean needToUpdate();

	boolean backup();

	String getName();

	FileConfiguration getFileConfiguration();

	String getVersion();

}
