package com.cdero.gamerbot.audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class AudioLoggingAndReplies {
	
	/**
	 * Logger for the AudioReplies class.
	 */
	private final static Logger log = LogManager.getLogger(AudioLoggingAndReplies.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final static String PREFIX = ">>";

	protected static void audioHelpReply(TextChannel channel, Guild guild, User author) {
		
		channel.sendMessage("```"
				+ "Audio Player Commands:"
				+ "\n" + PREFIX + "play [YouTube Search/URL]"
				+ "\n" + PREFIX + "pause"
				+ "\n" + PREFIX + "queue [YouTube Search/URL]"
				+ "\n" + PREFIX + "skip"
				+ "\n" + PREFIX + "stop"
				+ "\n" + PREFIX + "leave"
				+ "```").queue();
		
		log.info("Audio Commands"
				+ "\nGuild: " + guild.toString()
				+ "\nChannel: " + channel.toString()
				+ "\nAuthor: " + author.toString());
		
		
	}
	
	protected static void audioLog(String command, TextChannel channel, Guild guild, User author) {
		
		log.info("Audio Command: " + command
				+ "\nGuild: " + guild.toString()
				+ "\nChannel: " + channel.toString()
				+ "\nAuthor: " + author.toString());
		
	}
	
}
