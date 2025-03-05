package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class FetchDates extends WbidBasepage {
	public static StringBuilder tripOutput = null;
	LZString lzstring = new LZString();
	public static String[] array;
	public List<String> acftChanges;
	public static Map<String, Double> tafbMap = new HashMap<>();
	public static Map<String, Double> totRigAdgMap = new HashMap<>();
	// public static int i=0;
	public static List<String> dynamicArray = new ArrayList<>();
	public static List<Double> tfpSums = new ArrayList<>();
	public static List<Double> dutyHrs = new ArrayList<>();
	public static double tafb;
	public static double TotalRigAdg;
	public static String tripCode;
	public static int passCount = 0, errorCount = 0;

	@Test(priority = 1)
	public static void fetchApiData(String domicile,String expectedRound, String expectedPosition, String expectedMonth) throws JsonProcessingException, ParseException {
		WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("Cred Values in an array");
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
				+ "    \"Password\": \"Vofox2025@2$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.3\"\n"
				+ "}";
		Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
				.then().extract().response();
		System.out.println("Response is " + response.getStatusCode());
		try {
			// Simulate a failure
			Assert.assertEquals(response.getStatusCode(), 400, "Status Code does not match");
		} catch (AssertionError e) {

			// Log the error and screenshot in the report
			logger.fail("Assertion failed: " + e.getMessage());

		}

		System.out.println("API Response is" + response.asString());
		String token = response.jsonPath().getString("Token");
		System.out.println("Extracted Token: " + token);

// Step 2: Use the Token as Authorization in the Next API Call
		String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
		String requestBody2 = "{"
		        + "\"Domicile\": \"" + domicile + "\","
		        + "\"EmpNum\": \"21221\","
		        + "\"FromAppNumber\": \"12\","
		        + "\"IsQATest\": false,"
		        + "\"IsRetrieveNewBid\": true,"
		        + "\"Month\": " + expectedMonth + ","
		        + "\"Platform\": \"Web\","
		        + "\"Position\": \"" + expectedPosition + "\","
		        + "\"Round\": " + expectedRound + ","
		        + "\"secretEmpNum\": \"21221\","
		        + "\"Version\": \"10.4.16.3\","
		        + "\"Year\": 2025,"
		        + "\"isSecretUser\": true"
		        + "}";// Replace with
// Replace with
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

		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(decompressedString, Object.class);
		String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

		try (PrintWriter writer = new PrintWriter(filePath)) {
			// Write data to the file
			writer.println(prettyJson); // Println for line break

		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileContents1 = nextResponse.jsonPath().getString("lstBidDataFiles.FileContent[1]");
		String filePath1 = "WBP_Decompressed.txt";
		String decompressedString1 = LZString.decompressFromUTF16(fileContents1);

		ObjectMapper mapper1 = new ObjectMapper();
		Object json1 = mapper1.readValue(decompressedString1, Object.class);
		String prettyJson1 = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(json1);

		try (PrintWriter writer = new PrintWriter(filePath1)) {
			// Write data to the file
			writer.println(prettyJson1); // Println for line break

		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject responseObject = new JSONObject(prettyJson);
// Initialize a list to store all pairings
		List<String> allPairings = new ArrayList<>();
		List<String> allPositions = new ArrayList<>();

// Get the "Lines" object
		JSONObject linesObject = responseObject.getJSONObject("Lines");
		int linecount = 0;
// Iterate through the keys of "Lines"
		for (String lineKey : linesObject.keySet()) {

			JSONObject lineData = linesObject.getJSONObject(lineKey);

			// Check if "Pairings" exists and is an array
			if (lineData.has("Pairings")) {
				JSONArray pairingsArray = lineData.getJSONArray("Pairings");

				// Add all pairings to the main list
				for (int i = 0; i < pairingsArray.length(); i++) {
					allPairings.add(pairingsArray.getString(i));
					// System.out.println("All Pairings Array" +allPairings);
				}
			}
		}

		// List to store tripName, date, and line number
		List<TripEntry> tripData = new ArrayList<>();
		// Date formatter
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM");

		// Iterate over all lines
		for (String lineKey : linesObject.keySet()) {
			JSONObject line = linesObject.getJSONObject(lineKey);
			JSONArray bidLineTemplates = line.getJSONArray("BidLineTemplates");

			// Iterate over each BidLineTemplate
			for (int i = 0; i < bidLineTemplates.length(); i++) {
				JSONObject bidLine = bidLineTemplates.getJSONObject(i);

				if (bidLine.has("TripName") && bidLine.get("TripName") != JSONObject.NULL) {
					String tripName = bidLine.getString("TripName");

					// Check if tripName starts with any of the specified prefixes
					for (String prefix : allPairings) {
						if (tripName.startsWith(prefix)) {
							String rawDate = bidLine.getString("Date");
							// Date date = (Date) inputFormat.parse(rawDate); // Parse original date
							java.util.Date utilDate = inputFormat.parse(rawDate); // Parse util.Date
							java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); // Convert to SQL Date
							String formattedDate = outputFormat.format(sqlDate);
							tripData.add(new TripEntry(tripName, formattedDate, Integer.parseInt(lineKey))); // Store
																												// line
																												// number
							break; // No need to check other prefixes once matched
						}
					}
				}
			}
		}
		// Sort the list by line number in ascending order
		Collections.sort(tripData, Comparator.comparingInt(entry -> entry.lineNumber));

		System.out.println("Matching TripName - Date - Line Pairs:");
		for (TripEntry entry : tripData) {
			// System.out.println("Line: " + entry.lineNumber + " | TripName: " +
			// entry.tripName + " -> Date: " + entry.date);
			logger.info("Line: " + entry.lineNumber + " | TripName: " + entry.tripName + " -> Date: " + entry.date);
		}
	}

	static class TripEntry {
		String tripName;
		String date;
		int lineNumber; // Changed to integer for proper sorting

		TripEntry(String tripName, String date, int lineNumber) {
			this.tripName = tripName;
			this.date = date;
			this.lineNumber = lineNumber;
		}
	}

}
