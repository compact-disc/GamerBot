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
	
	/**
	 * Operations that are run on the play command.
	 * 
	 * @param event	Information about the event including the voice channel and text channel.
	 */
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
	
	/**
	 * 
	 * Method to play a track given a URL and reply to the Text Channel with a response.
	 * 
	 * @param channel	The Text Channel that replies will be given to.
	 * @param URL	The web URL for the audio that will be played.
	 */
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
	
	/**
	 * Method to add the audio to the queue for a given server.
	 * 
	 * @param musicManager	The music manager for the given guild.
	 * @param track	The specified audio track that is passed into the command.
	 */
	private void play(GuildMusicManager musicManager, AudioTrack track) {
		
		musicManager.scheduler.queue(track);
		
	}
		
}
