package com.cdero.gamerbot.audio;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand {
	
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
