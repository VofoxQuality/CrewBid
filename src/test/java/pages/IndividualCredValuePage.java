package pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
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
	/*
	 * public void compareCredDatas(Map<String, Map<String, List<Integer>>>
	 * uiCredData, Map<String, Map<String, List<Integer>>> apiCredData) { boolean
	 * isMatch = true;
	 * 
	 * for (String tripCode : apiCredData.keySet()) { // Ensure API keys are used to
	 * prevent missing trip codes if (!uiCredData.containsKey(tripCode)) {
	 * WbidBasepage.logger.fail("Mismatch! API has TripCode: " + tripCode +
	 * ", but UI does not."); isMatch = false; continue; }
	 * 
	 * Map<String, List<Integer>> uiDatesMap = uiCredData.get(tripCode); Map<String,
	 * List<Integer>> apiDutSeqMap = apiCredData.get(tripCode);
	 * 
	 * // Sort UI Dates (e.g., ["01Apr", "03Apr"]) List<String> sortedUiDates = new
	 * ArrayList<>(uiDatesMap.keySet());
	 * sortedUiDates.sort(Comparator.comparing(this::convertDateToComparable));
	 * 
	 * // Sort API DutySeqNum as Integers List<String> sortedDutSeq = new
	 * ArrayList<>(apiDutSeqMap.keySet());
	 * sortedDutSeq.sort(Comparator.comparingInt(Integer::parseInt));
	 * 
	 * int uiIndex = 0, apiIndex = 0;
	 * 
	 * while (apiIndex < sortedDutSeq.size()) { if (uiIndex >= sortedUiDates.size())
	 * { WbidBasepage.logger.
	 * fail("No more UI dates available to compare for TripCode: " + tripCode);
	 * break; }
	 * 
	 * String uiDate = sortedUiDates.get(uiIndex); String dutySeq =
	 * sortedDutSeq.get(apiIndex);
	 * 
	 * List<Integer> uiCredits = uiDatesMap.get(uiDate); List<Integer> apiCredits =
	 * apiDutSeqMap.get(dutySeq);
	 * 
	 * // Log the current comparison WbidBasepage.logger.info("|Trip Code: " +
	 * tripCode + " | UI Date: " + uiDate + " | UI Credits: " + uiCredits +
	 * " | API Credits: " + apiCredits);
	 * 
	 * // If UI does not have the expected date, shift API mapping to the next
	 * available UI date if (!uiDatesMap.containsKey(uiDate)) {
	 * WbidBasepage.logger.fail("UI does not contain date " + uiDate +
	 * ", skipping to next UI date..."); uiIndex++; continue; }
	 * 
	 * // Compare credit values if (!uiCredits.equals(apiCredits)) {
	 * WbidBasepage.logger.fail("❌ Credit mismatch for TripCode: " + tripCode +
	 * ", Date: " + uiDate + " (DutySeqNum: " + dutySeq + ")");
	 * WbidBasepage.logger.info("UI Credits: " + uiCredits + " | API Credits: " +
	 * apiCredits); isMatch = false; }
	 * 
	 * // Move to the next comparison uiIndex++; apiIndex++; } }
	 * 
	 * if (isMatch) {
	 * WbidBasepage.logger.info("✅ UI and API credits match perfectly!"); } else {
	 * WbidBasepage.logger.fail("❌ Differences found in UI and API credit data!"); }
	 * }
	 */
	/*
	 * // comparison for date missing senario public Map<String, Map<String,
	 * List<Integer>>> getIndividualCredAndCompare() { // Stores (Trip Code -> {Date
	 * -> List of Credits}) Map<String, Map<String, List<Integer>>> tripDataMap =
	 * new LinkedHashMap<>(); // int i = 0;
	 * 
	 * for (WebElement tripElement : tripList) { try { // if (i >= 50) break; //
	 * Limit to 2 iterations if required // i++;
	 * 
	 * objwait.waitForElementTobeVisible(driver, tripElement, 90);
	 * ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
	 * tripElement); objwait.waitForElemntTobeClickable(driver, tripElement, 30);
	 * 
	 * try { tripElement.click(); } catch (Exception e) { ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].click();", tripElement); }
	 * 
	 * objwait.waitForElementTobeVisible(driver, tripSequence, 90); String
	 * tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+",
	 * " ");
	 * 
	 * Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); Matcher matcher =
	 * pattern.matcher(tripSequenceText);
	 * 
	 * if (matcher.find()) { String tripCode = matcher.group(1).trim();
	 * 
	 * for (WebElement tripEle : tripdata) { String tripDataText =
	 * objaction.gettext(tripEle).trim().replaceAll("\\s+", " "); //
	 * WbidBasepage.logger.info("Trip Data Text: " + tripDataText);
	 * 
	 * if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
	 * continue; }
	 * 
	 * // Extract date (e.g., "01Apr", "15May") Pattern patternD =
	 * Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b"); Matcher matcherD =
	 * patternD.matcher(tripDataText);
	 * 
	 * if (matcherD.find()) { String tripDate = matcherD.group(1).trim();
	 * 
	 * // Extract only the last three-digit number for credit values Pattern
	 * threeDigitPattern = Pattern.compile("\\b(\\d{3})\\b"); Matcher numberMatcher
	 * = threeDigitPattern.matcher(tripDataText);
	 * 
	 * String lastThreeDigit = null; while (numberMatcher.find()) { lastThreeDigit =
	 * numberMatcher.group(1); // Keep last occurrence }
	 * 
	 * if (lastThreeDigit != null) { try { int extractedValue =
	 * Integer.parseInt(lastThreeDigit); //
	 * WbidBasepage.logger.info("Extracted Credit for " + tripCode + ": " + //
	 * extractedValue);
	 * 
	 * // Store only the last credit value tripDataMap.computeIfAbsent(tripCode, k
	 * -> new LinkedHashMap<>()) .computeIfAbsent(tripDate, k -> new
	 * ArrayList<>()).add(extractedValue);
	 * 
	 * // WbidBasepage.logger.info("Updated Trip Data Map: " + tripDataMap);
	 * 
	 * } catch (NumberFormatException e) { //
	 * WbidBasepage.logger.fail("Failed to parse credit: " + lastThreeDigit); } } }
	 * else {
	 * WbidBasepage.logger.fail("Trip date not found in trip sequence text."); } }
	 * 
	 * }
	 * 
	 * // Close modal popup if present try { new
	 * Actions(driver).sendKeys(Keys.ESCAPE).perform(); } catch (Exception e) {
	 * WbidBasepage.logger.info("Modal close action failed, continuing."); }
	 * 
	 * } catch (Exception e) {
	 * WbidBasepage.logger.info("Error processing trip element: "); } }
	 * 
	 * WbidBasepage.logger.info("Final Extracted Trip Data: " + tripDataMap); return
	 * tripDataMap; }
	 */
//TC 25  -->Get Total Cred compare with API total cred
// Format--->Stores (Trip Code -> no of times Rtp occurs in one trip-> total Credit})

	public boolean totalCredCompareAPI() {
		boolean isComparisonSuccessful = true; // Assume success unless proven otherwise
		Map<String, Map<Integer, Integer>> tripDataMap = new LinkedHashMap<>(); // Keep trip data across iterations

		// int i = 0;
		for (WebElement tripElement : tripList) {
			try {
				// if (i >= 50) break; // Limit to 3 iterations if required
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

				if (!matcher.find()) {
					WbidBasepage.logger.fail("Trip code not found in text: " + tripSequenceText);
					continue; // Skip this trip element
				}

				String tripCode = matcher.group(1).trim();
				tripDataMap.putIfAbsent(tripCode, new LinkedHashMap<>());
				int rptCount = 0;

				for (WebElement tripEle : tripdata) {
					String tripDataText = objaction.gettext(tripEle).trim().replaceAll("\\s+", " ");

					if (tripDataText.contains("Rpt")) {
						rptCount++; // Count occurrences of "Rpt"

						Matcher numberMatcher = Pattern.compile("\\b(\\d{3,4})\\b").matcher(tripDataText);
						String lastCredit = null;
						while (numberMatcher.find()) {
							lastCredit = numberMatcher.group(1); // Get last occurrence
						}
						if (lastCredit != null) {
							try {
								int extractedCredit = Integer.parseInt(lastCredit);
								tripDataMap.get(tripCode).put(rptCount, extractedCredit);
							} catch (NumberFormatException e) {
								WbidBasepage.logger.fail("Failed to parse credit value: " + lastCredit);
							}
						}
					}

					// WbidBasepage.logger.info("Extracted Data: " + tripDataMap);

					// Compare UI data with API data
					boolean isCurrentComparisonSuccessful = compareTotalCredData(tripDataMap,
							TrialBidAPI.apiTotalCredData);
					if (!isCurrentComparisonSuccessful) {
						isComparisonSuccessful = false; // Mark overall failure if one comparison fails
					}
				}
				// Close modal popup if present
				try {
					new Actions(driver).sendKeys(Keys.ESCAPE).perform();
				} catch (Exception e) {
					WbidBasepage.logger.info("Modal close action failed, continuing.");
				}

			} catch (Exception e) {
				WbidBasepage.logger.fail("Error processing trip element: " + e.getMessage());
			}
		}

		WbidBasepage.logger.info("Final Extracted Trip Data: " + tripDataMap);
		return isComparisonSuccessful;
	}

	public boolean compareTotalCredData(Map<String, Map<Integer, Integer>> uiData,
			Map<String, Map<String, Integer>> apiData) {
		boolean isDataMatching = true; // Assume data matches unless proven otherwise

		for (Map.Entry<String, Map<Integer, Integer>> uiEntry : uiData.entrySet()) {
			String tripCode = uiEntry.getKey();
			Map<Integer, Integer> uiTripData = uiEntry.getValue();

			if (!apiData.containsKey(tripCode)) {
				WbidBasepage.logger.fail("Trip Code missing in API data: " + tripCode);
				isDataMatching = false;
				continue; // Skip this trip and move to the next one
			}

			Map<String, Integer> apiTripData = apiData.get(tripCode);

			for (Map.Entry<Integer, Integer> uiInnerEntry : uiTripData.entrySet()) {
				String rptKey = String.valueOf(uiInnerEntry.getKey()); // Convert rptCount (Integer) to String
				int uiCreditValue = uiInnerEntry.getValue();

				if (!apiTripData.containsKey(rptKey)) {
					WbidBasepage.logger.fail("Rpt count " + rptKey + " missing in API data for trip: " + tripCode);
					isDataMatching = false;
					continue;
				}

				int apiCreditValue = apiTripData.get(rptKey);

				if (uiCreditValue != apiCreditValue) {
					WbidBasepage.logger.fail("Mismatch for Trip: " + tripCode + ", Rpt: " + rptKey + " | UI Value: "
							+ uiCreditValue + ", API Value: " + apiCreditValue);
					isDataMatching = false;
				}
			}
		}

		if (isDataMatching) {
			WbidBasepage.logger.info("All UI and API credit data match successfully.");
		} else {
			WbidBasepage.logger.fail("Discrepancies found in UI and API credit data.");
		}

		return isDataMatching;
	}
//CP & FO reserve line  the Individual cred value in the trip details  is 6 	  
//CP,FO-round 1 & 2---mR(mixed Reserve)--color-green,brown

//	Reserve Line Trip --color-->greenish-cyan and  dark orange-red(AM /PM)
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and (contains(@style,'background-color: rgb(17, 166, 124)') or contains(@style,'background-color: rgb(185, 54, 16)'))]")
	public List<WebElement> reserveTripsCP;// Trip detail visible for Reserve line
//	Reserve Line Trip --color-->greenish-cyan	
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and (contains(@style,'background-color: rgb(17, 166, 124)'))]")
	public List<WebElement> reserveAMTripsCP;// Trip detail visible for Reserve line
//	Reserve Line Trip --color-->dark orange-red	
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and contains(@style,'background-color: rgb(185, 54, 16)')]")
	public List<WebElement> reservePMTripsCP;// Trip detail visible for Reserve line

	public boolean ReserveLinesIndividualCred() {
		boolean isComparisonSuccessful = true; // Assume success unless proven otherwise
		// int i = 0;
		for (WebElement tripElement : reserveTripsCP) {
			Map<String, Map<String, List<Integer>>> tripDataMap = new LinkedHashMap<>();

			try {
				// if (i >= 5) break; // Limit to 3 iterations if required
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
									// WbidBasepage.logger.info("Individual credit: " + extractedValue);

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
						// Log the extracted data
						// WbidBasepage.logger.info("Individual Cred Data for Comparison: " +
						// tripDataMap);

						// Compare extracted values with 600
						boolean isComparisonSuccessful1 = true;
						for (Map<String, List<Integer>> dateMap : tripDataMap.values()) {
							for (List<Integer> values : dateMap.values()) {
								WbidBasepage.logger.info("Individual Cred Data for Comparison: " + tripDataMap);

								if (!values.contains(600)) {
									isComparisonSuccessful1 = false; // If any value is not 600, mark as failure
									WbidBasepage.logger.fail("Individual Cred Data for Comparison: " + tripDataMap);
								}
							}
						}

						if (!isComparisonSuccessful1) {
							isComparisonSuccessful = false; // If any comparison fails, mark the entire function as
															// unsuccessful
							WbidBasepage.logger.fail("Individual Cred Data for Comparison: " + tripDataMap);
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
				WbidBasepage.logger.fail("Error processing trip element: " + e.getMessage());
				isComparisonSuccessful = false; // Mark as unsuccessful if an error occurs
			}
		}

		return isComparisonSuccessful;
	}

	public boolean ReserveLinesAMCred() {
		boolean isComparisonSuccessful = true; // Assume success unless proven otherwise
		// int i = 0;
		for (WebElement tripElement : reserveAMTripsCP) {
			Map<String, Map<String, List<Integer>>> tripDataMap = new LinkedHashMap<>();

			try {
				// if (i >= 5) break; // Limit to 3 iterations if required
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
									// WbidBasepage.logger.info("Individual credit: " + extractedValue);

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
						// Log the extracted data
						// WbidBasepage.logger.info("Individual Cred Data for Comparison: " +
						// tripDataMap);

						// Compare extracted values with 600
						boolean isComparisonSuccessful1 = true;
						for (Map<String, List<Integer>> dateMap : tripDataMap.values()) {
							for (List<Integer> values : dateMap.values()) {
								WbidBasepage.logger.info("AM color-greenish-cyan Individual Cred: " + tripDataMap);

								if (!values.contains(600)) {
									isComparisonSuccessful1 = false; // If any value is not 600, mark as failure
									WbidBasepage.logger.fail("Individual Cred Data for Comparison: " + tripDataMap);
								}
							}
						}

						if (!isComparisonSuccessful1) {
							isComparisonSuccessful = false; // If any comparison fails, mark the entire function as
															// unsuccessful
							WbidBasepage.logger.fail("Individual Cred Data for Comparison: " + tripDataMap);
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
				WbidBasepage.logger.fail("Error processing trip element: " + e.getMessage());
				isComparisonSuccessful = false; // Mark as unsuccessful if an error occurs
			}
		}

		return isComparisonSuccessful;
	}

	public boolean ReserveLinesPMCred() {
		boolean isComparisonSuccessful = true; // Assume success unless proven otherwise
		// int i = 0;
		for (WebElement tripElement : reservePMTripsCP) {
			Map<String, Map<String, List<Integer>>> tripDataMap = new LinkedHashMap<>();

			try {
				// if (i >= 5) break; // Limit to 3 iterations if required
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
									// WbidBasepage.logger.info("Individual credit: " + extractedValue);

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
						// Log the extracted data
						// WbidBasepage.logger.info("Individual Cred Data for Comparison: " +
						// tripDataMap);

						// Compare extracted values with 600
						boolean isComparisonSuccessful1 = true;
						for (Map<String, List<Integer>> dateMap : tripDataMap.values()) {
							for (List<Integer> values : dateMap.values()) {
								WbidBasepage.logger.info("Individual Cred Data for Comparison: " + tripDataMap);

								if (!values.contains(600)) {
									isComparisonSuccessful1 = false; // If any value is not 600, mark as failure
									WbidBasepage.logger
											.fail("PM lines(color-->dark orange-red)Individual Cred:" + tripDataMap);
								}
							}
						}

						if (!isComparisonSuccessful1) {
							isComparisonSuccessful = false; // If any comparison fails, mark the entire function as
															// unsuccessful
							WbidBasepage.logger.fail("Individual Cred Data for Comparison: " + tripDataMap);
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
				WbidBasepage.logger.fail("Error processing trip element: " + e.getMessage());
				isComparisonSuccessful = false; // Mark as unsuccessful if an error occurs
			}
		}

		return isComparisonSuccessful;
	}

////FA Round-2 Reserve lines--sa-green
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and contains(@style,'background-color: rgb(17, 166, 124)')]")
	public List<WebElement> saReserveTripsFA;// Trip detail visible for Reserve line

////FA Round-2 Reserve lines--ja-light Green
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and contains(@style,'background-color: rgb(113, 203, 181)')]")
	public List<WebElement> jaReserveTripsFA;// Trip detail visible for Reserve line

////FA Round-2 Reserve lines--sp-brick red
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and contains(@style,'background-color: rgb(185, 54, 16)')]")
	public List<WebElement> spReserveTripsFA;// Trip detail visible for Reserve line

////FA Round-2 Reserve lines--jp-light pink
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and contains(@style,'background-color: rgb(201, 118, 97)')]")
	public List<WebElement> jpReserveTripsFA;// Trip detail visible for Reserve line

////FA Round-2 Reserve lines--jl--brown	
	@FindBy(xpath = "//td[contains(@class,'left-side-radius') and contains(@style,'background-color: rgb(201, 118, 97)')]")
	public List<WebElement> jlReserveTripsFA;// Trip detail visible for Reserve line

	public boolean ReserveLinesFACred(List<WebElement> ReserveTripsFA) {
		boolean isComparisonSuccessful = true; // Assume success unless proven otherwise
		// int i = 0;
		for (WebElement tripElement : ReserveTripsFA) {
			Map<String, Map<String, List<Integer>>> tripDataMap = new LinkedHashMap<>();

			try {
				// if (i >= 5) break; // Limit to 3 iterations if required
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
									// WbidBasepage.logger.info("Individual credit: " + extractedValue);

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
						// Log the extracted data
						// WbidBasepage.logger.info("Individual Cred Data for Comparison: " +
						// tripDataMap);

						// Compare extracted values with 650
						boolean isComparisonSuccessful1 = true;
						for (Map<String, List<Integer>> dateMap : tripDataMap.values()) {
							for (List<Integer> values : dateMap.values()) {
								WbidBasepage.logger.info("Individual Cred Data for Comparison: " + tripDataMap);

								if (!values.contains(650)) {
									isComparisonSuccessful1 = false; // If any value is not 650, mark as failure
									WbidBasepage.logger.fail("Individual Cred:" + tripDataMap);
								}
							}
						}

						if (!isComparisonSuccessful1) {
							isComparisonSuccessful = false; // If any comparison fails, mark the entire function as
															// unsuccessful
							WbidBasepage.logger.fail("Individual Cred Data for Comparison: " + tripDataMap);
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
				WbidBasepage.logger.fail("Error processing trip element: " + e.getMessage());
				isComparisonSuccessful = false; // Mark as unsuccessful if an error occurs
			}
		}

		return isComparisonSuccessful;
	}
}
