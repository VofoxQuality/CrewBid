package pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class DutyPeriodPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();

	public DutyPeriodPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);/// initial page factory
		objaction = new ActionUtilities(driver);
	}
	@FindBy(xpath = "(//td[contains(@class,'left-side-radius')])[1]")
	public WebElement firstTrip;

	@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[4]/div/pre")
	public WebElement CredHead;

	public boolean CredHeadVisible() {
		objwait.waitForElementTobeVisible(driver, firstTrip, 10);
		objaction.click(firstTrip);
		objwait.waitForElementTobeVisible(driver, CredHead, 10);
		String txt = objaction.gettext(CredHead);
		WbidBasepage.logger.pass("Head  :" + txt);
		boolean isVisible = txt.contains("Cred");
		WbidBasepage.logger.pass("Cred Head Visible :" + isVisible);
		return isVisible;
	}
	// Get individual Duty hour in each trip

	@FindBy(xpath = "//div[contains(@class,'cala-view')]")
	public List<WebElement> tripLines; // Each 'div' contains trip details

	@FindBy(xpath = "//td[contains(@class,'left-side-radius')]")
	public List<WebElement> tripList;// Trip detail visible when we click tripList

	@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[1]/div/pre")
	public WebElement tripSequence;// Trip code Extracted from Trip Sequence

	@FindBy(xpath = "(//pre[@style='text-decoration: inherit;' and normalize-space(.)!=''])[position() > 2]")
	public List<WebElement> tripdata;// gets data starting from trip details skip 1st 2 lines which show trip code
										// and table header

	public String getTripXPath(int i) {
		return String.format(
				"(//div[contains(@class,'cala-view')])[%d]/div/div[2]/table/tr/td[contains(@class,'left-side-radius')]",
				i);
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////
	public Map<String, List<String>> dutyhourUI = new LinkedHashMap<>();

//	public Map<String, List<String>> getCPDutyHour() {
//		Set<String> processedTripCodes = new HashSet<>();
////int i = 0;
//		for (WebElement tripElement : tripList) {
//			try {
////   if (i >= 5)
////      break; // Limit to 5 iterations
////   i++;
//				objwait.waitForElementTobeVisible(driver, tripElement, 90);
//				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
//						tripElement);
//				objwait.waitForElemntTobeClickable(driver, tripElement, 30);
//
//				try {
//					tripElement.click();
//				} catch (Exception e) {
//					((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
//				}
//
//				objwait.waitForElementTobeVisible(driver, tripSequence, 90);
//				String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");
//
//// Extract trip code
//				Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
//				Matcher matcher = pattern.matcher(tripSequenceText);
//
//				if (matcher.find()) {
//					String tripCode = matcher.group(1).trim();
//
//// Skip if already processed
//					if (processedTripCodes.contains(tripCode)) {
//						WbidBasepage.logger.info("Trip Code: " + tripCode + " already processed, skipping.");
//						continue;
//					}
//
//					processedTripCodes.add(tripCode);
//					List<String> grndValues = new ArrayList<>();
//
//					for (WebElement tripEle : tripdata) {
//						String tripDataText = objaction.gettext(tripEle).trim();
//
//						if (tripDataText.startsWith("Rpt") ) {
//							// Extract only numeric tokens
//				            List<String> numbers = new ArrayList<>();
//				            for (String token : tokens) {
//				                if (token.matches("\\d+")) {
//				                    numbers.add(token);
//				                }
//				            }
//
//				            // Get second last number
//				            String secondLast = getSecondLastNumber(numbers);
//				            System.out.println("Second last number: " + secondLast);
//				        }
//				    }
//
////// Extract all numbers
////						Pattern numberPattern = Pattern.compile("\\b(\\d{1,4})\\b");
////						Matcher numberMatcher = numberPattern.matcher(tripDataText);
////
////						List<String> numbers = new ArrayList<>();
////						while (numberMatcher.find()) {
////							numbers.add(numberMatcher.group(1));
////						}
////
////// ✅ Use reliable method to get second-last number
////						String grndValue = getReliableSecondLastNumber(numbers);
////
////						if (grndValue != null) {
////							String formattedGrndValue = convertToApiFormat(grndValue);
////							grndValues.add(formattedGrndValue);
////							WbidBasepage.logger
////									.info("Trip Code: " + tripCode + " | Grnd (formatted): " + formattedGrndValue);
////						}
////					}
////
////					if (!grndValues.isEmpty()) {
////						dutyhourUI.put(tripCode, grndValues);
////					}
////				}
////
////			} catch (Exception e) {
////				System.out.println("Error extracting Grnd time: " + e.getMessage());
////			}
////		}
//
//		WbidBasepage.logger.info("✅ UI Duty Hours (Formatted): " + dutyhourUI);
//		return dutyhourUI ;}}		  private static String getSecondLastNumber(List<String> numbers) {
//				  			        if (numbers == null || numbers.size() < 2) {
//				            return null;
//				        }
//				        return numbers.get(numbers.size() - 2);
//				    }
//}

	public Map<String, List<String>> getCPDutyHour() {
	    Map<String, List<String>> dutyHourMap = new LinkedHashMap<>();
	    Set<String> processedTripCodes = new HashSet<>();

	    int i = 0;
	    for (WebElement tripElement : tripList) {
	        try {
//	            if (i >= 5) break; // Limit to 5 trips
//	            i++;

	            // Wait and click trip
	            objwait.waitForElementTobeVisible(driver, tripElement, 90);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
	            objwait.waitForElemntTobeClickable(driver, tripElement, 30);

	            try {
	                tripElement.click();
	            } catch (Exception e) {
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
	            }

	            // Extract trip code
	            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
	            String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");
	            Matcher matcher = Pattern.compile("Trip\\s(\\w+)\\sDated").matcher(tripSequenceText);
	            if (!matcher.find()) continue;

	            String tripCode = matcher.group(1).trim();
	            if (!processedTripCodes.add(tripCode)) {
	                WbidBasepage.logger.info("Trip Code: " + tripCode + " already processed, skipping.");
	                continue;
	            }

	            // Collect all duty hours from all Rpt lines
	            List<String> dutyHours = new ArrayList<>();
	            for (WebElement tripLine : tripdata) {
	                String line = objaction.gettext(tripLine).trim().replaceAll("\\s+", " ");
	                if (line.startsWith("Rpt")) {
	                    List<String> numbers = new ArrayList<>();
	                    for (String token : line.split(" ")) {
	                        if (token.matches("\\d+")) {
	                            numbers.add(token);
	                        }
	                    }
	                    String dutyHour = getSecondLastNumber(numbers);
	                    if (dutyHour != null) {
	                    	String dutyhourformattedvalue=convertToTimeFormat(dutyHour);
	                    	dutyHours.add(dutyhourformattedvalue); // ✅ fix: add to local list
	                    }
	                }
	            }

	            if (!dutyHours.isEmpty()) {
	                dutyHourMap.put(tripCode, dutyHours);
	                WbidBasepage.logger.info("Trip: " + tripCode + " | Duty Hours: " + dutyHours);
	            } else {
	                WbidBasepage.logger.warning("No duty hours found for trip: " + tripCode);
	            }

	        } catch (Exception e) {
	            WbidBasepage.logger.fail("Error processing trip element: " + e.getMessage());
	        }
	    }

	    WbidBasepage.logger.info("✅ UI Duty Hours Extracted: " + dutyHourMap);
	    return dutyHourMap;
	}

	// ✅ Helper method
	private String getSecondLastNumber(List<String> numbers) {
	    if (numbers == null || numbers.size() < 2) return null;
	    return numbers.get(numbers.size() - 2);
	}
	private String convertToTimeFormat(String value) {
	    if (value == null || value.length() < 3) return "00:00";
	    int time = Integer.parseInt(value);
	    int hours = time / 100;
	    int minutes = time % 100;
	  String dutyhrformatted= String.format("%02d:%02d", hours, minutes);
	  return dutyhrformatted;
	}

}
