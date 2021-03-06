package com.conventnunnery.libraries.config;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import sun.nio.cs.StreamDecoder;

import java.io.*;
import java.util.logging.Level;

public class ConventYamlConfiguration extends YamlConfiguration implements ConventConfiguration {

    private File file;
    private String version;

    public ConventYamlConfiguration() {
        this(null, null);
    }

    /**
     * Instantiates a new com.conventnunnery.libraries.config.ConventYamlConfiguration.
     *
     * @param version The version of the default values
     * @param file    File to use as the basis
     */
    public ConventYamlConfiguration(File file, String version) {
        super();

        this.file = file;
        this.version = version;
    }

    /**
     * Instantiates a new com.conventnunnery.libraries.config.ConventYamlConfiguration.
     *
     * @param file File to use as the basis
     */
    public ConventYamlConfiguration(File file) {
        this(file, null);
    }

    public static ConventYamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");

        ConventYamlConfiguration config = new ConventYamlConfiguration();

        try {
            config.load(file);
        } catch (IOException ignored) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        return config;
    }

    public static ConventYamlConfiguration loadConfiguration(InputStream inputStream) {
        Validate.notNull(inputStream, "Stream cannot be null");

        ConventYamlConfiguration config = new ConventYamlConfiguration();

        try {
            config.load(inputStream);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Cannot load configuration from stream", ex);
        }

        return config;
    }

    @Override
    public String getName() {
        return file.getName();
    }

    /**
     * Loads the file specified by the constructor.
     *
     * @return if the file was correctly loaded
     */
    @Override
    public boolean load() {
        return load(options().updateOnLoad(), options().createDefaultFile());
    }

    /**
     * Saves the file specified by the constructor.
     *
     * @return if the file was correctly saved
     */
    @Override
    public boolean save() {
        try {
            save(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void setDefaults(final InputStream inputStream) {
        super.setDefaults(YamlConfiguration.loadConfiguration(inputStream));
    }

    @Override
    public boolean needToUpdate() {
        return getString("version") == null || (getVersion() != null && !getVersion().equalsIgnoreCase(getString("version")));
    }

    @Override
    public boolean backup() {

        File backup = new File(file.getParent(), file.getName().replace(".yml", "_old.yml"));

        try {

            Files.createParentDirs(backup);

            Bukkit.getLogger().info("Backing up " + file.getPath());

            save(backup);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void save(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        Files.createParentDirs(file);

        String data = saveToString();

        FileOutputStream stream = new FileOutputStream(file);
        OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8");

        try {
            writer.write(data);
        } finally {
            writer.close();
        }
    }

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        if (this.file == null) {
            this.file = file;
        }
    }

    @Override
    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        if (stream == null) {
            throw new IllegalArgumentException("Stream cannot be null");
        }

        String encoding = StreamDecoder.forInputStreamReader(stream, this, (String) null).getEncoding();
        InputStreamReader reader = new InputStreamReader(stream, encoding);
        StringBuilder builder = new StringBuilder();
        BufferedReader input = new BufferedReader(reader);

        try {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }

        loadFromString(builder.toString());
    }

    /**
     * Loads the file specified by the constructor.
     *
     * @param update            specifies if it should update the file using the defaults if versions differ
     * @param createDefaultFile specifies if it should create a default file if there is no existing one
     * @return if the file was correctly loaded
     */
    public boolean load(boolean update, boolean createDefaultFile) {

        try {

            if (file.exists()) {

                load(file);

                if (needToUpdate() && update) update();

            } else if (createDefaultFile) {

                options().copyDefaults(true);

                return save();

            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }

    /**
     * Updates the file with the defaults
     *
     * @return if the file was correctly updated
     */
    public boolean update() {
        Bukkit.getLogger().info("Updating " + file.getPath());
        if (options().backupOnUpdate()) {
            if (!backup()) {
                return false;
            }
            if (!file.delete()) {
                return false;
            }
        }

        options().copyDefaults(true);

        set("version", getVersion());

        return save();
    }

    @Override
    public ConventYamlConfigurationOptions options() {
        if (options == null) {
            options = new ConventYamlConfigurationOptions(this);
        }
        return (ConventYamlConfigurationOptions) options;
    }

    /**
     * Loads the file specified by the constructor.
     *
     * @param update specifies if it should update the file using the defaults if versions differ
     * @return if the file was correctly loaded
     */
    public boolean load(boolean update) {
        return load(update, options().createDefaultFile());
    }
}
