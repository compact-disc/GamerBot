package com.cdero.gamerbot.events;

//import statements
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class MemberJoinEventListener extends ListenerAdapter {

	/**
	 * Logger for the MemberJoinEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(MemberJoinEventListener.class.getName());
	
	/**
	 * Overridden event that will run when a new guild member joins the guild.
	 * 
	 * @param	event	includes information about the event
	 */
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		
		Member member = event.getMember();
		Guild guild = event.getGuild();
		String user = event.getMember().getAsMention();
		TextChannel channel = event.getGuild().getDefaultChannel();
		
		try {
			
			channel.sendMessage("Welcome " + user + " to " + guild.getName() + "!").queue();
			log.info("Event: Member Join Event"
					+ "\nGuild: " + guild.getName()
					+ "\nMember: " + member.getEffectiveName());
			
		} catch (IllegalArgumentException e) {
			
			log.warn("Event: Member Join Event --> IllegalArgumentException");
			
		} catch (InsufficientPermissionException e) {
			
			log.warn("Event: Member Join Event --> InsufficientPermissionException");
			
		} catch (UnsupportedOperationException e) {
			
			log.warn("Event: Member Join Event --> UnsupportedOperationException");
			
		}
		
	}
	
}
