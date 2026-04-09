import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;


public class Convertor {

    private static Scanner sc = new Scanner(System.in);

    static void main() {

        // We define our URL for API
        String url = "https://v6.exchangerate-api.com/v6/e205b32940e69fd6f58e09c4/latest/";

        // The main program
        while (true){
            System.out.println("Enter the currency you want to convert: ");
            String currencyToBeConverted = sc.nextLine().toUpperCase();
            System.out.println("Enter the currency you want to convert to: ");
            String currencyConverterResult = sc.nextLine().toUpperCase();
            System.out.println("Enter how much you want to convert: ");
            double totalMoney = Double.parseDouble(sc.nextLine());
            // Adding currencyToBeConverted to url
            url += currencyToBeConverted;
            String requestedBody = getRequestBody(url);

            // Convert a raw String into a JsonObject
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(requestedBody, JsonObject.class);

            // Get the conversion rate section from the jsonObject
            JsonObject allRates = jsonObject.getAsJsonObject("conversion_rates");

            // Check if the user input exists in the JsonObject
            // Get the conversion rate for user input from the allRates
            if (allRates.has(currencyConverterResult)){
                double exchangeRate = allRates.get(currencyConverterResult).getAsDouble();
                double convertedMoney = totalMoney * exchangeRate;
                System.out.println("The exchange rate for " + currencyConverterResult + " is: " + exchangeRate);
                System.out.println("For "+ totalMoney + currencyToBeConverted + "you will receive "+ convertedMoney + currencyConverterResult);
            }
        }

    }

    private static String getRequestBody(String url){

        // Build HTTP Request using Builder pattern
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        // Initialize the Client
        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            // Send the request and capture the response as a String
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200){
                return response.body();
            }else{
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }
}
