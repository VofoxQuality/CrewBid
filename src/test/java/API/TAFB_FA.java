package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;





public class TAFB_FA extends WbidBasepage  {
	public static Map<String, Integer> calculatedTafbMap = new HashMap<>();
	public static Map<String, Integer> tafbMap = new HashMap<>();
	 public static Map<String, List<List<Double>>> tripTimeMap = new HashMap<>();
	 public static int errorcount;
	 public static int passcount;
	 public static double LastReportTime=0.0;
	 public static double LastReportTimeAPI=0;
	 public static double finalReportTime=0.0;
	@Test(priority = 1)
	public static void fetchTafb(String domicile,String expectedRound, String expectedPosition, String expectedMonth) throws Throwable {
		WbidBasepage.logger = WbidBasepage.extent.createTest("Bid Download API").assignAuthor("VS/445");

		WbidBasepage.logger.info("Cred Values in an array");
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n" + "    \"Password\": \"Vofox2025@2$\",\n"
				+ "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.4\"\n"
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
		        + "\"Version\": \"10.4.16.4\","
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
		
	//JSONObject responseObject = new JSONObject(prettyJson);
	JSONObject TripresponseObject = new JSONObject(prettyJson1);
	TripresponseObject.keys().forEachRemaining(tripCode -> {
		logger.info("Trip Code: " + tripCode);
	    JSONObject tripDetails = TripresponseObject.getJSONObject(tripCode);
	    List<List<Double>> dutyPeriodsList = new ArrayList<>();

	    if (tripDetails.has("DutyPeriods")) {
            JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");

            for (int i = 0; i < dutyPeriods.length(); i++) {
                JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);

                // Extract ShowTime and ReleaseTime (default to 0.0 if missing)
                double reportTime = dutyPeriod.optDouble("ShowTime", 0.0);
                double releaseTime = dutyPeriod.optDouble("ReleaseTime", 0.0);

                // Check if "Flights" exists before accessing
               if (dutyPeriod.has("Flights")) {
                    JSONArray flights = dutyPeriod.getJSONArray("Flights");

                 // Get first flight departure time from the last duty period
                    if (i == dutyPeriods.length() - 1 && flights.length() > 0) {
                        JSONObject firstFlight = flights.getJSONObject(0); // Fetch first flight
                        LastReportTimeAPI = firstFlight.optDouble("DepTime", 0.0) ; // Subtract 30 minutes
          
                        logger.info("Last Report Time from API is" + LastReportTimeAPI);
                        /*finalReportTime = LastReportTimeAPI % (24 * 60);  //
                        logger.info("first conversion-final Report Time is" + finalReportTime);
                        double hours = finalReportTime / 60; // Get the number of hours
                        logger.info("Final Report Time Hours is" + hours);
                        
                    
                        double remainingMinutes = Math.round(finalReportTime % 60); // Get the remaining minutes
                        logger.info("Final Report Time minutes is " + remainingMinutes );
                        LastReportTime= hours*100+remainingMinutes;
                        logger.info("second conversion-Last Report Time(UI Format) is "+LastReportTime );
                      */  
                    }
                }

                // Log each duty period in report
                logger.info(tripCode + " DutSeq " + (i + 1) + " -> Report Time: " + reportTime + 
                            ", Release Time: " + releaseTime + ", Last Report Time: " + LastReportTimeAPI);

                // Store each duty period as a list [reportTime, releaseTime, LastReportTime]
                dutyPeriodsList.add(Arrays.asList(reportTime, releaseTime, LastReportTimeAPI));
            }
        }

        // Put the list of duty periods in the map
        tripTimeMap.put(tripCode, dutyPeriodsList);
        

        
        
   

	    // Extract direct TAFB from API response
	    double directTafb = tripDetails.optDouble("Tafb", 0.0);
	    logger.info("Direct TAFB "+directTafb);
	 // Convert to hours and minutes
	    int Directhours = (int) (directTafb / 60);       // Quotient gives hours
	    int Directminutes = (int) (directTafb % 60);  
      //  logger.info("DirectHours " + Directhours);
       // int DirTafbtotalMinutes = int(directTafb % 60;
       // logger.info("DirectMinutes "+ Directminutes);
        int concatenatedTafb = (Directhours * 100) + Directminutes; // Example: 12h 13m -> 1213
        //double finalTafbValue = concatenatedTafb / 100.0; 
        logger.info("📌 Direct TAFB (Formatted): " + Directhours + " hours " + Directminutes + " minutes");
	    tafbMap.put(tripCode, concatenatedTafb);

	    // 🟢 Calculate TAFB based on extracted duty periods
	    if (!dutyPeriodsList.isEmpty()) {
	        double firstReportTime = dutyPeriodsList.get(0).get(0);
	        double finalReleaseTime = dutyPeriodsList.get(dutyPeriodsList.size() - 1).get(1);
	        logger.info("Final Release Time from API is"+ finalReleaseTime);
	        double finalReportTime=dutyPeriodsList.get(dutyPeriodsList.size() - 1).get(2);
	        double finalReportTimeUltimate = finalReportTime % (24 * 60); 
	        int lastreportHours = (int) (finalReportTimeUltimate / 60);
            int lastreportMinutes = (int) (finalReportTimeUltimate % 60);
            lastreportMinutes -= 30;
            if (lastreportMinutes < 0) {  // If minutes go negative
                lastreportMinutes += 60;  // Add 60 to make it positive
                lastreportHours--;        // Decrease hours
            }

            double ModifiedFinalReportTime=(lastreportHours*100+lastreportMinutes);
            logger.info("finalReportTime is" +ModifiedFinalReportTime);
	     // Convert minutes into HH:MM format for First Report Time
            int reportHours = (int) (firstReportTime / 60);
            int reportMinutes = (int) (firstReportTime % 60);
            String reportFormattedTime = String.format("%02d:%02d", reportHours, reportMinutes);
            

            // Normalize Final Release Time within 24-hour cycle
            finalReleaseTime = finalReleaseTime % (24 * 60);  // Ensure it stays within 0-1439 minutes

            // Convert finalReleaseTime into hours and minutes
            int releaseHours = (int) (finalReleaseTime / 60);
            logger.info("release hours is" + releaseHours);
            int releaseMinutes = (int) (finalReleaseTime % 60);
            logger.info("Release minutes is " + releaseMinutes);
         // Store original hours before adding 30 minutes
            int originalReleaseHours = releaseHours;


            // Add 30 minutes
            releaseMinutes += 30;
            if (releaseMinutes >= 60) {
                releaseMinutes -= 60;
                releaseHours++;  // Carry over extra hour
            }
            logger.info("Adjusted release hours "+ releaseHours);
            logger.info("Adjusted release minutes "+ releaseMinutes);
            //double FinalllReleaseTime=releaseHours*100+releaseMinutes;
            //logger.info("After Adjust release time" + FinalllReleaseTime);
            //double Finalhours = FinalllReleaseTime / 100; // Extract hours
            //logger.info("After Adjust release final hours" + Finalhours);
            //double Finalminutes = FinalllReleaseTime % 100; // Extract minutes
            //logger.info("After Adjust release time" + Finalminutes);

            // Check if hours are 24 or more
            if (releaseHours >= 24) {
            	releaseHours = releaseHours % 24; // Convert hours to valid 24-hour format
            }
            double formattedTime = (releaseHours * 100) + releaseMinutes;

           // double ReleaseTimeUltimate = FinalllReleaseTime % (24 * 60); 
            logger.info("Final Release Time is "+ formattedTime);
           // logger.info("Release time is" + FinalllReleaseTime);
            // Ensure hours stay within 24-hour format
           // releaseHours = releaseHours % 24;
            

            String releaseFormattedTime = String.format("%02d:%02d", releaseHours, releaseMinutes);

            logger.info("First Report Time: " + reportFormattedTime);
            logger.info("Final Release Time: " + releaseFormattedTime);
        
            // TAFB Calculation
            int totalSequences = dutyPeriodsList.size();
            // If the release time crosses midnight, increment totalSequences
          /*  if (originalReleaseHours == 23 && releaseHours == 0) {
                totalSequences++;
            }*/
            if (ModifiedFinalReportTime>(formattedTime)) {
            	logger.info("Final Report Time is greater "+ ModifiedFinalReportTime+"Formatted Time is " + formattedTime);
            	totalSequences++;
             }
            double firstDayHours = 24 - (reportHours + (reportMinutes / 60.0));
//logger.info("firstdayhour "+ firstDayHours);//16.58
            double lastDayHours = releaseHours + (releaseMinutes / 60.0);
           // logger.info("lastdayhour "+ lastDayHours);10.416
            double middleDayHours = (totalSequences - 2) * 24; // Full 24 hours per middle day
            //logger.info("middledayhours "+ middleDayHours);
//50.99
            //double tafb = firstDayHours + middleDayHours + lastDayHours;
            double tafb = firstDayHours + middleDayHours + lastDayHours; 
            //logger.pass("Total Away From Base (TAFB): " + tafb + " hours");

         // Extract whole hours
            int totalHours = (int) tafb;  // Get the integer part (7 from 7.5833333)
//logger.info("after hours"+tafb);
            // Convert the decimal part to minutes correctly
            int totalMinutes = (int)Math.round((tafb - totalHours) * 60);  // (0.5833333 * 60) = 35 minutes
           // logger.info("after minutes"+tafb);
            //logger.info("Calculated TAFB: " + totalHours + " hours " + totalMinutes + " minutes");
            if(totalMinutes>=60)
            {
            	totalMinutes-=60;
            	totalHours++;
            }
	        int concatenatedTafbCalc = (totalHours * 100) + totalMinutes; // Example: 12h 13m -> 1213
	        //double finalTafbValueCalc = concatenatedTafbCalc / 100.0; 
	       // logger.info("📌 Calculated TAFB (Formatted): " + totalHours + " hours " + totalMinutes + " minutes");
	        calculatedTafbMap.put(tripCode, concatenatedTafbCalc);
	     // 🟢 Log direct vs calculated TAFB
	        
	        logger.info("📌 Direct TAFB from API: " + concatenatedTafb);
	        logger.info("📌 Calculated TAFB: " + concatenatedTafbCalc);

	        // 🔴 Comparison (Fail case if not matching)
	        
			if (Math.abs(concatenatedTafbCalc - concatenatedTafb) > 0.5) { 
	            logger.fail("❌ Mismatch in TAFB! Direct: " + concatenatedTafb + " vs Calculated: " + concatenatedTafbCalc);
	            errorcount++;
	        } else {
	            logger.pass("✅ TAFB Matched Successfully!");
	            passcount++;
	        }
	        System.out.println("Pass Count is" +passcount);
	        System.out.println("Error Count is" + errorcount);
	    }
	});

}}