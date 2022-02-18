package com.fxl.frame.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ConfigProUtils {
	public static final String R_PATH = "conf/config.properties";

	static Properties properties = null;

	static {
//		String path = System.getenv("WEBAPP_TEMPLATE_CONF") + R_PATH;
//		URL path = ClassLoader.getSystemResource(R_PATH);
		URL path = Thread.currentThread().getContextClassLoader().getResource(R_PATH);

//		Resource resource = new DefaultResourceLoader().getResource(path);
		Resource resource = new UrlResource(path);
		try {
			properties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static void put(String key, String value) {
		properties.setProperty(key, value);
	}
}
