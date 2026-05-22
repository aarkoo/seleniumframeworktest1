package seleniumcode.com.learn.seleniumframework.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private final Properties properties = new Properties();

	public ConfigReader() {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (inputStream == null) {
				throw new IllegalStateException("config.properties was not found on the classpath");
			}
			properties.load(inputStream);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to load config.properties", e);
		}
	}

	public String getProperty(String key) {
		String systemValue = System.getProperty(key);
		if (systemValue != null && !systemValue.isBlank()) {
			return systemValue;
		}

		String environmentKey = key.toUpperCase().replace('.', '_');
		String environmentValue = System.getenv(environmentKey);
		if (environmentValue != null && !environmentValue.isBlank()) {
			return environmentValue;
		}

		String value = properties.getProperty(key);
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("Missing required configuration value: " + key);
		}
		return value;
	}

	public String getApplicationUrl() {
		return getProperty("application.url");
	}

	public String getUserEmail() {
		return getProperty("user.email");
	}

	public String getUserPassword() {
		return getProperty("user.password");
	}

	public String getProductName() {
		return getProperty("product.name");
	}
}
