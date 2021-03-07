package com.cdero.gamerbot.audio;

//import statements
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import com.cdero.gamerbot.audio.lavaplayer.GuildMusicManager;
import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
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
public class QueueCommand {

	/**
	 * Gets and sends the current audio queue to the channel where the message was sent from.
	 * 
	 * 
	 * @param event	Information about the message requesting the command.
	 */
	protected QueueCommand(GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		GuildMusicManager musicManager = MusicManagers.musicManagers.get(event.getGuild().getIdLong());
		
		if (voiceChannel == null) {
			
			channel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		} else if (musicManager == null) {
			
			channel.sendMessage(":x: **There is nothing playing!**").queue();
			
		} else {
			
			BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
			AudioTrack track;
			int numTracks = Integer.min(queue.size(), 20);
			List<AudioTrack> trackList = new ArrayList<>(queue);
			
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setColor(Color.CYAN);
			embedBuilder.setTitle("Audio Queue");
			
			if (musicManager != null) {
				
				embedBuilder.addField("Now Playing: " + musicManager.player.getPlayingTrack().getInfo().title, "Length: " + ((musicManager.player.getPlayingTrack().getInfo().length / 1000) / 60) + ":" + (int) ((musicManager.player.getPlayingTrack().getInfo().length / 1000)  % 60), false);
				
			}
			
			for(int i = 0; i < numTracks; i++) {
				
				track = trackList.get(i);
				
				embedBuilder.addField((i + 1) + ": " + track.getInfo().title, "Length: " + ((track.getInfo().length / 1000) / 60) + ":" + (int) ((track.getInfo().length / 1000)  % 60), false);
				
			}
			
			channel.sendMessage(embedBuilder.build()).queue();
			
		}
		
	}
	
}
