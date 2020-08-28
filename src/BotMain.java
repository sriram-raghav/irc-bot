/*
 * The BotMain class creates a Bot and connects it to the server
 */
public class BotMain {
	public static final String CONNECTION_URL = "irc.freenode.net";
	public static final String CHANNEL_NAME = "#rsriram2336";
	
	public static void main(String[] args) throws Exception 
	{
		//Creates a bot, allows it to speak and connects it to the server and channel
		Bot firstBot = new Bot();
		firstBot.setVerbose(true);
		firstBot.connect(CONNECTION_URL);
		firstBot.joinChannel(CHANNEL_NAME);
		
		//Tells the user to type the help command when they first get the bot
		firstBot.sendMessage(CHANNEL_NAME, "Hello! Please type !help to get started.");
	}

}
