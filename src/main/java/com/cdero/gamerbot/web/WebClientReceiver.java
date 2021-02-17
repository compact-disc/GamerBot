package com.cdero.gamerbot.web;

//import statements
import java.io.*;
import java.net.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author 		Christopher DeRoche
 * @version		1.0
 * @since		1.0
 *
 */
public class WebClientReceiver extends Thread {

	/**
	 * Logger for the WebClientReceiver class.
	 */
	private final static Logger log = LogManager.getLogger(WebClientReceiver.class.getName());

	/**
	 * Port number for the socket, 5000
	 */
	private final int PORT = 5000;

	/**
	 * The server socket for the application
	 */
	private ServerSocket receiverSocket;
	
	/**
	 * The receiver socket to get data from Spring
	 */
	private Socket receiver;
	
	/**
	 * A data input stream to read the data from the socket
	 */
	private DataInputStream dataInput; 

	/**
	 * Create the server socket and bind it to port 5000
	 * 
	 * @throws IOException	When there is an error creating the socket
	 */
	public WebClientReceiver() throws IOException {

		receiverSocket = new ServerSocket();
		receiverSocket.setReuseAddress(true);
		receiverSocket.bind(new InetSocketAddress(PORT));

	}

	/**
	 * Method to run when the Thread.start() function is called. This is so the application can get data from Spring separately from running.
	 */
	public void run() {
		
		while(true) {
			
			try {
				
				receiver = receiverSocket.accept();
				
				dataInput = new DataInputStream(receiver.getInputStream());
				
				String data;
				
				while(receiver.isConnected()) {
					
					if((data = dataInput.readUTF()) != null) {
						
						log.info(data);
						
					}
					
				}
				
				receiver.close();
				
			} catch (IOException e) {

				log.fatal("Gamer Bot Spring Error! Is the application still connected?");
				
			}
			
		}

	}

}
