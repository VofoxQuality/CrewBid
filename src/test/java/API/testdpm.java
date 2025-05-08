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

public class testdpm extends WbidBasepage {

    public static HashMap<String, String> testDataMap = testData("qa environment");
    public static String expectedVersion = testDataMap.get("Version");
    public static String tripCode;
    public static int errorcount;
	public static int passcount;
    //public static double rigDhrDirect;
   // public static double totalRigDpmForLine;
    //public static double totalRigDhrForTripCode;
    public static Map<String, Double> tripCodeToRigDhrSum = new HashMap<>();

    
    // Moved the tripCodeToTfpSum outside the loop to retain data across lines
    public static Map<String, Double> tripCodeToTfpSum = new LinkedHashMap<>(); 

    public static void fetchAndCalculateDHR(String domicile, String expectedRound, String expectedPosition, String expectedMonth) throws Throwable {
        
        // Validate expectedVersion
        if (expectedVersion == null || expectedVersion.isEmpty()) {
            logger.fail("Expected version is not provided in test data map.");
            throw new IllegalArgumentException("Expected version is missing");
        }

        RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";

        // Step 1: Auth API Call
        String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
        String requestBody1 = "{\n" +
                "    \"Base\": null,\n" +
                "    \"BidRound\": 0,\n" +
                "    \"EmployeeNumber\": \"x21221\",\n" +
                "    \"FromAppNumber\": \"12\",\n" +
                "    \"Month\": null,\n" +
                "    \"OperatingSystem\": null,\n" +
                "    \"Password\": \"Vofox2025@2$\",\n" +
                "    \"Platform\": \"Web\",\n" +
                "    \"Postion\": null,\n" +
                "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" +
                "    \"Version\": \"" + expectedVersion + "\"\n" +
                "}";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody1)
                .when().post(endpoint)
                .then().extract().response();

        String token = response.jsonPath().getString("Token");

        // Step 2: Get Bid Files
        String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
        String requestBody2 = "{" +
                "\"Domicile\": \"" + domicile + "\"," +
                "\"EmpNum\": \"21221\"," +
                "\"FromAppNumber\": \"12\"," +
                "\"IsQATest\": false," +
                "\"IsRetrieveNewBid\": true," +
                "\"Month\": " + expectedMonth + "," +
                "\"Platform\": \"Web\"," +
                "\"Position\": \"" + expectedPosition + "\"," +
                "\"Round\": " + expectedRound + "," +
                "\"secretEmpNum\": \"21221\"," +
                "\"Version\": \"" + expectedVersion + "\"," +
                "\"Year\": 2025," +
                "\"isSecretUser\": true" +
                "}";

        Response nextResponse = RestAssured.given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody2)
                .when().post(nextEndpoint)
                .then().statusCode(200)
                .extract().response();

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
            double totalRigDpmForLine = 0.0;

            // Extract Pairings and RigDhrInBp
            JSONArray pairingsArray = lineData.getJSONArray("Pairings");
            double rigDPMDirect = lineData.optDouble("RigDailyMinInLine");

            // Log the Direct DHR value for the line
            logger.info("Line No: " + lineKey + " | Direct Dpm: " + rigDPMDirect);

            

            // Process each pairing (trip code) in the current line
            for (int i = 0; i < pairingsArray.length(); i++) {
                String pairing = pairingsArray.getString(i);
                if (pairing.length() >= 4) {
                    String tripCode = pairing.substring(0, 4); // Extract trip code (first 4 characters)
                    logger.info("Trip Code is " + tripCode);

                    // Now get the corresponding trip data
                    if (tripJson.has(tripCode)) {
                        JSONObject tripDetails = tripJson.getJSONObject(tripCode);

                        if (tripDetails.has("DutyPeriods")) {
                            JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");

                            // Loop through DutyPeriods for the current trip
                            for (int dpIndex = 0; dpIndex < dutyPeriods.length(); dpIndex++) {
                                JSONObject dutyPeriod = dutyPeriods.getJSONObject(dpIndex);

                                if (dutyPeriod.has("Flights")) {
                                    JSONArray flightsArray = dutyPeriod.getJSONArray("Flights");

                                    double tfpSum = 0.0;
                                    // Sum all TFPs inside the Flights array
                                    for (int fIndex = 0; fIndex < flightsArray.length(); fIndex++) {
                                        JSONObject flight = flightsArray.getJSONObject(fIndex);
                                        double tfp = flight.optDouble("Tfp", 0.0); // Default to 0.0 if missing
                                        tfpSum += tfp;
                                    }

                                    // Calculate RigDHR for this duty period
                                    double rigDpm = 0.0;
                                    if (tfpSum < 5.0) {
                                        rigDpm = 5.0 - tfpSum;
                                    } else {
                                        rigDpm = 0.0;
                                    }

                                    // Log RigDHR for this Duty Period
                                    logger.info("Trip Code: " + tripCode + 
                                                " | Duty Period Index: " + dpIndex + 
                                                " | TFP Sum: " + tfpSum + 
                                                " | Rigdpm for this Duty Period: " + rigDpm);

                                    // Add the RigDHR for this duty period to the total RigDHR for the line
                                    totalRigDpmForLine += rigDpm;
                                }
                            }
                        } else {
                            logger.info("No DutyPeriods found for trip code: " + tripCode);
                        }
                    } else {
                        logger.info("Trip code not found in Trip File: " + tripCode);
                    }
                }
            }

            // After processing all trips in this line, log the total RigDHR for the line
            logger.info("Total RigDpm for all trips in Line No: " + lineKey + ": " +totalRigDpmForLine);
            if (Math.abs(totalRigDpmForLine - rigDPMDirect) > 0.5) { 
	            logger.fail("❌ Mismatch in dpm! Direct: " + rigDPMDirect + " vs Calculated: " + totalRigDpmForLine);
	            errorcount++;
	        } else {
	            logger.pass("✅ dpm Matched Successfully!");
	            passcount++;
	        }
	        System.out.println("Pass Count is" +passcount);
	        System.out.println("Error Count is" + errorcount);
}}
}
