package pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import API.TrialBidAPI;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class IndividualCredValuePage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public IndividualCredValuePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}

//TC 3
	@FindBy(xpath = "//img[@class='manin-logo']")
	public WebElement logo;

	public boolean logoVisibile() {
		objwait.waitForElementTobeVisible(driver, logo, 30);
		boolean logoVisible = objaction.fordisplay(logo);
		WbidBasepage.logger.log(Status.INFO, "Logo of the application Visible: " + logoVisible);
		return logoVisible;
	}

//TC 4
	@FindBy(xpath = "//a[text()='Retrieve New BidData']")
	public WebElement retrievenewbiddata;
	@FindBy(xpath = "//a[text()='Retrieve Historical BidData']")
	public WebElement retrievehistoricalbiddata;

	public boolean isVisibleBiddata() {
		objwait.waitForElementTobeVisible(driver, retrievenewbiddata, 30);
		String txt1 = objaction.gettext(retrievenewbiddata);
		String txt2 = objaction.gettext(retrievehistoricalbiddata);
		boolean isVisible = objaction.fordisplay(retrievenewbiddata) && objaction.fordisplay(retrievehistoricalbiddata);
		WbidBasepage.logger.log(Status.INFO, "Retrive Button Visible: " + txt1 + " & " + txt2);
		return isVisible;
	}

//TC 5
	@FindBy(xpath = "//h2[text()='Enter Employee Number']")
	public WebElement empNo;

	public String checkEmpnumHeader() {
		objwait.waitForElementTobeVisible(driver, empNo, 60);
		String headerName = empNo.getText().trim();
		WbidBasepage.logger.pass("popup appears with Header :" + headerName);
		return headerName;
	}

//TC 7
	@FindBy(xpath = "(//button[text()=' Download '])[1]")
	public WebElement downloadBtn;

	public boolean downloadEnable() {
		boolean isDownloadEnabled;
		if (downloadBtn.isEnabled()) {
			isDownloadEnabled = true;
			WbidBasepage.logger.pass("Download button is enabled :" + isDownloadEnabled);
		} else {
			isDownloadEnabled = false;
			WbidBasepage.logger.fail("Download button is NOT enabled ");
		}
		return isDownloadEnabled;
	}

//TC 8
	@FindBy(xpath = "//div[text()='Downloading Bid..']")
	public WebElement loading;

	public boolean displayloadingicon() {
		boolean isPresent = objaction.fordisplay(loading);
		WbidBasepage.logger.pass("Loading appears :" + isPresent);
		return isPresent;
	}

//TC 9
	@FindBy(xpath = "//h2[text()='Early Bid Warning']")
	public WebElement earlyBidPopup;

	public boolean visibleEarlyBidPopup() {
		objwait.waitForElementTobeVisible(driver, earlyBidPopup, 30);
		boolean isPopupPresent = earlyBidPopup.isDisplayed();
		WbidBasepage.logger.pass("popup appears :" + isPopupPresent);
		return isPopupPresent;
	}

	@FindBy(xpath = "//button[text()='Ok']")
	public WebElement okBtn;

	public void clickOkEarlyBid() {
		objwait.waitForElementTobeVisible(driver, okBtn, 60);
		objaction.click(okBtn);
	}

//TC 10
	@FindBy(xpath = "//div[text()='Bid Data is not available. Please try again later.']")
	public WebElement bidNotAvailable;

	public boolean bidNotAvailableBidPopup() {
		objwait.waitForElementTobeVisible(driver, bidNotAvailable, 30);
		boolean isPopupPresent = bidNotAvailable.isDisplayed();
		WbidBasepage.logger.pass("popup appears :" + isPopupPresent);
		return isPopupPresent;
	}

	@FindBy(xpath = "//button[text()='OK']")
	public WebElement bidNotAvailableokBtn;

	public void clickOkbidNotAvailable() {
		objwait.waitForElementTobeVisible(driver, bidNotAvailableokBtn, 60);
		objaction.click(bidNotAvailableokBtn);
	}

//TC 11
	@FindBy(xpath = "(//button[@class='submit-cancel'])[1]")
	public WebElement cancelBtn;

	public void clickcancelBtn() {
		objwait.waitForElementTobeVisible(driver, cancelBtn, 60);
		objaction.click(cancelBtn);
	}

// TC 15 Seniority List 
	@FindBy(xpath = "//h2[text()='Seniority List']")
	public WebElement seniorityHead;

	public String displaySeniority() {
		WbidBasepage.logger.pass("Seniority Pop Up header :" + objaction.gettext(seniorityHead));
		return objaction.gettext(seniorityHead);
	}

//TC 16
	@FindBy(xpath = "//h2[text()='Latest News']")
	public WebElement latestNwzHead;

	public String latestnewHeader() {
		objwait.waitForElementTobeVisible(driver, latestNwzHead, 70);
		String news = objaction.gettext(latestNwzHead);
		WbidBasepage.logger.pass("Latest News Pop Up header :" + news);
		return news;
	}

//TC 17
	@FindBy(xpath = "//h2[text()='Cover Letter']")
	public WebElement coverLetterHead;

	public String coverLetterHeader() {
		objwait.waitForElementTobeVisible(driver, coverLetterHead, 10);
		String news = objaction.gettext(coverLetterHead);
		WbidBasepage.logger.pass("Cover Letter Pop Up header :" + news);
		return news;
	}

//TC 18  intial scratchpad Count and start over 
	@FindBy(xpath = "//h6[@class='scr-head']")
	public WebElement scrLinesHead;

	public int scrLinesTotalCount() {
		objwait.waitForElementTobeVisible(driver, scrLinesHead, 90);
		String text = scrLinesHead.getText().trim();
		WbidBasepage.logger.pass("Scratchpad Lines total count UI: " + text);
		// Extract number using regex
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			int scratchpadCount = Integer.parseInt(matcher.group());
			WbidBasepage.logger.pass("Scratchpad Lines total count UI: " + scratchpadCount);
			return scratchpadCount;

		} else {
			throw new NumberFormatException("No numeric value found in: " + text);
		}
	}

	@FindBy(xpath = "//i[@class='fas fa-forward']")
	public WebElement moveIcon;

	public boolean movearrowclickable() {
		objwait.waitForElementTobeVisible(driver, moveIcon, 90);
		if (moveIcon.isDisplayed() && moveIcon.isEnabled()) {
			objaction.click(moveIcon);// Simulate user click
			WbidBasepage.logger.pass("Arrow button was clicked successfully.");
			return true;
		} else {
			WbidBasepage.logger.fail("Arrow button is not enabled or visible.");
			return false;
		}
	}

//TC 19 
	@FindBy(xpath = "//h6[contains(@class,'bid-head')]")
	public WebElement bidListHead;

	public int bidListHeadCount() {
		objwait.waitForElementTobeVisible(driver, bidListHead, 90);
		String text = bidListHead.getText().trim();
		WbidBasepage.logger.pass("BidList Head total count UI: " + text);
		// Extract only the first number at the start of the string
		Pattern pattern = Pattern.compile("^\\d+");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			int bidListCount = Integer.parseInt(matcher.group());
			WbidBasepage.logger.pass("Bid List count UI: " + bidListCount);
			return bidListCount;

		} else {
			throw new NumberFormatException("No numeric value found in: " + text);
		}
	}

//TC 20
	@FindBy(xpath = "//i[@class='fas fas fa-ellipsis-h']")
	public WebElement ellipsis;

	public boolean ellipisIconVisible() {
		WbidBasepage.logger.pass("Ellipis Icon Visible  :" + objaction.fordisplay(ellipsis));
		return objaction.fordisplay(ellipsis);
	}

	@FindBy(xpath = "//li[@class='dropdown-item']/a[text()='Start Over']")
	public WebElement startOverBtn;

	@FindBy(xpath = "//button[text()='Yes']")
	public WebElement yesBtn;

	public void startOver() {
		objwait.waitForElementTobeVisible(driver, ellipsis, 90);
		objaction.click(ellipsis);
		objwait.waitForElementTobeVisible(driver, startOverBtn, 90);
		objaction.click(startOverBtn);
		objwait.waitForElementTobeVisible(driver, yesBtn, 90);
		objaction.click(yesBtn);
		objwait.waitForElementTobeVisible(driver, yesBtn, 90);
		objaction.click(yesBtn);
	}

//TC 23
	@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[4]/div/pre")
	public WebElement CredHead;

	public boolean CredHeadVisible() {
		objwait.waitForElementTobeVisible(driver, CredHead, 10);
		String txt = objaction.gettext(CredHead);
		WbidBasepage.logger.pass("Head  :" + txt);
		boolean isVisible = txt.contains("Cred");
		WbidBasepage.logger.pass("Cred Head Visible :" + isVisible);
		return isVisible;
	}

//Get individual cred value

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

	public Map<String, Map<String, Map<String, Integer>>> getAllTripData() {
		Map<String, Map<String, Map<String, Integer>>> allTripsMap = new HashMap<>(); // Stores (Line -> (Trip Code ->
																						// Date))
		Map<String, Map<String, Integer>> credMapUI = new HashMap<>();

		// Line counter
		int lineCount = tripLines.size();

		// for (WebElement tripLine : tripLines) {
		for (int i = 1; i < lineCount; i++) {
			try {
				// Find trip elements inside this specific trip line
				String tripXPath = getTripXPath(i);
				List<WebElement> tripList = driver.findElements(By.xpath(tripXPath));
				Map<String, String> tripDataMap = new HashMap<>(); // Stores (Trip Code -> Date) for each line

				if (tripList.isEmpty()) {
					WbidBasepage.logger.info("No trips found in Line " + (i + 1));
					continue;
				}

				for (WebElement tripElement : tripList) {
					try {
						objwait.waitForElementTobeVisible(driver, tripElement, 90);

						// Scroll into view
						((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
								tripElement);
						objwait.waitForElemntTobeClickable(driver, tripElement, 30);

						// Click the trip element
						try {
							tripElement.click();
						} catch (Exception e) {
							((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
						}

						objwait.waitForElementTobeVisible(driver, tripSequence, 90);
						String tripSequenceText = objaction.gettext(tripSequence).trim();
						// WbidBasepage.logger.info("Line No: "+i+" Trip Sequence Text: " +
						// tripSequenceText);

						tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
						Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
						Matcher matcher = pattern.matcher(tripSequenceText);

						if (matcher.find()) {
							String tripCode = matcher.group(1).trim();
							// WbidBasepage.logger.info("Line No: "+i+" Extracted Trip Code: " + tripCode);

							for (WebElement tripEle : tripdata) {
								String tripDataText = objaction.gettext(tripEle).trim();
								WbidBasepage.logger.info("Line No: " + i + " Trip Data Text: " + tripDataText);

								// Ignore unwanted trip data
								if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
									// WbidBasepage.logger.info("Line No: "+i+" Ignoring trip data: " +
									// tripDataText);
									continue;
								}

								tripDataText = tripDataText.replaceAll("\\s+", " ");

								// Extract date (e.g., "01Apr", "15May")
								Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
								Matcher matcherD = patternD.matcher(tripDataText);

								if (matcherD.find()) {
									String tripDate = matcherD.group(1).trim();
									WbidBasepage.logger.info("Line No: " + i + " Extracted Trip Code: " + tripCode
											+ " and Trip Date: " + tripDate);

									// Store the trip code and date in the map for this line
									tripDataMap.put(tripCode, tripDate);
									WbidBasepage.logger.info("Line No: " + i + " Extracted Trip Data: " + tripDataMap);

									// Extract last three-digit number for individual credits
									Pattern threeDigitPattern = Pattern.compile("\\b(\\d{3})\\b");
									Matcher numberMatcher = threeDigitPattern.matcher(tripDataText);
									String lastThreeDigit = null;

									while (numberMatcher.find()) {
										lastThreeDigit = numberMatcher.group(1); // Get the last occurrence
									}

									if (lastThreeDigit != null) {
										try {
											int extractedValue = Integer.parseInt(lastThreeDigit);
											WbidBasepage.logger.info("Extracted Individual Credit for " + "Line No: "
													+ i + " Extracted Trip Code: " + tripCode + "Trip Date : "
													+ tripDate + "Individual Cred value : " + extractedValue);
											credMapUI.computeIfAbsent(tripCode, k -> new HashMap<>()).put(tripDate,
													extractedValue);
											WbidBasepage.logger.info("credMapUI");

										} catch (NumberFormatException e) {
											WbidBasepage.logger.fail(
													"Failed to parse extracted individual credit: " + lastThreeDigit);
										}
									}

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
				allTripsMap.put("Line " + i, credMapUI);
				WbidBasepage.logger.info("Line No: " + i + " Extracted Trip Data for this Line: " + allTripsMap);

			} catch (Exception e) {
				WbidBasepage.logger.fail("Error processing trip line: " + e.getMessage());
			}
		}
		WbidBasepage.logger.info(" Extracted Trip Data for this Line: " + allTripsMap);
		return allTripsMap; // Return all trip data mapped to line numbers
	}
//TC 24 ----> Get individual cred values line No-->Trip code-->date-->Individual Cred

	public Map<String, Map<String, Map<String, List<Integer>>>> getAllIndivdualCred() {
		Map<String, Map<String, Map<String, List<Integer>>>> allTripsMap = new HashMap<>(); // Stores (Line -> (Trip
																							// Code -> {Date -> List of
																							// Credits}))

		int lineCount = tripLines.size();

		for (int i = 1; i < lineCount; i++) {
			try {
				String tripXPath = getTripXPath(i);
				List<WebElement> tripList = driver.findElements(By.xpath(tripXPath));

				Map<String, Map<String, List<Integer>>> tripDataMap = new HashMap<>(); // Stores (Trip Code -> {Date ->
																						// List of Credits})

				if (tripList.isEmpty()) {
					WbidBasepage.logger.info("No trips found in Line " + (i + 1));
					continue;
				}

				for (WebElement tripElement : tripList) {
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
						String tripSequenceText = objaction.gettext(tripSequence).trim();
						tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");

						Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
						Matcher matcher = pattern.matcher(tripSequenceText);

						if (matcher.find()) {
							String tripCode = matcher.group(1).trim();

							for (WebElement tripEle : tripdata) {
								String tripDataText = objaction.gettext(tripEle).trim();
								WbidBasepage.logger.info("Line No: " + i + " Trip Data Text: " + tripDataText);

								if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
									continue;
								}

								tripDataText = tripDataText.replaceAll("\\s+", " ");

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
										lastThreeDigit = numberMatcher.group(1); // Overwrites to keep only the last
																					// occurrence
									}

									if (lastThreeDigit != null) {
										try {
											int extractedValue = Integer.parseInt(lastThreeDigit);
											WbidBasepage.logger.info("Extracted Individual Credit for " + tripCode
													+ ": " + extractedValue);

											// Store only the last credit value
											tripDataMap.computeIfAbsent(tripCode, k -> new HashMap<>())
													.computeIfAbsent(tripDate, k -> new ArrayList<>())
													.add(extractedValue);

										} catch (NumberFormatException e) {
											WbidBasepage.logger
													.fail("Failed to parse extracted credit: " + lastThreeDigit);
										}
									}
								} else {
									WbidBasepage.logger.fail("Trip date not found in trip sequence text.");
								}
							}
						}

						try {
							new Actions(driver).sendKeys(Keys.ESCAPE).perform();
						} catch (Exception e) {
							WbidBasepage.logger.info("Modal close action failed, continuing.");
						}

					} catch (Exception e) {
						WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
					}
				}

				// Store data for this line only if it's not empty
				if (!tripDataMap.isEmpty()) {
					allTripsMap.put("Line " + i, tripDataMap);
					WbidBasepage.logger.info("Extracted Trip Data for Line " + i + ": " + allTripsMap);
				} else {
					WbidBasepage.logger.info("No valid trip data found for Line " + i);
				}

			} catch (Exception e) {
				WbidBasepage.logger.fail("Error processing trip line: " + e.getMessage());
			}
		}
		WbidBasepage.logger.info("Final Extracted Trip Data: " + allTripsMap);
		return allTripsMap;
	}

	// Format--->Stores (Trip Code -> {Date -> List of Credits})
	public Map<String, Map<String, List<Integer>>> getIndividualCred() {
		// Stores (Trip Code -> {Date -> List of Credits})
		Map<String, Map<String, List<Integer>>> tripDataMap = new LinkedHashMap<>();
		// int i = 0;

		for (WebElement tripElement : tripList) {
			try {
				// if (i >= 50) break; // Limit to 2 iterations if required
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

						if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
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

		WbidBasepage.logger.info("Final Extracted Trip Data: " + tripDataMap);
		return tripDataMap;
	}
//Compare UI individual cred and API Individual cred values  

	public void compareCredDatas(Map<String, Map<String, List<Integer>>> uiCredData,
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
				WbidBasepage.logger.info("UI Credits: " + uiCredits + " | API Credits: " + apiCredits);

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
	}

//Converts "07Apr" to "Apr07" for correct sorting
	private String convertDateToComparable(String date) {
		return date.substring(2) + date.substring(0, 2);
	}

}
