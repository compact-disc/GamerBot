package com.cdero.gamerbot.audio;

//import statements
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
		
		if(command[0].equalsIgnoreCase(PREFIX + "leave") || command[0].equalsIgnoreCase(PREFIX + "pause") || command[0].equalsIgnoreCase(PREFIX + "play") || command[0].equalsIgnoreCase(PREFIX + "skip") || command[0].equalsIgnoreCase(PREFIX + "join")) {
			
			if(commandLength == 2 && command[1].equalsIgnoreCase("help")) {
				
				channel.sendMessage(":white_check_mark: **Audio Player Commands:"
						+ "\n"
						+ "\n" + PREFIX + "play [YouTube URL] :arrow_right: To play the audio from a YouTube video."
						+ "\n"
						+ "\n" + PREFIX + "pause :arrow_right: To pause and unpause audio."
						+ "\n"
						+ "\n" + PREFIX + "skip :arrow_right: To skip the current song."
						+ "\n"
						+ "\n" + PREFIX + "leave :arrow_right: To make Gamer Bot leave the voice channel."
						+ "**").queue();
				
				log.info("Audio Commands"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
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
						channel.sendMessage(":white_check_mark: **Audio Player Commands:"
								+ "\n"
								+ "\n" + PREFIX + "play [YouTube URL] :arrow_right: To play the audio from a YouTube video."
								+ "\n"
								+ "\n" + PREFIX + "pause :arrow_right: To pause and unpause audio."
								+ "\n"
								+ "\n" + PREFIX + "skip :arrow_right: To skip the current song."
								+ "\n"
								+ "\n" + PREFIX + "leave :arrow_right: To make Gamer Bot leave the voice channel."
								+ "**").queue();
						
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
						channel.sendMessage(":white_check_mark: **Audio Player Commands:"
								+ "\n"
								+ "\n" + PREFIX + "play [YouTube URL] :arrow_right: To play the audio from a YouTube video."
								+ "\n"
								+ "\n" + PREFIX + "pause :arrow_right: To pause and unpause audio."
								+ "\n"
								+ "\n" + PREFIX + "skip :arrow_right: To skip the current song."
								+ "\n"
								+ "\n" + PREFIX + "leave :arrow_right: To make Gamer Bot leave the voice channel."
								+ "**").queue();
						
						log.info("Audio Commands"
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						break;
				
				}
				
			}else {
				
				channel.sendMessage(":white_check_mark: **Audio Player Commands:"
						+ "\n"
						+ "\n" + PREFIX + "play [YouTube URL] :arrow_right: To play the audio from a YouTube video."
						+ "\n"
						+ "\n" + PREFIX + "pause :arrow_right: To pause and unpause audio."
						+ "\n"
						+ "\n" + PREFIX + "skip :arrow_right: To skip the current song."
						+ "\n"
						+ "\n" + PREFIX + "leave :arrow_right: To make Gamer Bot leave the voice channel."
						+ "**").queue();
				
				log.info("Audio Commands"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
}
