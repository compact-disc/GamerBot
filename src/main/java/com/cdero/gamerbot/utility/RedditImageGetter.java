package com.cdero.gamerbot.utility;

//import statements
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 
 * @author Christopher DeRoche
 * @version 1.0
 * @since 1.0
 *
 */
public class RedditImageGetter {

	/**
	 * Takes in an array of subreddit URL's and returns an InputStream of an image from a random post.
	 * 
	 * @param subredditLinkArray	The array containing subreddit URLs
	 * @param subredditLinkArrayLength	The length of the subreddit URL array
	 * @return	An InputStream of an image.
	 * @throws IOException	When there is an issue getting the image or parsing json.
	 */
	public static InputStream getImageLink(String[] subredditLinkArray, int subredditLinkArrayLength) throws IOException {

		// Takes the length of the URL array and gets a random number between 0 and the
		// length.
		int randomURLPosition = new Random().nextInt(subredditLinkArrayLength);

		// Integer to hold the position of the children posts, will get updated with
		// another random number, but defaults to zero.
		int randomPost = 0;

		// String of the URL, defaults to null and is what is returned.
		String imageURL = null;

		// Set the URL class to a random URL in the URL's array
		URL url = new URL(subredditLinkArray[randomURLPosition]);

		// Open the URLConnection over the given URL.
		URLConnection conn = url.openConnection();

		// Set the Request Property to Mozilla and a User-Agent to not be HTTP:429
		conn.addRequestProperty("User-Agent", "Mozilla/5.0");

		// Connect to the site.
		conn.connect();

		// Get the input stream from the URLConnection
		InputStream input = conn.getInputStream();

		// Setup the Stream reader from the input stream.
		InputStreamReader ireader = new InputStreamReader(input);

		// Setup the buffered reader from the input stream reader.
		BufferedReader reader = new BufferedReader(ireader);

		// Setup the string builder to have the results of the JSON appended to it.
		StringBuilder sb = new StringBuilder();

		// String to hold each line of the buffered reader.
		String str;

		// Keep reading from the buffered reader until it is null.
		while ((str = reader.readLine()) != null) {

			// Append the line to the string builder.
			sb.append(str);

		}

		// Set a JSOB object to the entire returned JSON response
		JSONObject entireJson = new JSONObject(sb.toString());

		// Setup another JSOB object to get the data from the response.
		JSONObject jsonLayer1 = entireJson.getJSONObject("data");

		// Get the number of posts given in the response to choose from.
		int postingLength = jsonLayer1.getInt("dist");

		// Choose a random post between 0 and the post length, usually zero to 25 or 26.
		randomPost = new Random().nextInt(postingLength);

		// Get a JSON array of all the posts
		JSONArray children = jsonLayer1.getJSONArray("children");

		// Get an individual post from the JSON array at a random position
		JSONObject post = children.getJSONObject(randomPost);

		// Get the data from the post in another JSON object
		JSONObject postData = post.getJSONObject("data");

		// Check if the post an image, if not keep iterating through the JSON
		// objects/array until we find one.
		while (!postData.getString("post_hint").equalsIgnoreCase("image")) {

			randomPost = new Random().nextInt(postingLength);
			children = jsonLayer1.getJSONArray("children");

			post = children.getJSONObject(randomPost);
			postData = post.getJSONObject("data");

		}

		// Get the URL of the image posted in a string format.
		imageURL = postData.getString("url_overridden_by_dest");

		//Returns an InputStream of the image from Reddit given the random imageURL that is generated above
		return new URL(imageURL).openStream();

	}

}
