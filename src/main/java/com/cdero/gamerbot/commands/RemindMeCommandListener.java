package com.cdero.gamerbot.commands;

//import statements
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class RemindMeCommandListener extends ListenerAdapter {
	
	/**
	 * Logger for the RemindMeListener class.
	 */
	private final static Logger log = Logger.getLogger(RemindMeCommandListener.class.getPackage().getName());
	
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
		
		User author = event.getAuthor();
		
		if(author.isBot()) {
			
			return;
			
		}
		
		Guild guild = event.getGuild();
		
		StringBuilder rawReminder;
		String[] command = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		
		TextChannel channel = event.getChannel();
		
		String time;
		Long queueTime;
		
		if(command[0].equals(PREFIX + "remind") && command[1].equals("me")) {
			
			channel.sendMessage("```" + "Usage: Create a reminder on the present day given an exact time.\n" + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nSupported Zones: EST/EDT/CST/CDT/PST/PDT\nExample: " + PREFIX + "remindme Play Games! => 2:00.PM.EST" + "```").queue();
			log.info("Command: " + PREFIX + "remindme"
					+ "\nGuild: " + guild.toString()
					+ "\nChannel: " + channel.toString()
					+ "\nAuthor: " + author.toString());
			
		}
		
		if(command[0].equals(PREFIX + "remindme")) {
			
			if(commandLength == 1 || command[1].equals("help")) {
				
				channel.sendMessage("```" + "Usage: Create a reminder on the present day given an exact time.\n" + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nSupported Zones: EST/EDT/CST/CDT/PST/PDT\nExample: " + PREFIX + "remindme Play Games! => 2:00.PM.EST" + "```").queue();
				log.info("Command: " + PREFIX + "remindme"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else if (command[commandLength - 2].equals("=>")) {
				
				rawReminder = new StringBuilder();
				
				time = command[commandLength - 1];
				
				for(int i = 1; i < commandLength - 2; i++) {
					
					rawReminder.append(command[i] + " ");
					
				}
				
				queueTime = getTime(time);
				
				if(queueTime == -1) {
					
					channel.sendMessage("<@!" + author.getIdLong() + ">, " + "there was an issue adding your reminder! Check your input!").queue();
					log.warning("Error with the remindme command!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					return;
					
				}
				
				try {
					
					channel.sendMessage("<@!" + author.getIdLong() + ">, " + rawReminder.toString()).queueAfter(queueTime, TimeUnit.MILLISECONDS);
					log.info("Command: " + PREFIX + "remindme"
							+ "\nTime: " + queueTime + "ms"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				} catch (ErrorResponseException e) {
					
					channel.sendMessage("<@!" + author.getIdLong() + ">, " + "there was an issue adding your reminder! Check your input!").queue();
					log.warning("Error with the remindme command!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				} catch (IllegalArgumentException e) {
					
					channel.sendMessage("<@!" + author.getIdLong() + ">, " + "there was an issue adding your reminder! Check your input!").queue();
					log.warning("Error with the remindme command!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				} finally {
					
					channel.sendMessage("<@!" + author.getIdLong() + ">, " + "your reminder has been scheduled!").queue();
					log.warning("Error with the remindme command!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
					
				
			}else {
				
				channel.sendMessage("```" + "Usage: Create a reminder on the present day given an exact time.\n" + PREFIX + "remindme [task] => [time].[AM/PM].[zone]\nSupported Zones: EST/EDT/CST/CDT/PST/PDT\nExample: " + PREFIX + "remindme Play Games! => 2:00.PM.EST" + "```").queue();
				log.info("Command: " + PREFIX + "remindme"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
	/**
	 * Does input validation on the time parameter before calculation to avoid errors. Then calls calculateTime to calculate the difference in time, in milliseconds.
	 * 
	 * @param completeTime	The full [time].[AM/PM].[zone] in a String format.
	 * @return	long	The time in milliseconds.
	 */
	private long getTime(String completeTime) {
		
		int hoursInt;
		int minutesInt;
		
		String[] timeAndZone = completeTime.split("\\.");
		
		if(timeAndZone.length != 3) {
			
			return -1;
			
		}
		
		String time = timeAndZone[0];
		String ampm = timeAndZone[1];
		String zone = timeAndZone[2];
		
		String[] timeNumerals = time.split(":");
		
		if(timeNumerals.length != 2) {
			
			return -1;
			
		}
		
		String hoursString = timeNumerals[0];
		String minutesString = timeNumerals[1];
		
		String ZoneIdString = null;
		
		try {
			
			hoursInt = Integer.parseInt(hoursString);
			minutesInt = Integer.parseInt(minutesString);
			
		} catch (NumberFormatException e) {
			
			return -1;
			
		}

		
		if(!ampm.equalsIgnoreCase("am") && !ampm.equalsIgnoreCase("pm")) {
			
			return -1;
			
		}
		
		if(ampm.equalsIgnoreCase("pm")) {
			
			hoursInt += 12;
			
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
			default:
				return -1;
		
		}
		
		return calculateTime(hoursInt, minutesInt, ZoneIdString, ampm);
		
	}
	
	/**
	 * Calculates the difference between the given time in UTC and the system local time in UTC. Returns the value in milliseconds from now.
	 * 
	 * 
	 * @param hoursIntMilitary	Integer value of the hours, in 24 hour format.
	 * @param minutesInt	Integer value of the minutes
	 * @param ZoneIdString	The time zone in a string format.
	 * @param ampm	Value is either AM or PM for time.
	 * @return	long	The time, in milliseconds, until the reminder will be run.
	 */
	private long calculateTime(int hoursInt, int minutesInt, String ZoneIdString, String ampm) {
		
		LocalDateTime givenDateTime = LocalDate.now().atTime(hoursInt, minutesInt);
		
		ZonedDateTime currentTimeUTC = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"));
		ZonedDateTime givenTime = givenDateTime.atZone(ZoneId.of(ZoneIdString));
		ZonedDateTime givenTimeUTC = givenTime.withZoneSameInstant(ZoneId.of("UTC"));

		return currentTimeUTC.until(givenTimeUTC, ChronoUnit.MILLIS);
		
	}

}
