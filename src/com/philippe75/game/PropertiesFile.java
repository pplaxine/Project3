package com.philippe75.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFile {

	public static String getPropertiesFile(String param) {
		Properties p = new Properties();
		
		try(InputStream is = new FileInputStream("src/resources/dataConfig.properties")) {	
			p.load(is);
			
		} catch (NullPointerException e) {
			System.err.print("The file dataConfig.properties could not be found.");
		
		} catch (IOException e) {
			System.err.println("Error with the propertiesFiles.");
			
			if(param.equals("CombinationLength")) {
				return "4";
			}else if(param.equals("errorAllowed")){
				return "4";
			}else if(param.equals("devMode")) {
				return "true"; 
			}
			
		}
		return p.getProperty(param);
	}
}
