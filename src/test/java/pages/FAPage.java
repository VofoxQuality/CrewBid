package pages;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import API.TrialBidAPI;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class FAPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public FAPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
		
		@FindBy(xpath = "//td[contains(@class,'left-side-radius')]")
		public List<WebElement> tripList;// Trip detail visible when we click tripList

		@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[1]/div/pre")
		public WebElement tripSequence;// Trip code Extracted from Trip Sequence

		@FindBy(xpath = "(//pre[@style='text-decoration: inherit;' and normalize-space(.)!=''])[position() > 2]")
		public List<WebElement> tripdata;// gets data starting from trip details skip 1st 2 lines which show trip code
											// and table header
		
		public boolean IndividualCredCompareAPI() {
			boolean isComparisonSuccessful = true; // Assume success unless proven otherwise
			// int i = 0;
			for (WebElement tripElement : tripList) {
				Map<String, Map<String, List<Integer>>> tripDataMap = new LinkedHashMap<>();

				try {
					// if (i >= 10) break; // Limit to 2 iterations if required
					// i++;
					objwait.waitForElementTobeVisible(driver, tripElement, 90);
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
							tripElement);
					objwait.waitForElemntTobeClickable(driver, tripElement, 30);

					try {
						tripElement.click();
					} catch (Exception e) {
						((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
					}

					objwait.waitForElementTobeVisible(driver, tripSequence, 90);
					String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");

					Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
					Matcher matcher = pattern.matcher(tripSequenceText);

					if (matcher.find()) {
						String tripCode = matcher.group(1).trim();

						for (WebElement tripEle : tripdata) {
							String tripDataText = objaction.gettext(tripEle).trim().replaceAll("\\s+", " ");

							if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
								continue;
							}

							// Extract date (e.g., "01Apr", "15May")
							Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
							Matcher matcherD = patternD.matcher(tripDataText);

							if (matcherD.find()) {
								String tripDate = matcherD.group(1).trim();
								 tripDate = tripDate.substring(0, 2) + tripDate.substring(2, 3).toUpperCase() + tripDate.substring(3).toLowerCase();
								    //  Use dynamic year
								    int year = LocalDate.now().getYear(); 

								    String fullTripDate = tripDate + year;
								    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy", Locale.ENGLISH);
								    LocalDate parsedDate = LocalDate.parse(fullTripDate, formatter);

								    String normalizedDate = parsedDate.format(DateTimeFormatter.ofPattern("ddMMM", Locale.ENGLISH));

						        // Now use normalizedDate for further processing

								// Extract only the last three-digit number for credit values
								Pattern threeDigitPattern = Pattern.compile("\\b(\\d{3})\\b");
								Matcher numberMatcher = threeDigitPattern.matcher(tripDataText);

								String lastThreeDigit = null;
								while (numberMatcher.find()) {
									lastThreeDigit = numberMatcher.group(1); // Keep last occurrence
								}

								if (lastThreeDigit != null) {
									try {
										int extractedValue = Integer.parseInt(lastThreeDigit);

										// Store only the last credit value
										tripDataMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
												.computeIfAbsent(normalizedDate, k -> new ArrayList<>()).add(extractedValue);

									} catch (NumberFormatException e) {
										WbidBasepage.logger.fail("Failed to parse credit: " + lastThreeDigit);
									}
								}
							} else {
								WbidBasepage.logger.fail("Trip date not found in trip sequence text.");
							}
						}

						// Compare UI data with API data
						// WbidBasepage.logger.info("Individual Cred Data for Comparison: " +
						// tripDataMap);

						boolean isCurrentComparisonSuccessful = compareCredData(tripDataMap, TrialBidAPI.apiCredData);
						if (!isCurrentComparisonSuccessful) {
							isComparisonSuccessful = false; // If any comparison fails, mark as unsuccessful
						}
					}

					// Close modal popup if present
					try {
						new Actions(driver).sendKeys(Keys.ESCAPE).perform();
					} catch (Exception e) {
						WbidBasepage.logger.info("Modal close action failed, continuing.");
					}

				} catch (Exception e) {
					WbidBasepage.logger.info("Error processing trip element: " + e.getMessage());
					//isComparisonSuccessful = false; // Mark as unsuccessful if an error occurs
				}
			}

			return isComparisonSuccessful;
		}
	
		public boolean compareCredData(Map<String, Map<String, List<Integer>>> uiCredData,
				Map<String, Map<String, List<Integer>>> apiCredData) {
			boolean isMatch = true;

			for (String tripCode : uiCredData.keySet()) {
				if (!apiCredData.containsKey(tripCode)) {
					WbidBasepage.logger.fail("Mismatch! UI has TripCode: " + tripCode + ", but API does not.");
					isMatch = false;
					continue;
				}

				Map<String, List<Integer>> uiDatesMap = uiCredData.get(tripCode);
				Map<String, List<Integer>> apiDutSeqMap = apiCredData.get(tripCode);

// Sort UI Dates with dynamic year handling
				List<String> sortedUiDates = new ArrayList<>(uiDatesMap.keySet());

				int currentYear = LocalDate.now().getYear(); // or set to 2025 manually if needed
				int[] yearHolder = { currentYear };
				int[] lastMonthHolder = { -1 };

				sortedUiDates
						.sort(Comparator.comparing(date -> convertDateToComparable(date, yearHolder, lastMonthHolder)));

// Sort API DutySeqNum as Integers
				List<String> sortedDutSeq = new ArrayList<>(apiDutSeqMap.keySet());
				sortedDutSeq.sort(Comparator.comparingInt(Integer::parseInt));

// Compare credit values for each mapped Date - DutSeqNum pair
				for (int i = 0; i < Math.min(sortedUiDates.size(), sortedDutSeq.size()); i++) {
					String uiDate = sortedUiDates.get(i);
					String dutySeq = sortedDutSeq.get(i);

					List<Integer> uiCredits = uiDatesMap.get(uiDate);
					List<Integer> apiCredits = apiDutSeqMap.get(dutySeq);

					WbidBasepage.logger.info("|Trip Code: " + tripCode + " | UI Date: " + uiDate + " | UI Credits: "
							+ uiCredits + " | API Credits: " + apiCredits);

					if (!uiCredits.equals(apiCredits)) {
						WbidBasepage.logger.fail("Credit mismatch for TripCode: " + tripCode + ", Date: " + uiDate
								+ " (DutySeqNum: " + dutySeq + ")");
						WbidBasepage.logger.info("UI Credits: " + uiCredits + " | API Credits: " + apiCredits);
						isMatch = false;
					}
				}
			}

			if (isMatch) {
				WbidBasepage.logger.info("✅ UI and API credits match perfectly!");
			} else {
				WbidBasepage.logger.fail("❌ Differences found in UI and API credit data!");
			}

			return isMatch;
		}
		private LocalDate convertDateToComparable(String date, int[] yearHolder, int[] lastMonthHolder) {
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMMyyyy", Locale.ENGLISH);

		    // Extract month part (e.g., "May" from "30May")
		    String monthStr = date.substring(2);
		    Month currentMonth = Month.valueOf(monthStr.toUpperCase(Locale.ENGLISH));

		    int currentMonthValue = currentMonth.getValue(); // 1=Jan, 12=Dec

		    // If month rolls backward (December to January), increment year
		    if (lastMonthHolder[0] != -1 && currentMonthValue < lastMonthHolder[0]) {
		        yearHolder[0]++;
		    }

		    // Update last processed month
		    lastMonthHolder[0] = currentMonthValue;

		    String fullDate = date + yearHolder[0]; // e.g., "30May2025"
		    return LocalDate.parse(fullDate, formatter);
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////
		// ✅ Trip list excluding Reserve Line Trips (greenish-cyan background)
		@FindBy(xpath = "//td[contains(@class,'left-side-radius') and not(contains(@style,'background-color: rgb(17, 166, 124)')) and not(contains(@style,'background-color: rgb(185, 54, 16)'))]")
		public List<WebElement> tripL;

	
		
		public Map<String, List<Integer>> tripindiCredUI = new LinkedHashMap<>();
		
		public Map<String, List<Integer>> getFAindiCredHour() {
		    Set<String> processedTripCodes = new HashSet<>();
		    Map<String, List<Integer>> tripindiCredUI = new LinkedHashMap<>();
		   // int i = 0;

		    for (WebElement tripElement : tripL) {
		        try {
		        //    if (i >= 5) break;
		         //   i++;

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

		            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
		            Matcher matcher = pattern.matcher(tripSequenceText);

		            if (matcher.find()) {
		                String tripCode = matcher.group(1).trim();

		                if (processedTripCodes.contains(tripCode)) {
		                    WbidBasepage.logger.info("Trip Code: " + tripCode + " already processed, skipping.");
		                    continue;
		                }

		                processedTripCodes.add(tripCode);
		                List<Integer> credValues = new ArrayList<>();

		                for (WebElement tripEle : tripdata) {
		                    String tripDataText = objaction.gettext(tripEle).trim();

		                    if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
		                        continue;
		                    }

		                    Pattern numberPattern = Pattern.compile("\\b(\\d{1,4})\\b");
		                    Matcher numberMatcher = numberPattern.matcher(tripDataText);

		                    List<String> numbers = new ArrayList<>();
		                    while (numberMatcher.find()) {
		                        numbers.add(numberMatcher.group(1));
		                    }

		                    String credValue = getLastNumber(numbers);
		                    if (credValue != null) {
		                        try {
		                            int extractedValue = Integer.parseInt(credValue);
		                            credValues.add(extractedValue);
		                            WbidBasepage.logger.info("Trip Code: " + tripCode + " | indiCred: " + extractedValue);
		                        } catch (NumberFormatException e) {
		                            WbidBasepage.logger.info("Invalid number found: " + credValue);
		                        }
		                    }
		                }

		                if (!credValues.isEmpty()) {
		                    tripindiCredUI.put(tripCode, credValues);
		                }
		            }

		        } catch (Exception e) {
		            WbidBasepage.logger.fail("Error extracting indiCred time: " + e.getMessage());
		        }
		    }

		    WbidBasepage.logger.info("✅ UI indiCred Hours (Formatted): " + tripindiCredUI);
		    return tripindiCredUI;
		}

		private String getLastNumber(List<String> numbers) {
		    if (numbers == null || numbers.isEmpty()) return null; 
		    return numbers.get(numbers.size() - 1); 
		}



		public boolean compareindiCredFA(Map<String, List<Integer>> tripCredUI, Map<String, List<Integer>> apiCredHr) {
		    boolean isMatch = true;

		    for (Map.Entry<String, List<Integer>> uiEntry : tripCredUI.entrySet()) {
		        String tripCode = uiEntry.getKey();
		        List<Integer> uiCredList = uiEntry.getValue();

		        if (!apiCredHr.containsKey(tripCode)) {
		            WbidBasepage.logger.fail("❌ TripCode " + tripCode + " found in UI but missing in API.");
		            isMatch = false;
		            continue;
		        }

		        List<Integer> apiCredList = apiCredHr.get(tripCode);

		        if (!uiCredList.equals(apiCredList)) {
		            WbidBasepage.logger.fail("❌ Mismatch for TripCode " + tripCode +
		                "UI cred Hour Hours: " + uiCredList +
		                "API cred Hour Hours: " + apiCredList);
		            isMatch = false;
		        } else {
		            WbidBasepage.logger.info("✅ Match for TripCode " + tripCode + "UI cred Hour Hours: " + uiCredList +
			                "API cred Hour Hours: " + apiCredList);
		        }
		    }

		    for (String apiTripCode : apiCredHr.keySet()) {
		        if (!tripCredUI.containsKey(apiTripCode)) {
		            WbidBasepage.logger.info("TripCode " + apiTripCode + " found in API but missing in UI.");
		            
		        }
		    }

		    if (isMatch) {
		        WbidBasepage.logger.info("✅ All cred Hour hours match between UI and API!");
		    } else {
		        WbidBasepage.logger.fail("❌ Some cred Hour hour mismatches found between UI and API.");
		    }

		    return isMatch;
		}
		
}
