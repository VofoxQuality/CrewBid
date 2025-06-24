package API;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.WbidBasepage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.given;

public class HoliRigFA_Initial extends WbidBasepage {
	private static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private static final SimpleDateFormat OUTPUT_FORMAT = new SimpleDateFormat("dd MMM");

    private static final Map<String, Double> tfpMap = new HashMap<>(); // Load this from trip file

    public static List<TripEntry> tripData = new ArrayList<>();
    public static List<Map<String, Object>> holirigResult = new ArrayList<>();
    @Test(priority = 1)
    public static void fetchApiData(String domicile, String expectedRound, String expectedPosition, String expectedMonth) throws Throwable {
        logger = extent.createTest("Bid Download API").assignAuthor("VS/445");
        
        String token = authenticateAndGetToken();
        JSONObject responseObject = fetchMonthlyBidData(token, domicile, expectedRound, expectedPosition, expectedMonth);
        extractTripData(responseObject);
        
     // ✅ Call processHolirig() to process the extracted trip data
       // processHolirig(expectedMonth);
        responseObject.keys().forEachRemaining(tripCode -> {
            if (tripCode.length() < 4) return; // Ignore invalid trip codes

            String tripCode4 = tripCode.substring(0, 4);
            System.out.println("Trip code" + tripCode4);
            JSONObject tripDetails = responseObject.getJSONObject(tripCode);

            if (tripDetails.has("DutyPeriods")) {
                JSONArray dutyPeriods = tripDetails.getJSONArray("DutyPeriods");
                double totalTfp = 0.0;

                for (int i = 0; i < dutyPeriods.length(); i++) {
                    JSONObject dutyPeriod = dutyPeriods.getJSONObject(i);
                    if (dutyPeriod.has("Tfp")) {
                        totalTfp += dutyPeriod.getDouble("Tfp");
                    }
                }

                // Store the sum of Tfp in the map
                tfpMap.put(tripCode4, totalTfp);
                logger.info("Added TFP for trip code: " + tripCode4 + " -> " + totalTfp);
            }
        });
    }

    private static String authenticateAndGetToken() {
        RestAssured.baseURI = "https://www.auth.wbidmax.com/WBidCoreService/api";
        String endpoint = "/user/GetSWAAndWBidAuthenticationDetails/";

        String requestBody = "{\n" + "    \"Base\": null,\n" + "    \"BidRound\": 0,\n"
				+ "    \"EmployeeNumber\": \"x21221\",\n" + "    \"FromAppNumber\": \"12\",\n"
				+ "    \"Month\": null,\n" + "    \"OperatingSystem\": null,\n" + "    \"Password\": \"Vofox2025@2$\",\n"
				+ "    \"Platform\": \"Web\",\n" + "    \"Postion\": null,\n"
				+ "    \"Token\": \"00000000-0000-0000-0000-000000000000\",\n" + "    \"Version\": \"10.4.16.3\"\n"
				+ "}";

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post(endpoint)
                .then().extract().response();

        Assert.assertEquals(response.getStatusCode(), 200, "Status Code does not match");
        return response.jsonPath().getString("Token");
    }

    private static JSONObject fetchMonthlyBidData(String token, String domicile, String expectedRound, String expectedPosition, String expectedMonth) {
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
		        + "\"Version\": \"10.4.16.3\","
		        + "\"Year\": 2025,"
		        + "\"isSecretUser\": true"
		        + "}";

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().post(nextEndpoint)
                .then().statusCode(200)
                .extract().response();

        String decompressedString = LZString.decompressFromUTF16(response.jsonPath().getString("lstBidDataFiles.FileContent[0]"));
        return new JSONObject(decompressedString);
    }

    private static void extractTripData(JSONObject responseObject) throws ParseException {
        JSONObject linesObject = responseObject.getJSONObject("Lines");
        List<String> allPairings = new ArrayList<>();

        for (String lineKey : linesObject.keySet()) {
            JSONObject lineData = linesObject.getJSONObject(lineKey);
            if (lineData.has("Pairings")) {
                JSONArray pairingsArray = lineData.getJSONArray("Pairings");
                for (int i = 0; i < pairingsArray.length(); i++) {
                    allPairings.add(pairingsArray.getString(i));
                }
            }
        }

        for (String lineKey : linesObject.keySet()) {
            JSONObject line = linesObject.getJSONObject(lineKey);
            JSONArray bidLineTemplates = line.getJSONArray("BidLineTemplates");

            for (int i = 0; i < bidLineTemplates.length(); i++) {
                JSONObject bidLine = bidLineTemplates.getJSONObject(i);
                if (bidLine.has("TripName") && bidLine.get("TripName") != JSONObject.NULL) {
                    String tripName = bidLine.getString("TripName");
                    for (String prefix : allPairings) {
                        if (tripName.startsWith(prefix)) {
                            String rawDate = bidLine.getString("Date");
                            Date utilDate = INPUT_FORMAT.parse(rawDate);
                            String formattedDate = OUTPUT_FORMAT.format(utilDate);
                            tripData.add(new TripEntry(tripName, formattedDate, Integer.parseInt(lineKey)));
                            break;
                        }
                    }
                }
            }
        }
     // Log trip data in the report
        

        tripData.sort(Comparator.comparingInt(entry -> entry.lineNumber));
        for (TripEntry entry : tripData) {
           // logger.info("Trip Data - Name: " + entry.tripName + ", Date: " + entry.date + ", Line Number: " + entry.lineNumber);
        }
    }

    
    
    private static void processHolirig(String expectedMonth) throws Throwable {
        List<String> targetDates = getTargetDatesFromExcel(Integer.parseInt(expectedMonth));
        Map<Integer, Double> resultMap = new LinkedHashMap<>();

        for (TripEntry entry : tripData) {
            boolean hasMatchingDate = targetDates.contains(entry.date);

            if (!hasMatchingDate) {
                resultMap.put(entry.lineNumber, 0.0);
                continue;
            }

            String tripCode = entry.tripName.length() >= 6 ? entry.tripName.substring(0, 6) : entry.tripName;
            String tripCode4 = tripCode.substring(0, 4);

            double tfpValue = tfpMap.getOrDefault(tripCode4, 0.0);
            double holiRig = (tfpValue < 4.0) ? 4.0 : tfpValue;

            resultMap.put(entry.lineNumber, holiRig);
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
    }}