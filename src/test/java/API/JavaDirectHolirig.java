package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

public class JavaDirectHolirig {
	
	public static List<Map<String, Object>> result = new ArrayList<>();
	
	 public static void fetchParam(String domicile,String expectedRound, String expectedPosition, String expectedMonth) throws JsonProcessingException{
		
			RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
			String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
			String requestBody1 ="{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
					+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
					+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
					+ "    \"Password\": \"Vofox2025@2$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
					+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.5\"\n"
					+ "}";
			Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
					.then().extract().response();
			System.out.println("Response is " + response.getStatusCode());

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

	            // Convert Pairings JSONArray to a List
	           // List<String> linesHolirig = new ArrayList<>();
	            /*for (int i = 0; i < sortedKeys.size(); i++) {
	            	linesHolirig.add(sortedKeys.get(i));
	            }*/

	            // Create a map to represent the line's data
	            Map<String, Object> lineResult = new LinkedHashMap<>();
	           
	            
	            lineResult.put("Lines", lineKey);
	            lineResult.put("HolRig", holRig);
	            

	            // Add to the result list
	            result.add(lineResult);
	        }

	        // Print the result
	        System.out.println(result);
	        WbidBasepage.logger.info("Direct Holirig "+result);
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
	    	            WbidBasepage.logger.info("Direct Map: " + holirigMap);
	    	            WbidBasepage.logger.info("Calculated Map: " + resultMap);
	    	        } else {
	    	            //System.out.println("❌ No match at index: " + i);
	    	            WbidBasepage.logger.info("Direct Map: " + holirigMap);
	    	            WbidBasepage.logger.info("Calculated Map: " + resultMap);
	    	        }
	    	    }
	    	}
			return ismatching;

	    }

}
