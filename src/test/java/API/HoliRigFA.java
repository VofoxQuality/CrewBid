package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import API.HoliRigFACopy.TripEntry;
import utilities.WbidBasepage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.given;

public class HoliRigFA extends WbidBasepage {
    private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("dd MMM");

    private static final Map<String, Double> tfpMap = new LinkedHashMap<>(); // TFP storage
    public static List<TripEntry> tripData = new ArrayList<>();
    public static List<Map<String, Object>> holirigResult = new ArrayList<>();
    public static Map<String, Double> resultMap ;
    public static Map<String, Double> resultsTfp ;

    @Test(priority = 1)
    public static List<Map<String, Object>> fetchApiData(String domicile, String expectedRound, String expectedPosition, String expectedMonth) throws Throwable {
        logger = extent.createTest("Bid Download API").assignAuthor("VS/445");
        
        String token = authenticateAndGetToken();
        JSONObject responseObject = fetchMonthlyBidData(token, domicile, expectedRound, expectedPosition, expectedMonth);
        JSONObject responseObject1 = fetchMonthlyBidData1(token, domicile, expectedRound, expectedPosition, expectedMonth);
        
        extractTripData(responseObject);
        extractTFP(responseObject1);
        List<String> targetDates = getTargetDatesFromExcel(Integer.parseInt(expectedMonth));
     // ✅ Find Matching TFP Values
      //  Map<String, Map<Integer, Double>> results = findTFPValues(tripData, tfpMap, targetDates);
        resultsTfp = findTFPValues(tripData, tfpMap, targetDates);
        logger.info("Resulting TFPs"+resultsTfp);
        // ✅ Print Results
       /* for (String date : targetDates) {
            System.out.println("Results for Target Date: " + date);
            Map<Integer, Double> resultMap = results.get(date);
            for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
               // logger.info("Line Number: " + entry.getKey() + " -> TFP Value: " + entry.getValue());
            }
            System.out.println();
        }*/
        
        return processHolirig(expectedMonth);
    }
   /* public static Map<String, Map<Integer, Double>> findTFPValues(List<TripEntry> tripData, Map<String, Double> tfpMap, List<String> targetDates) {
        Map<String, Map<Integer, Double>> resultsByDate = new LinkedHashMap<>();

        // ✅ Step 1: Group All Trips by (TripCode + LineNumber) and Maintain Order
        Map<String, List<TripEntry>> tripGroups = new LinkedHashMap<>();
        for (TripEntry entry : tripData) {
            String tripKey = entry.tripName.substring(0, 4) + "_" + entry.lineNumber;
            tripGroups.computeIfAbsent(tripKey, k -> new ArrayList<>()).add(entry);
        }

        // ✅ Step 2: Track Current Processing Index for Each (TripCode + LineNumber)
        Map<String, Integer> tripProcessingIndex = new HashMap<>();

        // ✅ Step 3: Find Correct TFP Values
        for (String targetDate : targetDates) {
            Map<Integer, Double> resultMap = new LinkedHashMap<>();

            for (TripEntry entry : tripData) {
                if (targetDate.equals(entry.date)) { // ✅ Process only matching target dates
                    String tripCode4 = entry.tripName.substring(0, 4);
                    String tripKey = tripCode4 + "_" + entry.lineNumber;

                    // ✅ Get the occurrence index from pre-grouped map
                    List<TripEntry> tripList = tripGroups.getOrDefault(tripKey, new ArrayList<>());
                    int occurrenceIndex = tripList.indexOf(entry) + 1;  // ✅ Get correct sequence position
                    tripProcessingIndex.put(tripKey, occurrenceIndex);

                    // ✅ Correctly fetch TFP using sequence number
                    String tfpKey = tripCode4 + "_" + occurrenceIndex;
                    if (tfpMap.containsKey(tfpKey)) {
                        resultMap.put(entry.lineNumber, tfpMap.get(tfpKey));
                    } else {
                        System.out.println("Warning: No TFP found for key: " + tfpKey);
                    }
                }
            }
            resultsByDate.put(targetDate, resultMap);
        }
        return resultsByDate;
    }*/
    public static Map<String, Double> findTFPValues(List<TripEntry> tripData, Map<String, Double> tfpMap, List<String> targetDates) {
        Map<String, Map<Integer, Double>> resultsByDate = new LinkedHashMap<>();

        // ✅ Step 1: Group All Trips by (TripCode + LineNumber) and Maintain Order
        Map<String, List<TripEntry>> tripGroups = new LinkedHashMap<>();
        for (TripEntry entry : tripData) {
            String tripKey = entry.tripName.substring(0, 4) + "_" + entry.lineNumber;
            tripGroups.computeIfAbsent(tripKey, k -> new ArrayList<>()).add(entry);
        }

        // ✅ Step 2: Track Current Processing Index for Each (TripCode + LineNumber)
        Map<String, Integer> tripProcessingIndex = new HashMap<>();
        Map<String, Integer> resultMaptoReturn = new HashMap<>();

        // ✅ Step 3: Find Correct TFP Values
        for (String targetDate : targetDates) {
            resultMap = new LinkedHashMap<>();

            for (TripEntry entry : tripData) {
                if (targetDate.equals(entry.date)) { // ✅ Process only matching target dates
                    String tripCode4 = entry.tripName.substring(0, 4);
                    String tripKey = tripCode4 + "_" + entry.lineNumber;

                    // ✅ Get the occurrence index from pre-grouped map
                    List<TripEntry> tripList = tripGroups.getOrDefault(tripKey, new ArrayList<>());
                    int occurrenceIndex = tripList.indexOf(entry) + 1;  // ✅ Get correct sequence position
                    tripProcessingIndex.put(tripKey, occurrenceIndex);

                    // ✅ Correctly fetch TFP using sequence number
                    String tfpKey = tripCode4 + "_" + occurrenceIndex;
                    if (tfpMap.containsKey(tfpKey)) {
                        resultMap.put(entry.tripName, tfpMap.get(tfpKey));
                    } else {
                        System.out.println("Warning: No TFP found for key: " + tfpKey);
                    }
                }
            }
           
            //resultsByDate.put(targetDate, resultMap);
        }
        return resultMap;
    }
    private static void extractTFP(JSONObject responseObject1) {
        responseObject1.keys().forEachRemaining(tripCode -> {
            if (tripCode.length() < 4) return;

            String tripCode4 = tripCode.substring(0, 4);
            JSONObject tripDetails = responseObject1.getJSONObject(tripCode);

            if (tripDetails.has("DutyPeriods")) {
                JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");
                for (int i = 0; i < dutyPeriods.length(); i++) {
                    JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);
                    if (dutyPeriod.has("Tfp")) {
                        double tfpValue = dutyPeriod.getDouble("Tfp");
                        tfpMap.put(tripCode4 + "_" + (i + 1), tfpValue); // Store with Duty Sequence
                    }
                }
            }
        });
        logger.info("TFP MAP: "+tfpMap);
        
    }
    private static String authenticateAndGetToken() {
        RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
        String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";

        String requestBody = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n" + "    \"Password\": \"Vofox2025@2$\",\n"
				+ "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.5\"\n"
				+ "}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post(endpoint)
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Status Code does not match");
        System.out.println("Status code is :"+response.getStatusCode());
        return response.jsonPath().getString("Token");
    }

    private static JSONObject fetchMonthlyBidData(String token, String domicile, String expectedRound, String expectedPosition, String expectedMonth) throws JsonMappingException, JsonProcessingException {
        String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
        String requestBody = "{"
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
		        + "\"Year\": 2024,"
		        + "\"isSecretUser\": true"
		        + "}";

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post(nextEndpoint)
                .then().statusCode(200)
                .extract().response();

        String fileContents = LZString.decompressFromUTF16(response.jsonPath().getString("lstBidDataFiles.FileContent[0]"));
        
        
        String filePath = "WBL_Decompressed.txt";
        
		String decompressedString = fileContents;
		//decompressedString = decompressedString.replaceAll("^@+", "").trim();
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
        
        
        return new JSONObject(fileContents);
    }
    private static JSONObject fetchMonthlyBidData1(String token, String domicile, String expectedRound, String expectedPosition, String expectedMonth) throws JsonMappingException, JsonProcessingException {
        String nextEndpoint = "/BidData/GetMonthlyBidFiles/";
        String requestBody = "{"
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
		        + "\"Year\": 2024,"
		        + "\"isSecretUser\": true"
		        + "}";

        Response response1 = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post(nextEndpoint)
                .then().statusCode(200)
                .extract().response();

        String fileContents1 = LZString.decompressFromUTF16(response1.jsonPath().getString("lstBidDataFiles.FileContent[1]"));
        
        
        String filePath1 = "WBP_Decompressed.txt";
		String decompressedString1 = fileContents1;
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
        return new JSONObject(fileContents1);
    }
    private static void extractTripData(JSONObject responseObject) throws ParseException {
        JSONObject linesObject = responseObject.getJSONObject("Lines");
        for (String lineKey : linesObject.keySet()) {
            JSONObject line = linesObject.getJSONObject(lineKey);
            JSONArray bidLineTemplates = line.getJSONArray("BidLineTemplates");

            for (int i = 0; i < bidLineTemplates.length(); i++) {
                JSONObject bidLine = bidLineTemplates.getJSONObject(i);
                if (bidLine.has("TripName") && bidLine.get("TripName") != JSONObject.NULL) {
                    String tripName = bidLine.getString("TripName");
                    String rawDate = bidLine.getString("Date");
                    Date utilDate = INPUT_FORMAT.parse(rawDate);
                    String formattedDate = OUTPUT_FORMAT.format(utilDate);
                    tripData.add(new TripEntry(tripName, formattedDate, Integer.parseInt(lineKey)));
                }
            }
        }

        tripData.sort(Comparator.comparingInt(entry -> entry.lineNumber));
        for (TripEntry entry : tripData) {
        logger.info("Trip Data - Name: " + entry.tripName + ", Date: " + entry.date + ", Line Number: " + entry.lineNumber);
        }
    	
    }

    private static List<Map<String, Object>> processHolirig(String expectedMonth) throws Throwable {
        List<String> targetDates = getTargetDatesFromExcel(Integer.parseInt(expectedMonth));
        Map<Integer, Double> resultMap = new LinkedHashMap<>();
        Set<Integer> matchedLines = new HashSet<>(); // To track line numbers with a matching date

        for (TripEntry entry : tripData) {
            boolean hasMatchingDate = targetDates.contains(entry.date);
           // logger.info("Entry Line, Date and matching date" +entry.lineNumber+","+ entry.date +hasMatchingDate );
         // If we have already found a matching date for this line number, skip further iterations
            if (matchedLines.contains(entry.lineNumber)) {
                continue;
            }

            if (hasMatchingDate) {
                String tripCode = entry.tripName.length() >= 6 ? entry.tripName.substring(0, 6) : entry.tripName;
                //String tripCode4 = tripCode.substring(0, 4);
                String tripCode4 = tripCode;

                double tfpValue = resultsTfp.getOrDefault(tripCode4, 0.0);
                double holiRig = (tfpValue < 4.0) ? 4.0 : tfpValue;

                resultMap.put(entry.lineNumber, holiRig);
                matchedLines.add(entry.lineNumber); // Mark this line number as matched
            } else if (!resultMap.containsKey(entry.lineNumber)) {
                // Only set to 0 if it's the first time we encounter this line number
                resultMap.put(entry.lineNumber, 0.0);
            }
        }

        holirigResult.clear();
        for (Map.Entry<Integer, Double> entry : resultMap.entrySet()) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("Lines", entry.getKey());
            map.put("HolRig", entry.getValue());
            holirigResult.add(map);
        }

        holirigResult.sort(Comparator.comparingInt(o -> (int) o.get("Lines")));
        logger.info("Holirig Final result: " + holirigResult);
        return holirigResult;
    }

    public static List<String> getTargetDatesFromExcel(int month) throws IOException {
		List<String> targetDates = new ArrayList<>();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") +"\\src\\main\\resources\\Excelfiles\\Excelfile.xls");
		Workbook workbook = new HSSFWorkbook(fis);
		Sheet sheet = workbook.getSheetAt(3);

		boolean isHeader = true; // Flag to skip the header row
		for (Row row : sheet) {
			if (isHeader) {
				isHeader = false;
				continue; // Skip the header row
			}

			int cellMonth;
			try {
				if (row.getCell(0).getCellType() == CellType.NUMERIC) {
					cellMonth = (int) row.getCell(0).getNumericCellValue();
				} else if (row.getCell(0).getCellType() == CellType.STRING) {
					cellMonth = Integer.parseInt(row.getCell(0).getStringCellValue().trim());
				} else {
					continue; // Skip invalid rows
				}

				if (cellMonth == month) {
					targetDates.add(row.getCell(1).getStringCellValue());
				}
			} catch (NumberFormatException e) {
				// Log the invalid row and continue
				System.out.println("Skipping invalid row: " + row.getRowNum());
				continue;
			}
		}

		workbook.close();
		return targetDates;
	}

    static class TripEntry {
        String tripName;
        String date;
        int lineNumber;

        TripEntry(String tripName, String date, int lineNumber) {
            this.tripName = tripName;
            this.date = date;
            this.lineNumber = lineNumber;
        }
    }
}
