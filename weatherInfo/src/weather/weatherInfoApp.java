package weather;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.*;

public class weatherInfoApp {
    private static final String API_KEY = "c0901c466192dd615b6866e9d5f329c6";

    @SuppressWarnings("resource")
	public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        
        try {
            String city = URLEncoder.encode(scanner.nextLine(), "UTF-8");
            String endpoint = "https://api.openweathermap.org/data/2.5/weather?q=" 
                            + city + "&appid=" + API_KEY + "&units=metric";

            @SuppressWarnings("deprecation")
			URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            if (conn.getResponseCode() == 404) {
                System.out.println("City not found. Please enter a valid city name.");
                return;
            }

            if (conn.getResponseCode() != 200) {
                System.out.println("Error fetching weather data: HTTP " + conn.getResponseCode());
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);
            in.close();

            JSONObject json = new JSONObject(response.toString());
            JSONObject main = json.getJSONObject("main");
            double temp = main.getDouble("temp");
            int humidity = main.getInt("humidity");

            String weatherDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");

            System.out.println("\nWeather in " + city + ":");
            System.out.println("Temperature: " + temp + "°C");
            System.out.println("Humidity: " + humidity + "%");
            System.out.println("Description: " + weatherDescription);

        } catch (Exception e) {
            System.out.println("Error fetching weather data.");
            e.printStackTrace();
        }
        scanner.close();
    }
}
