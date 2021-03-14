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
	
	private void checkSQLTextChannels(JDA jda) throws SQLException {
		
		List<Guild> guilds = jda.getGuilds();
		long guildId;
		ResultSet queryResult;
		
		for(Guild guild : guilds) {
			
			boolean empty = true;
			List<Long> sqlTextChannelIds = new ArrayList<Long>();
			List<Long> guildTextChannelIds = new ArrayList<Long>();
			List<TextChannel> textChannels = guild.getTextChannels();
			
			guildId = guild.getIdLong();
			queryResult = SQLConnection.query("SELECT text_channel_id FROM text_channels WHERE guild_id='" + guildId + "';");
			
			while (queryResult.next()) {
				
				empty = false;
				sqlTextChannelIds.add(Long.parseLong(queryResult.getString("text_channel_id")));
				
			}
			
			Collections.sort(sqlTextChannelIds);
			
			for (TextChannel channel : textChannels) {
				
				guildTextChannelIds.add(channel.getIdLong());
				
			}
			
			Collections.sort(guildTextChannelIds);
			
			if(!sqlTextChannelIds.equals(guildTextChannelIds)) {
				
				if (!empty) {
					
					SQLConnection.query("DELETE FROM text_channels WHERE guild_id='" + guildId + "';");
					
				}
				
				updateSQLTextChannels(guildId, textChannels);
				
			}
			
		}
		
	}
	
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
