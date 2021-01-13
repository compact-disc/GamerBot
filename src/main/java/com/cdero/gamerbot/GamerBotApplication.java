package com.cdero.gamerbot;

//import statements
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;

import com.cdero.gamerbot.messagelisteners.BeansListener;
import com.cdero.gamerbot.messagelisteners.DiceRollListener;
import com.cdero.gamerbot.messagelisteners.PingListener;
import com.cdero.gamerbot.messagelisteners.RemindMeListener;
import com.cdero.gamerbot.messagelisteners.TestListener;
import com.cdero.gamerbot.messagelisteners.TimerListener;
import com.cdero.gamerbot.sql.SQLConnection;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class GamerBotApplication {
	
	/**
	 * Properties class to store and save the settings for the Gamer Bot Application.
	 */
	private Properties config = new Properties();
	
	/**
	 * File Input Stream to read the properties file into the Properties class.
	 */
	private FileInputStream configInput;
	
	/**
	 * File Output Stream to save the properties file from the Properties class.
	 */
	private FileOutputStream configOutput;
	
	/**
	 * File class for the properties file.
	 */
	private File configFile;
	
	/**
	 * Logger for the GamerBotApplication class.
	 */
	private final static Logger log = Logger.getLogger(GamerBotApplication.class.getPackage().getName());

	/**
	 * Main method for the Gamer Bot Application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		GamerBotApplication gba = new GamerBotApplication();

	}
	
	/**
	 * Default constructor to build the Gamer Bot Application where the properties file is read.
	 */
	private GamerBotApplication() {
		
		shutdownOperations();
		
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
				
				log.info("Add token to configuration file and restart...");
				System.exit(0);
				
			} catch (IOException io) {
				
				log.severe("There was an I/O error creating the configuration file...");
				System.exit(1);
				
			} catch (SecurityException se) {
				
				log.severe("There was a permissions error when trying to write the configuration file...");
				System.exit(1);
				
			}
			
		}
		
		//Start reading the properties from the file
		try {
			
			configInput = new FileInputStream(configFile);
			
			config.load(configInput);
			
		} catch (FileNotFoundException fnfe) {
			
			log.severe("There was no configuration file found...");
			System.exit(1);
			
		} catch (IOException io) {
			
			log.severe("There was an I/O error when loading the configuration file...");
			System.exit(1);
			
		}
		
		//Connect the application to the SQL server
		//SQLConnection.connectToSQL();
		
		String token = config.getProperty("token");
		
		JDABuild(token);
		
	}
	
	/**
	 * Build the Java Discord API and connect to Discord. This will add the activity and event listeners.
	 * 
	 * 
	 * @param	token	String of the Discord token to connect the application to Discord.
	 * @return	void
	 */
	private void JDABuild(String token) {
		
		JDABuilder jda = JDABuilder.createDefault(token);
		
		try {
			
			jda.setActivity(Activity.watching("You"));
			
			//Command Listeners
			jda.addEventListeners(new PingListener());
			jda.addEventListeners(new RemindMeListener());
			jda.addEventListeners(new TimerListener());
			jda.addEventListeners(new TestListener());
			jda.addEventListeners(new BeansListener());
			jda.addEventListeners(new DiceRollListener());
			
			jda.addEventListeners(new OnReady());
			
			jda.setEnableShutdownHook(true);
			
			jda.build();
			
		} catch (LoginException e) {
			
			log.severe("Gamer Bot cannot login...");
			System.exit(1);
			
		}
		
	}
	
	private void shutdownOperations() {
		
		
		Thread shutdown = new Thread() {
			
			public void run() {
				
				log.info("Type \"stop\" to stop the Gamer Bot Application...");
			
				Scanner input = new Scanner(System.in);
				
				if(input.next().equals("stop")) {
					
					input.close();
					System.exit(0);
					
				}
				
			}
			
		};
		
		shutdown.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			public void run() {
				
				try {
					
					configInput.close();
					
				} catch (IOException e) {
					
					log.warning("I/O error when closing configuration file...");
					
				} catch (NullPointerException ne) {
					
					log.warning("Input configuration file was null...");
					
				}
				
				try {
					
					configOutput.close();
					
				} catch (IOException e) {
					
					log.warning("I/O error when closing configuration file...");
					
				} catch (NullPointerException ne) {
					
					log.warning("Output configuration file was null...");
					
				}
				
			}
			
		});
		
	}

}
