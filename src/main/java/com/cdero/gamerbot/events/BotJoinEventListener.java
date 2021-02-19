package com.cdero.gamerbot.events;

//import statements
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cdero.gamerbot.sql.SQLConnection;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class BotJoinEventListener extends ListenerAdapter {
	
	/**
	 * Logger for the MemberJoinEventListener class.
	 */
	private final static Logger log = LogManager.getLogger(MemberJoinEventListener.class.getName());
	
	/**
	 * Instance variable to keep track of the Guild Join Event for use in various methods.
	 */
	private GuildJoinEvent event;
	
	/**
	 * Overridden method get the event when the Gamer Bot joins a Discord Guild.
	 * 
	 * @param	event	Information about the event.
	 */
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		
		this.event = event;
		
		TextChannel channel = event.getGuild().getDefaultChannel();
		
		try {
			
			channel.sendMessage(":sunglasses: **I have arrived!**").queue();
			log.info("Joined Discord Guild: " + event.getGuild().getName());
			
		} catch (IllegalArgumentException e) {
			
			log.warn("Event: Guild Join Event --> IllegalArgumentException");
			
		} catch (InsufficientPermissionException e) {
			
			log.warn("Event: Guild Join Event --> InsufficientPermissionException");
			
		} catch (UnsupportedOperationException e) {
			
			log.warn("Event: Guild Join Event --> UnsupportedOperationException");
			
		}
		
		try {
			
			getAndStoreGuildInfo();
			getAndStoreTextChannels();
			
		} catch (SQLException e) {
			
			channel.sendMessage(":x: **SQLException: There was an error updating our databases with your server information! Contact us so your web experience is uneffected!**").queue();
			log.fatal("There was an error connecting to the MySQL/MariaDB server!");
			
		}
		
	}
	
	/**
	 * Method that gets the guild id and the guild name and puts it into a SQL database.
	 */
	private void getAndStoreGuildInfo() throws SQLException {
		
		String guild_id = this.event.getGuild().getId();
		String guild_name = this.event.getGuild().getName();
		
		SQLConnection.query("INSERT INTO guild_list(guild_id, guild_name) VALUES ('" + guild_id + "','" + guild_name + "')");
		
	}
	
	/**
	 * Gets all the guild's text channels with their name, id, and related guild. Then puts them all into a batch query.
	 * 
	 * 
	 * @throws SQLException	When there is an error creating the batch query.
	 */
	private void getAndStoreTextChannels() throws SQLException {
		
		String guild_id = this.event.getGuild().getId();
		String text_channel_id;
		String text_channel_name;
		List<TextChannel> textChannels = this.event.getGuild().getTextChannels();
		
		Statement sqlStatement = SQLConnection.getSQL().createStatement();
		
		for(TextChannel channel : textChannels) {
			
			text_channel_id = channel.getId();
			text_channel_name = channel.getName();
			
			sqlStatement.addBatch("INSERT INTO text_channels(guild_id, text_channel_id, text_channel_name) VALUES ('"+ guild_id + "','" + text_channel_id + "','" + text_channel_name + "')");
			
			
		}
		
		SQLConnection.batchQuery(sqlStatement);
		
	}

}
