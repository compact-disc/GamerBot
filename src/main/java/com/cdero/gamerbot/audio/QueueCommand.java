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
		BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();
		
		if (voiceChannel == null) {
			
			channel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		}
		
		if (queue.isEmpty()) {
			
			channel.sendMessage(":x: **There are no tracks in the queue!**").queue();
			return;
			
		} else {
			
			AudioTrack track;
			int numTracks = Integer.min(queue.size(), 20);
			List<AudioTrack> trackList = new ArrayList<>(queue);
			
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setAuthor("Gamer Bot");
			embedBuilder.setColor(Color.BLUE);
			embedBuilder.setTitle("Audio Queue");
			embedBuilder.setDescription("The audio tracks queued to be played by Gamer Bot and their order.");
			
			for(int i = 1; i <= numTracks; i++) {
				
				track = trackList.get(i);
				embedBuilder.addField(i + ": " + track.getInfo().title, "Length: " + track.getInfo().length, false);
				
			}
			
			channel.sendMessage(embedBuilder.build()).queue();
			
		}
		
	}
	
}
