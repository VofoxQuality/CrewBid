package pages;

import java.util.ArrayList;
import java.util.Comparator;
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

import API.TrialBidAPI;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class DutyPeriodPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait=new WaitCondition();
	
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
	//Get individual Duty hour in each trip

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
		public Map<String, Map<String, List<Integer>>> getDutyperiod() {
			// Stores (Trip Code -> {Date -> List of Credits})

			Map<String, Map<String, List<Integer>>> alltripDataMap = new LinkedHashMap<>();
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
							// WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

							if (tripDataText.startsWith("Rpt")){
								continue;
							}

							// Extract date (e.g., "01Apr", "15May")
							Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
							Matcher matcherD = patternD.matcher(tripDataText);

							if (matcherD.find()) {
								String tripDate = matcherD.group(1).trim();

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
										// WbidBasepage.logger.info("Extracted Credit for " + tripCode + ": " +
										// extractedValue);

										// Store only the last credit value
										tripDataMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
												.computeIfAbsent(tripDate, k -> new ArrayList<>()).add(extractedValue);

										// WbidBasepage.logger.info("Updated Trip Data Map: " + tripDataMap);
									} catch (NumberFormatException e) {
										// WbidBasepage.logger.fail("Failed to parse credit: " + lastThreeDigit);
									}
								}
							} else {
								WbidBasepage.logger.fail("Trip date not found in trip sequence text.");
							}
						}
						//// Comparison
						WbidBasepage.logger.info("Individual Cred Data for Comparison: " + tripDataMap);
						compareCredData(tripDataMap, TrialBidAPI.apiCredData);

						// compareCredDatas(tripDataMap,TrialBidAPI.apiCredData);

					}

					// Close modal popup if present
					try {
						new Actions(driver).sendKeys(Keys.ESCAPE).perform();
					} catch (Exception e) {
						WbidBasepage.logger.info("Modal close action failed, continuing.");
					}

				} catch (Exception e) {
					WbidBasepage.logger.info("Error processing trip element: ");
				}
			}
			// alltripDataMap=tripDataMap;
			WbidBasepage.logger.info("Final Extracted Trip Data: " + alltripDataMap);
			return alltripDataMap;
		}

	public boolean IndividualCredCompareAPI() {
		boolean isComparisonSuccessful = true; // Assume success unless proven otherwise

		for (WebElement tripElement : tripList) {
			Map<String, Map<String, List<Integer>>> tripDataMap = new LinkedHashMap<>();

			try {
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

						if (tripDataText.startsWith("Rpt")) {
							continue;
						}

						// Extract date (e.g., "01Apr", "15May")
						Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
						Matcher matcherD = patternD.matcher(tripDataText);

						if (matcherD.find()) {
							String tripDate = matcherD.group(1).trim();

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
											.computeIfAbsent(tripDate, k -> new ArrayList<>()).add(extractedValue);

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
				isComparisonSuccessful = false; // Mark as unsuccessful if an error occurs
			}
		}

		return isComparisonSuccessful;
	}
	//Compare UI individual cred and API Individual cred values 
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

	// Sort UI Dates (e.g., ["07Apr", "08Apr", "09Apr"])
				List<String> sortedUiDates = new ArrayList<>(uiDatesMap.keySet());
				sortedUiDates.sort(Comparator.comparing(this::convertDateToComparable));

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
		//Converts "07Apr" to "Apr07" for correct sorting
		private String convertDateToComparable(String date) {
			return date.substring(2) + date.substring(0, 2);
		}
		
}
