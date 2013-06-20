/*
 * Copyright (c) 2013. ToppleTheNun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.conventnunnery.libraries.config;

import com.conventnunnery.libraries.config.ConventConfigurationFile;
import com.conventnunnery.libraries.config.ConventYamlConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class ConventConfigurationManager {

    private final Map<ConventConfigurationFile, ConventYamlConfiguration> configurations;
    private final Set<ConventConfigurationFile> configurationFiles;
    private final Plugin plugin;

    /**
     * Instantiates a new version of the configuration manager for a plugin
     * @param plugin Plugin that is using the manager
     * @param configurationFiles Set of ConventConfigurationFiles this plugin uses
     */
    public ConventConfigurationManager(Plugin plugin, Set<ConventConfigurationFile> configurationFiles) {
        this.plugin = plugin;
        
        this.configurationFiles = configurationFiles;
        configurations = new HashMap<ConventConfigurationFile, ConventYamlConfiguration>();

        loadConfig();
    }

    private void createConfig(ConventConfigurationFile config) {
        ConventYamlConfiguration file = new ConventYamlConfiguration(
                new File(plugin.getDataFolder(), config.getFileName()));
        saveDefaults(file, config);
        configurations.put(config, file);
    }

    /**
     * Returns the com.conventnunnery.libraries.config.ConventYamlConfiguration that enables access to the file itself.
     * @param file The file to return
     * @return Object allowing access to the file itself
     */
    public ConventYamlConfiguration getConfiguration(ConventConfigurationFile file) {
        return configurations.get(file);
    }

    /**
     * Loads the plugin's configuration files
     */
    public final void loadConfig() {
        for (ConventConfigurationFile file : configurationFiles) {
            File confFile = new File(plugin.getDataFolder(), file.getFileName());
            if (confFile.exists()) {
                ConventYamlConfiguration config = new ConventYamlConfiguration(
                        confFile);
                config.load();
                if (needToUpdate(config, file)) {
                    plugin.getLogger().info("Backing up " + file.getFileName());
                    backup(file);
                    plugin.getLogger().info("Updating " + file.getFileName());
                    saveDefaults(config, file);
                }
                configurations.put(file, config);
            } else {
                File parentFile = confFile.getParentFile();
                if (!parentFile.exists()) {
                    boolean mkdirs = parentFile.mkdirs();
                    if (!mkdirs) {
                        continue;
                    }
                }
                createConfig(file);
            }
        }
    }

    /**
     * Saves the plugin's configs
     */
    public final void saveConfig() {
        for (ConventConfigurationFile file : configurationFiles) {
            if (configurations.containsKey(file)) {
                try {
                    configurations.get(file).save(
                            new File(plugin.getDataFolder(), file.getFileName()));
                } catch (IOException e) {
                    plugin.getLogger().log(Level.WARNING,
                            "Could not save " + file.getFileName(), e);
                }
            }
        }
    }

    /*
     * Checks if a specified file needs to be updated by checking the version field in the supplied
     * com.conventnunnery.libraries.config.ConventYamlConfiguration against the version field in the copy of the file inside the jar.
     */
    private boolean needToUpdate(ConventYamlConfiguration config, ConventConfigurationFile file) {
        YamlConfiguration inPlugin = YamlConfiguration.loadConfiguration(plugin
                .getResource(file.getFileName()));
        if (inPlugin == null) {
            return false;
        }
        String configVersion = config.getString("version");
        String currentVersion = inPlugin.getString("version");
        return configVersion == null || currentVersion != null && !(configVersion.equalsIgnoreCase(currentVersion));
    }

    /*
     * Backs up the specified file by changing its name from <filename>.yml to <filename>_old.yml.
     */
    private boolean backup(ConventConfigurationFile file) {
        File actualFile = new File(plugin.getDataFolder(), file.getFileName());
        if (!actualFile.exists()) {
            return false;
        }
        File newFile = new File(plugin.getDataFolder(), file.getFileName().replace(".yml", "_old.yml"));
        return actualFile.renameTo(newFile);
    }

    /*
     * Saves the default values from the copy of the com.conventnunnery.libraries.config.ConventConfigurationFile in the jar to the
     * specified com.conventnunnery.libraries.config.ConventYamlConfiguration by clearing the file and reprinting values.
     */
    private void saveDefaults(ConventYamlConfiguration config,
                              ConventConfigurationFile file) {
        YamlConfiguration yc = ConventYamlConfiguration.loadConfiguration(plugin
                .getResource(file.getFileName()));
        for (String key : config.getKeys(true)) {
            config.set(key, null);
        }
        config.setDefaults(yc);
        config.options().copyDefaults(true);
        config.save();
    }

}
