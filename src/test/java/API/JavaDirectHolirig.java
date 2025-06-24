package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.log.Logger;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class JavaDirectHolirig{
	/////////////Direct HoliRig and TAFB line parameter////
	public static List<Map<String, Object>> resultAPI = new ArrayList<>();
	public static List<Map<String, Object>> result = new ArrayList<>();//direct Holirig
	public static List<Map<String, Object>> tafbLineAPI = new ArrayList<>();//direct TAFB line parameter
	public static HashMap<String, String> testDataMap = WbidBasepage.testData("qa environment");
	public static String expectedVersion = testDataMap.get("Version");
	public static int mismatchcount = 0;
	public static int matchcount = 0;

	
	 public static List<Map<String, Object>> fetchParam(String domicile,String expectedRound, String expectedPosition, String expectedMonth) throws JsonProcessingException{
		
			RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
			String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		/*	String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
					+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
					+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
					+ "    \"Password\": \"Vofox2025@3$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
					+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.5\"\n"
					+ "}";*/
			String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
					+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
					+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
					+ "    \"Password\": \"Vofox2025@3$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
					+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \""+expectedVersion+"\"\n"
					+ "}";
			Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
					.then().extract().response();
			System.out.println("Response is " + response.getStatusCode());

			System.out.println("API Response is" + response.asString());
			String token = response.jsonPath().getString("Token");
			System.out.println("Extracted Token: " + token);

	//Step 2: Use the Token as Authorization in the Next API Call
			String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
		/*	String requestBody2 = "{" + "\"Domicile\": \"" + domicile + "\"," + "\"EmpNum\": \"21221\","
					+ "\"FromAppNumber\": \"12\"," + "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": "
					+ expectedMonth + "," + "\"Platform\": \"Web\"," + "\"Position\": \"" + expectedPosition + "\","
					+ "\"Round\": " + expectedRound + "," + "\"secretEmpNum\": \"21221\"," +  "\"Version\": \"10.4.16.5\","
					+ "\"Year\": 2025," + "\"isSecretUser\": true" + "}";// Replace with*/
			String requestBody2 = "{" + "\"Domicile\": \"" + domicile + "\"," + "\"EmpNum\": \"21221\","
					+ "\"FromAppNumber\": \"12\"," + "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": "
					+ expectedMonth + "," + "\"Platform\": \"Web\"," + "\"Position\": \"" + expectedPosition + "\","
					+ "\"Round\": " + expectedRound + "," + "\"secretEmpNum\": \"21221\"," + "\"Version\": \""+expectedVersion+"\","
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
			String filePath = "Paramfetch1.txt";
			String decompressedString = LZString.decompressFromUTF16(fileContents);
			//System.out.println("Decompressed String: " + decompressedString);
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
			// Parse the JSON response
	        JSONObject jsonObject = new JSONObject(prettyJson);
	        JSONObject lines = jsonObject.getJSONObject("Lines");

	        // Create a list to store the result
	       // List<Map<String, Object>> result = new ArrayList<>();
	        // Get sorted keys
	        List<String> sortedKeys = lines.keySet().stream()
	                .sorted((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)))
	                .collect(Collectors.toList());


	        // Iterate through the lines
	        for (String lineKey : sortedKeys) {
	            JSONObject lineData = lines.getJSONObject(lineKey);

	            // Extract Pairings and HolRig
	            //JSONArray pairingsArray = lineData.getJSONArray("Pairings");
	            //JSONArray linesArray = lineData.getJSONArray("Lines");
	            double holRig = lineData.getDouble("HolRig");
	            // Extract TAFB line parameter
	            
	            String tafbLineAPIString = null;
	            if (lineData.has("TafbInLine") && !lineData.isNull("TafbInLine")) {
	                tafbLineAPIString = lineData.get("TafbInLine").toString(); // Safe fallback
	            } else {
	                tafbLineAPIString = "00:00"; // or whatever default makes sense
	            }


	            // Convert Pairings JSONArray to a List
	           // List<String> linesHolirig = new ArrayList<>();
	            /*for (int i = 0; i < sortedKeys.size(); i++) {
	            	linesHolirig.add(sortedKeys.get(i));
	            }*/

	            // Create a map to represent the line's data
	            Map<String, Object> lineResult = new LinkedHashMap<>();
	            Map<String, Object> lineResult1 = new LinkedHashMap<>();
	            lineResult1.put("Lines", lineKey);
	            lineResult1.put("HolRig", holRig);
	            result.add(lineResult1);
	            
	            // Create a map to represent the line's data and corresponding TAFB line parameter
	            Map<String, Object> lineTAFB = new LinkedHashMap<>();
	            lineTAFB.put("Lines", lineKey);
	            lineTAFB.put("TafbInLine", tafbLineAPIString);
	            tafbLineAPI.add(lineTAFB);
	           
	            JSONArray pairingsArray = lineData.getJSONArray("Pairings");
	            if(pairingsArray.length()>0)
	            {
	            lineResult.put("Lines", lineKey);
	            lineResult.put("HolRig", holRig);
	            resultAPI.add(lineResult);
	            }
	            

	            // Add to the result list
	            
	        }

	        // Print the result
	        //System.out.println(result);
	        WbidBasepage.logger.info("Direct Holirig "+resultAPI);
	        WbidBasepage.logger.info("API TAFB line parameter : "+tafbLineAPI);
	        return resultAPI;
	    }
	 
	// Method to compare the two lists of maps
	    public static boolean compareLists(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
	        
	    	boolean ismatching=true;
	    	int holirigSize = list1.size();

	    	for (int i = 0; i < holirigSize; i++) {
	    	    Map<String, Object> holirigMap = list1.get(i);

	    	    // Ensure index does not go out of bounds
	    	    if (i < list2.size()) {
	    	        Map<String, Object> resultMap = list2.get(i);

	    	        // Compare key-value pairs instead of whole map
	    	        boolean isMatch = true;
	    	        for (String key : holirigMap.keySet()) {
	    	            Object value1 = holirigMap.get(key);
	    	            Object value2 = resultMap.get(key);

	    	            if (!value1.equals(value2)) {
	    	                isMatch = false;
	    	                break;
	    	            }
	    	        }

	    	        // Now print result based on match
	    	        if (isMatch) {
	    	            //System.out.println("✅ Match found at index: " + i);
	    	            WbidBasepage.logger.info("Direct Map: " + resultMap);
	    	            WbidBasepage.logger.info("Calculated Map: " + holirigMap);
	    	        } else {
	    	            //System.out.println("❌ No match at index: " + i);
	    	            WbidBasepage.logger.info("Direct Map: " + resultMap);
	    	            WbidBasepage.logger.info("Calculated Map: " + holirigMap);
	    	        }
	    	    }
	    	}
			return ismatching;

	    }
	    
	    
	    
	 // Method to compare the two lists of maps in HolirigFA
	    public static boolean compareListsFA(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
	    	boolean ismatch = true;
	    	double TOLERANCE = 0.1;

	        if (list1.size() != list2.size()) {
	            System.out.println("❌ Lists have different sizes! Cannot compare.");
	            WbidBasepage.logger.info("❌ Lists have different sizes! Cannot compare.");
	            return false;
	        }

	        for (int i = 0; i < list1.size(); i++) {
	            if (i < list2.size()) {
	                double holRig1 = parseAndRoundHolRig(list1.get(i).get("HolRig"));
	                double holRig2 = parseAndRoundHolRig(list2.get(i).get("HolRig"));

	                // ✅ Print HolRig values for both lists
	                System.out.println("Index " + i + " -> Calculated HolRig: " + holRig1 + " | Direct Holrig: " + holRig2);
	                WbidBasepage.logger.info("Index " + i + " -> Calculated HolRig: " + holRig1 + " | Direct Holrig: " + holRig2);
	                // ✅ Fix: Use BigDecimal for precise comparison
	                if (Math.abs(holRig1 - holRig2) > TOLERANCE) {
	                	mismatchcount++;
	                    ismatch = false;
	                    System.out.println("❌ MISMATCH at index: " + i);
	                    WbidBasepage.logger.info("❌ MISMATCH at index: " + i);
	                } else {
	                	matchcount++;
	                    System.out.println("✅ MATCH at index: " + i);
	                    WbidBasepage.logger.info("✅ MATCH at index: " + i);
	                }
	            }
	        }
	        System.out.println("Total mismatches=" + mismatchcount);
			System.out.println("Total matches=" + matchcount);

	        if (ismatch) {
	            System.out.println("🎉 All values matched successfully!");
	            WbidBasepage.logger.info("🎉 All values matched successfully!");
	        } else {
	            System.out.println("⚠️ There were mismatches in the lists.");
	            WbidBasepage.logger.info("⚠️ There were mismatches in the lists.");
	        }

	        return ismatch;
	    }

	    private static double parseAndRoundHolRig(Object value) {
	        double numericValue;

	        if (value instanceof Double) {
	            numericValue = (double) value;
	        } else if (value instanceof String) {
	            numericValue = Double.parseDouble((String) value);
	        } else {
	            throw new IllegalArgumentException("Invalid type for HolRig: " + value);
	        }

	        return roundToTwoDecimals(numericValue);
	    }

	    private static double roundToTwoDecimals(double value) {
	        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
	    }

}
