package com.cdero.gamerbot;

//import statements
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import javax.security.auth.login.LoginException;
import com.cdero.gamerbot.audio.AudioCommandsListener;
import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;
import com.cdero.gamerbot.commands.BeansCommandListener;
import com.cdero.gamerbot.commands.DiceRollCommandListener;
import com.cdero.gamerbot.commands.MagicEightBallCommandListener;
import com.cdero.gamerbot.commands.MemeCommandListener;
import com.cdero.gamerbot.commands.PingCommandListener;
import com.cdero.gamerbot.commands.PurgeCommandListener;
import com.cdero.gamerbot.commands.RemindMeCommandListener;
import com.cdero.gamerbot.commands.RoleCommandListener;
import com.cdero.gamerbot.commands.ShutdownCommandListener;
import com.cdero.gamerbot.commands.StockInformationCommandListener;
import com.cdero.gamerbot.commands.TimerCommandListener;
import com.cdero.gamerbot.events.BotJoinEventListener;
import com.cdero.gamerbot.events.BotLeaveVoiceOnEmpty;
import com.cdero.gamerbot.events.MemberJoinEventListener;
import com.cdero.gamerbot.sql.SQLConnection;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	//private final static Logger log = Logger.getLogger(GamerBotApplication.class.getPackage().getName());
	private static final Logger log = LogManager.getLogger(GamerBotApplication.class.getName());

	/**
	 * Main method for the Gamer Bot Application.
	 * 
	 * @param args	Program arguments
	 */
	public static void main(String[] args) {
		
		new GamerBotApplication();

	}
	
	/**
	 * Default constructor to build the Gamer Bot Application where the properties file is read.
	 */
	private GamerBotApplication() {
		
		serverCommands();
		
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
				
				configOutput.close();
				System.exit(0);
				
			} catch (IOException io) {
				
				log.fatal("There was an I/O error creating the configuration file...");
				System.exit(1);
				
			} catch (SecurityException se) {
				
				log.fatal("There was a permissions error when trying to write the configuration file...");
				System.exit(1);
				
			}
			
		}
		
		//Start reading the properties from the file
		try {
			
			configInput = new FileInputStream(configFile);
			
			config.load(configInput);
			
			configInput.close();
			
		} catch (FileNotFoundException fnfe) {
			
			log.fatal("There was no configuration file found...");
			System.exit(1);
			
		} catch (IOException io) {
			
			log.fatal("There was an I/O error when loading the configuration file...");
			System.exit(1);
			
		}
		
		//Connect the application to the SQL server
		SQLConnection.connectToSQL();
		
		String token = config.getProperty("token");
		
		if(token.equals("ENTER YOUR TOKEN HERE")) {
			
			log.fatal("Invalid token! It looks like you have not entered one yet! Please enter a valid token and then start Gamer Bot...");
			System.exit(1);
			
		}
		
		JDABuild(token);
		
	}
	
	/**
	 * Build the Java Discord API and connect to Discord. This will add the activity and event listeners.
	 * 
	 * 
	 * @param	token	String of the Discord token to connect the application to Discord.
	 */
	private void JDABuild(String token) {
		
		JDABuilder jda = JDABuilder.createDefault(token);
		
		try {
			
			//Set the activity
			jda.setActivity(Activity.watching("You"));
			
			/*
			 * Start Command Event Listeners
			 */
			jda.addEventListeners(new PingCommandListener());
			jda.addEventListeners(new RemindMeCommandListener());
			jda.addEventListeners(new TimerCommandListener());
			jda.addEventListeners(new BeansCommandListener());
			jda.addEventListeners(new DiceRollCommandListener());
			jda.addEventListeners(new PurgeCommandListener());
			jda.addEventListeners(new RoleCommandListener());
			jda.addEventListeners(new ShutdownCommandListener());
			jda.addEventListeners(new MemeCommandListener());
			jda.addEventListeners(new MagicEightBallCommandListener());
			jda.addEventListeners(new StockInformationCommandListener());
			/*
			 * End Command Event Listeners
			 */
			
			/*
			 * Start Audio Command/Event Listeners
			 */
			jda.addEventListeners(new AudioCommandsListener());
			/*
			 * End Audio Command/Event Listeners
			 */
			
			/*
			 * Start Event Listeners
			 */
			jda.addEventListeners(new MemberJoinEventListener());
			jda.addEventListeners(new BotJoinEventListener());
			jda.addEventListeners(new BotLeaveVoiceOnEmpty());
			/*
			 * End Event Listeners
			 */
			
			/*
			 * Start Utility Events
			 */
			jda.addEventListeners(new OnReady());
			/*
			 * End Utility Events
			 */
			
			jda.setEnableShutdownHook(true);
			
			jda.build();
			
		} catch (LoginException e) {
			
			log.fatal("Gamer Bot cannot login...");
			System.exit(1);
			
		} catch (IllegalArgumentException e) {
			
			log.fatal("Error starting Gamer Bot...");
			System.exit(1);
			
		}
		
	}
	
	/**
	 * Setup the thread to run server side commands. This includes the shutdown hook.
	 */
	private void serverCommands() {
		
		Thread shutdown = new Thread() {
			
			public void run() {
				
				log.info("Type \"stop\" to stop the Gamer Bot Application...");
			
				Scanner input = new Scanner(System.in);
				
				while(true) {
					
					if(input.next().equals("stop")) {
						
						input.close();
						log.info("Shutting down Gamer Bot...");
						System.exit(0);
						
					}
					
				}
				
			}
			
		};
		
		shutdown.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			
			public void run() {
				
				MusicManagers.musicManagers.clear();
				MusicManagers.playerManager.shutdown();
				log.info("Gamer Bot Closed...");
				
			}
			
		});
		
	}

}
