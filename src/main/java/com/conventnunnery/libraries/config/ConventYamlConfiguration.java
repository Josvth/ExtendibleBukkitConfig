package com.conventnunnery.libraries.config;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import sun.nio.cs.StreamDecoder;

public class ConventYamlConfiguration extends YamlConfiguration implements ConventConfiguration {

	private final File file;
	private final String version;

	/**
	 * Instantiates a new com.conventnunnery.libraries.config.ConventYamlConfiguration.
	 *
	 * @param file File to use as the basis
	 */
	public ConventYamlConfiguration(File file) {
		this(file, null);
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

	@Override
	public void load(InputStream stream) throws IOException, InvalidConfigurationException {
		if (stream == null) {
			throw new IllegalArgumentException("Stream cannot be null");
		}

		String encoding = StreamDecoder.forInputStreamReader(stream, this, (String)null).getEncoding();
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

	/**
	 * Updates the file with the defaults
	 *
	 * @return if the file was correctly updated
	 */
	public boolean update() {

		if (options().backupOnUpdate())
			if (!backup()) return false;

		options().copyDefaults(true);

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
	 * Saves the file specified by the constructor.
	 *
	 * @return if the file was correctly saved
	 */
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
	public void setDefaults(final InputStream inputStream) {
		super.setDefaults(YamlConfiguration.loadConfiguration(inputStream));
	}

	@Override
	public void saveDefaults(InputStream inputStream) {

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
	public FileConfiguration getFileConfiguration() {
		return this;
	}

	@Override
	public String getVersion() {
		return version;
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
