package com.cdero.gamerbot.commands;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageHistory.MessageRetrieveAction;
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
public class PurgeListener extends ListenerAdapter {

	/**
	 * Logger for the PurgeListener class.
	 */
	private final static Logger log = Logger.getLogger(PurgeListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	private final int PURGE_MAX = 20;
	
	/**
	 * Overridden method to receive and respond to the purge command.
	 * 
	 * @param		event	includes information about the event.
	 * @return		void
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			
			return;
			
		}
		
		String[] command = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		Member member = event.getGuild().getMember(event.getAuthor());
		
		if(command[0].equals(PREFIX + "purge")) {
			
			TextChannel channel = event.getChannel();
			
			if(commandLength == 1 || command[1].equalsIgnoreCase("help") && commandLength == 2) {
				
				channel.sendMessage("```" + "Usage: Purge x number of messages above, including the command, where is between 1 and 20.\n" + PREFIX + "purge [number]\nExample: " + PREFIX + "purge 5" + "```").queue();
				
			} else if (!command[1].equals("help") && commandLength == 2) {
				
				int purgeNumberInt = 0;
				
				try {
					
					purgeNumberInt = Integer.parseInt(command[1]);
					
				} catch (NumberFormatException e) {
					
					channel.sendMessage("Error when purging messages from channel!").queue();
					
				}
				
				if(member.hasPermission(Permission.MESSAGE_MANAGE) && purgeNumberInt > 0 && purgeNumberInt < 21) {
					
					MessageHistory history = new MessageHistory(event.getChannel());
					List<Message> messages = history.retrievePast(purgeNumberInt  + 1).complete();
					event.getChannel().deleteMessages(messages).queue();
					
				} else if (purgeNumberInt < 1 || purgeNumberInt > 20) {
					
					channel.sendMessage("```" + "Usage: Purge x number of messages above, including the command, where is between 1 and 20.\n" + PREFIX + "purge [number]\nExample: " + PREFIX + "purge 5" + "```").queue();
					
				} else {
					
					channel.sendMessage("You do not have permission to do that.").queue();
					
				}
				
				
			}else {
				
				channel.sendMessage("```" + "Usage: Purge x number of messages above, including the command, where is between 1 and 20.\n" + PREFIX + "purge [number]\nExample: " + PREFIX + "purge 5" + "```").queue();
				
			}
			
		}
				
	}
	
}
