package com.cdero.gamerbot.events;

//import statements
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cdero.gamerbot.sql.SQLConnection;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Christopher DeRoche
 * @version 1.0
 * @since 1.0
 *
 */
public class TextChannelDeleteEventListener extends ListenerAdapter {
	
	/**
	 * Logger for the TextChannelDeleteEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(TextChannelDeleteEventListener.class.getName());
	
	
	/**
	 * When there is a channel deleted in the Discord Guild, this event is run, and the channel is removed from the database.
	 * 
	 * @param	event	Information about the event.
	 */
	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		
		String guild_id = event.getGuild().getId();
		String text_channel_id = event.getGuild().getId();
		
		try {
			
			SQLConnection.query("DELETE FROM text_channels WHERE text_channel_id='" + text_channel_id + "'");
			
		} catch (SQLException e) {
			
			log.error("Event Error: SQL Text Channel Delete"
					+ "\nGuild: " + guild_id
					+ "\nChannel: " + text_channel_id);
			
		} finally {
			
			log.info("Event: SQL Text Channel Delete"
					+ "\nGuild: " + guild_id
					+ "\nChannel: " + text_channel_id);
			
		}
		
	}

}
