package com.denis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesClass {

	private static final String PREFIX = "settings/";
	private static final String POSTFIX = ".properties";
	private static final String ERROR_MSG_READ = "Exception in reading properties.";
	private static final String ERROR_MSG_CLOSE = "Exception in closing properties stream.";

	public static Properties getSettings(String nameSettings) {
		String settingsPath = PREFIX + nameSettings + POSTFIX;

		Properties properties = new Properties();
		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(settingsPath);

		try {
			properties.load(stream);
		} catch (IOException e) {
			throw new ExceptionUtils(ERROR_MSG_READ, e);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				throw new ExceptionUtils(ERROR_MSG_CLOSE, e);
			}
		}

		return properties;
	}

}
