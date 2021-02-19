package com.cdero.gamerbot.events;

//import statements
import java.util.List;
import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class GuildVoiceLeaveEventListener extends ListenerAdapter {
	
	/**
	 * Method that is triggered when a user leaves 
	 * 
	 * @param	event	Information about the event.
	 */
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
	
		if(event.getGuild().getAudioManager().isConnected()) {
			
			List<Member> members = event.getChannelLeft().getMembers();
			
			if(members.size() == 1) {
				
				event.getGuild().getAudioManager().closeAudioConnection();
				MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).player.destroy();
				
			}

		}
		
	}

}
