package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
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

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class LineParameterDirect {
/////////////Direct Line parameter - DPM, DHR ////
	
	public static List<Map<String, Object>> dpmLineAPI = new ArrayList<>();//direct DPM line parameter
	public static List<Map<String, Object>> dhrLineAPI = new ArrayList<>();//direct DHR line parameter
	public static List<Map<String, Object>> adgLineAPI = new ArrayList<>();//direct ADG line parameter
	public static List<Map<String, Object>> thrLineAPI = new ArrayList<>();//direct THR line parameter
	public static HashMap<String, String> testDataMap = WbidBasepage.testData("qa environment");
	public static String expectedVersion = testDataMap.get("Version");
	public static String userPassword = testDataMap.get("Password");
	
	 public static List<Map<String, Object>> fetchParam(String domicile,String expectedRound, String expectedPosition, String expectedMonth) throws JsonProcessingException{
		
			RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
			String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
			/*String requestBody1 = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
					+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
					+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n"
					+ "    \"Password\": \"Vofox2025@3$\",\n" + "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
					+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \""+expectedVersion+"\"\n"
					+ "}";*/
			
			String requestBody1 = "{\n" +
				    "    \"Base\": null,\n" +
				    "    \"BidRound\": 0,\n" +
				    "    \"EmployeeNumber\": \"x21221\",\n" +
				    "    \"FromAppNumber\": \"12\",\n" +
				    "    \"Month\": null,\n" +
				    "    \"OperatingSystem\": null,\n" +
				    "    \"Password\": \"" + userPassword + "\",\n" +
				    "    \"Platform\": \"Web\",\n" +
				    "    \"Postion\": null,\n" +
				    "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" +
				    "    \"Version\": \"" + expectedVersion + "\"\n" +
				    "}";
			
			Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
					.then().extract().response();
			System.out.println("Response is " + response.getStatusCode());

			System.out.println("API Response is" + response.asString());
			String token = response.jsonPath().getString("Token");
			System.out.println("Extracted Token: " + token);

	//Step 2: Use the Token as Authorization in the Next API Call
			String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
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
			
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(decompressedString, Object.class);
			String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
	
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

// Extract Pairings and DPM line parameter
	            String dpmLineAPIString = null;
	            if (lineData.has("RigDailyMinInLine") && !lineData.isNull("RigDailyMinInLine")) {
	                dpmLineAPIString = lineData.get("RigDailyMinInLine").toString(); // Safe fallback
	            } else {
	                dpmLineAPIString = "0.0"; // or whatever default makes sense
	            }           
	            // Create a map to represent the line's data and corresponding DPM line parameter
	            Map<String, Object> lineDPM = new LinkedHashMap<>();
	            lineDPM.put("Lines", lineKey);
	            lineDPM.put("RigDailyMinInLine", dpmLineAPIString);
	            dpmLineAPI.add(lineDPM);
	            
	            
// Extract Pairings and DHR line parameter	    
	            String dhrLineAPIString = null;
	            if (lineData.has("RigDhrInLine") && !lineData.isNull("RigDhrInLine")) {
	            	dhrLineAPIString = lineData.get("RigDhrInLine").toString(); // Safe fallback
	            } else {
	            	dhrLineAPIString = "0.0"; // or whatever default makes sense
	            }
	            
	            // Create a map to represent the line's data and corresponding ADG line parameter
	            Map<String, Object> lineDHR = new LinkedHashMap<>();
	            lineDHR.put("Lines", lineKey);
	            lineDHR.put("RigDhrInLine", dhrLineAPIString);
	            dhrLineAPI.add(lineDHR);
	            
// Extract Pairings and ADG line parameter
	            
	            String adgLineAPIString = null;
	            if (lineData.has("RigAdgInLine") && !lineData.isNull("RigAdgInLine")) {
	                adgLineAPIString = lineData.get("RigAdgInLine").toString(); // Safe fallback
	            } else {
	                adgLineAPIString = "0.0"; // or whatever default makes sense
	            }           
	            // Create a map to represent the line's data and corresponding DPM line parameter
	            Map<String, Object> lineADG = new LinkedHashMap<>();
	            lineADG.put("Lines", lineKey);
	            lineADG.put("RigAdgInLine", adgLineAPIString);
	            adgLineAPI.add(lineADG);
	            
	            
// Extract Pairings and THR line parameter
	            
	            String thrLineAPIString = null;
	            if (lineData.has("RigTafbInBp") && !lineData.isNull("RigTafbInBp")) {
	                thrLineAPIString = lineData.get("RigTafbInBp").toString(); // Safe fallback
	            } else {
	                thrLineAPIString = "0.0"; // or whatever default makes sense
	            }           
	            // Create a map to represent the line's data and corresponding DPM line parameter
	            Map<String, Object> lineTHR = new LinkedHashMap<>();
	            lineTHR.put("Lines", lineKey);
	            lineTHR.put("RigTafbInBp", thrLineAPIString);
	            thrLineAPI.add(lineTHR);
	           
	        }

	        // Print the result	      
	        WbidBasepage.logger.info("API DPM line parameter : "+dpmLineAPI);
	        WbidBasepage.logger.info("API DHR line parameter : "+dhrLineAPI);
	        WbidBasepage.logger.info("API ADG line parameter : "+adgLineAPI);
	        WbidBasepage.logger.info("API THR line parameter : "+thrLineAPI);
	        return dpmLineAPI;
	    }
	 
	

}
