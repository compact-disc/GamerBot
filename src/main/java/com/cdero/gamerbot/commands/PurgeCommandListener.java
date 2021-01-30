package com.cdero.gamerbot.commands;

//import statements
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class PurgeCommandListener extends ListenerAdapter {

	/**
	 * Logger for the PurgeListener class.
	 */
	private final static Logger log = LogManager.getLogger(PurgeCommandListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * The max number of messages that can be purged at once.
	 */
	private final int PURGE_MAX = 20;
	
	/**
	 * The minimum number of messages that can be purged at once.
	 */
	private final int PURGE_MIN = 1;
	
	/**
	 * Overridden method to receive and respond to the purge command.
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
		
		if(command[0].equals(PREFIX + "purge")) {
			
			TextChannel channel = event.getChannel();
			
			if(commandLength == 1 || command[1].equalsIgnoreCase("help") && commandLength == 2) {
				
				channel.sendMessage(":white_check_mark: **Usage: Purge x number of messages above where x is between 1 and 20."
						+ "\n" + PREFIX + "purge [number]"
						+ "\nExample: " + PREFIX + "purge 5"
						+ "**").queue();
				log.info("Command: " + PREFIX + "purge"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			} else if (!command[1].equals("help") && commandLength == 2) {
				
				int purgeNumberInt = 0;
				
				try {
					
					purgeNumberInt = Integer.parseInt(command[1]);
					
				} catch (NumberFormatException e) {
					
					channel.sendMessage(":x: **Error when purging messages from channel!**").queue();
					log.warn("Error with the purge command!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
				
				if(guild.getMember(author).hasPermission(Permission.MESSAGE_MANAGE) && purgeNumberInt >= PURGE_MIN && purgeNumberInt <= PURGE_MAX) {
					
					MessageHistory history = new MessageHistory(channel);
					List<Message> messages = history.retrievePast(purgeNumberInt  + 1).complete();
					channel.deleteMessages(messages).queue();
					log.info("Command: " + PREFIX + "purge"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				} else if (purgeNumberInt < PURGE_MIN || purgeNumberInt > PURGE_MAX) {
					
					channel.sendMessage(":white_check_mark: **Usage: Purge x number of messages above where x is between 1 and 20."
							+ "\n" + PREFIX + "purge [number]"
							+ "\nExample: " + PREFIX + "purge 5"
							+ "**").queue();
					log.info("Command: " + PREFIX + "purge"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				} else {
					
					channel.sendMessage(":x: **You do not have permission to do that.**").queue();
					log.warn("Error with the purge command!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
				
				
			}else {
				
				channel.sendMessage(":white_check_mark: **Usage: Purge x number of messages above where x is between 1 and 20."
						+ "\n" + PREFIX + "purge [number]"
						+ "\nExample: " + PREFIX + "purge 5"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "purge"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
				
	}
	
}
