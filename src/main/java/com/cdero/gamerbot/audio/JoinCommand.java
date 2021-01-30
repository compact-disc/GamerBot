package com.cdero.gamerbot.audio;

//import statements
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class JoinCommand {
	
	/**
	 * Operations that are run on the join command.
	 * 
	 * @param event	Information about the event including the voice channel and text channel.
	 */
	protected JoinCommand(GuildMessageReceivedEvent event) {
		
		VoiceChannel voiceChannel = event.getMember().getVoiceState().getChannel();
		TextChannel textChannel = event.getChannel();
		
		if(voiceChannel == null) {
			
			textChannel.sendMessage(":x: **You are not connected to a voice channel!**").queue();
			return;
			
		}
		
		if(event.getGuild().getAudioManager().isConnected()) {
			
			textChannel.sendMessage(":x: **I am already connected!**").queue();
			return;
			
		}else {
			
			try {
				
				AudioManager audioManager = event.getGuild().getAudioManager();
				audioManager.openAudioConnection(voiceChannel);
				
			} catch (IllegalStateException e) {
				
				textChannel.sendMessage(":x: **Error connecting to voice!**").queue();

				
			} catch (IllegalArgumentException e) {
				
				textChannel.sendMessage(":x: **Error connecting to voice!**").queue();
				
			} catch (UnsupportedOperationException e) {
				
				textChannel.sendMessage(":x: **Error connecting to voice!**").queue();
				
			} finally {
				
				textChannel.sendMessage(":white_check_mark: **Connected to voice!**").queue();
				
			}
			
		}
		
	}

}
