package com.conventnunnery.libraries.config;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.InputStream;

public interface ConventConfiguration extends Configuration {

    boolean load();

    boolean save();

    void setDefaults(InputStream inputStream);

	boolean needToUpdate();

	boolean backup();

	String getName();

	String getVersion();

}
