package com.cdero.gamerbot;

//import statements
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.cdero.gamerbot.audio.lavaplayer.MusicManagers;
import com.cdero.gamerbot.sql.SQLConnection;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
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
	private final static Logger log = LogManager.getLogger(OnReady.class.getName());
	
	/**
	 * Runs when the Bot is finished loading and connected to the Discord servers. Sets up the music managers for Lava Player and audio commands.
	 * This also handles the text channel, guild id, and other naming changes if the bot is offline.
	 * 
	 */
	@Override
	public void onReady(ReadyEvent event) {
		
		JDA jda = event.getJDA();
		
		AudioSourceManagers.registerRemoteSources(MusicManagers.playerManager);
		AudioSourceManagers.registerLocalSource(MusicManagers.playerManager);
		
		log.info("Gamer Bot Started...");
		
		try {
			
			log.info("Checking Guild List Updates...");
			checkSQLGuildList(jda);
			
		} catch (SQLException e) {
			
			log.error("Error: SQL Guild List Startup Check");
			
		} finally {
			
			log.info("SQL Guild List Update Complete...");
			
		}
		
		try {
			
			log.info("Checking Text Channel Updates...");
			checkSQLTextChannels(jda);
			
		} catch (SQLException e) {
			
			log.error("Error: SQL Text Channel Startup Check");
			
		} finally {
			
			log.info("SQL Text Channel Update Complete...");
			
		}
		
	}
	
	/**
	 * 
	 * Checks the text channel list to see if the text channels are added to the SQL Database, updates if needed.
	 * 
	 * @param jda	JDA information about the bot instance that can give a list of text channels and guilds for checking.
	 * @throws SQLException	If there is an error running the SQL query on the database.
	 */
	private void checkSQLTextChannels(JDA jda) throws SQLException {
		
		List<Guild> guilds = jda.getGuilds();
		long guildId;
		ResultSet queryResult;
		
		for(Guild guild : guilds) {
			
			boolean empty = true;
			List<Long> sqlTextChannelIds = new ArrayList<Long>();
			List<String> sqlTextChannelName = new ArrayList<String>();
			
			List<Long> guildTextChannelIds = new ArrayList<Long>();
			List<String> guildTextChannelNames = new ArrayList<String>();
			
			List<TextChannel> textChannels = guild.getTextChannels();
			
			guildId = guild.getIdLong();
			queryResult = SQLConnection.query("SELECT * FROM text_channels WHERE guild_id='" + guildId + "';");
			
			while (queryResult.next()) {
				
				empty = false;
				sqlTextChannelIds.add(Long.parseLong(queryResult.getString("text_channel_id")));
				sqlTextChannelName.add(queryResult.getString("text_channel_name"));
				
			}
			
			Collections.sort(sqlTextChannelIds);
			Collections.sort(sqlTextChannelName);
			
			for (TextChannel channel : textChannels) {
				
				guildTextChannelIds.add(channel.getIdLong());
				guildTextChannelNames.add(channel.getName());
				
			}
			
			Collections.sort(guildTextChannelIds);
			Collections.sort(guildTextChannelNames);
			
			if(!sqlTextChannelIds.equals(guildTextChannelIds) || !sqlTextChannelName.equals(guildTextChannelNames)) {
				
				if (!empty) {
					
					SQLConnection.query("DELETE FROM text_channels WHERE guild_id='" + guildId + "';");
					
				}
				
				updateSQLTextChannels(guildId, textChannels);
				
			}
			
		}
		
	}
	
	/**
	 * If there was a change in the text channels, then re-add the text channels to the database for the guilds.
	 * 
	 * @param guildId	The ID for the guild.
	 * @param textChannels	A list of Text Channels for the guild.
	 * @throws SQLException	If there is an error running the SQL query on the database.
	 */
	private void updateSQLTextChannels(long guildId, List<TextChannel> textChannels) throws SQLException {
		
		String text_channel_id;
		String text_channel_name;
		Statement sqlStatement = SQLConnection.getSQL().createStatement();
		
		for(TextChannel channel : textChannels) {
			
			text_channel_id = channel.getId();
			text_channel_name = channel.getName();
			
			sqlStatement.addBatch("INSERT INTO text_channels(guild_id, text_channel_id, text_channel_name) VALUES ('"+ guildId + "','" + text_channel_id + "','" + text_channel_name + "')");
			
			
		}
		
		SQLConnection.batchQuery(sqlStatement);
		
	}
	
	/**
	 * 
	 * Checks the guild list to see if the Guild is added to the SQL Database, updates if needed.
	 * 
	 * @param jda	JDA information about the bot instance that can give a list of guilds for checking.
	 * @throws SQLException	If there is an error running the SQL query on the database.
	 */
	private void checkSQLGuildList(JDA jda) throws SQLException {
		
		List<Guild> guilds = jda.getGuilds();
		String guildId;
		ResultSet queryResult;
		boolean empty = true;
		String guild_id;
		String guild_name;
		
		for(Guild guild : guilds) {
			
			guildId = guild.getId();
			queryResult = SQLConnection.query("SELECT guild_id FROM guild_list WHERE guild_id='" + guildId + "';");

			while(queryResult.next()) {
				
				empty = false;
				
			}
			
			if(empty) {
				
				guild_id = guild.getId();
				guild_name = guild.getName();
				
				SQLConnection.query("INSERT INTO guild_list(guild_id, guild_name) VALUES ('" + guild_id + "','" + guild_name + "')");
				
			}
			
		}
		
	}
	
}
