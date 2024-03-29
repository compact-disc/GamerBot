package com.cdero.gamerbot;

//import statements
import java.io.IOException;
import java.sql.SQLException;
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
import com.cdero.gamerbot.events.GuildJoinEventListener;
import com.cdero.gamerbot.events.GuildUpdateNameEventListener;
import com.cdero.gamerbot.events.GuildVoiceLeaveEventListener;
import com.cdero.gamerbot.events.MemberJoinEventListener;
import com.cdero.gamerbot.events.TextChannelCreateEventListener;
import com.cdero.gamerbot.events.TextChannelDeleteEventListener;
import com.cdero.gamerbot.events.TextChannelUpdateNameEventListener;
import com.cdero.gamerbot.sql.SQLConnection;
import com.cdero.gamerbot.web.WebClientReceiver;
import net.dv8tion.jda.api.JDA;
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
	 * The Thread for the web client connection. This will capture data from Spring Boot Gamer Bot.
	 */
	private Thread webClientThread;
	
	/**
	 * Logger for the GamerBotApplication class.
	 */
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
		
		//Connect the application to the SQL server
		try {
			
			SQLConnection.connectToSQL();
			
		} catch (SQLException e) {
			
			log.fatal("Unable to connect to MySQL/MariaDB server, shutting down...");
			System.exit(1);
			
		} finally {
			
			log.info("Connected to MariaDB...");
			
		}
		
		JDABuild();
		
	}
	
	/**
	 * Build the Java Discord API and connect to Discord. This will add the activity and event listeners.
	 * 
	 * 
	 * @param	token	String of the Discord token to connect the application to Discord.
	 */
	private void JDABuild() {
		
		//Use static Configuration.botTken to get the Discord bot token
		JDABuilder jda = JDABuilder.createDefault(Configuration.botToken);
		JDA botInstance = null;
		
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
			jda.addEventListeners(new GuildJoinEventListener());
			jda.addEventListeners(new GuildVoiceLeaveEventListener());
			jda.addEventListeners(new TextChannelCreateEventListener());
			jda.addEventListeners(new TextChannelDeleteEventListener());
			jda.addEventListeners(new TextChannelUpdateNameEventListener());
			jda.addEventListeners(new GuildUpdateNameEventListener());
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
			
			botInstance = jda.build();
			
		} catch (LoginException e) {
			
			log.fatal("Gamer Bot cannot login...");
			System.exit(1);
			
		} catch (IllegalArgumentException e) {
			
			log.fatal("Error starting Gamer Bot...");
			System.exit(1);
			
		}
		
		try {
			
			webClientThread = new WebClientReceiver(botInstance);
			webClientThread.start();
			
		} catch (IOException e) {
			
			log.fatal("Error starting Gamer Bot Spring listener thread...");
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
				
				try {
					
					SQLConnection.getSQL().close();
					log.info("SQL Connection closed...");
					
				} catch (SQLException e) {
					
					log.error("Error closing the SQL connection...");
					
				}
				
				MusicManagers.musicManagers.clear();
				MusicManagers.playerManager.shutdown();
				log.info("Gamer Bot Closed...");
				
			}
			
		});
		
	}

}
