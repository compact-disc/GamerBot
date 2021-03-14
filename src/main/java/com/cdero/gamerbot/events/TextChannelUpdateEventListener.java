package com.cdero.gamerbot.events;

//import statements
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


/**
 * 
 * @author		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class TextChannelUpdateEventListener extends ListenerAdapter {
	
	/**
	 * Logger for the TextChannelUpdateEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(TextChannelUpdateEventListener.class.getName());
	
	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
		
		log.info("hello");
		
	}

}
