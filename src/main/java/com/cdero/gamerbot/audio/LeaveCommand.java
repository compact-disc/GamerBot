package com.cdero.gamerbot.audio;

import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class LeaveCommand {
	
	/**
	 * Operations that are run on the leave command.
	 * 
	 * @param event	Information about the event including the voice channel and text channel.
	 */
	protected LeaveCommand(GuildMessageReceivedEvent event) {
		
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		TextChannel textChannel = event.getChannel();
		
		if(voiceChannel == null) {
			
			textChannel.sendMessage(":x: **I am not connected to voice!**").queue();
			return;
			
		}
		
		event.getGuild().getAudioManager().closeAudioConnection();
		textChannel.sendMessage(":white_check_mark: **Disconnected from voice!**").queue();
		
		MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).player.destroy();
		
	}

}
