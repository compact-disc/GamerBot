package com.cdero.gamerbot;

//import statements
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class OnReady extends ListenerAdapter {
	
	/**
	 * Logger for the OnReady class.
	 */
	private final static Logger log = LogManager.getLogger(OnReady.class.getName());
	
	@Override
	public void onReady(ReadyEvent event) {
		
		log.info("Gamer Bot Started...");
		
		AudioSourceManagers.registerRemoteSources(MusicManagers.playerManager);
		AudioSourceManagers.registerLocalSource(MusicManagers.playerManager);
		
	}
	
}
