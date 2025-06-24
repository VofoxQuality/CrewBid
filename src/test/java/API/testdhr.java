package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class testdhr extends WbidBasepage {

	public static HashMap<String, String> testDataMap = testData("qa environment");
	public static String expectedVersion = testDataMap.get("Version");
	public static String tripCode;
	public static int errorcount;
	public static int passcount;
	 public static double rigDhrDirect;
	public static double totalRigDpmForLine;
	 public static double totalRigDhrForTripCode;
	public static Map<String, Double> tripCodeToRigDhrSum = new HashMap<>();

	// Moved the tripCodeToTfpSum outside the loop to retain data across lines
	public static Map<String, Double> tripCodeToTfpSum = new LinkedHashMap<>();

	public static void fetchAndCalculateDHR(String domicile, String expectedRound, String expectedPosition,
			String expectedMonth) throws Throwable {

		// Validate expectedVersion
		if (expectedVersion == null || expectedVersion.isEmpty()) {
			logger.fail("Expected version is not provided in test data map.");
			throw new IllegalArgumentException("Expected version is missing");
		}

		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";

		// Step 1: Auth API Call
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
				+ "    \"Password\": \"Vofox2025@3$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"" + expectedVersion
				+ "\"\n" + "}";

		Response response = RestAssured.given().header("Content-Type", "application/json").body(requestBody1).when()
				.post(endpoint).then().extract().response();

		String token = response.jsonPath().getString("Token");

		// Step 2: Get Bid Files
		String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
		String requestBody2 = "{" + "\"Domicile\": \"" + domicile + "\"," + "\"EmpNum\": \"21221\","
				+ "\"FromAppNumber\": \"12\"," + "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": "
				+ expectedMonth + "," + "\"Platform\": \"Web\"," + "\"Position\": \"" + expectedPosition + "\","
				+ "\"Round\": " + expectedRound + "," + "\"secretEmpNum\": \"21221\"," + "\"Version\": \""
				+ expectedVersion + "\"," + "\"Year\": 2025," + "\"isSecretUser\": true" + "}";

		Response nextResponse = RestAssured.given().header("Authorization", "Bearer " + token)
				.header("Content-Type", "application/json").body(requestBody2).when().post(nextEndpoint).then()
				.statusCode(200).extract().response();

		String lineFile = nextResponse.jsonPath().getString("lstBidDataFiles.FileContent[0]");
		String filePath = "WBL_Decompressed.txt";
		String decompressedString = LZString.decompressFromUTF16(lineFile);
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(decompressedString, Object.class);
		String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

		try (PrintWriter writer = new PrintWriter(filePath)) {
			writer.println(prettyJson);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String tripFile = nextResponse.jsonPath().getString("lstBidDataFiles.FileContent[1]");
		String filePath1 = "WBP_Decompressed.txt";
		String decompressedString1 = LZString.decompressFromUTF16(tripFile);
		ObjectMapper mapper1 = new ObjectMapper();
		Object json1 = mapper1.readValue(decompressedString1, Object.class);
		String prettyJson1 = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(json1);

		try (PrintWriter writer = new PrintWriter(filePath1)) {
			writer.println(prettyJson1);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject lineJson = new JSONObject(decompressedString);
		JSONObject linesObject = lineJson.getJSONObject("Lines");

		JSONObject tripJson = new JSONObject(decompressedString1);

		List<String> sortedKeys = linesObject.keySet().stream()
				.sorted((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)))
				.collect(Collectors.toList());

		// Iterate over lines in the linesObject
		for (String lineKey : sortedKeys) {
		    JSONObject lineData = linesObject.getJSONObject(lineKey);

		    JSONArray pairingsArray = lineData.getJSONArray("Pairings");
		    double rigDhrDirect = lineData.optDouble("RigDhrInLine");

		    logger.info("Line No: " + lineKey + " | Direct Dhr: " + rigDhrDirect);

		    double totalRigDhrForLine = 0.0; // <-- Initialize for each line

		    // Process each pairing (trip code) in the current line
		    for (int i = 0; i < pairingsArray.length(); i++) {
		        String pairing = pairingsArray.getString(i);
		        if (pairing.length() >= 4) {
		            String tripCode = pairing.substring(0, 4);

		            logger.info("Trip Code is " + tripCode);

		            if (tripJson.has(tripCode)) {
		                JSONObject tripDetails = tripJson.getJSONObject(tripCode);

		                if (tripDetails.has("DutyPeriods")) {
		                    JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");

		                    double totalRigDhrForTrip = 0.0; // <-- Initialize for each trip

		                    // Loop through DutyPeriods for the current trip
		                    for (int dpIndex = 0; dpIndex < dutyPeriods.length(); dpIndex++) {
		                        JSONObject dutyPeriod = dutyPeriods.getJSONObject(dpIndex);
		                        double Dutyhrs = dutyPeriod.optDouble("DutyTime", 0.0);

		                        if (dutyPeriod.has("Flights")) {
		                            JSONArray flightsArray = dutyPeriod.getJSONArray("Flights");
		                            double tfpSum = 0.0;

		                            for (int fIndex = 0; fIndex < flightsArray.length(); fIndex++) {
		                                JSONObject flight = flightsArray.getJSONObject(fIndex);
		                                double tfp = flight.optDouble("Tfp", 0.0);
		                                tfpSum += tfp;
		                            }

		                            double threshold = 5.0;
		                            double new_Cred = (tfpSum > threshold) ? tfpSum : threshold;

		                            double convertedDutyHour = (Dutyhrs / 60) * 0.74;
		                            logger.info("Duty Period TFP Sum: " + tfpSum);
		                           // logger.info("Duty Period TFP Sum: " + tfpSum + " | New_Cred Value: " + new_Cred);
		                            logger.info("Converted Duty Hour: " + convertedDutyHour);

		                            double rigDhr;
		                           if (convertedDutyHour > new_Cred) {
		                                rigDhr = convertedDutyHour - new_Cred;
		                            } else {
		                                rigDhr = 0.0;
		                            }
		                           /*  if (convertedDutyHour > tfpSum) {
		                                rigDhr = convertedDutyHour - tfpSum;
		                            } else {
		                                rigDhr = 0.0;
		                            logger.info("Calculated RigDHR for Duty Period: " + rigDhr);
		                            }*/
		                           logger.info("Calculated RigDHR for Duty Period: " + rigDhr);
		                            totalRigDhrForTrip += rigDhr; // <-- Add to trip-level total
		                        }
		                    } // End of dutyPeriods loop

		                    logger.info("Total RigDHR for Trip " + tripCode + ": " + totalRigDhrForTrip);

		                    totalRigDhrForLine += totalRigDhrForTrip; // <-- Add trip DHR to line-level total
		                }
		            }
		        }
		    } // End of pairings loop

		    logger.info("Line Number: " + lineKey + " | Total Line DHR: " + totalRigDhrForLine);

		    if (Math.abs(totalRigDhrForLine - rigDhrDirect) > 0.5) { 
	            logger.fail("❌ Mismatch in dhr! Direct: " + rigDhrDirect + " vs Calculated: " + totalRigDhrForLine);
	            errorcount++;
	        } else {
	            logger.pass("✅ dhr Matched Successfully!");
	            passcount++;
	        }
	        System.out.println("Pass Count is" +passcount);
	        System.out.println("Error Count is" + errorcount);
}}}