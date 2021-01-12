package com.cdero.gamerbot.messagelisteners;

import java.time.ZoneId;
import java.util.TimeZone;
import java.util.logging.Logger;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RemindMeListener extends ListenerAdapter {
	
	/**
	 * Logger for the RemindMeListener class.
	 */
	private final static Logger log = Logger.getLogger(RemindMeListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to the remind me command. This will setup reminders that users want.
	 * 
	 * @param		event	includes information about the event.
	 * @return		void
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		StringBuilder rawReminder;
		String[] command = event.getMessage().getContentRaw().split(" ");
		
		String time;
		long timeDifference;
		
		if(command[0].equals(PREFIX + "remind") && command[1].equals("me") && !event.getAuthor().isBot()) {
			
			event.getChannel().sendMessage("```" + "Try >>remindme\nUsage: " + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nExample: " + PREFIX + "remindme Play Games! => 7:00.PM.EST" + "```").queue();
			
		}
		
		if(command[0].equals(PREFIX + "remindme") && !event.getAuthor().isBot()) {
			
			if(command.length == 1 || command[1].equals("help")) {
				
				event.getChannel().sendMessage("```" + "Usage: " + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nExample: " + PREFIX + "remindme Play Games! => 7:00.PM.EST" + "```").queue();
				
			}else if (command[command.length - 2].equals("=>")) {
				
				rawReminder = new StringBuilder();
				
				time = command[command.length - 1];
				
				for(int i = 1; i < command.length - 2; i++) {
					
					rawReminder.append(command[i]);
					
				}
				
				timeDifference = calculateTime(time);
				
				
				//channel.sendMessage("This is the reminder").mention(event.getAuthor()).queueAfter(1, TimeUnit.MINUTES);
				
			}else {
				
				event.getChannel().sendMessage("```" + "Usage: " + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nExample: " + PREFIX + "remindme Play Games! => 7:00.PM.EST" + "```").queue();
				
			}
			
		}
		
	}
	
	private long calculateTime(String completeTime) {
		
		String[] timeAndZone = completeTime.split(".");
		String time = timeAndZone[0];
		String ampm = timeAndZone[1];
		String zone = timeAndZone[2];
		
		ZoneId system = ZoneId.systemDefault();
		ZoneId user = ZoneId.of("");
		
		return 0;
		
	}

}
