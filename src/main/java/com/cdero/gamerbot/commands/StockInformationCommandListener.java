package com.cdero.gamerbot.commands;

//import statements
import java.awt.Color;
import java.io.IOException;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class StockInformationCommandListener extends ListenerAdapter {
	
	/**
	 * Logger for the StockInformationCommandListener class.
	 */
	private final static Logger log = LogManager.getLogger(StockInformationCommandListener.class.getName());
	
	/**
	 * Prefix for the application commands on client side.
	 */
	private final String PREFIX = ">>";
	
	/**
	 * Overridden method to receive and respond to the stock command. It will reply with a stock based on the symbol.
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
		
		if(command[0].equalsIgnoreCase(PREFIX + "stocks") || command[0].equalsIgnoreCase(PREFIX + "stonks") || command[0].equalsIgnoreCase(PREFIX + "stock") || command[0].equalsIgnoreCase(PREFIX + "stonk")) {
			
			TextChannel channel = event.getChannel();
			
			if(commandLength == 1 || command[1].equalsIgnoreCase("help") && commandLength == 2) {
				
				channel.sendMessage(":white_check_mark: **Usage: Get information about stocks from Yahoo! Finance."
						+ "\n" 
						+ "\n" + PREFIX + "stocks [symbol] <symbol> <symbol> <symbol> <symbol>"
						+ "\nExamples:"
						+ "\n" + PREFIX + "stocks gme"
						+ "\n" + PREFIX + "stonks tsla gme amc mmm aapl"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "stocks"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}else if(commandLength >= 2 && commandLength <= 6 && !command[1].equalsIgnoreCase("help")) {
				
				try {
					
					channel.sendMessage(getStockInformation(command)).queue();
					
					log.info("Command: " + PREFIX + "stocks"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				} catch (IOException e) {
					
					channel.sendMessage(":x: **There was an error retrieving that stock information for you!**").queue();
					
					log.error("IOException when getting stock information from Yahoo! Finance!"
							+ "\nGuild: " + guild.toString()
							+ "\nChannel: " + channel.toString()
							+ "\nAuthor: " + author.toString());
					
				}
				
			}else {
				
				channel.sendMessage(":white_check_mark: **Usage: Get information about stocks from Yahoo! Finance."
						+ "\n" 
						+ "\n" + PREFIX + "stocks [symbol] <symbol> <symbol> <symbol> <symbol>"
						+ "\nExamples:"
						+ "\n" + PREFIX + "stocks gme"
						+ "\n" + PREFIX + "stonks tsla gme amc mmm aapl"
						+ "**").queue();
				
				log.info("Command: " + PREFIX + "stocks"
						+ "\nGuild: " + guild.toString()
						+ "\nChannel: " + channel.toString()
						+ "\nAuthor: " + author.toString());
				
			}
			
		}
		
	}
	
	/**
	 * 
	 * Method to return a fully built embed with stock information provided by Yahoo! Finance.
	 * 
	 * @param stocks	Array of stock symbols in a String format.
	 * @return	A completely built Discord embed to be sent.
	 * @throws IOException	When there is a connection problem when getting the stocks information from the Yahoo! Finance API.
	 */
	private MessageEmbed getStockInformation(String[] stocks) throws IOException {
		
		EmbedBuilder embedBuilder = new EmbedBuilder();
		
		Map<String, Stock> stocksMap = YahooFinance.get(stocks);
		
		String name;
		String price;
		String currency;
		String stockExchange;
		String dividend;
		String stats;
		
		for(Stock s : stocksMap.values()) {
			
			name = s.getName() == null ? "N/A" : s.getName();
			price = s.getQuote().toString() == null ? "N/A" : s.getQuote().toString();
			currency = s.getCurrency() == null ? "N/A" : s.getCurrency();
			stockExchange = s.getStockExchange() == null ? "N/A" : s.getStockExchange();
			dividend = s.getDividend().toString() == null ? "N/A" : s.getDividend().toString();
			stats = s.getStats().toString() == null ? "N/A" : s.getStats().toString();
			
			embedBuilder.addField(s.getSymbol(), "Name: " + name
								+ "\nPrice - " + price
								+ "\nCurrency - " + currency
								+ "\nStock Exchange - " + stockExchange
								+ "\nDividend - " + dividend
								+ "\nStats - " + stats
								, true);
			
			embedBuilder.addBlankField(true);
			embedBuilder.addBlankField(true);
			
		}
		
		if(embedBuilder.isEmpty()) {
			
			embedBuilder.setColor(Color.RED);
			embedBuilder.setDescription("No valid stock information was given!");
			embedBuilder.setTitle("Gamer Bot Stocks");
			
			return embedBuilder.build();
			
		}else {
			
			embedBuilder.setColor(Color.GREEN);
			embedBuilder.setAuthor("From Yahoo! Finance");
			embedBuilder.setDescription("Any invalid stock symbols have been skipped!");
			embedBuilder.setTitle("Gamer Bot Stocks");
			
			return embedBuilder.build();
			
		}
		
		
		
	}

}
