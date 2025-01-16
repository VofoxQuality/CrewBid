package API;

import static io.restassured.RestAssured.given;

import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
//import org.graalvm.polyglot.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewTest {
	public static StringBuilder tripOutput = null;
	LZString lzstring = new LZString();
	public String[] array;
	public List<String> acftChanges;
public static int i=0;
	@Test
	public static void fetchApiData() throws JsonProcessingException {
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 =  "{\n" +
			    "    \"Base\": null,\n" +
			    "    \"BidRound\": 0,\n" +
			    "    \"EmployeeNumber\": \"x21221\",\n" +
			    "    \"FromAppNumber\": \"12\",\n" +
			    "    \"Month\": null,\n" +
			    "    \"OperatingSystem\": null,\n" +
			    "    \"Password\": \"Vofox2024@5\",\n" +
			    "    \"Platform\": \"Web\",\n" +
			    "    \"Postion\": null,\n" +
			    "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" +
			    "    \"Version\": \"10.4.15.2\"\n" +
			    "}";
		Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
				.then().extract().response();
		System.out.println("Response is " + response.getStatusCode());

		System.out.println("API Response is" + response.asString());
		String token = response.jsonPath().getString("Token");
		System.out.println("Extracted Token: " + token);

// Step 2: Use the Token as Authorization in the Next API Call
		String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
		String requestBody2 = "{" + "\"Domicile\": \"ATL\"," + "\"EmpNum\": \"21221\"," + "\"FromAppNumber\": \"12\","
				+ "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": 02," + "\"Platform\": \"Web\","
				+ "\"Position\": \"CP\"," + "\"Round\": 1," + "\"secretEmpNum\": \"21221\","
				+ "\"Version\": \"10.4.16.0\"," + "\"Year\": 2025," + "\"isSecretUser\": true" + "}";// Replace with
																										// your next API
																										// endpoint
		Response nextResponse = given().header("Authorization", "Bearer " + token)
				.header("Content-Type", "application/json").body(requestBody2).when().post(nextEndpoint) // Replace with
																											// POST/GET/PUT
																											// as needed
				.then().statusCode(200) // Ensure the response is successful
				.extract().response();

		String fileContents = nextResponse.jsonPath().getString("lstBidDataFiles.FileContent[0]");
		String filePath = "WBL_Decompressed.txt";
		String decompressedString = LZString.decompressFromUTF16(fileContents);
		System.out.println("Decompressed String: " + decompressedString);
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(decompressedString, Object.class);
		String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
//System.out.println(prettyJson);
		try (PrintWriter writer = new PrintWriter(filePath)) {
			// Write data to the file
			writer.println(prettyJson); // Println for line break
			// System.out.println("Data has been written to the file: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileContents1 = nextResponse.jsonPath().getString("lstBidDataFiles.FileContent[1]");
		String filePath1 = "WBP_Decompressed.txt";
		String decompressedString1 = LZString.decompressFromUTF16(fileContents1);
		System.out.println("Decompressed String: " + decompressedString1);
		ObjectMapper mapper1 = new ObjectMapper();
		Object json1 = mapper1.readValue(decompressedString1, Object.class);
		String prettyJson1 = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(json1);
		// System.out.println(prettyJson);
		try (PrintWriter writer = new PrintWriter(filePath1)) {
			// Write data to the file
			writer.println(prettyJson1); // Println for line break
			// System.out.println("Data has been written to the file: " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject(prettyJson);
// Initialize a list to store all pairings
		List<String> allPairings = new ArrayList<>();

// Get the "Lines" object
		JSONObject linesObject = responseObject.getJSONObject("Lines");

// Iterate through the keys of "Lines"
		for (String lineKey : linesObject.keySet()) {
			JSONObject lineData = linesObject.getJSONObject(lineKey);

			// Check if "Pairings" exists and is an array
			if (lineData.has("Pairings")) {
				JSONArray pairingsArray = lineData.getJSONArray("Pairings");

				// Add all pairings to the main list
				for (int i = 0; i < pairingsArray.length(); i++) {
					allPairings.add(pairingsArray.getString(i));
					//System.out.println("All Pairings Array" +allPairings);
				}
			}
		}

// Convert the list to an String array
		String[] allPairingsArray = allPairings.toArray(new String[0]);

// Print the array
		System.out.println("All Pairings in a Single Array:");
		for (String pairing : allPairingsArray) {
			// System.out.println(pairing);
		}

		// Initialize a new array to store the first 4 characters of each pairing
		String[] firstFourCharsArray = new String[allPairingsArray.length];

		// Extract the first 4 characters from each pairing
		for (int i = 0; i < allPairingsArray.length; i++) {
			firstFourCharsArray[i] = allPairingsArray[i].substring(0, 4);
		}

		// Print the new array
		System.out.println("First 4 Characters from Pairings:");
		for (String pairing1 : firstFourCharsArray) {
			// System.out.println(pairing1);
		}
		// Parse the JSON response
		// JSONObject responseObject1 = new JSONObject(prettyJson1);
		// List<Double> tfpValuesInOrder = new ArrayList<>();
		// Map to store Tfp values
		// Map<String, Double> tfpValues = new HashMap<>();

		/*
		 * // Iterate through the firstFourCharsArray for (String key :
		 * firstFourCharsArray) { if (responseObject1.has(key)) { JSONObject tripDetails
		 * = responseObject1.getJSONObject(key);
		 * 
		 * // Check if DutyPeriods array exists if (tripDetails.has("DutyPeriods")) {
		 * JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");
		 * 
		 * // Fetch Tfp value from the first duty period if (dutyPeriods.length() > 0) {
		 * JSONObject firstDutyPeriod = dutyPeriods.getJSONObject(0); if
		 * (firstDutyPeriod.has("Tfp")) { double tfp = firstDutyPeriod.getDouble("Tfp");
		 * tfpValues.put(key, tfp); } // Iterate through each DutyPeriod for
		 * (Map<String, Object> dutyPeriod : dutyPeriods) { // Get Flights array
		 * List<Map<String, Object>> flights = (List<Map<String, Object>>)
		 * dutyPeriod.get("Flights");
		 * 
		 * // Sort flights by FlightSeqNum flights.sort(Comparator.comparingInt(flight
		 * -> (int) flight.get("FlightSeqNum")));
		 * 
		 * // Extract Tfp values in order for (Map<String, Object> flight : flights) {
		 * Double tfp = (Double) flight.get("Tfp"); tfpValuesInOrder.add(tfp); } } }
		 * 
		 * // Print all Tfp values in order of FlightSeqNum
		 * System.out.println("Tfp values in order of FlightSeqNum: " +
		 * tfpValuesInOrder); } } } } }
		 * 
		 * // Print the Tfp values System.out.println("Tfp Values for Each Key:"); for
		 * (Map.Entry<String, Double> entry : tfpValues.entrySet()) {
		 * System.out.println(entry.getKey() + ": " + entry.getValue()); } }
		 */
		// Extract the first 4 characters from each pairing

		// Print the first 4 characters
		// System.out.println("First 4 Characters from Pairings:");
		// Arrays.stream(firstFourCharsArray).forEach(System.out::println);

		// Parse the JSON response
		// JSONObject responseObject1 = new JSONObject(prettyJson1);

		// Parse the JSON string into a JSONObject
		JSONObject tripsData = new JSONObject(prettyJson1);

		// Iterate through each trip in the JSON object
		tripsData.keys().forEachRemaining(tripCode -> {
			JSONObject tripDetails = tripsData.getJSONObject(tripCode);

			// Start building the output for this trip
			tripOutput = new StringBuilder(tripCode + "  ");

			// Extract the DutyPeriods array
			JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");
			for (int i = 0; i < dutyPeriods.length(); i++) {
				JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);
				// Extract FlightSeqNum and Tfp
				int DutSeqNum = dutyPeriod.getInt("DutPerSeqNum");
				double TOTALtfp = dutyPeriod.getDouble("Tfp");
				tripOutput.append("DutySeqNum" + DutSeqNum + " TOTALtfp").append(":").append(TOTALtfp).append(" ");

				// Extract the Flights array
				JSONArray flights = dutyPeriod.getJSONArray("Flights");
				for (int j = 0; j < flights.length(); j++) {
					JSONObject flight = flights.getJSONObject(j);

					// Extract FlightSeqNum and Tfp
					int flightSeqNum = flight.getInt("FlightSeqNum");
					double tfp = flight.getDouble("Tfp");

					// Append to the trip output
					tripOutput.append("FlightSeqNum " + flightSeqNum).append(" ").append("Tfp: " + tfp).append("   ");
				}
			}

			// Print the result for this trip
			System.out.println(tripOutput.toString().trim());
			// convert stringbuilder to string

			String strOutput = tripOutput.toString();
			// Save the string to an array

			//String[] array = new String[1000]; // Array of size 1
			List<String> dynamicArray = new ArrayList<>();

			dynamicArray.add(strOutput);
			System.out.println("Array element "+ i+" " +dynamicArray);

			i++;
			
		});
		
	}

}
