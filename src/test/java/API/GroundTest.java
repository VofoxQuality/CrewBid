package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class GroundTest extends WbidBasepage{
	
		public static HashMap<String, String> testDataMap = testData("qa environment");
		public static String expectedVersion = testDataMap.get("Version");
		public static double ground;
		
		public static Map<String, Map<Integer, List<String>>> apiGrnd = new LinkedHashMap<>();
		//public static Map<String, Map<String, List<String>>> tripGrndMapAPI = new LinkedHashMap<>();
		public static Map<Integer, String> calGrnd = new LinkedHashMap<>();
		public static List<String> calGrndAPI = new ArrayList<>();
		public static double groundValue;
		public static String groundTimeFormatted;
		public static String groundValueTimeFormatted;
		public static int passCount;
		public static int errorCount;

  @Test
  public static void fetchground(String domicile,String expectedRound, String expectedPosition, String expectedMonth) throws Throwable {
		WbidBasepage.logger = WbidBasepage.extent.createTest("Ground from API").assignAuthor("VS/445");

		WbidBasepage.logger.info("Cred Values in an array");
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n" + "    \"Password\": \"Vofox2025@2$\",\n"
				+ "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.5\"\n"
				+ "}";
		Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
				.then().extract().response();
		System.out.println("Response is " + response.getStatusCode());
		try {
			// Simulate a failure
			//Assert.assertEquals(response.getStatusCode(), 400, "Status Code does not match");
		} catch (AssertionError e) {

			// Log the error and screenshot in the report
			WbidBasepage.logger.fail("Assertion failed: " + e.getMessage());

		}

		System.out.println("API Response is" + response.asString());
		String token = response.jsonPath().getString("Token");
		System.out.println("Extracted Token: " + token);

//Step 2: Use the Token as Authorization in the Next API Call
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
		        + "\"Version\": \"10.4.16.5\","
		        + "\"Year\": 2025,"
		        + "\"isSecretUser\": true"
		        + "}";// Replace with
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
		// System.out.println("Decompressed String: " + decompressedString);
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
		// System.out.println("Decompressed String: " + decompressedString1);
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
		
	//JSONObject responseObject = new JSONObject(prettyJson);
	JSONObject TripresponseObject = new JSONObject(prettyJson1);
	TripresponseObject.keys().forEachRemaining(tripCode -> {
		logger.info("Trip Code: " + tripCode);
	    JSONObject tripDetails = TripresponseObject.getJSONObject(tripCode);
	    List<List<Double>> dutyPeriodsList = new ArrayList<>();

	    if (tripDetails.has("DutyPeriods")) {
	    	JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");

	    	List<JSONObject> allFlights = new ArrayList<>();

	    	// Collect all flights in a flat list to calculate cross-duty ground
	    	for (int i = 0; i < dutyPeriods.length(); i++) {
	    	    JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);
	    	    int DutSeqNum = dutyPeriod.getInt("DutPerSeqNum");
	    	    String dutSeqNumStr = String.valueOf(DutSeqNum);
	    	    if (dutyPeriod.has("Flights")) {
	    	        JSONArray flights = dutyPeriod.getJSONArray("Flights");
	    	        for (int j = 0; j < flights.length(); j++) {
	    	        	 JSONObject flight = flights.getJSONObject(j);
	    	        	groundValue=flight.optDouble("TurnTime",0.0);
	    	        	 int groundValueHoursPart = (int) groundValue / 60;
	    		    	    int groundValueMinutesPart = (int) groundValue % 60;

	    		    	    if (groundValueMinutesPart >= 60) {
	    		    	    	groundValueMinutesPart -= 60;
	    		    	    	groundValueHoursPart++;
	    		    	    }
	    		    	    groundValueTimeFormatted = String.format("%02d:%02d", groundValueHoursPart, groundValueMinutesPart);
	    		    	    
	    		    	   // logger.info("Flight Seq " + (j +1) + ": Turn Time(Direct Grnd hr) = " + groundValueTimeFormatted);
	    		    	    allFlights.add(flights.getJSONObject(j));
	    	
	    		    	    calGrnd.put(DutSeqNum, groundValueTimeFormatted);
	    		    	    
	    		    	    Collections.addAll(calGrndAPI, tripCode, dutSeqNumStr, groundValueTimeFormatted);
	    		    	   
	    		    	    apiGrnd.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
							.computeIfAbsent(DutSeqNum, k -> new ArrayList<>()).add(groundValueTimeFormatted);
	    		    	    
	    	        }
	    	    }
	    	}
	    	//logger.info("Flight Seq " + apiGrnd);
	    	
	    	logger.info("Direct Grnd (turn Time) In API: " + apiGrnd);
	    	System.out.println("Direct Grnd value Formated(UI) In API: " +calGrndAPI);
	    	
	    	
	    	// Calculate ground time between all flights (including across duty periods)
	    	for (int i = 0; i < allFlights.size(); i++) {
	    	    JSONObject currentFlight = allFlights.get(i);
	    	    double currentArrival = currentFlight.optDouble("ArrTime", 0.0);
	    	    
	    	    if (i + 1 < allFlights.size()) {
	    	        JSONObject nextFlight = allFlights.get(i + 1);
	    	        double nextDeparture = nextFlight.optDouble("DepTime", 0.0);
	    	        ground = nextDeparture - currentArrival;
	    	    }

	    	    int groundHoursPart = (int) ground / 60;
	    	    int groundMinutesPart = (int) ground % 60;

	    	    if (groundMinutesPart >= 60) {
	    	        groundMinutesPart -= 60;
	    	        groundHoursPart++;
	    	    }

	    	    groundTimeFormatted = String.format("%02d:%02d", groundHoursPart, groundMinutesPart);
	    	    logger.info("Flight Seq " + (i + 1) + ": Ground Time = " + groundTimeFormatted);
	    	    
	    	  
	     		
	    	}
	    	// Comparison and logging
			if (Math.round(ground) == Math.round(groundValue)) {
				logger.pass( " Block Time matches. ✅ Calculated = "
						+ groundTimeFormatted + " | Actual = " + groundValueTimeFormatted);
				passCount++;
				// System.out.println("Pass count is" + passCount);
			} else {
				logger.fail( " Block Time mismatch. ❌ Calculated = "
						+ groundTimeFormatted + " | Actual = " + groundValueTimeFormatted);

				errorCount++;
				// System.out.println("Error count is" + errorCount);
			}
	    }
           
} 
	);
	}} 
