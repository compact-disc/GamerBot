package com.cdero.gamerbot.events;

//import statements
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cdero.gamerbot.sql.SQLConnection;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author Christopher DeRoche
 * @version 1.0
 * @since 1.0
 *
 */
public class TextChannelCreateEventListener extends ListenerAdapter {
	
	/**
	 * Logger for the TextChannelCreateEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(TextChannelCreateEventListener.class.getName());
	
	/**
	 * When there is a channel created in the Discord Guild, this event is run, and the channel is added to the database.
	 * 
	 * @param	event	Information about the event.
	 */
	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		
		String guild_id = event.getGuild().getId();
		String text_channel_id = event.getGuild().getId();
		String text_channel_name = event.getChannel().getName();
		
		try {
			
			SQLConnection.query("INSERT INTO text_channels(guild_id, text_channel_id, text_channel_name) VALUES ('" + guild_id + "','" + text_channel_id + "','" + text_channel_name + "')");
			
		} catch (SQLException e) {
			
			log.error("Event SQL Error: Text Channel Create"
					+ "\nGuild: " + guild_id
					+ "\nChannel: " + text_channel_id);
			
		} finally {
			
			log.info("Event: SQL Text Channel Create"
					+ "\nGuild: " + guild_id
					+ "\nChannel: " + text_channel_id);
			
		}
		
	}

}
