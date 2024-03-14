import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class WeatherForecast {
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Enter city name: ");
            String city = reader.readLine();

            String apiUrl = String.format(API_URL, city, API_KEY);
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject main = jsonResponse.getJSONObject("main");
                double temperature = main.getDouble("temp") - 273.15; // Convert from Kelvin to Celsius
                int humidity = main.getInt("humidity");
                String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");

                System.out.println("Weather forecast for " + city + ":");
                System.out.println("Temperature: " + String.format("%.2f", temperature) + "°C");
                System.out.println("Humidity: " + humidity + "%");
                System.out.println("Weather: " + weatherDescription);
            } else {
                System.out.println("Error: Unable to retrieve weather data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("Error: Failed to connect to the API. Please check your internet connection.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}