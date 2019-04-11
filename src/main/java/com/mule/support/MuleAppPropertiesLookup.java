package com.mule.support;

import java.util.Properties;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.StrLookup;

@Plugin(name = "mule", category = "Lookup")
public class MuleAppPropertiesLookup implements StrLookup {
	
	private Properties properties = new Properties();
	
	public MuleAppPropertiesLookup() throws Exception {
		try {
			properties.load(org.mule.util.IOUtils.getResourceAsStream("mule-app.properties", this.getClass(), true, true));
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String lookup(String key) {
		return properties.getProperty(key);
	}

	@Override
	public String lookup(LogEvent event, String key) {
		return properties.getProperty(key);
	}

}
