package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class NewBidAPI extends WbidBasepage {
	public static StringBuilder tripOutput = null;
	LZString lzstring = new LZString();
	public static String[] array;
	public List<String> acftChanges;
	public static Map<String, Double> tafbMap = new HashMap<>();
	// public static int i=0;
	public static List<String> dynamicArray = new ArrayList<>();
	public static List<Double> tfpSums=new ArrayList<>();
	public static List<Double> dutyHrs=new ArrayList<>();
	public static double tafb;

	@Test(priority = 1)
	public static void fetchApiData() throws JsonProcessingException {
		WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("Cred Values in an array");
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n" + "    \"Password\": \"Vofox2025@1\",\n"
				+ "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.2\"\n"
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
		String requestBody2 = "{" + "\"Domicile\": \"ATL\"," + "\"EmpNum\": \"21221\"," + "\"FromAppNumber\": \"12\","
				+ "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": 3," + "\"Platform\": \"Web\","
				+ "\"Position\": \"CP\"," + "\"Round\": 1," + "\"secretEmpNum\": \"21221\","
				+ "\"Version\": \"10.4.16.2\"," + "\"Year\": 2025," + "\"isSecretUser\": true" + "}";// Replace with
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
					// System.out.println("All Pairings Array" +allPairings);
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

		JSONObject tripsData = new JSONObject(prettyJson1);
		// List<String> dynamicArray=new ArrayList<>() ;
		// Iterate through each trip in the JSON object
		tripsData.keys().forEachRemaining(tripCode -> {
			JSONObject tripDetails = tripsData.getJSONObject(tripCode);
			double tafb = tripDetails.getInt("Tafb");
			// Store tafb values along with trip codes
	        //Map<String, Integer> tafbMap = new HashMap<>();
	        tafbMap.put(tripCode, tafb);
	        //System.out.println("Stored Tafb Data:");
	       // tafbMap.forEach((key, value) -> System.out.println("TripCode: " + key + ", Tafb: " + value));
			
			// Start building the output for this trip
			tripOutput = new StringBuilder(tripCode + "  ");

			// Extract the DutyPeriods array
			JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");
			for (int i = 0; i < dutyPeriods.length(); i++) {
				JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);
				// Extract FlightSeqNum and Tfp
				int DutSeqNum = dutyPeriod.getInt("DutPerSeqNum");
				double TOTALtfp = dutyPeriod.getDouble("Tfp");
				double Dutyhrs = dutyPeriod.getDouble("DutyTime");
				
				 // Calculate hours and remaining minutes
		        int hours = (int) (Dutyhrs / 60);
		        int minutes = (int) (Dutyhrs % 60);
		        double timeAsDouble = hours + (minutes / 100.0);
				// tripOutput.append("DutySeqNum" + DutSeqNum + "
				// TOTALtfp").append(":").append(TOTALtfp).append(" ");
				double RigAdg = dutyPeriod.getDouble("RigAdg");
				tripOutput.append("DutySeqNum" + DutSeqNum + " TOTALfp").append(":").append(TOTALtfp + RigAdg)
						.append(" ").append(" Dutyhrs "+timeAsDouble+" ");

				// Extract the Flights array
				JSONArray flights = dutyPeriod.getJSONArray("Flights");
				for (int j = 0; j < flights.length(); j++) {
					JSONObject flight = flights.getJSONObject(j);

					// Extract FlightSeqNum and Tfp
					int flightSeqNum = flight.getInt("FlightSeqNum");
					double tfp = flight.getDouble("Tfp");

					// Append to the trip output
					tripOutput.append("FlightSeqNum " + flightSeqNum).append(" ").append("Tfp:" + tfp).append("   ");
				}
			}

			// Print the result for this trip
			System.out.println(tripOutput.toString().trim());
			// convert stringbuilder to string

			String strOutput = tripOutput.toString();
			// Save the string to an array

			dynamicArray.add(strOutput);
			//System.out.println("Stored Tafb Data:");
	       // tafbMap.forEach((key, value) -> System.out.println("TripCode: " + key + ", Tafb: " + value));
			//logger.info("Stored Tafb Data:");
	       // tafbMap.forEach((key, value) -> logger.info("TripCode: " + key + ", Tafb: " + value));
			

		});
		System.out.println("Stored Tafb Data:");
        tafbMap.forEach((key, value) -> System.out.println("TripCode: " + key + ", Tafb: " + value));
		// System.out.println("DYNAMIC ARRAY IS "+dynamicArray);
		for (int i = 0; i < dynamicArray.size(); i++) {
			//System.out.println("Dynamic Array element " + i + ": " + dynamicArray.get(i));
			logger.info("Dynamic Array element " + i + " " + dynamicArray.get(i));
		}
		/*Fetching Each Tfp values for the corresponding trip code*/
		/*ArrayList<Double> newArr = new ArrayList<>();

		for (String entry : dynamicArray) {
			if (entry.startsWith("AA43")) {
				String[] parts = entry.split("Tfp:");
				for (int i = 1; i < parts.length; i++) {
					try {
						String valuePart = parts[i].split(" ")[0];
						System.out.println("Value Part" + valuePart);
						newArr.add(Double.parseDouble(valuePart.trim()));
						
					} catch (NumberFormatException e) {
						logger.fail("Invalid Tfp value in entry: " + parts[i]);
					}
				}
				break; // Exit loop after finding the matching entry
			}
		}*/
		for (String s : dynamicArray) {
			
            if (s.startsWith("AAFH")) {
            	if (tafbMap.containsKey("AAFH")) {
            	    System.out.println("For TripCode: AAFH, Tafb is: " + tafbMap.get("AAFH"));
            	    tafb=tafbMap.get("AAFH");
            	    System.out.println("TafB is: " + tafb);
            	    
            	} else {
            	    System.out.println("TripCode " + "AAFH" + " not found.");
            	}
                // Extract and calculate the sum of TFP for each dynamic DutySeqNum
               // List<Double> tfpSums = extractAndSumTfps(s);
                tfpSums = extractAndSumTfps(s);

                // Print the TFP sums for each DutySeqNum
                for (int i = 0; i < tfpSums.size(); i++) {
                    System.out.println("tfpsum" + (i + 1) + ": " + tfpSums.get(i));
                }
                System.out.println("tfpsum array"  + tfpSums);
            }
        }
		
		  // Extracting Dutyhrs for AA33
        for (String s : dynamicArray) {
            if (s.startsWith("AAFH")) {
                //List<Double> dutyHrs = extractDutyHours(s);
            	 dutyHrs = extractDutyHours(s);
                System.out.println("Dutyhrs array: " + dutyHrs);
            }
        }
        CredCalculation();
    }

	 public static List<Double> extractDutyHours(String input) {
	        List<Double> dutyHrs = new ArrayList<>();
	        String[] parts = input.split("Dutyhrs ");
	        for (int i = 1; i < parts.length; i++) {
	            dutyHrs.add(Double.parseDouble(parts[i].split("FlightSeqNum")[0].trim()));
	        }
	        return dutyHrs;
	    }
	
	// Method to extract and sum the TFP values for all DutySeqNum numbers dynamically
    public static List<Double> extractAndSumTfps(String data) {
        List<Double> tfpSums = new ArrayList<>();

        // Regular expression to match DutySeqNum followed by number and Tfp values
        String dutySeqPattern = "DutySeqNum(\\d+).*?TOTALfp:[\\d\\.]+(.*?)((?=DutySeqNum|$))";
        
        Pattern pattern = Pattern.compile(dutySeqPattern, Pattern.DOTALL); // Pattern.DOTALL allows for multiline matching
        Matcher matcher = pattern.matcher(data);

        // Loop through each DutySeqNum block
        while (matcher.find()) {
            String dutySeqData = matcher.group(2);  // Extract the Tfp values section for this DutySeqNum
            double sum = sumTfpValues(dutySeqData);  // Sum the Tfp values for this DutySeqNum
            tfpSums.add(roundToDecimal(sum, 1)); // Store the rounded sum with 1 decimal place
        }

        return tfpSums;
    }

    // Method to sum the TFP values for a given DutySeqNum section
    public static double sumTfpValues(String dutySeqData) {
        double sum = 0.0;
        
        // Pattern to match Tfp values under each DutySeqNum section
        String tfpPattern = "Tfp:([\\d\\.]+)";  // Match any Tfp value

        Pattern pattern = Pattern.compile(tfpPattern);
        Matcher matcher = pattern.matcher(dutySeqData);

        // Sum all the Tfp values in this section
        while (matcher.find()) {
            sum += Double.parseDouble(matcher.group(1));  // Sum the TFP values
        }

        return sum;
    }

    // Method to round a number to a specified number of decimal places
    public static double roundToDecimal(double value, int places) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);  // Round to specified decimal places
        return bd.doubleValue();
    }
    
    

    public static void CredCalculation (){
        
            // Initial values
        //    List<Double> cred = Arrays.asList(7.5, 7.9, 3.2); // Original cred values
          //  List<Double> dhr = Arrays.asList(9.45, 10.35, 5.0); // Original dhr values
            double threshold = 5.0;          // Threshold for rig dpm
            double tmpPerHour = 6.5;         // TMP value per hour
            //double tafb = 19.15;             // TAFB value

            // Step 1: Calculate new rig dpm values
            List<Double> newRigDpm = new ArrayList<>();
            for (double value : tfpSums) {
                newRigDpm.add(Math.max(value, threshold));
            }

            // Step 2: Calculate new dhr values
            List<Double> newDhr = new ArrayList<>();
            for (double value : dutyHrs) {
                int hours = (int) value; // Extract hours
                double minutes = (value - hours) * 100; // Extract minutes
               // newDhr.add((hours + (minutes / 60)) * 0.74); // Perform the calculation
                newDhr.add(roundToDecimal((hours + (minutes / 60.0)) * 0.74));
               
            }
            

            // Step 3: Compare new rig dpm with new dhr to get new cred values
            List<Double> ar = new ArrayList<>();
            for (int i = 0; i < tfpSums.size(); i++) {
                ar.add(Math.max(newRigDpm.get(i), newDhr.get(i)));
            }

            // Step 4: Calculate total cred value
            double totCredValue = ar.stream().mapToDouble(Double::doubleValue).sum();

            // Step 5: Calculate TMP
            double tmp = tmpPerHour*tfpSums.size(); // TMP = per hour value  count
            
int size=tfpSums.size();    
System.out.println("No of days Array Size" + size);
// Calculate hours and remaining minutes
int tafbhours = (int) (tafb / 60);
int tafbminutes = (int) (tafb % 60);
double tafbtimeAsDouble = (tafbhours + (tafbminutes / 100.0));

// Step 6: Determine final cred
          //  double finalCred = Math.max(tmp, tafbtimeAsDouble/size);

            // Step 7: Calculate Grand Value
           // double grandValue = finalCred - totCredValue;
double RigAdg = 0;
double RigThr = 0;
double newTafb=(tafb/60)/size;

if (tmp > totCredValue) {
    RigAdg = tmp - totCredValue;
} else if (newTafb > totCredValue) {
    RigThr = newTafb - totCredValue;
} 

            // Output the results
            logger.info("New Rig DPM: " + newRigDpm);
            logger.info("New DHR: " + newDhr);
            logger.info("New Cred Values (ar): " + ar);
            logger.info("Total Cred Value: " + totCredValue);
            logger.info("TMP: " + tmp);
            logger.info("TAFB: " + newTafb);
            logger.info("Rig Adg: " + RigAdg);
            logger.info("Rig Thr: " + RigThr);
            
        }
    
    public static double roundToDecimal(double value) {
        return Math.round(value * 100.0) / 100.0; // Rounds to 2 decimal places
    }
	/*
	public static List<Double> extractAndSumTfps(String data) {
        List<Double> tfpSums = new ArrayList<>();

        // Regular expression to match DutySeqNum followed by number and Tfp values
        String dutySeqPattern = "DutySeqNum(\\d+).*?TOTALtfp:[\\d\\.]+(.*?)((?=DutySeqNum|$))";
        
        Pattern pattern = Pattern.compile(dutySeqPattern, Pattern.DOTALL); // Pattern.DOTALL allows for multiline matching
        Matcher matcher = pattern.matcher(data);

        // Loop through each DutySeqNum block
        while (matcher.find()) {
            String dutySeqData = matcher.group(2);  // Extract the Tfp values section for this DutySeqNum
            double sum = sumTfpValues(dutySeqData);  // Sum the Tfp values for this DutySeqNum
            tfpSums.add(sum);
        }

        return tfpSums;
    }

    // Method to sum the TFP values for a given DutySeqNum section
    public static double sumTfpValues(String dutySeqData) {
        double sum = 0.0;
        
        // Pattern to match Tfp values under each DutySeqNum section
        String tfpPattern = "Tfp:([\\d\\.]+)";  // Match any Tfp value

        Pattern pattern = Pattern.compile(tfpPattern);
        Matcher matcher = pattern.matcher(dutySeqData);

        // Sum all the Tfp values in this section
        while (matcher.find()) {
            sum += Double.parseDouble(matcher.group(1));
        }

        return sum;
    }*/
    
	
}
