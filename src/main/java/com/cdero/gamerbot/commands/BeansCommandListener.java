package com.cdero.gamerbot.commands;

//import statements
import java.io.File;
import java.util.logging.Logger;
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
public class BeansCommandListener extends ListenerAdapter{
	
	/**
	 * Logger for the BeansListener class.
	 */
	private final static Logger log = Logger.getLogger(BeansCommandListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to any time the word bean/beans is mentioned.
	 * 
	 * @param		event	includes information about the event.
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User author = event.getAuthor();
		
		if(author.isBot()) {
			
			return;
			
		}
		
		String content = event.getMessage().getContentRaw();
		TextChannel channel = event.getChannel();
		Guild guild = event.getGuild();
		
		if(content.equalsIgnoreCase(PREFIX + "beans") || content.equalsIgnoreCase(PREFIX + "bean")) {
			
			event.getChannel().sendFile(beansImage()).append(":b:eans").queue();
			log.info("Command: " + PREFIX + "beans"
					+ "\nGuild: " + guild.toString()
					+ "\nChannel: " + channel.toString()
					+ "\nAuthor: " + author.toString());
			
			return;
			
		} else if ((content.contains("bean") || content.contains(" bean ")
				|| content.contains("beans") || content.contains(" beans ")
				|| content.contains("Bean") || content.contains(" Bean ")
				|| content.contains("Beans") || content.contains(" Beans "))
				&& !author.isBot()) {
			
			event.getChannel().sendFile(beansImage()).append(":b:eans").queue();
			log.info("Event: Beans Detected"
					+ "\nGuild: " + guild.toString()
					+ "\nChannel: " + channel.toString()
					+ "\nAuthor: " + author.toString());
			
			return;
			
		}
		
	}
	
	/**
	 * Returns the file and path to the beans image.
	 * 
	 * 
	 * @return	File	the beans image file
	 */
	private File beansImage() {
		
		log.info("Loaded beans.png...");
		return (new File("src/main/resources/images/beans.png"));
		
	}

}
