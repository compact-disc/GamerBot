package com.cdero.gamerbot.commands;

//import statements
import java.util.logging.Logger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class DiceRollListener extends ListenerAdapter {

	/**
	 * Logger for the DiceRollListener class.
	 */
	private final static Logger log = Logger.getLogger(DiceRollListener.class.getPackage().getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond the roll function, this includes rolling dice.
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
		String[] command = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		
		if(command[0].equalsIgnoreCase(PREFIX + "roll")) {
			
			TextChannel channel = event.getChannel();
			
			if(commandLength == 1 || command[1].equalsIgnoreCase("help") && commandLength == 2) {
				
				channel.sendMessage("```" + "Usage: Roll any dice d, x number of times.\n" + PREFIX + "roll [dice] <times>\nExamples:\n" + PREFIX + "roll d6\n"+ PREFIX + "roll d20 5" + "```").queue();
				log.info("Command: " + PREFIX + "roll"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else if(command[1].startsWith("d") && commandLength == 2 || commandLength == 3) {
				
				int diceNumberInteger = Integer.parseInt(command[1].replace("d", ""));
				int numberOfRolls = 0;
				
				if(commandLength == 3) {
					
					numberOfRolls = Integer.parseInt(command[2]);
					
				}else {
					
					numberOfRolls = 1;
					
				}
				
				channel.sendMessage(rollDice(diceNumberInteger, numberOfRolls)).queue();
				log.info("Command: " + PREFIX + "roll"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else {
				
				channel.sendMessage("```" + "Usage: Roll any dice d, x number of times.\n" + PREFIX + "roll [dice] <times>\nExamples:\n" + PREFIX + "roll d6\n"+ PREFIX + "roll d20 5" + "```").queue();
				log.info("Command: " + PREFIX + "roll"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
	/**
	 * Returns a String with the dice rolls made.
	 * 
	 * 
	 * @param dice	The type of dice to be rolled.
	 * @param rolls	The number of rolls to make with the dice.
	 * @return	String	All of the dice rolls formatted correctly.
	 */
	private String rollDice(int dice, int rolls) {
		
		StringBuilder reply = new StringBuilder();
		int rollResult;
		
		if(rolls == 1) {
			
			reply.append("Roll Result: ");
			rollResult = (int)(Math.random() * dice) + 1;
			reply.append(rollResult);
			
		}else {
			
			reply.append("Roll Results:\n");
			
			for(int i = 1; i <= rolls; i++) {
				
				rollResult = (int)(Math.random() * dice) + 1;
				reply.append("Roll " + i + ": " + rollResult + "\n");
				
				
			}
			
		}
		
		return reply.toString();
		
	}
	
}
