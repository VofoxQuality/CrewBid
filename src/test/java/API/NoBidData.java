package API;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilities.WbidBasepage;

public class NoBidData extends WbidBasepage {
	//public static StringBuilder tripOutput = null;
	//LZString lzstring = new LZString();
	//public static String[] array;
	//public List<String> acftChanges;
	//public static Map<String, Double> tafbMap = new HashMap<>();
	//public static Map<String, Double> totRigAdgMap = new HashMap<>();
	// public static int i=0;
	//public static List<String> dynamicArray = new ArrayList<>();
	//public static List<Double> tfpSums = new ArrayList<>();
	//public static List<Double> dutyHrs = new ArrayList<>();
	//public static double tafb;
	//public static double TotalRigAdg;
	//public static String tripCode;
	//public static int passCount = 0, errorCount = 0;

	@Test(priority = 1)
	public static void fetchNoBidApiData() throws JsonProcessingException {
		WbidBasepage.logger = extent.createTest("No Bid API").assignAuthor("VS/445");

		
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
			Assert.assertEquals(response.getStatusCode(), 200, "Status Code does not match");
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
				+ "\"IsQATest\": false," + "\"IsRetrieveNewBid\": true," + "\"Month\": 4," + "\"Platform\": \"Web\","
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

		String fileContents = nextResponse.jsonPath().getString("lstBidDataFiles");
		System.out.println("File Content "+ fileContents);
		
		logger.info("Bid Data File is "+ fileContents);
		
		
		
		
	}

}
