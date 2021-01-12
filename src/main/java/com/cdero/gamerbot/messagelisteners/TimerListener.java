package com.cdero.gamerbot.messagelisteners;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TimerListener extends ListenerAdapter {

	/**
	 * Logger for the MessageListener class.
	 */
	private final static Logger log = Logger.getLogger(TimerListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to the timer command. Users can have a task and timer to be notified later.
	 * 
	 * @param		event	includes information about the event.
	 * @return		void
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		String[] command = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		
		TextChannel channel = event.getChannel();
		
		if(command[0].equalsIgnoreCase(PREFIX + "timer") && !event.getAuthor().isBot()) {
			
			if(commandLength == 1 || (command[1].equalsIgnoreCase("help") && commandLength == 2)) {
				
				channel.sendMessage("```" + "Usage: " + PREFIX + "timer [task] => [value].[hours/minutes/seconds]\nExample: " + PREFIX + "timer Play Games! => 4.hours" + "```").queue();
				
			}else if(command[commandLength - 2].equals("=>") && (command[commandLength - 1].contains(".hours") || command[commandLength - 1].contains(".hour") || command[commandLength - 1].contains(".minutes") || command[commandLength - 1].contains(".minute") || command[commandLength - 1].contains(".seconds") || command[commandLength - 1].contains(".second"))) {
				
				StringBuilder task = new StringBuilder();
				String[] totalTime = command[commandLength - 1].split("\\.");
				String timeValue = "";
				String timeNumeral = "";
				TimeUnit timeUnit = null;
				
				Long author = event.getAuthor().getIdLong();
				
				for(int i = 1; i < commandLength - 2; i++) {
					
					task.append(command[i] + " ");
					
				}
				
				timeNumeral = totalTime[0];
				timeValue = totalTime[1];
				
				switch(timeValue) {
				
					case "hours":
						timeUnit = TimeUnit.HOURS;
						break;
					
					case "hour":
						timeUnit = TimeUnit.HOURS;
						break;
						
					case "minutes":
						timeUnit = TimeUnit.MINUTES;
						break;
						
					case "minute":
						timeUnit = TimeUnit.MINUTES;
						break;
						
					case "seconds":
						timeUnit = TimeUnit.SECONDS;
						break;
						
					case "second":
						timeUnit = TimeUnit.SECONDS;
						break;
				
				}
				
				try {
					
					channel.sendMessage("<@!" + author + ">, " + task.toString()).queueAfter(Integer.parseInt(timeNumeral), timeUnit);
					
				} catch (ErrorResponseException e){
					
					channel.sendMessage("<@!" + author + ">" + ", there was an error creating your timer!").queue();
					
				} catch (IllegalArgumentException e) {
					
					channel.sendMessage("<@!" + author + ">" + ", there was an error creating your timer!").queue();
					
				} finally {
					
					channel.sendMessage("<@!" + author + ">" + ", you will be notified in " + timeNumeral + " " + timeValue + "!").queue();
					
				}
				
				
			}else {
				
				channel.sendMessage("```" + "Usage: " + PREFIX + "timer [task] => [value].[hours/minutes/seconds]\nExample: " + PREFIX + "timer Play Games! => 4.hours" + "```").queue();
				
			}
			
		}
		
	}
	
}
