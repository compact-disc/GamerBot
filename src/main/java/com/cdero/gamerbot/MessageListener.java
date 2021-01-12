package com.cdero.gamerbot;

//import statements
import java.util.logging.Logger;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class MessageListener extends ListenerAdapter {
	
	/**
	 * Logger for the MessageListener class.
	 */
	private final static Logger log = Logger.getLogger(MessageListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to messages received in a Discord server text chat.
	 * 
	 * @param		event	includes information about the event.
	 * @return		void
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		// Get the message from the event that is passed
		Message message = event.getMessage();
		
		MessageBuilder reply = new MessageBuilder();
		
		// Check first if the message contains the command prefix before doing anything
		if (message.getContentRaw().startsWith(PREFIX) && !event.getAuthor().isBot()) {
			
			// Get the channel in which the message was sent
			MessageChannel channel = event.getChannel();
			
			String GuildMessageReceived = message.getContentRaw();
			
			// Ping command in the Gamer Bot application to check connectivity
			if (GuildMessageReceived.equals(PREFIX + "ping")) {
				
				// Respond with a message but queue it first in an asynchronous operation
				channel.sendMessage("Pong!").queue();
				
			} else if (GuildMessageReceived.contains(PREFIX + "remindme")) {
				
				if(GuildMessageReceived.equals(PREFIX + "remindme") || GuildMessageReceived.equals(PREFIX + "remindme help")) {
					
					channel.sendMessage("```" + "Usage: " + PREFIX + "remindme [task] => [time].[zone]" + "```").queue();
					
				}
				
				//channel.sendMessage("This is the reminder").mention(event.getAuthor()).queueAfter(1, TimeUnit.MINUTES);
				
			}
			
		}
		
	}

}
