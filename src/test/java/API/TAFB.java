package API;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;

import API.HoliRigFA.TripEntry;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class TAFB extends WbidBasepage {
	public static StringBuilder tripOutput = null;
	LZString lzstring = new LZString();
	public static String[] array;
	public List<String> acftChanges;
	public static Map<String, Double> tafbMap = new HashMap<>();
	public static Map<String, Double> totRigAdgMap = new HashMap<>();
	public static Map<String, Double> tafbMapNew = new HashMap<>();
	// public static int i=0;
	public static List<String> dynamicArray = new ArrayList<>();
	public static List<Double> tfpSums = new ArrayList<>();
	public static List<Double> dutyHrs = new ArrayList<>();
	public static double tafb;
	public static double TotalRigAdg;
	public static List<TripEntry> sortedTripData = new ArrayList<>();
	public static String tripCode;
	public static int passCount = 0, errorCount = 0;
	 public static List<TripEntry> tripData = new ArrayList<>();
	 private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    private static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("dd MMM");
	    private static final Map<String, Double> tfpMap = new HashMap<>(); // Load this from trip file
	    public static double holiRig;
	    public static List<Map<String, Object>> holirigResult = new ArrayList<>();
	 // Map to store reportTime and releaseTime for each tripCode
	       public static Map<String, List<List<Double>>> tripTimeMap = new HashMap<>();

	@Test(priority = 1)
	public static void fetchApiData(String domicile,String expectedRound, String expectedPosition, String expectedMonth) throws Throwable {
		WbidBasepage.logger = WbidBasepage.extent.createTest("Bid Download API").assignAuthor("VS/445");

		WbidBasepage.logger.info("Cred Values in an array");
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n" + "    \"Password\": \"Vofox2025@2$\",\n"
				+ "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
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
			WbidBasepage.logger.fail("Assertion failed: " + e.getMessage());

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
		        + "\"Year\": 2024,"
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
		JSONObject responseObject = new JSONObject(prettyJson);
		JSONObject TripresponseObject = new JSONObject(prettyJson1);
		// Parse each trip and extract duty periods
        TripresponseObject.keys().forEachRemaining(tripCode -> {
           // ExtentTest tripTest = test.createNode("Trip Code: " + tripCode);
            JSONObject tripDetails = TripresponseObject.getJSONObject(tripCode);
            List<List<Double>> dutyPeriodsList = new ArrayList<>();

            if (tripDetails.has("DutyPeriods")) {
                JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");

                for (int i = 0; i < dutyPeriods.length(); i++) {
                    JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);

                    // Extract ShowTime and ReleaseTime (default to 0.0 if missing)
                    double reportTime = dutyPeriod.optDouble("ShowTime", 0.0);
                    double releaseTime = dutyPeriod.optDouble("ReleaseTime", 0.0);

                    // Log each duty period in report
                    logger.info(tripCode+"DutSeq" + (i + 1) + " -> Report Time: " + reportTime + ", Release Time: " + releaseTime);

                    // Store each duty period as a list [reportTime, releaseTime]
                    dutyPeriodsList.add(Arrays.asList(reportTime, releaseTime));
                }
            }
            // Put the list of duty periods in the map
            tripTimeMap.put(tripCode, dutyPeriodsList);
        });

     // Calculate and display TAFB for each trip
        for (String tripCode : tripTimeMap.keySet()) {
        	logger.info("Trip Code is"+tripCode);
            List<List<Double>> dutyPeriods = tripTimeMap.get(tripCode);

            if (dutyPeriods.isEmpty()) continue;

            // First and last sequence
            double firstReportTime = dutyPeriods.get(0).get(0);
            double finalReleaseTime = dutyPeriods.get(dutyPeriods.size() - 1).get(1);

            // Convert minutes into HH:MM format for First Report Time
            int reportHours = (int) (firstReportTime / 60);
            int reportMinutes = (int) (firstReportTime % 60);
            String reportFormattedTime = String.format("%02d:%02d", reportHours, reportMinutes);

            // Normalize Final Release Time within 24-hour cycle
            finalReleaseTime = finalReleaseTime % (24 * 60);  // Ensure it stays within 0-1439 minutes

            // Convert finalReleaseTime into hours and minutes
            int releaseHours = (int) (finalReleaseTime / 60);
            int releaseMinutes = (int) (finalReleaseTime % 60);
         // Store original hours before adding 30 minutes
            int originalReleaseHours = releaseHours;


            // Add 30 minutes
            releaseMinutes += 30;
            if (releaseMinutes >= 60) {
                releaseMinutes -= 60;
                releaseHours++;  // Carry over extra hour
            }

            // Ensure hours stay within 24-hour format
            releaseHours = releaseHours % 24;

            String releaseFormattedTime = String.format("%02d:%02d", releaseHours, releaseMinutes);

           // logger.info("First Report Time: " + reportFormattedTime);
           // logger.info("Final Release Time: " + releaseFormattedTime);
        
            // TAFB Calculation
            int totalSequences = dutyPeriods.size();
            // If the release time crosses midnight, increment totalSequences
            if (originalReleaseHours == 23 && releaseHours == 0) {
                totalSequences++;
            }
            double firstDayHours = 24 - (reportHours + (reportMinutes / 60.0));
logger.info("firstdayhour "+ firstDayHours);
            double lastDayHours = releaseHours + (releaseMinutes / 60.0);
            logger.info("lastdayhour "+ lastDayHours);
            double middleDayHours = (totalSequences - 2) * 24; // Full 24 hours per middle day
            logger.info("middledayhours "+ middleDayHours);

            //double tafb = firstDayHours + middleDayHours + lastDayHours;
            double tafb = firstDayHours + middleDayHours + lastDayHours; 
            logger.pass("Total Away From Base (TAFB): " + tafb + " hours");

         // Extract whole hours
            int totalHours = (int) tafb;  // Get the integer part (7 from 7.5833333)
logger.info("after hours"+tafb);
            // Convert the decimal part to minutes correctly
            int totalMinutes = (int)Math.round((tafb - totalHours) * 60);  // (0.5833333 * 60) = 35 minutes
            logger.info("after minutes"+tafb);
            logger.info("Total TAFB: " + totalHours + " hours " + totalMinutes + " minutes");
         
            // Log results in Extent Report
            
            logger.pass("Total Sequences: " + totalSequences);
            logger.pass("First Report Time: " + reportFormattedTime + " hours");
            logger.pass("Final Release Time (+30 min): " + releaseFormattedTime + " hours");
            logger.pass("Last Total Away From Base (TAFB): " + tafb + " hours");
        }

	}
	}