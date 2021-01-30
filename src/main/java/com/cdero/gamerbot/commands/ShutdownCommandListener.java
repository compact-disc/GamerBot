package com.cdero.gamerbot.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.api.entities.Guild;
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
public class ShutdownCommandListener extends ListenerAdapter {

	/**
	 * Logger for the ShutdownCommandListener class.
	 */
	private final static Logger log = LogManager.getLogger(ShutdownCommandListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to shutdown the Gamer Bot Application. Can only be used by user 276544589861486593 for administration/development.
	 * 
	 * @param		event	includes information about the event.
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User author = event.getAuthor();
		TextChannel channel = event.getChannel();
		Guild guild = event.getGuild();
		String command[] = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		
		if(author.isBot()) {
			
			return;
			
		}
		
		if(command[0].equalsIgnoreCase(PREFIX + "shutdown") && commandLength == 1 && author.getId().equalsIgnoreCase("276544589861486593")) {
			
			channel.sendMessage(":white_check_mark: **Gamer Bot Application shutting down!**").queue();
			log.info("Command: " + PREFIX + "shutdown"
					+ "\nGuild: " + guild.toString()
					+ "\nChannel: " + channel.toString()
					+ "\nAuthor: " + author.toString());
			
			System.exit(0);
			
		}else if(command[0].equalsIgnoreCase(PREFIX + "shutdown") && commandLength == 1 && !author.getId().equalsIgnoreCase("276544589861486593")) {
			
			channel.sendMessage(":x: **You do not have permission to do that!**").queue();
			log.info("Command: " + PREFIX + "shutdown"
					+ "\nGuild: " + guild.toString()
					+ "\nChannel: " + channel.toString()
					+ "\nAuthor: " + author.toString());
			return;
			
		}
		
	}
	
}
