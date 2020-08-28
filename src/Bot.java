/*
 * The Bot class sets up the skeleton for the IRC Bot 
 * and outlines its actions
 */
import org.jibble.pircbot.*;

public class Bot extends PircBot 
{
	/*
	 * Constructs a bot and sets the name
	 */
	public Bot() 
	{
		this.setName("MrBot");
	}
	
	/*
	 * Activates when a user sends a message, and controls the control flow regarding
	 * what message to output to the user 
	 * 
	 * @param channel the current channel that the bot is in
	 * @param sender the sender of the message
	 * @param login the login of the message sender
	 * @param hostname the hostname of the message sender
	 * @param message The message sent to the channel
	 */
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		//Controls the various commands of the bot and what to output if the are entered
		if(message.toLowerCase().startsWith("!help"))
		{
			//The help menu when the user firsts starts the bot
			sendMessage(channel, "COMMANDS:"); 
			sendMessage(channel,"!weather <location>: Shows a short weather summary from the given location. " +
								 "The location can be entered in the following formats: " + 
								 "<city> <zipcode,alpha-2 country code> <state> <country> <zipcode>. " +
								 "The more specific the input, the less potential ambiguity in the result.");
			sendMessage(channel, "!superhero <hero>: Gives information about a superhero of the user's choice.");
		}
		else if(message.toLowerCase().startsWith("!weather"))
		{
			//Gets the locationInfo and passes it to the WeatherData class
			String locationInfo = message.substring("!weather".length() + 1).toLowerCase();
			System.out.print(locationInfo);
			sendMessage(channel, sender + ", " + " " + WeatherData.getWeatherData(locationInfo.trim()));
		}
		else if(message.toLowerCase().startsWith("!superhero"))
		{
			//Gets the heroInfo and passes it to the SuperheroData class
			String heroInfo = message.substring("!superhero".length() + 1).toLowerCase();
			sendMessage(channel, sender + ", " + SuperheroData.getSuperheroData(heroInfo.trim()));
		}
		else if (message.equalsIgnoreCase("!time")) 
        {
            String time = new java.util.Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }
		else if(message.substring(0, 1).equals("!"))
		{
			//Tells user to enter valid command if none of the other "ifs" are hit
			sendMessage(channel, sender + ", Please enter a valid command!");
		}
	}
}
