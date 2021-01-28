package com.cdero.gamerbot.events;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class BotJoinEventListener extends ListenerAdapter {
	
	/**
	 * Logger for the MemberJoinEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(MemberJoinEventListener.class.getName());
	
	/**
	 * Overridden method get the event when the Gamer Bot joins a Discord Guild.
	 * 
	 * @param	event	Information about the event.
	 */
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		
		TextChannel channel = event.getGuild().getDefaultChannel();
		
		try {
			
			channel.sendMessage("**I have arrived!**").queue();
			
		} catch (IllegalArgumentException e) {
			
			log.warn("Event: Guild Join Event --> IllegalArgumentException");
			
		} catch (InsufficientPermissionException e) {
			
			log.warn("Event: Guild Join Event --> InsufficientPermissionException");
			
		} catch (UnsupportedOperationException e) {
			
			log.warn("Event: Guild Join Event --> UnsupportedOperationException");
			
		}
		
	}

}
