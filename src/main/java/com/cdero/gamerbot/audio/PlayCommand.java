package com.cdero.gamerbot.audio;

import com.cdero.gamerbot.audio.lavaplayer.GuildMusicManager;
import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class PlayCommand {
	
	protected PlayCommand(String[] command, GuildMessageReceivedEvent event) {
		
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		TextChannel textChannel = event.getChannel();
		
		if(voiceChannel == null) {
			
			textChannel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		}
		
		if(command.length == 1 && !event.getGuild().getAudioManager().isConnected()) {
			
			textChannel.sendMessage(":x: **There is nothing to play!**").queue();
			return;
			
		}
		
		if(event.getGuild().getAudioManager().isConnected() && command.length == 2) {
			
			playTrack(textChannel, command[1]);
			
		}else if(event.getGuild().getAudioManager().isConnected() && command.length == 1 && MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).getSendHandler().getAudioPlayer().isPaused()) {
			
			MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).getSendHandler().getAudioPlayer().setPaused(false);
			
			textChannel.sendMessage(":white_check_mark: **Track unpaused!**").queue();
			
		}else {
			
			try {
				
				AudioManager audioManager = event.getGuild().getAudioManager();
				audioManager.openAudioConnection(voiceChannel);
				
				playTrack(textChannel, command[1]);
				
			} catch (IllegalStateException e) {
				
				textChannel.sendMessage(":x: **Error connecting to voice!**").queue();

				
			} catch (IllegalArgumentException e) {
				
				textChannel.sendMessage(":x: **Error connecting to voice!**").queue();
				
			} catch (UnsupportedOperationException e) {
				
				textChannel.sendMessage(":x: **Error connecting to voice!**").queue();
				
			} finally {
				
				textChannel.sendMessage(":white_check_mark: **Connected to voice!**").queue();
				
			}
			
		}

				
	}
	
	private void playTrack(final TextChannel channel, final String URL) {
		
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		
		MusicManagers.playerManager.loadItemOrdered(musicManager, URL, new AudioLoadResultHandler() {
			
			@Override
			public void trackLoaded(AudioTrack track) {
				
				channel.sendMessage(":white_check_mark: Adding to queue :arrow_right: " + track.getInfo().title).queue();
				
				play(musicManager, track);
				
			}
			
			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				
				AudioTrack firstTrack = playlist.getSelectedTrack();
				
				if(firstTrack == null) {
					
					firstTrack = playlist.getTracks().get(0);
					
				}
				
				channel.sendMessage(":white_check_mark: Adding to queue :arrow_right: " + firstTrack.getInfo().title + " [First track in playlist " + playlist.getName() + "]").queue();
				
				play(musicManager, firstTrack);
				
			}
			
			@Override
			public void noMatches() {
				
				channel.sendMessage(":x: **Nothing found by :arrow_right: <" + URL + ">**").queue();
				
			}
			
			@Override
			public void loadFailed(FriendlyException exception) {
				
				channel.sendMessage(":x: **Could not play :arrow_right: <" + exception.getMessage() + ">**").queue();
				
			}
			
		});
		
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
	
	private void play(GuildMusicManager musicManager, AudioTrack track) {
		
		musicManager.scheduler.queue(track);
		
	}
		
}
