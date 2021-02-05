package com.cdero.gamerbot.commands;

//import statements
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
public class MagicEightBallCommandListener extends ListenerAdapter {
	
	/**
	 * Logger for the MagicEightBallCommandListener class.
	 */
	private final static Logger log = LogManager.getLogger(MagicEightBallCommandListener.class.getName());
	
	/**
	 * Possible affirmative answers to be returned.
	 */
	private final String[] AFFIRMATIVE = {"It is certain.", "It is decidedly so.", "Without a doubt.", "Yes--definitely.", "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.", "Signs point to yes."};
	
	/**
	 * Possible non-committal answers to be returned.
	 */
	private final String[] NON_COMMITTAL = {"Reply hazy, try again.", "Ask again later.", "Better not tell you now.", "Cannot predict now.", "Concentrate and ask again."};
	
	/**
	 * Possible negative answers to be returned.
	 */
	private final String[] NEGATIVE = {"Don't count on it.", "My reply is no.", "My sources say no.", "Outlook not so good.", "Very doubtful."};
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond the magic 8 ball command.
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
		
		if(command[0].equalsIgnoreCase(PREFIX + "magic8ball")) {
			
			TextChannel channel = event.getChannel();
			
			String response = null;
			
			if(commandLength == 2 && command[1].equalsIgnoreCase("help")) {
				
				channel.sendMessage(":8ball: **Usage: Shake the magic 8 ball."
						+ "\n" 
						+ "\n" + PREFIX + "magic8ball [question]"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "magic8ball"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else if(commandLength >= 2 && !command[1].equalsIgnoreCase("help") && command[commandLength - 1].contains("?")) {
				
				response = getResponse();
				
				if(response != null) {
					
					channel.sendMessage("**<@!" + author.getId() + ">"
							+ "\n:8ball: " + getResponse() + "**").queue();
					
					log.info("Command: " + PREFIX + "magic8ball"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}else {
					
					channel.sendMessage(":x: **There was an error, try again!**").queue();
					
				}
			
			}else if(commandLength >= 2 && !command[1].equalsIgnoreCase("help") && !command[commandLength - 1].contains("?")) {
				
				channel.sendMessage(":x: **You must ask the :8ball: in the form of a question!**").queue();
				
				log.info("Command: " + PREFIX + "magic8ball"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else {
				
				channel.sendMessage(":8ball: **Usage: Shake the magic 8 ball."
						+ "\n" 
						+ "\n" + PREFIX + "magic8ball [question]"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "magic8ball"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
	/**
	 * Get and return a random String with an 8 ball response. Chosen from 3 different arrays.
	 * 
	 * 
	 * @return	A string with a random 8 ball response.
	 */
	private String getResponse() {
		
		int random = new Random().nextInt(3);
		
		String response = null;
		
		switch(random) {
		
			case 0:
				random = new Random().nextInt(AFFIRMATIVE.length);
				response = AFFIRMATIVE[random];
				break;
				
			case 1:
				random = new Random().nextInt(NON_COMMITTAL.length);
				response = NON_COMMITTAL[random];
				break;
				
			case 2:
				random = new Random().nextInt(NEGATIVE.length);
				response = NEGATIVE[random];
				break;
		
		}
		
		return response;
		
	}

}
