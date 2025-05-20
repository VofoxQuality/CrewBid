package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class DutyPeriodTest extends WbidBasepage{
	public static HashMap<String, String> testDataMap = testData("qa environment");
	public static String expectedVersion = testDataMap.get("Version");
	public static  Map<String, List<String>> dutyHourMapAPI = new LinkedHashMap<>();
	public static int errorcount;
	public static int passcount;
	
	@Test(priority = 1)
	public static void fetchApiData(String domicile, String expectedRound, String expectedPosition,
			String expectedMonth) throws ParseException, NumberFormatException, IOException {
		WbidBasepage.logger = extent.createTest("Duty Period Calculation").assignAuthor("VS/445");

		//logger.info("Cred Values in an array");
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
				+ "    \"Password\": \"Vofox2025@2$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \""+expectedVersion+"\"\n"
				+ "}";
	/*	String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
				+ "    \"Password\": \"Vofox2025@2$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.5\"\n"
				+ "}";*/
		Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
				.then().extract().response();
		System.out.println("Response is " + response.getStatusCode());
		try {
			// Simulate a failure
			Assert.assertEquals(response.getStatusCode(), 200, "Status Code does not match");
		} catch (AssertionError e) {

			// Log the error and screenshot in the report
			logger.fail("Assertion failed: " + e.getMessage());

		}

		System.out.println("API Response is" + response.asString());
		String token = response.jsonPath().getString("Token");
		System.out.println("Extracted Token: " + token);

// Step 2: Use the Token as Authorization in the Next API Call
		String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
		String requestBody2 = "{" + "\"Domicile\": \"" + domicile + "\"," + "\"EmpNum\": \"21221\","
				+ "\"FromAppNumber\": \"12\"," + "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": "
				+ expectedMonth + "," + "\"Platform\": \"Web\"," + "\"Position\": \"" + expectedPosition + "\","
				+ "\"Round\": " + expectedRound + "," + "\"secretEmpNum\": \"21221\"," + "\"Version\": \""+expectedVersion+"\","
				+ "\"Year\": 2025," + "\"isSecretUser\": true" + "}";// Replace with
		
		/*String requestBody2 = "{"
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
		        + "}";// Replace with*/
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
		//JSONObject nextResponseObject = new JSONObject(prettyJson1);
		JSONObject TripresponseObject = new JSONObject(prettyJson1);
		TripresponseObject.keys().forEachRemaining(tripCode -> {
			logger.info("Trip Code: " + tripCode);
		    JSONObject tripDetails = TripresponseObject.getJSONObject(tripCode);
		    List<List<Double>> dutyPeriodsList = new ArrayList<>();

		    if (tripDetails.has("DutyPeriods")) {
	            JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");
	           

	            for (int i = 0; i < dutyPeriods.length(); i++) {
	                JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);
	                int DirectDutyHour = dutyPeriod.getInt("DutyTime");
	                int DutSeqNum = dutyPeriod.getInt("DutPerSeqNum");
	             // Convert to String
					String dutSeqNumStr = String.valueOf(DutSeqNum);
	                // Extract ShowTime and ReleaseTime (default to 0.0 if missing)
	                double reportTimeAPI = dutyPeriod.optDouble("ShowTime", 0.0);
	             
	                double releaseTimeAPI = dutyPeriod.optDouble("ReleaseTime", 0.0);
	                
	             // Store the times as a list
	               // List<Double> timePair = Arrays.asList(reportTime, releaseTime);
	               // dutyPeriodsList.add(timePair);
	             // Calculate duty period in minutes
	                double dutyMinutes = releaseTimeAPI - reportTimeAPI;
	                int dutyHoursPart = (int) dutyMinutes / 60;
	                int dutyMinutesPart = (int) dutyMinutes % 60;
	                String dutyPeriodFormatted = String.format("%02d:%02d", dutyHoursPart, dutyMinutesPart);

	                // Add 30 minutes
	               // releaseMinutes += 30;
	                if (dutyMinutesPart >= 60) {
	                	dutyMinutesPart -= 60;
	                	dutyHoursPart++;  // Carry over extra hour
	                }
	              
	                logger.info("Trip Code " + tripCode + "DutySeqNum" + DutSeqNum + "Report Time" + reportTimeAPI + "Release Time " + releaseTimeAPI +"Duty Period" + dutyPeriodFormatted); 
	                if (Math.abs(dutyMinutes - DirectDutyHour) > 0.5) { 
	    	            logger.fail("❌ Mismatch in DutyHour! Direct: " + DirectDutyHour + " vs Calculated: " + dutyMinutes);
	    	            errorcount++;
	    	        } else {
	    	            logger.pass("✅ Duty Hour Matched Successfully!");
	    	            passcount++;
	    	        }
	    	        System.out.println("Pass Count is" +passcount);
	    	        System.out.println("Error Count is" + errorcount);
	    	        dutyHourMapAPI.computeIfAbsent(tripCode,k -> new ArrayList<>()).add(dutyPeriodFormatted);
	    	        
	    	    }}
	            });
		    }}
		    

		
 

