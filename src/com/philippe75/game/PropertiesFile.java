package com.philippe75.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.oracle.tools.packager.Log;

public class PropertiesFile {

	private static final Logger log = Logger.getLogger(Properties.class);
	
	public static String getPropertiesFile(String param) {
		Properties p = new Properties();
		
		try(InputStream is = PropertiesFile.class.getResourceAsStream("/resources/dataConfig.properties")) {	
			p.load(is);
			
		} catch (NullPointerException e) {
			System.err.print("The file dataConfig.properties could not be found.");
			log.fatal("The file dataConfig.properties could not be found.");
		
		} catch (IOException e) {
			System.err.println("Error with the propertiesFiles.");
			log.fatal("Error with the propertiesFiles.");
		}
		return p.getProperty(param);
	}
}
