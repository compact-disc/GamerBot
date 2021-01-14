package com.cdero.gamerbot.messagelisteners;

import java.io.File;
import java.util.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BeansListener extends ListenerAdapter{
	
	/**
	 * Logger for the BeansListener class.
	 */
	private final static Logger log = Logger.getLogger(BeansListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to any time the word bean/beans is mentioned.
	 * 
	 * @param		event	includes information about the event.
	 * @return		void
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		if(!event.getAuthor().isBot()) {
			
			return;
			
		}
		
		String content = event.getMessage().getContentRaw();
		
		if(content.equalsIgnoreCase(PREFIX + "beans") || content.equalsIgnoreCase(PREFIX + "bean")) {
			
			event.getChannel().sendFile(beansImage()).append(":b:eans").queue();
			
			return;
			
		} else if ((content.contains("bean") || content.contains(" bean ")
				|| content.contains("beans") || content.contains(" beans ")
				|| content.contains("Bean") || content.contains(" Bean ")
				|| content.contains("Beans") || content.contains(" Beans "))
				&& !event.getAuthor().isBot()) {
			
			event.getChannel().sendFile(beansImage()).append(":b:eans").queue();
			
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
		
		return (new File("src/main/resources/images/beans.png"));
		
	}

}
