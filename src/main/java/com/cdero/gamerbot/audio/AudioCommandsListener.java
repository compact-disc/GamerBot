package com.cdero.gamerbot.audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AudioCommandsListener extends ListenerAdapter {
	
	/**
	 * Logger for the AudioCommandsListener class.
	 */
	private final static Logger log = LogManager.getLogger(AudioCommandsListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive audio player commands
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
		
		if(command[0].equalsIgnoreCase(PREFIX + "leave") || command[0].equalsIgnoreCase(PREFIX + "pause") || command[0].equalsIgnoreCase(PREFIX + "play") || command[0].equalsIgnoreCase(PREFIX + "queue") || command[0].equalsIgnoreCase(PREFIX + "skip") || command[0].equalsIgnoreCase(PREFIX + "stop")) {
		
			if(commandLength == 1 || (command[1].equalsIgnoreCase("help") && commandLength == 2)) {
				
				switch(command[0].toLowerCase()) {
				
					case PREFIX + "skip":
						AudioLoggingAndReplies.audioLog(command[0], channel, guild, author);
						new SkipCommand(channel, guild, author);
						break;
					
					case PREFIX + "stop":
						AudioLoggingAndReplies.audioLog(command[0], channel, guild, author);
						new StopCommand(channel, guild, author);
						break;
						
					case PREFIX + "leave":
						AudioLoggingAndReplies.audioLog(command[0], channel, guild, author);
						new LeaveCommand(channel, guild, author);
						break;
						
					case PREFIX + "pause":
						AudioLoggingAndReplies.audioLog(command[0], channel, guild, author);
						new PauseCommand(channel, guild, author);
						break;
					
					default:
						AudioLoggingAndReplies.audioHelpReply(channel, guild, author);
						break;
				
				}
				
			}else if(commandLength >= 2 && !command[1].equalsIgnoreCase("help")) {
				
				switch(command[0].toLowerCase()) {
						
					case PREFIX + "play":
						AudioLoggingAndReplies.audioLog(command[0], channel, guild, author);
						new PlayCommand(command, channel, guild, author);
						break;
						
					case PREFIX + "queue":
						AudioLoggingAndReplies.audioLog(command[0], channel, guild, author);
						new QueueCommand(command, channel, guild, author);
						break;
						
					default:
						AudioLoggingAndReplies.audioHelpReply(channel, guild, author);
						break;
				
				}
				
			}else {
				
				AudioLoggingAndReplies.audioHelpReply(channel, guild, author);
				
			}
			
		}
		
	}
	
}
