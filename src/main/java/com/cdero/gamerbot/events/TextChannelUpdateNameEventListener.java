package com.cdero.gamerbot.events;

//import statements
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cdero.gamerbot.sql.SQLConnection;
import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.sql.SQLException;

/**
 * 
 * @author		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class TextChannelUpdateNameEventListener extends ListenerAdapter {
	
	/**
	 * Logger for the TextChannelUpdateNameEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(TextChannelUpdateNameEventListener.class.getName());
	
	
	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
		
		long guildId = event.getGuild().getIdLong();
		long textChannelId = event.getChannel().getIdLong();
		String newName = event.getNewName();
		
		try {
			
			log.info("SQL Text Channel Name Update...");
			SQLConnection.query("UPDATE text_channels SET text_channel_name='" + newName + "' WHERE text_channel_id='" + textChannelId + "';");
			
		} catch (SQLException e) {
			
			log.error("Error: SQL Text Channel Update Name"
					+ "\nGuild: " + guildId
					+ "\nChannel: " + textChannelId);
			
		} finally {
			
			log.info("SQL Text Channel Name Update Complete...");
			
		}
		
	}

}
