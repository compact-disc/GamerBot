package com.cdero.gamerbot.commands;

//import statements
import java.util.logging.Logger;
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
		
		User author = event.getAuthor();
		
		if(author.isBot()) {
			
			return;
			
		}
		
		Guild guild = event.getGuild();
		TextChannel channel = event.getChannel();
		
		if(event.getMessage().getContentRaw().equals(PREFIX + "ping")) {
			
			channel.sendMessage("Current Gamer Bot Response Time: " + event.getJDA().getGatewayPing() + "ms").queue();
			log.info("Command: " + PREFIX + "ping"
					+ "\nGuild: " + guild.toString()
					+ "\nChannel: " + channel.toString()
					+ "\nAuthor: " + author.toString());
			
		}
		
	}

}
