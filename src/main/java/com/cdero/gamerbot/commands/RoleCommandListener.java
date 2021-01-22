package com.cdero.gamerbot.commands;

//import statements
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.Permission;
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
public class RoleCommandListener extends ListenerAdapter {

	/**
	 * Logger for the RoleCommandListener class.
	 */
	private final static Logger log = LogManager.getLogger(RoleCommandListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to the timer command. Users can have a task and timer to be notified later.
	 * 
	 * @param		event	includes information about the event.
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		
		User author = event.getAuthor();
		
		if (author.isBot()) {
			
			return;
			
		}
		
		TextChannel channel = event.getChannel();
		Guild guild = event.getGuild();
		
		String[] command = event.getMessage().getContentRaw().split(" ");
		int commandLength = command.length;
		
		if (command[0].equalsIgnoreCase(PREFIX + "role")) {
			
			if (commandLength == 1 || (command[1].equalsIgnoreCase("help") && commandLength == 2)) {
				
				channel.sendMessage("```" + "Usage: Modify a user's roles.\n" + PREFIX + "role [add/remove/help] [role] [user]\nExample: " + PREFIX + "role add developer compact-disc" + "```").queue();
				log.info("Command: " + PREFIX + "role"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else if (command[1].equalsIgnoreCase("add") && commandLength == 4) {
				
				if(guild.getMember(author).hasPermission(Permission.MANAGE_ROLES)) {
					
					try {
						
						guild.addRoleToMember(event.getMessage().getMentionedMembers().get(0), guild.getRolesByName(command[2], true).get(0)).queue();
						log.info("Command: " + PREFIX + "role"
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						
					} catch (IllegalArgumentException e) {
						
						channel.sendMessage("Error adding role to user!").queue();
						log.warn("Error adding role to user!"
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						
					}
						
					
				} else {
					
					channel.sendMessage("You don't have permission to do that!").queue();
					log.warn("User does not have permission to do that!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
				
			}else if (command[1].equalsIgnoreCase("remove") && commandLength == 4) {
				
				if(guild.getMember(author).hasPermission(Permission.MANAGE_ROLES)) {
					
					try {
						
						guild.removeRoleFromMember(event.getMessage().getMentionedMembers().get(0), guild.getRolesByName(command[2], true).get(0)).queue();
						log.info("Command: " + PREFIX + "role"
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						
					} catch (IllegalArgumentException e) {
						
						channel.sendMessage("Error removing role from user!").queue();
						log.warn("Error removing role from user!"
								+ "\nGuild: " + guild.toString()
								+ "\nChannel: " + channel.toString()
								+ "\nAuthor: " + author.toString());
						
					}
					
				} else {
					
					channel.sendMessage("You don't have permission to do that!").queue();
					log.warn("User does not have permission to do that!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
				
			} else {
				
				channel.sendMessage("```" + "Usage: Modify a user's roles.\n" + PREFIX + "role [add/remove/help] [role] [user]\nExample: " + PREFIX + "role add developer compact-disc" + "```").queue();
				log.info("Command: " + PREFIX + "role"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
}
