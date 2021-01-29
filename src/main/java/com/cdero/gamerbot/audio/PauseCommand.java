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
public class PauseCommand {
	
	protected PauseCommand(GuildMessageReceivedEvent event) {
		
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		TextChannel textChannel = event.getChannel();
		
		if(voiceChannel == null) {
			
			textChannel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		}
		
		if(event.getGuild().getAudioManager().isConnected() && !MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).getSendHandler().getAudioPlayer().isPaused()) {
			
			MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).getSendHandler().getAudioPlayer().setPaused(true);
			
			textChannel.sendMessage(":white_check_mark: **Track paused!**").queue();
			
		}else if(event.getGuild().getAudioManager().isConnected() && MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).getSendHandler().getAudioPlayer().isPaused()) {
			
			MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).getSendHandler().getAudioPlayer().setPaused(false);
			
			textChannel.sendMessage(":white_check_mark: **Track unpaused!**").queue();
			
		}else if (!event.getGuild().getAudioManager().isConnected()){
			
			textChannel.sendMessage(":x: **The bot is not in voice!**").queue();
			
		}
		
	}
	
}
