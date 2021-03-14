package com.cdero.gamerbot.events;

//import statements
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cdero.gamerbot.sql.SQLConnection;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildUpdateNameEventListener extends ListenerAdapter {
	
	/**
	 * Logger for the GuildUpdateNameEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(GuildUpdateNameEventListener.class.getName());
	
	/**
	 * Update the SQL entry for the guild name.
	 * 
	 * @param	event	Information about the Guild Update Name event that was called.
	 */
	@Override
	public void onGuildUpdateName(GuildUpdateNameEvent event) {
		
		long guildId = event.getGuild().getIdLong();
		String newName = event.getNewName();
		
		try {
			
			log.info("SQL Guild Name Update...");
			SQLConnection.query("UPDATE guild_list SET guild_name='" + newName + "' WHERE guild_id='" + guildId + "';");
			
		} catch (SQLException e) {
			
			log.error("Error: SQL Text Channel Update Name"
					+ "\nGuild: " + guildId);
			
		} finally {
			
			log.info("SQL Guild Name Update Complete...");
			
		}
		
	}

}
