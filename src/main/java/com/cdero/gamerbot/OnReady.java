package com.cdero.gamerbot;

//import statements
import java.util.logging.Logger;
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
	private final static Logger log = Logger.getLogger(OnReady.class.getPackage().getName());
	
	@Override
	public void onReady(ReadyEvent event) {
		
		log.info("Gamer Bot Started...");
		
	}
	
}
