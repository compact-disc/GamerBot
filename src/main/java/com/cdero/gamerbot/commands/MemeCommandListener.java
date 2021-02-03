package com.cdero.gamerbot.commands;

//import statements
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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
public class MemeCommandListener extends ListenerAdapter{
	
	/**
	 * Logger for the MemeCommandListener class.
	 */
	private final static Logger log = LogManager.getLogger(MemeCommandListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * String array of RSS feeds to pull images from.
	 */
	private final String[] JSON_URL_FEEDS_ARRAY = {"https://www.reddit.com/r/memes/rising.json"};
	
	/**
	 * Length of the array that holds the RSS feeds to choose from.
	 */
	private final int JSON_URL_FEEDS_LENGTH = JSON_URL_FEEDS_ARRAY.length;
	
	/**
	 * Overridden method to receive and respond the meme function.
	 * 
	 * @param		event	includes information about the event.
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
		
		if(command[0].equalsIgnoreCase(PREFIX + "meme")) {
			
			TextChannel channel = event.getChannel();
			
			if(commandLength == 1) {
				
				InputStream image = getMemeInputStream();
				
				if(image != null) {
					
					//Send all images as JPEG because size should be smaller, reduce overall traffic and system load.
					channel.sendFile(image, "meme.jpg").queue();
					
					log.info("Command: " + PREFIX + "meme"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}else {
					
					channel.sendMessage(":x: There was an error processing a meme for you!");
					log.error("Error getting meme"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
				
			}else if(commandLength == 2 && command[1].equalsIgnoreCase(PREFIX + "help")) {
				
				channel.sendMessage(":white_check_mark: **Usage: Get a meme from the internet."
						+ "\n" 
						+ "\n" + PREFIX + "meme"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "meme"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else {
				
				channel.sendMessage(":white_check_mark: **Usage: Get a meme from the internet."
						+ "\n" 
						+ "\n" + PREFIX + "meme"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "meme"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
	/**
	 * Returns the input stream for an image found online.
	 * 
	 * @return	The input stream for an image found online.
	 */
	private InputStream getMemeInputStream() {
		
		try {
			
			//Open an input stream to get the image data from Reddit, then return the input stream.
			return new URL(getRandomMemeURLFromReddit()).openStream();
			
		} catch (IOException e) {
			
			log.error("IO Error Getting Image");
			
		}
		
		//Return null if there is an error getting the Input Stream.
		return null;
		
	}
	
	/**
	 * Randomly gets a Reddit post URL image link and returns it in a string format.
	 * 
	 * 
	 * @return	A string formatted URL to an image.
	 */
	private String getRandomMemeURLFromReddit() {
		
		//Takes the length of the URL array and gets a random number between 0 and the length.
		int randomURLPosition = new Random().nextInt(JSON_URL_FEEDS_LENGTH);
		
		//Integer to hold the position of the children posts, will get updated with another random number, but defaults to zero.
		int randomPost = 0;
		
		//String of the URL, defaults to null and is what is returned.
		String imageURL = null;
		
		try {
			
			//Set the URL class to a random URL in the URL's array
			URL url = new URL(JSON_URL_FEEDS_ARRAY[randomURLPosition]);
			
			//Open the URLConnection over the given URL.
			URLConnection conn = url.openConnection();
			
			//Set the Request Property to Mozilla and a User-Agent to not be HTTP:429
			conn.addRequestProperty("User-Agent", "Mozilla/5.0");
			
			//Connect to the site.
			conn.connect();
			
			//Get the input stream from the URLConnection
			InputStream input = conn.getInputStream();
			
			//Setup the Stream reader from the input stream.
			InputStreamReader ireader = new InputStreamReader(input);
			
			//Setup the buffered reader from the input stream reader.
			BufferedReader reader = new BufferedReader(ireader);
			
			//Setup the string builder to have the results of the JSON appended to it.
			StringBuilder sb = new StringBuilder();
			
			//String to hold each line of the buffered reader.
			String str;
			
			//Keep reading from the buffered reader until it is null.
			while((str = reader.readLine()) != null) {
				
				//Append the line to the string builder.
				sb.append(str);
				
			}
			
			//Set a JSOB object to the entire returned JSON response
			JSONObject entireJson = new JSONObject(sb.toString());
			
			//Setup another JSOB object to get the data from the response.
			JSONObject jsonLayer1 = entireJson.getJSONObject("data");
			
			//Get the number of posts given in the response to choose from.
			int postingLength = jsonLayer1.getInt("dist");
			
			//Choose a random post between 0 and the post length, usually zero to 25 or 26.
			randomPost = new Random().nextInt(postingLength);
			
			//Get a JSON array of all the posts
			JSONArray children = jsonLayer1.getJSONArray("children");
			
			//Get an individual post from the JSON array at a random position
			JSONObject post = children.getJSONObject(randomPost);
			
			//Get the data from the post in another JSON object
			JSONObject postData = post.getJSONObject("data");
			
			//Check if the post an image, if not keep iterating through the JSON objects/array until we find one.
			while(!postData.getString("post_hint").equalsIgnoreCase("image")) {
				
				randomPost = new Random().nextInt(postingLength);
				children = jsonLayer1.getJSONArray("children");
				
				post = children.getJSONObject(randomPost);
				postData = post.getJSONObject("data");
				
			}
			
			//Get the URL of the image posted in a string format.
			imageURL = postData.getString("url_overridden_by_dest");
			
		} catch (IOException e) {
			
			log.error("Error reading JSON from Reddit");
			
		}
		
		// Return the URL of the image in a String format. It will still return if null and error in the next method.
		return imageURL;
	}

}
