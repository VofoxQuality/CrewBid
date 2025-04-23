package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

public class BlockTest extends WbidBasepage {
	public static int passCount;
	public static int errorCount;
	public static double block;
	public static Map<String, Map<Integer, List<String>>> apiBlk = new LinkedHashMap<>();

	@Test
	public static void fetchBlock(String domicile, String expectedRound, String expectedPosition, String expectedMonth)
			throws Throwable {
		WbidBasepage.logger = WbidBasepage.extent.createTest("Block from API").assignAuthor("VS/445");

		WbidBasepage.logger.info("Cred Values in an array");
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
				+ "    \"Password\": \"Vofox2025@2$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.5\"\n"
				+ "}";
		Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
				.then().extract().response();
		System.out.println("Response is " + response.getStatusCode());
		try {
			// Simulate a failure
			Assert.assertEquals(response.getStatusCode(), 200, "Status Code does not match");
		} catch (AssertionError e) {

			// Log the error and screenshot in the report
			WbidBasepage.logger.fail("Assertion failed: " + e.getMessage());

		}

		System.out.println("API Response is" + response.asString());
		String token = response.jsonPath().getString("Token");
		System.out.println("Extracted Token: " + token);

		// Step 2: Use the Token as Authorization in the Next API Call
		String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
		String requestBody2 = "{" + "\"Domicile\": \"" + domicile + "\"," + "\"EmpNum\": \"21221\","
				+ "\"FromAppNumber\": \"12\"," + "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": "
				+ expectedMonth + "," + "\"Platform\": \"Web\"," + "\"Position\": \"" + expectedPosition + "\","
				+ "\"Round\": " + expectedRound + "," + "\"secretEmpNum\": \"21221\"," + "\"Version\": \"10.4.16.5\","
				+ "\"Year\": 2025," + "\"isSecretUser\": true" + "}";// Replace with
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
		// System.out.println(prettyJson);
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

		// JSONObject responseObject = new JSONObject(prettyJson);
		JSONObject TripresponseObject = new JSONObject(prettyJson1);
		TripresponseObject.keys().forEachRemaining(tripCode -> {
			logger.info("Trip Code: " + tripCode);
			JSONObject tripDetails = TripresponseObject.getJSONObject(tripCode);

			if (tripDetails.has("DutyPeriods")) {
				JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");

				for (int i = 0; i < dutyPeriods.length(); i++) {
					JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);
					logger.info("Duty Seq " + (i + 1));
					int DutSeqNum = dutyPeriod.getInt("DutPerSeqNum");
		    	
					if (dutyPeriod.has("Flights")) {
						JSONArray flights = dutyPeriod.getJSONArray("Flights");
						
						for (int j = 0; j < flights.length(); j++) {
							JSONObject flight = flights.getJSONObject(j);

							int flightNum = flight.optInt("FltNum", -1);  // Safe fallback
							if(flightNum!=0) {
							// Get actual Block value from JSON
							double actualBlock = flight.optDouble("Block", 0.0);

							double depTime = flight.optDouble("DepTime", 0.0);
							double arrTime = flight.optDouble("ArrTime", 0.0);
							if (flight.optBoolean("DeadHead") == false) {

								block = arrTime - depTime;
							}
							else
							{
								block=0.0;
							}

							int blockHours = (int) block / 60;
							int blockMinutes = (int) block % 60;

							if (blockMinutes >= 60) {
								blockMinutes -= 60;
								blockHours++;
							}

							String blockTimeFormatted = String.format("%02d:%02d", blockHours, blockMinutes);
							//logger.info("Flight Seq " + (j + 1) + ": Block Time = " + blockTimeFormatted);

							int actualHours = (int) actualBlock / 60;
							int actualMinutes = (int) actualBlock % 60;

							String actualBlockFormatted = String.format("%02d:%02d", actualHours, actualMinutes);

							// Comparison and logging
							if (Math.round(block) == Math.round(actualBlock)) {
								logger.pass("Flight Seq " + (j + 1) + ": Block Time matches. ✅ Calculated = "
										+ blockTimeFormatted + " | Actual = " + actualBlockFormatted);
								passCount++;
								// System.out.println("Pass count is" + passCount);
							} else {
								logger.fail("Flight Seq " + (j + 1) + ": Block Time mismatch. ❌ Calculated = "
										+ blockTimeFormatted + " | Actual = " + actualBlockFormatted);

								errorCount++;
								// System.out.println("Error count is" + errorCount);
							}
							apiBlk.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
							.computeIfAbsent(DutSeqNum, k -> new ArrayList<>()).add(actualBlockFormatted);
							logger.info("Direct Blk Hour (Block) In API: " + apiBlk);
						}
						}
					}
				}
			}

		});
		System.out.println("Pass count is" + passCount);
		System.out.println("Error count is" + errorCount);
		logger.info("Direct Blk Hour (Block) In API: " + apiBlk);
	}

}
