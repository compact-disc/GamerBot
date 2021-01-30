package com.cdero.gamerbot.audio.lavaplayer;

//import statements
import java.util.HashMap;
import java.util.Map;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class MusicManagers {

	/**
	 * HashMap to store the Guild Music Manager for each Guild when the application is running.
	 */
	public static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
	
	/**
	 * Audio Player Manager to manage the audio player.
	 */
	public static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	
}
