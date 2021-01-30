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
	
	/**
	 * Operations that are run on the skip command.
	 * 
	 * @param event	Information about the event including the voice channel and text channel.
	 */
	protected SkipCommand(GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		
		if(voiceChannel == null) {
			
			channel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		}
		
		skipTrack(channel);
		
	}
	
	/**
	 * Skip the current audio track in the music manager.
	 * 
	 * @param channel	The Text Channel where the bot will reply to the user.
	 */
	private void skipTrack(TextChannel channel) {
		
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack();
		
		channel.sendMessage(":white_check_mark: **Skipped to next track!**").queue();
		
	}
	
	/**
	 * Get the Audio Player for a specific guild that is requesting the player.
	 * 
	 * @param guild	The Discord Guild that the command was given in.
	 * @return	The GuildMusicManager for the given Guild to play and manage the music.
	 */
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
