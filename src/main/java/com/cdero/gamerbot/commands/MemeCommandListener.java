package com.cdero.gamerbot.commands;

//import statements
import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cdero.gamerbot.utility.RedditImageGetter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

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
	private final String[] REDDIT_JSON_ARRAY = {"https://www.reddit.com/r/memes/rising.json", "https://www.reddit.com/r/dankmemes/rising.json"};
	
	/**
	 * Length of the array that holds the RSS feeds to choose from.
	 */
	private final int REDDIT_JSON_LENGTH = REDDIT_JSON_ARRAY.length;
	
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
				
				InputStream image = null;
				
				try {
					
					image = RedditImageGetter.getImageLink(REDDIT_JSON_ARRAY, REDDIT_JSON_LENGTH);
					
				} catch (IOException e) {
					
					channel.sendMessage(":x: **There was an error getting a meme for you!**").queue();
					log.error("IOException when running the >>meme command"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					return;
					
				} catch (Exception e) {
					
					channel.sendMessage(":x: **There was an error getting a meme for you!**").queue();
					log.error("Error when running the >>meme command"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					return;
					
				}
				
				if(image != null) {
					
					//Send all images as JPEG because size should be smaller, reduce overall traffic and system load.
					try{
						
						channel.sendFile(image, "meme.jpg").queue();
						
					} catch (ErrorResponseException e) {
						
						channel.sendMessage(":x: **There was an error getting a meme for you!**").queue();
						log.error("Error when sending the meme to Discord."
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						return;
					
					} catch (Exception e) {
						
						channel.sendMessage(":x: **There was an error getting a meme for you!**").queue();
						log.error("Error when sending the meme to Discord."
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						return;
						
					}
					
					
					log.info("Command: " + PREFIX + "meme"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}else {
					
					channel.sendMessage(":x: **There was an error getting a meme for you!**").queue();
					log.error("Error getting meme"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					return;
					
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

}
