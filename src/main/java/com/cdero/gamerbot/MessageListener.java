package com.cdero.gamerbot;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to messages received in a Discord server text chat.
	 * 
	 * @author 		Christopher DeRoche 
	 * @version		1.0
	 * @since		1.0
	 * 
	 * @param		event	includes information about the event
	 * @return		void
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		// Get the message from the event that is passed
		Message message = event.getMessage();
		
		// Check first if the message contains the command prefix before doing anything
		if (message.getContentRaw().startsWith(PREFIX)) {
			
			// Get the channel in which the message was sent
			MessageChannel channel = event.getChannel();
			
			// Ping command in the Gamer Bot application to check connectivity
			if (message.getContentRaw().equals(PREFIX + "ping")) {
				
				// Respond with a message but queue it first in an asynchronous operation
				channel.sendMessage("Pong!").queue();
				
			} else if (message.getContentRaw().equals("")) {
				
				
				
			}
			
		}
		
	}

}
