package com.cdero.gamerbot.audio.lavaplayer;

import java.util.HashMap;
import java.util.Map;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;

public class MusicManagers {

	public static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
	
	public static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
}
