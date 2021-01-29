package com.cdero.gamerbot.audio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;

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
		
		if(command[0].equalsIgnoreCase(PREFIX + "leave") || command[0].equalsIgnoreCase(PREFIX + "pause") || command[0].equalsIgnoreCase(PREFIX + "play") || command[0].equalsIgnoreCase(PREFIX + "queue") || command[0].equalsIgnoreCase(PREFIX + "skip") || command[0].equalsIgnoreCase(PREFIX + "stop") || command[0].equalsIgnoreCase(PREFIX + "join")) {
		
			if(commandLength == 1 && command[0].equalsIgnoreCase(PREFIX + "play") && MusicManagers.musicManagers.get(Long.parseLong(event.getGuild().getId())).getSendHandler().getAudioPlayer().isPaused()) {
				
				log.info("Audio Command: " + command[0]
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				new PlayCommand(command, event);
				return;
				
			}
			
			if(commandLength == 1 || (command[1].equalsIgnoreCase("help") && commandLength == 2)) {
				
				switch(command[0].toLowerCase()) {
				
					case PREFIX + "skip":
						log.info("Audio Command: " + command[0]
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						new SkipCommand(event);
						break;
						
					case PREFIX + "leave":
						log.info("Audio Command: " + command[0]
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						new LeaveCommand(event);
						break;
						
					case PREFIX + "pause":
						log.info("Audio Command: " + command[0]
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						new PauseCommand(event);
						break;
						
					case PREFIX + "join":
						log.info("Audio Command: " + command[0]
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						new JoinCommand(event);
						break;
					
					default:
						channel.sendMessage("```"
								+ "Audio Player Commands:"
								+ "\n" + PREFIX + "play [YouTube URL]"
								+ "\n" + PREFIX + "pause"
								+ "\n" + PREFIX + "skip"
								+ "\n" + PREFIX + "stop"
								+ "\n" + PREFIX + "leave"
								+ "```").queue();
						
						log.info("Audio Commands"
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						break;
				
				}
				
			}else if((commandLength == 2 && !command[1].equalsIgnoreCase("help"))) {
				
				switch(command[0].toLowerCase()) {
						
					case PREFIX + "play":
						log.info("Audio Command: " + command[0]
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						new PlayCommand(command, event);
						break;
						
					default:
						channel.sendMessage("```"
								+ "Audio Player Commands:"
								+ "\n" + PREFIX + "play [YouTube URL]"
								+ "\n" + PREFIX + "pause"
								+ "\n" + PREFIX + "skip"
								+ "\n" + PREFIX + "stop"
								+ "\n" + PREFIX + "leave"
								+ "```").queue();
						
						log.info("Audio Commands"
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						break;
				
				}
				
			}else {
				
				channel.sendMessage("```"
						+ "Audio Player Commands:"
						+ "\n" + PREFIX + "play [YouTube URL]"
						+ "\n" + PREFIX + "pause"
						+ "\n" + PREFIX + "skip"
						+ "\n" + PREFIX + "leave"
						+ "```").queue();
				
				log.info("Audio Commands"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
}
