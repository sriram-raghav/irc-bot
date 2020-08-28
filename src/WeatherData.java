/*
 * The WeatherData class gets information from the OpenWeatherMap API, parses it
 * and formats the data for output
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

public class WeatherData 
{
	private static final String API_KEY = "8e4eef94d54cd990facb47c10baf0de1";
	private static final String ENDPOINT = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=%s";
	private static final double KELVIN_TO_CELSIUS = 273.15;
	
	/*
	 * Gets the weather data from the OpenWeatherMap API based on
	 * the location passed into the method
	 * 
	 * @param location the location to get the weather of
	 * @return the fully formatted and parsed weather data
	 */
	public static String getWeatherData(String location)
	{
		try
		{
			//Builds the URL and forms a new HTTP connection
			URL url = new URL(String.format(ENDPOINT, location, API_KEY));
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			//Ensures the users location is valid
			int responseCode = con.getResponseCode();
			if(responseCode > 299)
				return "Please enter a valid Zipcode, ID pair or city.";

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
			
			String result = parseWeatherData(content.toString());
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
	 * @return the parsed and formatted weather data
	 */
	private static String parseWeatherData(String objectToParse)
	{
		//Parse the entire JSON object
		JsonObject obj = JsonParser.parseString(objectToParse).getAsJsonObject();
		
		//Get the location and weather from the object
		String city = obj.get("name").getAsString();
		String country = obj.getAsJsonObject("sys").get("country").getAsString();
		String weather = obj.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		//Get the current, high and low temperatures at the location
		double curr = obj.getAsJsonObject("main").get("temp").getAsDouble();
		double high = obj.getAsJsonObject("main").get("temp_max").getAsDouble();
		double low = obj.getAsJsonObject("main").get("temp_min").getAsDouble();
		

		return formatWeatherData(city, country, weather, curr, high, low);
	}
	
	/*
	 * Formats the the weather data passed for eventual output to
	 * the user
	 * 
	 * @param city the city that the weather was retrieved from
	 * @param country the country that the weather was retrieved from
	 * @param weather the weather at that location (cloudy, sunny, etc)
	 * @param curr the current temperature at the location
	 * @param high the projected high temperature at the location
	 * @param low the projected low temperature at the location
	 * @return the formatted weather data
	 */
	private static String formatWeatherData(String city, String country, String weather, double curr, double high, double low)
	{
		//Converts the temperatures to farenheit
		curr = (curr - KELVIN_TO_CELSIUS)*(9.0/5) + 32;
		high = (high - KELVIN_TO_CELSIUS)*(9.0/5) + 32;
		low = (low - KELVIN_TO_CELSIUS)*(9.0/5) + 32;
		
		//Formats the output and returns it
		String output = "The weather in %s, %s is %s. It is currently %.2f degrees Farenheit, with a high of %.2f degrees Farenheit and a low of %.2f degrees Farenheit :)";
		
		return String.format(output, city, country, weather.toLowerCase(), curr, high, low);
	}
	
}
