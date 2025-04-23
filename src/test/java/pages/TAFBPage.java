package pages;

import java.util.HashMap;
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
	
/*		public Map<String, Double> reserveTripTAFB() {
		    Map<String, Double> tafbMapUI = new HashMap<>();
		    
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
		                    tafbMapUI.put(tripCode, tafbValue);

		                    if (tafbValue != 0.0) {
		                        WbidBasepage.logger.fail("TAFB is not zero for trip: " + tripCode + ", found: " + tafbValue);
		                    } else {
		                        WbidBasepage.logger.pass("TAFB is zero for trip: " + tripCode + ", found: " + tafbValue);
		                    }
		                } else {
		                    WbidBasepage.logger.fail("TAFB value not found in trip data for trip: " + tripCode);
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
		        }
		    }

		    return tafbMapUI; // Map of tripCode -> TAFB value
		}
		*/
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


}
