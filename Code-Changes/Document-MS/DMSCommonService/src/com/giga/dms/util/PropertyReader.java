package com.giga.dms.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.giga.dms.exception.ServiceException;



/**
 * The Class ApplicationProperties.
 */
public final class PropertyReader implements ServiceConstants {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory.getLogger(PropertyReader.class);

	/** The properties. */
	private static Properties properties;

	/** The Constant PROPERTIES_FILE_NAME. */
	public static String propsFileName = "FilenetConnection.properties";

	/**
	 * Gets the.
	 * 
	 * @param key
	 *            the key
	 * @return the string
	 */
	public static String get(String key) throws ServiceException {

		try {
		if (properties == null) {
			loadProperties();
		}
		}catch(Exception e) {
			LOG.error("Error while Loading Properties key::"+e);
			throw new ServiceException(PROPERTY_FILE_ERROR,e.getMessage());
		}

		String value = (String) properties.get(key);
		return value;
	}

	/**
	 * Load properties.
	 */
	private static synchronized void loadProperties() throws ServiceException {

		if (properties != null) {
			return;
		}

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream propertiesInputStream = loader
				.getResourceAsStream(propsFileName);

		if (propertiesInputStream == null) {
			throw new ServiceException("PropertyFileNotFound",  "Properties file not found. Please ensure it is on the class path.");
		}
		properties = new Properties();

		try {
			properties.load(propertiesInputStream);
		} catch (IOException e) {
			LOG.error("Error while Loading Properties::"+e);
			throw new ServiceException("Properties file could not load:",e.getMessage());
		}

	}
	
	public static void main (String args[]) throws Exception{
		PropertyReader ps = new PropertyReader();
	}

}
