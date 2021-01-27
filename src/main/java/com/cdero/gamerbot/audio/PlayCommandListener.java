package com.cdero.gamerbot.audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class PlayCommandListener extends ListenerAdapter {

	/**
	 * Logger for the PlayCommandListener class.
	 */
	private final static Logger log = LogManager.getLogger(PlayCommandListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to play YouTube video audio in a Discord voice channel.
	 * 
	 * @param		event	includes information about the event.
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User author = event.getAuthor();
		
		if(author.isBot()) {
			
			return;
			
		}
		
		Guild guild = event.getGuild();
		
		String[] command = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		
		TextChannel channel = event.getChannel();
		
		if(command[0].equalsIgnoreCase(PREFIX + "play")) {
		
			if(commandLength == 1 || (command[1].equalsIgnoreCase("help") && commandLength == 2)) {
			
				channel.sendMessage("```" + "Usage: Play audio from YouTube.\n" + PREFIX + "play [YouTube Search/URL]\nExamples: \n" + PREFIX + "play https://www.youtube.com/watch?v=dQw4w9WgXcQ\n" + PREFIX + "play sopranos theme" + "```").queue();
				log.info("Command: " + PREFIX + "play"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else if(commandLength >= 2 && !command[1].equalsIgnoreCase("help")) {
				
				
				
			}else {
				
				channel.sendMessage("```" + "Usage: Play audio from YouTube.\n" + PREFIX + "play [YouTube Search/URL]\nExamples: \n" + PREFIX + "play https://www.youtube.com/watch?v=dQw4w9WgXcQ\n" + PREFIX + "play sopranos theme" + "```").queue();
				log.info("Command: " + PREFIX + "play"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
}
