package com.cdero.gamerbot.commands;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.Random;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class MemeCommandListener extends ListenerAdapter{
	
	/**
	 * Logger for the MemeCommandListener class.
	 */
	private final static Logger log = LogManager.getLogger(MemeCommandListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * String array of RSS feeds to pull images from.
	 */
	private final String[] JSON_URL_FEEDS_ARRAY = {"https://www.reddit.com/r/memes/.json"};
	
	/**
	 * Length of the array that holds the RSS feeds to choose from.
	 */
	private final int JSON_URL_FEEDS_LENGTH = JSON_URL_FEEDS_ARRAY.length;
	
	/**
	 * Overridden method to receive and respond the meme function.
	 * 
	 * @param		event	includes information about the event.
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User author = event.getAuthor();
		
		if(author.isBot()) {
			
			return;
			
		}
		
		Guild guild = event.getGuild();
		String[] command = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		
		if(command[0].equalsIgnoreCase(PREFIX + "meme")) {
			
			TextChannel channel = event.getChannel();
			
			if(commandLength == 1) {
				
				InputStream image = getMemeInputStream();
				
				if(image != null) {
					
					//Send all images as JPEG because size should be smaller, reduce overall traffic and system load.
					channel.sendFile(image, "meme.jpg").queue();
					
					log.info("Command: " + PREFIX + "meme"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}else {
					
					channel.sendMessage(":x: There was an error processing a meme for you!");
					log.error("Error getting meme"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
				
			}else if(commandLength == 2 && command[1].equalsIgnoreCase(PREFIX + "help")) {
				
				channel.sendMessage(":white_check_mark: **Usage: Get a meme from the internet."
						+ "\n" 
						+ "\n" + PREFIX + "meme"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "meme"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else {
				
				channel.sendMessage(":white_check_mark: **Usage: Get a meme from the internet."
						+ "\n" 
						+ "\n" + PREFIX + "meme"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "meme"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
	private InputStream getMemeInputStream() {
		
		try {
			
			return new URL(getRandomMemeURL()).openStream();
			
		} catch (IOException e) {
			
			log.error("IO Error");
			
		}
		
		return null;
		
	}
	
	private String getRandomMemeURL() {
	
		int random = new Random().nextInt(JSON_URL_FEEDS_LENGTH);
		
		
		
		return "";
		
	}

}
