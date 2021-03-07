package com.cdero.gamerbot.audio;

//import statements
import com.cdero.gamerbot.audio.lavaplayer.GuildMusicManager;
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
public class NowPlayingCommand {
	
	/**
	 * Responds with a message showing the currently playing track from Gamer Bot.
	 * 
	 * @param event	Gets and sends the current audio queue to the channel where the message was sent from.
	 */
	protected NowPlayingCommand(GuildMessageReceivedEvent event) {
		
		TextChannel channel = event.getChannel();
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		GuildMusicManager musicManager = MusicManagers.musicManagers.get(event.getGuild().getIdLong());
		
		if (voiceChannel == null) {
			
			channel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		} else if (musicManager == null || musicManager.player.getPlayingTrack() == null) {
			
			channel.sendMessage(":x: **There is nothing playing!**").queue();
			return;
			
		} else {
			
			channel.sendMessage(":white_check_mark: **Currently playing :arrow_right: " + musicManager.player.getPlayingTrack().getInfo().title + "**").queue();
			return;
			
		}
		
	}

}
