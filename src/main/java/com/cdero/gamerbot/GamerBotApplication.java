package com.cdero.gamerbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class GamerBotApplication {
		
	private Properties config = new Properties();
	private FileInputStream configInput;
	private FileOutputStream configOutput;
	private File configFile;
	
	private final static Logger log = Logger.getLogger(GamerBotApplication.class.getPackage().getName());

	public static void main(String[] args) {
		
		GamerBotApplication gba = new GamerBotApplication();

	}
	
	private GamerBotApplication() {
		
		log.info("Starting Gamer Bot...");
		
		//Declare the configuration file object with the proper name
		configFile = new File("config.properties");
		
		//Check if there is a configuration file, if not, create one
		//Exit after creating the file so the user can add the token to the file
		if(!configFile.exists()) {
			
			try {
				
				configOutput = new FileOutputStream(configFile);
				
				config.setProperty("token", "ENTER YOUR TOKEN HERE");
				config.store(configOutput, "Gamer Bot Configuration File");
				
				log.info("Add client secret to config and restart...");
				System.exit(0);
				
			} catch (IOException io) {
				
				io.printStackTrace();
				log.severe("There was an I/O error creating the configuration file...");
				System.exit(1);
				
			} catch (SecurityException se) {
				
				se.printStackTrace();
				log.severe("There was a permissions error when trying to write the configuration file...");
				System.exit(1);
				
			}
			
		}
		
		//Start reading the properties from the file
		try {
			
			configInput = new FileInputStream(configFile);
			
			config.load(configInput);
			
		} catch (FileNotFoundException fnfe) {
			
			fnfe.printStackTrace();
			log.severe("There was no configuration file found...");
			System.exit(1);
			
		} catch (IOException io) {
			
			io.printStackTrace();
			log.severe("There was an I/O error when loading the configuration file...");
			System.exit(1);
			
		}
		
		String token = config.getProperty("token");
		
		JDABuild(token);
		
	}
	
	private void JDABuild(String token) {
		
		JDABuilder jda = JDABuilder.createDefault(token);
		jda.setActivity(Activity.watching("You"));
		
		try {
			
			jda.addEventListeners(new MessageListener());
			jda.addEventListeners(new OnReady());
			
			jda.build();
			
		} catch (LoginException e) {
			
			e.printStackTrace();
			log.severe("Gamer Bot cannot login...");
			System.exit(1);
			
		}
		
	}

}
