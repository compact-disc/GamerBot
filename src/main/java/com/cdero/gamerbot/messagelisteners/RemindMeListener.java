package com.cdero.gamerbot.messagelisteners;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.dv8tion.jda.api.entities.TextChannel;
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
		
		if(event.getAuthor().isBot()) {
			
			return;
			
		}
		
		StringBuilder rawReminder;
		String[] command = event.getMessage().getContentRaw().split(" ");
		
		TextChannel channel = event.getChannel();
		
		String time;
		
		if(command[0].equals(PREFIX + "remind") && command[1].equals("me")) {
			
			channel.sendMessage("```" + "Usage: " + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nSupported Zones: EST/EDT/CST/CDT/PST/PDT\nExample: " + PREFIX + "remindme Play Games! => 2:00.PM.EST" + "```").queue();
			
		}
		
		if(command[0].equals(PREFIX + "remindme")) {
			
			if(command.length == 1 || command[1].equals("help")) {
				
				channel.sendMessage("```" + "Usage: " + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nSupported Zones: EST/EDT/CST/CDT/PST/PDT\nExample: " + PREFIX + "remindme Play Games! => 2:00.PM.EST" + "```").queue();
				
			}else if (command[command.length - 2].equals("=>")) {
				
				rawReminder = new StringBuilder();
				
				time = command[command.length - 1];
				
				for(int i = 1; i < command.length - 2; i++) {
					
					rawReminder.append(command[i] + " ");
					
				}
					
				channel.sendMessage("<@!" + event.getAuthor().getIdLong() + ">, " + rawReminder.toString()).queueAfter(calculateTime(time), TimeUnit.MILLISECONDS);
				
			}else {
				
				channel.sendMessage("```" + "Usage: " + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nSupported Zones: EST/EDT/CST/CDT/PST/PDT\nExample: " + PREFIX + "remindme Play Games! => 2:00.PM.EST" + "```").queue();
				
			}
			
		}
		
	}
	
	/**
	 * Return the time difference in milliseconds from the given time and the current system time.
	 * 
	 * @param completeTime	The full [time].[AM/PM].[zone] in a String format.
	 * @return	long	The time in milliseconds.
	 */
	private long calculateTime(String completeTime) {
		
		String[] timeAndZone = completeTime.split("\\.");
		String time = timeAndZone[0];
		String ampm = timeAndZone[1];
		String zone = timeAndZone[2];
		
		String[] timeNumerals = time.split(":");
		String hoursString = timeNumerals[0];
		String minutesString = timeNumerals[1];
		
		String ZoneIdString = "";
		
		int hoursInt = Integer.parseInt(hoursString);
		int hoursIntMilitary = hoursInt;
		int minutesInt = Integer.parseInt(minutesString);
		
		if(ampm.equalsIgnoreCase("pm")) {
			
			hoursIntMilitary += 12;
			
		}
		
		///////////////////////////////////////////
		// America/Chicago - CST/CDT
		// America/Indiana/Indianapolis - EST/EDT
		// America/Los_Angeles - PST/PDT
		///////////////////////////////////////////
		
		switch(zone) {
		
			case "CST":
			case "CDT":
				ZoneIdString = "America/Chicago";
				break;
				
			case "EST":
			case "EDT":
				ZoneIdString = "America/Indiana/Indianapolis";
				break;
				
			case "PST":
			case "PDT":
				ZoneIdString = "America/Los_Angeles";
				break;
		
		}
		
		
		LocalDateTime givenDateTime = LocalDate.now().atTime(hoursIntMilitary, minutesInt);
		
		ZonedDateTime currentTimeUTC = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime givenTime = givenDateTime.atZone(ZoneId.of(ZoneIdString));
		ZonedDateTime givenTimeUTC = givenTime.withZoneSameInstant(ZoneId.of("UTC"));

		log.info(">>remindme in " + currentTimeUTC.until(givenTimeUTC, ChronoUnit.MILLIS) + "ms");
		return currentTimeUTC.until(givenTimeUTC, ChronoUnit.MILLIS);
		
	}

}
