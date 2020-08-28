/*
 * The SuperheroData class gets information from the Superhero API, parses it
 * and formats the data for output
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SuperheroData 
{
	private static final String API_KEY = "10207424040595685";
	private static final String ENDPOINT = "https://superheroapi.com/api/%s/search/%s";
	
	/*
	 * Gets the Superhero data from the Superhero API based on
	 * the name of the superhero passed into the method
	 * 
	 * @param name the name of the superhero
	 * @return the formatted superhero data
	 */
	public static final String getSuperheroData(String name)
	{
		try
		{
			//Builds the URL and forms a new HTTP connection
			URL url = new URL(String.format(ENDPOINT, API_KEY, name));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			//Creates BufferedReader to get the object from the API
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			//Builds and fills StringBuilder containing the JSON object
			String inputLine;
			StringBuilder content = new StringBuilder();
			
			while ((inputLine = in.readLine()) != null) 
			{
			    content.append(inputLine);
			}
			in.close();
			
			//Makes sure it is a valid superhero
			String error = JsonParser.parseString(content.toString()).getAsJsonObject().get("response").getAsString();
			if(error.equals("error"))
				return("Character with give name was not found!");
			
			String result = parseSuperheroData(content.toString(), name);
			return result;
		}
		//Catches potential errors in the API call
		catch(Exception e)
		{
			System.out.print("There was an error in the API call.");
		}
		
		return "There was an error in the API call.";
	}
	
	/*
	 * Takes the raw JSON object from the API and parses it
	 * to obtain the information needed
	 * 
	 * @param objectToParse the JSON object to be parsed
	 * @param heroName the hero name that the user entered
	 * @return the formatted superhero data
	 */
	private static final String parseSuperheroData(String objectToParse, String heroName)
	{
		//Parse the entire json object
		JsonObject obj = JsonParser.parseString(objectToParse).getAsJsonObject();
		
		//Get the array of results
		JsonArray results = obj.getAsJsonArray("results");
		
		//Get the hero that is the same as the user's input from the results array (Default of 0 if no exact match is found)
		int correctIndex = 0;
		for(int i = 0; i < results.size(); i++)
		{
			String currentName = results.get(i).getAsJsonObject().get("name").getAsString();
			System.out.println(currentName);
			if(currentName.equalsIgnoreCase(heroName))
			{
				correctIndex = i;
				break;
			}
		}
		
		//Gets the name, race and publisher from the JSON object
		String fullName = obj.getAsJsonArray("results").get(correctIndex).getAsJsonObject().getAsJsonObject("biography").get("full-name").getAsString();
		String race = obj.getAsJsonArray("results").get(correctIndex).getAsJsonObject().getAsJsonObject("appearance").get("race").getAsString();
		String publisher = obj.getAsJsonArray("results").get(correctIndex).getAsJsonObject().getAsJsonObject("biography").get("publisher").getAsString();
	
		return formatSuperheroData(fullName, race, publisher);
	}
	
	/*
	 * Formats the the superhero data passed for eventual output to
	 * the user
	 * 
	 * @param fullName the full name of the superhero
	 * @param race the race of the superhero
	 * @param publisher the publisher of the content in which the hero appears
	 * @return the formatted superhero data
	 */
	private static final String formatSuperheroData(String fullName, String race, String publisher)
	{
		//Formats the output and returns it
		String output = "Here are some details about the Superhero you entered: Fullname: %s, Race: %s, Publisher: %s";
		
		return String.format(output, fullName, race, publisher);
	}
	
}
