package pages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class TAFBPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public TAFBPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
	
	//Get trip details-Trip Code
		@FindBy(xpath = "//td[contains(@class,'left-side-radius')]")
		public List<WebElement> tripList;// Trip detail visible when we click tripList

		@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[1]/div/pre")
		public WebElement tripSequence;// Trip code Extracted from Trip Sequence
	//Get TAFB from UI for each Trip(corresponding TipCode)
		@FindBy(xpath = "(//pre[@style='text-decoration: inherit;' and normalize-space(.)!=''])[last()]")
		public WebElement tripTAFB;// get last data starting containing TAFB

		public Map<String, Double> getAllTAFB() {
			Map<String, String> tripTAFBMap = new HashMap<>();
			Map<String, Double> tafbMapUI = new HashMap<>();
			// int i=0;
			for (WebElement tripElement : tripList) {
				try {
					// i++;
					// if(i<=5) {
					objwait.waitForElementTobeVisible(driver, tripElement, 90);
					// Scroll into view
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
							tripElement);
					objwait.waitForElemntTobeClickable(driver, tripElement, 30);
					// Try normal click first, fallback to JavaScript click
					try {
						tripElement.click();
					} catch (Exception e) {
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
					}

					objwait.waitForElementTobeVisible(driver, tripSequence, 90);
					String tripSequenceText = objaction.gettext(tripSequence).trim();
					// WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);

					tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
					Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
					Matcher matcher = pattern.matcher(tripSequenceText);

					if (matcher.find()) {
						String tripCode = matcher.group(1).trim();
						// WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);

						String tripDataText = objaction.gettext(tripTAFB).trim();
						// WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

						// Look for TAFB data
						if (tripDataText.startsWith("TAFB")) {
							// WbidBasepage.logger.info("trip data: " + tripDataText);
							tripDataText = tripDataText.replaceAll("\\s+", " ");

							// Extract TAFB value (e.g., "TAFB 5250")
							Pattern patternD = Pattern.compile("TAFB\\s+(\\d+)");
							Matcher matcherD = patternD.matcher(tripDataText);

							if (matcherD.find()) {
								String tripTAFB = matcherD.group(1).trim();
								WbidBasepage.logger
										.info("UI Extracted Trip Code: " + tripCode + " UI Trip TAFB: " + tripTAFB);

								// Store the trip code and TAFB in the map
								if (tripTAFBMap.containsKey(tripCode)) {
									tafbMapUI.put(tripCode, Double.parseDouble(tripTAFBMap.get(tripTAFB)));
								}
							} else {
								WbidBasepage.logger.fail("TAFB value not found in trip data.");
							}
						}
					}
					// }

					// Close modal properly after processing the trip
					try {
						new Actions(driver).sendKeys(Keys.ESCAPE).perform();
					} catch (Exception e) {
						WbidBasepage.logger.info("Modal close action failed, continuing.");
					}

				} catch (Exception e) {
					WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
				}
			}

			return tafbMapUI; // Return trip code and corresponding TAFB value
		}

	//Compare TAFB with API data
		public boolean getAllTAFBAndCompare(Map<String, Double> apiTAFBMap) {
			Map<String, Double> tafbMapUI = new HashMap<>();
			boolean isDataMatching = true; // Assume data matches initially
			// int i = 0;

			for (WebElement tripElement : tripList) {
				try {
					// i++;
					// if (i <= 5) { // Process only the first 5 trips
					objwait.waitForElementTobeVisible(driver, tripElement, 90);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
							tripElement);
					objwait.waitForElemntTobeClickable(driver, tripElement, 30);

					// Try normal click, fallback to JavaScript click
					try {
						tripElement.click();
					} catch (Exception e) {
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
					}

					objwait.waitForElementTobeVisible(driver, tripSequence, 90);
					String tripSequenceText = objaction.gettext(tripSequence).trim();
					// WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);

					tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
					Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
					Matcher matcher = pattern.matcher(tripSequenceText);

					if (matcher.find()) {
						String tripCode = matcher.group(1).trim();
						// WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);

						// Extract TAFB from UI
						String tripDataText = objaction.gettext(tripTAFB).trim();
						// WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

						if (tripDataText.startsWith("TAFB")) {
							tripDataText = tripDataText.replaceAll("\\s+", " ");

							// Extract TAFB value (supporting decimals)
							Pattern patternD = Pattern.compile("TAFB\\s+(\\d+\\.?\\d*)");
							Matcher matcherD = patternD.matcher(tripDataText);

							if (matcherD.find()) {
								String tripTAFB = matcherD.group(1).trim();
								Double tafbValue = Double.parseDouble(tripTAFB);
								WbidBasepage.logger
										.info("UI Extracted Trip Code: " + tripCode + " | UI Trip TAFB: " + tafbValue);

								// Store in UI Map
								tafbMapUI.put(tripCode, tafbValue);
							} else {
								WbidBasepage.logger.fail("TAFB value not found for Trip Code: " + tripCode);
							}
						}
					}

					// Close modal properly after processing the trip
					try {
						new Actions(driver).sendKeys(Keys.ESCAPE).perform();
					} catch (Exception e) {
						WbidBasepage.logger.info("Modal close action failed, continuing.");
					}
					// }
				} catch (Exception e) {
					WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
				}
			}
			// ✅ **Compare API vs UI TAFB Values**
			for (String tripCode : apiTAFBMap.keySet()) {
				Double apiTAFB = apiTAFBMap.get(tripCode);
				Double uiTAFB = tafbMapUI.get(tripCode);

				if (uiTAFB == null) {
					WbidBasepage.logger.fail("Trip Code " + tripCode + " found in API but missing in UI!");
					isDataMatching = false;
					continue;
				}

				// ✅ Compare with tolerance
				if (Math.abs(uiTAFB - apiTAFB) > 0.0001) { // EPSILON = 0.0001
					WbidBasepage.logger.fail(
							"TAFB Mismatch for Trip " + tripCode + " | UI TAFB: " + uiTAFB + " | API TAFB: " + apiTAFB);
					isDataMatching = false;
				} else {
					WbidBasepage.logger
							.info("TAFB Match for Trip " + tripCode + " | UI TAFB: " + uiTAFB + " | API TAFB: " + apiTAFB);
				}
			}

			// ✅ Check for Trips in UI but Missing in API
			for (String uiTripCode : tafbMapUI.keySet()) {
				if (!apiTAFBMap.containsKey(uiTripCode)) {
					WbidBasepage.logger.fail("Trip Code " + uiTripCode + " found in UI but missing in API!");
					isDataMatching = false;
				}
			}

			return isDataMatching; // Returns false if any mismatch is found
		}
//Reserve Lines
			
		@FindBy(xpath = "//td[contains(@class,'left-side-radius') and (contains(@style,'background-color: rgb(185, 54, 16)') or contains(@style,'background-color: rgb(17, 166, 124)'))]")
		public List<WebElement> ReserveTrips; // Trip detail visible for Reserve line (Orange or Green)
	
		public boolean reserveTripTAFB() {
		    boolean allZero = true;

		    for (WebElement tripElement : ReserveTrips) {
		        try {
		            objwait.waitForElementTobeVisible(driver, tripElement, 90);
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		            objwait.waitForElemntTobeClickable(driver, tripElement, 30);

		            try {
		                tripElement.click();
		            } catch (Exception e) {
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
		            }

		            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		            String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");
		            Matcher matcher = Pattern.compile("Trip\\s(\\w+)\\sDated").matcher(tripSequenceText);

		            if (matcher.find()) {
		                String tripCode = matcher.group(1).trim();
		                String tripDataText = objaction.gettext(tripTAFB).trim().replaceAll("\\s+", " ");
		                Matcher matcherD = Pattern.compile("TAFB\\s+(\\d+)").matcher(tripDataText);

		                if (matcherD.find()) {
		                    String tripTAFBValue = matcherD.group(1).trim();
		                    double tafbValue = Double.parseDouble(tripTAFBValue);

		                    if (tafbValue != 0.0) {
		                        WbidBasepage.logger.fail("TAFB is not zero for trip: " + tripCode + ", found: " + tafbValue);
		                        allZero = false;
		                    } else {
		                        WbidBasepage.logger.pass("TAFB is zero for trip: " + tripCode+ ",  TAFB: " + tafbValue);
		                    }
		                } else {
		                    WbidBasepage.logger.fail("TAFB value not found in trip data for trip: " + tripCode);
		                    allZero = false;
		                }
		            }

		            // Close modal
		            try {
		                new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		            } catch (Exception e) {
		                WbidBasepage.logger.info("Modal close action failed, continuing.");
		            }

		        } catch (Exception e) {
		            WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
		            allZero = false;
		        }
		    }

		    return allZero;
		}
////TAFB-line parameter ////
		
		@FindBy(xpath = "//*[@class='date-com']")
		public WebElement linevalueBtn;

		@FindBy(xpath = "//button[text()=' Reset ']")
		public WebElement resetBtn;

		public void clickLineValue() {
			objwait.waitForElementTobeVisible(driver, linevalueBtn, 90);
			objaction.click(linevalueBtn);
		}

		public void clickResetLinevalues() {
			objwait.waitForElementTobeVisible(driver, resetBtn, 90);
			objaction.click(resetBtn);
		}

		@FindBy(xpath = "//*[@class='checkbox-animated']")
		public WebElement airCrftLineBtn;

		public void clickAirCrftLineBtn() {
			objwait.waitForElementTobeVisible(driver, airCrftLineBtn, 90);
			objaction.click(airCrftLineBtn);
		}

		@FindBy(xpath = "(//*[@class='checkbox-animated'])[48]")
		public WebElement TAFBLineBtn;

		public void clickTAFBLineBtn() {
			objaction.scrollToElement(TAFBLineBtn);
			objwait.waitForElementTobeVisible(driver, TAFBLineBtn, 90);
			objaction.click(TAFBLineBtn);
		}

		public void selectTAFBLine() {
			clickLineValue();// open lines modal
			clickResetLinevalues();// Reset line values so by default 5 default lines selected
			clickAirCrftLineBtn();// uncheck Air Craft changes line
			clickTAFBLineBtn();// check TAFB
			// Close modal properly
			try {
				Actions actions = new Actions(driver);
				actions.sendKeys(Keys.ESCAPE).perform();
			} catch (Exception e) {
				WbidBasepage.logger.info("Modal close action failed, continuing.");
			}
		}

		@FindBy(xpath = "//*[text()='TAFB']")
		public WebElement TAFBTxt;

		@FindBy(xpath = "//*[text()='TAFB']/following-sibling::span")
		public List<WebElement> TAFBLineVal;
		
		public boolean fordisplayTAFB() {
			objwait.waitForElementTobeVisible(driver, TAFBTxt, 60);
			return objaction.fordisplay(TAFBTxt);
		}
		
		//Get holirig data  from UI
		public List<Map<String, Object>> TAFBLineValueUI = new ArrayList<>();

		public boolean getTAFBLineVal() {
		    try {
		        objwait.waitForElementTobeVisible(driver, TAFBTxt, 90);

		        TAFBLineValueUI.clear(); // Clear previous data before populating new

		        if (!TAFBLineVal.isEmpty()) {
		            for (int i = 0; i < TAFBLineVal.size(); i++) {
		                String TAFBValue = TAFBLineVal.get(i).getText().trim();
		                WbidBasepage.logger.info("Lines: " + (i + 1) + " TAFBLine Value: " + TAFBValue);

		                Map<String, Object> map = new LinkedHashMap<>();
		                map.put("Lines", i + 1);
		                map.put("TAFB", TAFBValue);
		                TAFBLineValueUI.add(map);
		            }

		            WbidBasepage.logger.info("TAFB Line Parameter UI with Lines: " + TAFBLineValueUI);
		            return true;
		        } else {
		            WbidBasepage.logger.fail("No TAFB Line Parameter values found!");
		            return false;
		        }
		    } catch (Exception e) {
		        WbidBasepage.logger.fail("Exception while retrieving TAFB Line Parameter values: " + e.getMessage());
		        return false;
		    }
		}

		//Compare TAFB data  from UI and API
		public boolean isTAFBLineValCompare(List<Map<String, Object>> result) {
		    boolean allMatch = true; // Track overall result

		    if (result.size() != TAFBLineValueUI.size()) {
		        WbidBasepage.logger.fail("Mismatch in data size! API size: " + result.size() + ", UI size: " + TAFBLineValueUI.size());
		        allMatch = false;
		    } else {
		        WbidBasepage.logger.info("API and UI data sizes match. API size: " + result.size() + " UI size: " + TAFBLineValueUI.size());
		    }

		    int loopSize = Math.min(result.size(), TAFBLineValueUI.size());

		    for (int i = 0; i < loopSize; i++) {
		        Map<String, Object> apiData = result.get(i);
		        Map<String, Object> uiData = TAFBLineValueUI.get(i);

		        int apiLine = convertToInteger(apiData.get("Lines"));
		        int uiLine = convertToInteger(uiData.get("Lines"));

		        String apiValue = String.valueOf(apiData.get("TafbInLine")).trim();
		        String uiValue = String.valueOf(uiData.get("TAFB")).trim();

		        apiValue = normalizeTime(apiValue);
		        uiValue = normalizeTime(uiValue);

		        if (apiLine != uiLine || !apiValue.equals(uiValue)) {
		            WbidBasepage.logger.fail("Mismatch at Line " + (i + 1) +
		                " | API -> Line: " + apiLine + ", Value: " + apiValue +
		                " | UI -> Line: " + uiLine + ", Value: " + uiValue);
		            allMatch = false;
		        } else {
		            WbidBasepage.logger.pass("Match at Line " + (i + 1) +
		                " | Line: " + apiLine + ", Value: " + apiValue);
		        }
		    }

		    if (allMatch) {
		        WbidBasepage.logger.info("API and UI TAFB InLine data match perfectly!");
		    } else {
		        WbidBasepage.logger.fail("One or more mismatches found in TAFB InLine data.");
		    }

		    return allMatch;
		}

		private int convertToInteger(Object obj) {
		    try {
		        return Integer.parseInt(String.valueOf(obj).trim());
		    } catch (Exception e) {
		        return -1;
		    }
		}

		private String normalizeTime(String time) {
		    if (time == null || time.isEmpty() || time.equalsIgnoreCase("null")) {
		        return "00:00";
		    }

		    String[] parts = time.split(":");
		    if (parts.length == 2) {
		        try {
		            return String.format("%02d:%02d", Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		        } catch (NumberFormatException e) {
		            return time;
		        }
		    }
		    return time;
		}

}
