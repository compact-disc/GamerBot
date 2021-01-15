package com.cdero.gamerbot.messagelisteners;

import java.util.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingListener extends ListenerAdapter {
	
	/**
	 * Logger for the PingListener class.
	 */
	private final static Logger log = Logger.getLogger(PingListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to the ping command. Generally used for testing.
	 * 
	 * @param		event	includes information about the event.
	 * @return		void
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			
			return;
			
		}
		
		if(event.getMessage().getContentRaw().equals(PREFIX + "ping")) {
			
			event.getChannel().sendMessage("Current Gamer Bot Response Time: " + event.getJDA().getGatewayPing() + "ms").queue();
			
		}
		
	}

}
