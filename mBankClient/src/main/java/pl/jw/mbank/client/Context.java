package pl.jw.mbank.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import pl.jw.mbank.client.config.Configuration;
import pl.jw.mbank.common.exception.MBankSfiConfigurationException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Context {

	private final XStream xStream = new XStream(new DomDriver());

	private Configuration configuration;

	public Configuration getConfiguration() {
		if (configuration == null)
			init();

		return configuration;
	}

	private void init() {
		try {
			File configFile = new File(Env.CONFIG_FILE);
			if (!configFile.exists()) {
				configuration = new Configuration();
				save();
			}

			configuration = (Configuration) xStream.fromXML(new FileInputStream(Env.CONFIG_FILE));
		} catch (FileNotFoundException e) {
			throw new MBankSfiConfigurationException("Configuration file cannot be opened.", e);
		}
	}

	void save() {
		try {
			xStream.toXML(configuration, new FileOutputStream(Env.CONFIG_FILE));
		} catch (FileNotFoundException e) {
			throw new MBankSfiConfigurationException("Configuration file cannot be opened.", e);
		}

	}
}
