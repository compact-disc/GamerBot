package com.cdero.gamerbot.events;

import java.util.logging.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberJoinEventListener extends ListenerAdapter {

	/**
	 * Logger for the MemberJoinEventListener class.
	 */
	private final static Logger log = Logger.getLogger(MemberJoinEventListener.class.getPackage().getName());
	
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
		
		channel.sendMessage("Welcome " + user + " to " + guild.getName() + "!").queue();
		log.info("Event: Member Join Event"
				+ "\nGuild: " + guild.getName()
				+ "\nMember: " + member.getEffectiveName());
		
	}
	
}
