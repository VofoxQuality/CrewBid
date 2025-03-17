package pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class CredValuesPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();

	public CredValuesPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
	
	@FindBy(xpath = "//div[1]/div[1]/div/div[2]/table/tr/td[contains(@class,'left-side-radius')]")
	public List<WebElement> tripListLine;//Trip detail visible when we click tripList line 1

	@FindBy(xpath = "//div[contains(@class,'cala-view')][100]/div/div[2]/table/tr/td[contains(@class,'left-side-radius')]")
	public List<WebElement> tripListLine100;//Trip detail visible when we click tripList line 100
	
	@FindBy(xpath = "//div[contains(@class,'cala-view')]")
	public List<WebElement> tripLines; // Each 'div' contains trip details
	
	public String getTripXPath(int i) {
	    return String.format("(//div[contains(@class,'cala-view')])[%d]/div/div[2]/table/tr/td[contains(@class,'left-side-radius')]", i);
	}

	public Map<String, Map<String, String>> getAllTripData() {
	    Map<String, Map<String, String>> allTripsMap = new HashMap<>(); // Stores (Line -> (Trip Code -> Date))
	 // Line counter
   	 int lineCount =tripLines.size();
	   
	 //   for (WebElement tripLine : tripLines) {  
	    for ( int i = 1;i<lineCount;i++) {  
	        try {
	        	
	            // Find trip elements inside this specific trip line
	        	String tripXPath = getTripXPath(i);
	            List<WebElement> tripList = driver.findElements(By.xpath(tripXPath)); Map<String, String> tripDataMap = new HashMap<>(); // Stores (Trip Code -> Date) for each line
	            if (tripList.isEmpty()) {
	                WbidBasepage.logger.info("No trips found in Line " + (i + 1));
	                continue;
	            } 
	        	 
	            for (WebElement tripElement : tripList) {  
	                try {
	                    objwait.waitForElementTobeVisible(driver, tripElement, 90);

	                    // Scroll into view
	                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
	                    objwait.waitForElemntTobeClickable(driver, tripElement, 30);

	                    // Click the trip element
	                    try {
	                        tripElement.click();
	                    } catch (Exception e) {
	                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
	                    }

	                    objwait.waitForElementTobeVisible(driver, tripSequence, 90);
	                    String tripSequenceText = objaction.gettext(tripSequence).trim();
	          //          WbidBasepage.logger.info("Line No: "+i+" Trip Sequence Text: " + tripSequenceText);

	                    tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
	                    Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
	                    Matcher matcher = pattern.matcher(tripSequenceText);

	                    if (matcher.find()) {
	                        String tripCode = matcher.group(1).trim();
	                  //      WbidBasepage.logger.info("Line No: "+i+" Extracted Trip Code: " + tripCode);

	                        for (WebElement tripEle : tripdata) {
	                            String tripDataText = objaction.gettext(tripEle).trim();
	                            WbidBasepage.logger.info("Line No: "+i+" Trip Data Text: " + tripDataText);

	                            // Ignore unwanted trip data
	                            if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
	                          //      WbidBasepage.logger.info("Line No: "+i+" Ignoring trip data: " + tripDataText);
	                                continue;
	                            }

	                            tripDataText = tripDataText.replaceAll("\\s+", " ");

	                            // Extract date (e.g., "01Apr", "15May")
	                            Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
	                            Matcher matcherD = patternD.matcher(tripDataText);

	                            if (matcherD.find()) {
	                                String tripDate = matcherD.group(1).trim();
	                                WbidBasepage.logger.info("Line No: "+i+ " Extracted Trip Code: " + tripCode + " and Trip Date: " + tripDate);

	                                // Store the trip code and date in the map for this line
	                                tripDataMap.put(tripCode, tripDate);
	                                WbidBasepage.logger.info("Line No: "+i+ " Extracted Trip Data: " + tripDataMap );

	                            } else {
	                                WbidBasepage.logger.fail("Trip date not found in the trip sequence text.");
	                            }
	                        }
	                    }

	                    // Close modal properly
	                    try {
	                        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
	                    } catch (Exception e) {
	                        WbidBasepage.logger.info("Modal close action failed, continuing.");
	                    }
	                } catch (Exception e) {
	                    WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
	                }
	            }

	            // Store data for this line
	            allTripsMap.put("Line " + i, tripDataMap);
	            WbidBasepage.logger.info("Line No: "+i+ " Extracted Trip Data for this Line: " + allTripsMap );

	        } catch (Exception e) {
	            WbidBasepage.logger.fail("Error processing trip line: " + e.getMessage());
	        }
	    }
	    WbidBasepage.logger.info(" Extracted Trip Data for this Line: " + allTripsMap );
	    return allTripsMap; // Return all trip data mapped to line numbers
	}

	@FindBy(xpath = "//td[contains(@class,'left-side-radius')]")
	public List<WebElement> tripList;//Trip detail visible when we click tripList
	
	@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[1]/div/pre")
	public WebElement tripSequence;//Trip code Extracted from Trip Sequence
	
	@FindBy(xpath = "(//pre[@style='text-decoration: inherit;' and normalize-space(.)!=''])[position() > 2]")
	public List<WebElement> tripdata;//gets data starting  from trip details skip 1st 2 lines which show trip code and table header
	
	//Get individual Cred  and totalCred from UI 
	
	public Map<String, Map<String, Integer>> getAllCred() {
	    Map<String, Integer> credMapUI = new HashMap<>();
	    Map<String, Integer> totalCredMapUI = new HashMap<>();
	    int i = 0;

	    for (WebElement tripElement : tripList) {  
	        try {
	            i++;
	            if (i > 5) break; // Process only the first 5 trips

	            objwait.waitForElementTobeVisible(driver, tripElement, 90);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
	            objwait.waitForElemntTobeClickable(driver, tripElement, 30);

	            // Try normal click first, fallback to JavaScript click
	            try {
	                tripElement.click();
	            } catch (Exception e) {
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
	            }

	            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
	            String tripSequenceText = objaction.gettext(tripSequence).trim();
	            WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);

	            tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
	            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
	            Matcher matcher = pattern.matcher(tripSequenceText);

	            if (matcher.find()) {
	                String tripCode = matcher.group(1).trim();
	                WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);

	                for (WebElement tripEle : tripdata) {
	                    String tripDataText = objaction.gettext(tripEle).trim();
	                    WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

	                    // Handle total credit separately for "Rpt"
	                    if (tripDataText.startsWith("Rpt")) {
	                        Pattern threeDigitPattern = Pattern.compile("\\b(\\d{3})\\b");
	                        Matcher numberMatcher = threeDigitPattern.matcher(tripDataText);
	                        String lastThreeDigit = null;

	                        while (numberMatcher.find()) {
	                            lastThreeDigit = numberMatcher.group(1);  // Get the last occurrence
	                        }

	                        if (lastThreeDigit != null) {
	                            try {
	                                int extractedValue = Integer.parseInt(lastThreeDigit);
	                                WbidBasepage.logger.info("Extracted Total Credit (Rpt) for " + tripCode + ": " + extractedValue);
	                                totalCredMapUI.put(tripCode, extractedValue);
	                            } catch (NumberFormatException e) {
	                                WbidBasepage.logger.fail("Failed to parse extracted total credit: " + lastThreeDigit);
	                            }
	                        }
	                        continue; // Skip the rest of the loop
	                    }

	                    // Ignore "TAFB"
	                    if (tripDataText.startsWith("TAFB")) {
	                        WbidBasepage.logger.info("Ignoring trip data: " + tripDataText);
	                        continue;
	                    }

	                    // Extract last three-digit number for individual credits
	                    Pattern threeDigitPattern = Pattern.compile("\\b(\\d{3})\\b");
	                    Matcher numberMatcher = threeDigitPattern.matcher(tripDataText);
	                    String lastThreeDigit = null;

	                    while (numberMatcher.find()) {
	                        lastThreeDigit = numberMatcher.group(1);  // Get the last occurrence
	                    }

	                    if (lastThreeDigit != null) {
	                        try {
	                            int extractedValue = Integer.parseInt(lastThreeDigit);
	                            WbidBasepage.logger.info("Extracted Individual Credit for " + tripCode + ": " + extractedValue);
	                            credMapUI.put(tripCode, extractedValue);
	                        } catch (NumberFormatException e) {
	                            WbidBasepage.logger.fail("Failed to parse extracted individual credit: " + lastThreeDigit);
	                        }
	                    }
	                }
	            }

	        } catch (Exception e) {
	            WbidBasepage.logger.fail("Error processing trip: " + e.getMessage());
	        } finally {
	            // Close modal properly after processing the trip
	            try {
	                new Actions(driver).sendKeys(Keys.ESCAPE).perform();
	            } catch (Exception e) {
	                WbidBasepage.logger.info("Modal close action failed, continuing.");
	            }
	        }
	    }

	    // Return both individual and total credit maps
	    Map<String, Map<String, Integer>> result = new HashMap<>();
	    result.put("IndividualCred", credMapUI);
	    WbidBasepage.logger.info("IndividualCred"+ credMapUI);
	    result.put("TotalCred", totalCredMapUI);
	    WbidBasepage.logger.info("TotalCred"+ totalCredMapUI);
	    return result;
	}
	
	public Map<String, Map<String, List<Integer>>> tripDataWithLine() { 
	    Map<String, Map<String, List<Integer>>> allTripsMap = new HashMap<>(); // Stores (Line -> (Trip Code -> [Date, Credits]))
	    Map<String, Integer> totalCredMapUI = new HashMap<>();
	   
	    
	    Pattern tripCodePattern = Pattern.compile("Trip\\s(\\w+)\\sDated");  // Extract Trip Code
	    Pattern datePattern = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");   // Extract Trip Date
	    Pattern threeDigitPattern = Pattern.compile("\\b(\\d{3})\\b");      // Extract Trip Credits
	    
	    Set<String> processedTrips = new HashSet<>(); // Avoid duplicate clicks
	    int lineCount = tripLines.size();
	   
	    for (int i = 1; i < lineCount; i++) {  
	        try {
	            String tripXPath = getTripXPath(i);
	            List<WebElement> tripList = driver.findElements(By.xpath(tripXPath)); 
	            Map<String, List<Integer>> credMapUI = new HashMap<>(); // Stores (Trip Code -> [Dates, Credits])

	            if (tripList.isEmpty()) {
	                WbidBasepage.logger.info("No trips found in Line " + i);
	                continue;
	            } 

	            for (WebElement tripElement : tripList) {  
	                try {
	                    objwait.waitForElementTobeVisible(driver, tripElement, 90);
	                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
	                    objwait.waitForElemntTobeClickable(driver, tripElement, 30);

	                    String tripText = tripElement.getText().trim();
	                    if (processedTrips.contains(tripText)) continue; // Skip already processed trips
	                    processedTrips.add(tripText);

	                    try {
	                        tripElement.click();
	                    } catch (Exception e) {
	                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
	                    }

	                    objwait.waitForElementTobeVisible(driver, tripSequence, 90);
	                    String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");
	                    
	                    Matcher tripMatcher = tripCodePattern.matcher(tripSequenceText);
	                    if (!tripMatcher.find()) continue; // Skip if trip code not found
	                    
	                    String tripCode = tripMatcher.group(1).trim();

	                    for (WebElement tripEle : tripdata) {
	                        String tripDataText = objaction.gettext(tripEle).trim().replaceAll("\\s+", " ");
	                        WbidBasepage.logger.info("Line No: " + i + " Trip Data Text: " + tripDataText);

	                        // Handle special cases: Rpt (Total Credit) & TAFB
	                        if (tripDataText.startsWith("Rpt")) {
	                            Matcher rptMatcher = threeDigitPattern.matcher(tripDataText);
	                            String lastThreeDigit = null;
	                            while (rptMatcher.find()) lastThreeDigit = rptMatcher.group(1);

	                            if (lastThreeDigit != null) {
	                                totalCredMapUI.put(tripCode, Integer.parseInt(lastThreeDigit));
	                                WbidBasepage.logger.info("Line No: " + i + " Extracted Total Credit (Rpt) for " + tripCode + ": " + lastThreeDigit);
	                            }
	                            continue;
	                        }

	                        if (tripDataText.startsWith("TAFB")) {
	                            WbidBasepage.logger.info("Ignoring trip data: " + tripDataText);
	                            continue;
	                        }

	                        // Extract Date
	                        Matcher dateMatcher = datePattern.matcher(tripDataText);
	                        if (!dateMatcher.find()) continue; // Skip if no date found

	                        String tripDate = dateMatcher.group(1).trim();
	                        WbidBasepage.logger.info("Line No: " + i + " Extracted Trip Code: " + tripCode + " and Trip Date: " + tripDate);

	                        // Extract all credit values
	                        List<Integer> creditValues = new ArrayList<>();
	                        Matcher creditMatcher = threeDigitPattern.matcher(tripDataText);
	                        while (creditMatcher.find()) {
	                            creditValues.add(Integer.parseInt(creditMatcher.group(1)));
	                        }

	                        if (!creditValues.isEmpty()) {
	                        	credMapUI.putIfAbsent(tripDate, new ArrayList<>());
	                        	credMapUI.get(tripDate).addAll(creditValues);
	                            WbidBasepage.logger.info("Extracted Credits for " + tripCode + " on " + tripDate + ": " + creditValues);
	                        }
	                    }

	                    // Close modal properly
	                 // Close modal properly
	                    try {
	                        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
	                    } catch (Exception e) {
	                        WbidBasepage.logger.info("Modal close action failed, continuing.");
	                    }
	                } catch (Exception e) {
	                    WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
	                }
	            }

	            // Store trip data for this line
	            allTripsMap.put("Line " + i, credMapUI);
	            WbidBasepage.logger.pass("All trip data mapped to line numbers: " +allTripsMap);

	        } catch (Exception e) {
	            WbidBasepage.logger.fail("Error processing trip line: " + e.getMessage());
	        }
	    }

	    return allTripsMap; // Return all trip data mapped to line numbers
	}
	public Map<String, Map<String, Map<String, List<Integer>>>> getTripDataWithLine() { 
	    Map<String, Map<String, Map<String, List<Integer>>>> allTripsMap = new HashMap<>(); // Stores (Line -> (Trip Code -> [Date -> Credits]))
	    Map<String, Integer> totalCredMapUI = new HashMap<>();
	    
	    Pattern tripCodePattern = Pattern.compile("Trip\\s(\\w+)\\sDated");  // Extract Trip Code
	    Pattern datePattern = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");   // Extract Trip Date
	    Pattern threeDigitPattern = Pattern.compile("\\b(\\d{3})\\b");      // Extract Trip Credits
	    
	    Set<String> processedTrips = new HashSet<>(); // Avoid duplicate clicks
	    int lineCount = tripLines.size();
	   
	    for (int i = 1; i < lineCount; i++) {  
	        try {
	            String tripXPath = getTripXPath(i);
	            List<WebElement> tripList = driver.findElements(By.xpath(tripXPath)); 
	            Map<String, Map<String, List<Integer>>> tripDataPerLine = new HashMap<>(); // Stores (Trip Code -> [Date -> Credits])

	            if (tripList.isEmpty()) {
	                WbidBasepage.logger.info("No trips found in Line " + i);
	                continue;
	            } 

	            for (WebElement tripElement : tripList) {  
	                try {
	                    objwait.waitForElementTobeVisible(driver, tripElement, 90);
	                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
	                    objwait.waitForElemntTobeClickable(driver, tripElement, 30);

	                    String tripText = tripElement.getText().trim();
	                    if (processedTrips.contains(tripText)) continue; // Skip already processed trips
	                    processedTrips.add(tripText);

	                    try {
	                        tripElement.click();
	                    } catch (Exception e) {
	                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
	                    }

	                    objwait.waitForElementTobeVisible(driver, tripSequence, 90);
	                    String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");
	                    
	                    Matcher tripMatcher = tripCodePattern.matcher(tripSequenceText);
	                    if (!tripMatcher.find()) continue; // Skip if trip code not found
	                    
	                    String tripCode = tripMatcher.group(1).trim();
	                    Map<String, List<Integer>> credMapUI = tripDataPerLine.getOrDefault(tripCode, new HashMap<>());

	                    for (WebElement tripEle : tripdata) {
	                        String tripDataText = objaction.gettext(tripEle).trim().replaceAll("\\s+", " ");
	         //               WbidBasepage.logger.info("Line No: " + i + " Trip Data Text: " + tripDataText);

	                        // Handle special cases: Rpt (Total Credit) & TAFB
	                        if (tripDataText.startsWith("Rpt")) {
	                            Matcher rptMatcher = threeDigitPattern.matcher(tripDataText);
	                            String lastThreeDigit = null;
	                            while (rptMatcher.find()) lastThreeDigit = rptMatcher.group(1);

	                            if (lastThreeDigit != null) {
	                                totalCredMapUI.put(tripCode, Integer.parseInt(lastThreeDigit));
	                 //               WbidBasepage.logger.info("Line No: " + i + " Extracted Total Credit (Rpt) for " + tripCode + ": " + lastThreeDigit);
	                            }
	                            continue;
	                        }

	                        if (tripDataText.startsWith("TAFB")) {
	               //             WbidBasepage.logger.info("Ignoring trip data: " + tripDataText);
	                            continue;
	                        }

	                        // Extract Date
	                        Matcher dateMatcher = datePattern.matcher(tripDataText);
	                        if (!dateMatcher.find()) continue; // Skip if no date found

	                        String tripDate = dateMatcher.group(1).trim();
	           //             WbidBasepage.logger.info("Line No: " + i + " Extracted Trip Code: " + tripCode + " and Trip Date: " + tripDate);

	                        // Extract all credit values
	                        List<Integer> creditValues = new ArrayList<>();
	                        Matcher creditMatcher = threeDigitPattern.matcher(tripDataText);
	                        while (creditMatcher.find()) {
	                            creditValues.add(Integer.parseInt(creditMatcher.group(1)));
	                        }

	                        if (!creditValues.isEmpty()) {
	                            credMapUI.putIfAbsent(tripDate, new ArrayList<>());
	                            credMapUI.get(tripDate).addAll(creditValues);
	          //                  WbidBasepage.logger.info("Extracted Credits for " + tripCode + " on " + tripDate + ": " + creditValues);
	                        }
	                    }

	                    tripDataPerLine.put(tripCode, credMapUI);
	                    WbidBasepage.logger.pass("All trip data mapped to line number: "+i +":" +tripDataPerLine);

	                    // Close modal properly
	                    try {
	                        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
	                    } catch (Exception e) {
	                        WbidBasepage.logger.info("Modal close action failed, continuing.");
	                    }
	                } catch (Exception e) {
	                    WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
	                }
	            }

	            // Store trip data for this line
	            allTripsMap.put("Line " + i, tripDataPerLine);
	            WbidBasepage.logger.pass("All trip data mapped to line numbers: " + allTripsMap);

	        } catch (Exception e) {
	            WbidBasepage.logger.fail("Error processing trip line: " + e.getMessage());
	        }
	    }

	    return allTripsMap; // Return all trip data mapped to line numbers
	}

			        }
