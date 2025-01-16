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
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DirectParam {
  @Test
  public void fetchParam() throws JsonProcessingException {
		RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
		String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";
		String requestBody1 ="{\n" +
			    "    \"Base\": null,\n" +
			    "    \"BidRound\": 0,\n" +
			    "    \"EmployeeNumber\": \"x21221\",\n" +
			    "    \"FromAppNumber\": \"12\",\n" +
			    "    \"Month\": null,\n" +
			    "    \"OperatingSystem\": null,\n" +
			    "    \"Password\": \"Vofox2024@5\",\n" +
			    "    \"Platform\": \"Web\",\n" +
			    "    \"Postion\": null,\n" +
			    "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" +
			    "    \"Version\": \"10.4.15.2\"\n" +
			    "}";
		Response response = given().header("Content-Type", "application/json").body(requestBody1).when().post(endpoint)
				.then().extract().response();
		System.out.println("Response is " + response.getStatusCode());

		System.out.println("API Response is" + response.asString());
		String token = response.jsonPath().getString("Token");
		System.out.println("Extracted Token: " + token);

//Step 2: Use the Token as Authorization in the Next API Call
		String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
		String requestBody2 = "{" + "\"Domicile\": \"ATL\"," + "\"EmpNum\": \"21221\"," + "\"FromAppNumber\": \"12\","
				+ "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": 12," + "\"Platform\": \"Web\","
				+ "\"Position\": \"FA\"," + "\"Round\": 1," + "\"secretEmpNum\": \"21221\","
				+ "\"Version\": \"10.4.15.2\"," + "\"Year\": 2024," + "\"isSecretUser\": true" + "}";// Replace with
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
		// Parse the JSON response
        JSONObject jsonObject = new JSONObject(prettyJson);
        JSONObject lines = jsonObject.getJSONObject("Lines");

        // Create a list to store the result
        List<Map<String, Object>> result = new ArrayList<>();
        // Get sorted keys
        List<String> sortedKeys = lines.keySet().stream()
                .sorted((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b)))
                .collect(Collectors.toList());


        // Iterate through the lines
        for (String lineKey : sortedKeys) {
            JSONObject lineData = lines.getJSONObject(lineKey);

            // Extract Pairings and HolRig
            JSONArray pairingsArray = lineData.getJSONArray("Pairings");
            double holRig = lineData.getDouble("HolRig");

            // Convert Pairings JSONArray to a List
            List<String> pairings = new ArrayList<>();
            for (int i = 0; i < pairingsArray.length(); i++) {
                pairings.add(pairingsArray.getString(i));
            }

            // Create a map to represent the line's data
            Map<String, Object> lineResult = new LinkedHashMap<>();
           
            
            lineResult.put("Pairing", pairings);
            lineResult.put("HolRig", holRig);
            

            // Add to the result list
            result.add(lineResult);
        }

        // Print the result
        System.out.println(result);
    }

	  
  }
