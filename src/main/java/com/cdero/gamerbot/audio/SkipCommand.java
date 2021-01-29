package com.cdero.gamerbot.audio;

import com.cdero.gamerbot.audio.lavaplayer.GuildMusicManager;
import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;
import net.dv8tion.jda.api.entities.Guild;
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
public class SkipCommand {
	
	protected SkipCommand(GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		
		if(voiceChannel == null) {
			
			channel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		}
		
		skipTrack(channel);
		
	}
	
	private void skipTrack(TextChannel channel) {
		
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack();
		
		channel.sendMessage(":white_check_mark: **Skipped to next track!**").queue();
		
	}
	
	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		
		long guildId = Long.parseLong(guild.getId());
		
		GuildMusicManager musicManager = MusicManagers.musicManagers.get(guildId);
		
		if(musicManager == null) {
			
			musicManager = new GuildMusicManager(MusicManagers.playerManager);
			MusicManagers.musicManagers.put(guildId, musicManager);
			
		}
		
		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());
		
		return musicManager;
		
	}

}
