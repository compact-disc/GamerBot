package com.cdero.gamerbot.messagelisteners;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
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
		StringBuilder task;
		
		String[] totalTime = command[command.length - 1].split(".");;
		String timeValue;
		String timeNumeral;
		TimeUnit timeUnit = null;
		
		if(command[0].equals(PREFIX + "timer") && !event.getAuthor().isBot()) {
			
			if(command.length == 1 || (command[1].equals("help") && command.length == 2)) {
				
				event.getChannel().sendMessage("```" + "Usage: " + PREFIX + "timer [task] => [value].[hours/minutes/seconds]\nExample: " + PREFIX + "timer Play Games! => 4.hours" + "```").queue();
				
			}else if(command[command.length - 2].equals("=>") && (command[command.length - 1].contains(".hours") || command[command.length - 1].contains(".minutes") || command[command.length - 1].contains(".seconds"))) {
				
				task = new StringBuilder();
				
				for(int i = 1; i < command.length - 2; i++) {
					
					task.append(command[i] + " ");
					
				}
				
				timeValue = totalTime[1];
				timeNumeral = totalTime[0];
				
				switch(timeValue) {
				
					case "hours":
						timeUnit = TimeUnit.HOURS;
						break;
						
					case "minutes":
						timeUnit = TimeUnit.MINUTES;
						break;
						
					case "seconds":
						timeUnit = TimeUnit.SECONDS;
						break;
				
				}
				
				event.getChannel().sendMessage(task.toString()).queueAfter(Integer.parseInt(timeNumeral), timeUnit);
				//channel.sendMessage("This is the reminder").mention(event.getAuthor()).queueAfter(1, TimeUnit.MINUTES);
				
			}else {
				
				event.getChannel().sendMessage("```" + "Usage: " + PREFIX + "timer [task] => [value].[hours/minutes/seconds]\nExample: " + PREFIX + "timer Play Games! => 4.hours" + "```").queue();
				
			}
			
		}
		
	}
	
}
