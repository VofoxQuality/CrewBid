package pages;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import API.ScratchPadBlankReservedLines;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class BidDownloadPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	// ScratchPadBlankReservedLines objcount=new ScratchPadBlankReservedLines();

	public BidDownloadPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}

	@FindBy(xpath = "//nav[@class=\"navbar navbar-expand-sm navbar-light\"]/img")
	public WebElement logo;
	@FindBy(id = "navbardrop")
	public WebElement retrivedropdown;
	@FindBy(xpath = "//button[text()=\"Update Now\"]")
	public WebElement updatebtn;
	@FindBy(xpath = "//button[text()=\"Exit\"]")
	public WebElement exitbtn;

	public boolean fordisplaylogo() {
		return objaction.fordisplay(logo);
	}

	public boolean fordisplayretrivedropdown() {
		return objaction.fordisplay(retrivedropdown);
	}

	public void click_retrievedownload() {
		objaction.click(retrivedropdown);
	}

	@FindBy(xpath = "//a[text()=\"Retrieve New BidData\"]")
	public WebElement retrievenewbiddata;
	@FindBy(xpath = "//a[text()=\"Retrieve Historical BidData\"]")
	public WebElement retrievehistoricalbiddata;

	public boolean fordisplaynewbiddata() {
		return objaction.fordisplay(retrievenewbiddata) && objaction.fordisplay(retrievehistoricalbiddata);
	}

	// TC 7
	public void forclicknewbiddata() {
		objaction.click(retrievenewbiddata);
	}

	@FindBy(xpath = "//h2[text()=\"Enter Employee Number\"]")
	public WebElement empnumberpopupheader;

	public String checkEmpnumpopupheader() {
		return objaction.gettext(empnumberpopupheader);
	}

	// TC 8
	@FindBy(xpath = "(//p[@class=\"text-center\"])[2]")
	public WebElement emppopuptext;

	public String checkempPopupText() {
		return objaction.gettext(emppopuptext);
	}

	// TC 9
	@FindBy(xpath = "//button[@class=\"submit-cancel btn\"]")
	public WebElement cancel_btn;
	@FindBy(xpath = "//button[@class=\"submit-bid btn\"]")
	public WebElement ok_btn;

	public boolean fordisplaybtninpopup() {
		return objaction.fordisplay(cancel_btn) && objaction.fordisplay(ok_btn);
	}

	// Tc 10
	@FindBy(name = "empNum")
	public WebElement empplaceholder;

	public boolean checkplaceholder() {
		String att = objaction.getAttribute(empplaceholder, "placeholder");
		WbidBasepage.logger.info(att);
		if (att.contains("Employee No:")) {
			WbidBasepage.logger.pass("✅Placeholder is same");
			return true;
		} else {
			WbidBasepage.logger.fail("❌Placeholder mismatch");
			return false;
		}
	}

	// TC 11
	public void enterempid() {
		objaction.sendkey(empplaceholder, "21221");
		objaction.click(ok_btn);
	}

	public void forclickokbtn() {
		objaction.click(ok_btn);
	}

	@FindBy(xpath = "//h2[text()=\"Retrieve New Bid Period\"]")
	public WebElement retrievebidpopupheader;

	public String checkretrievebidpopupheader() {
		return objaction.gettext(retrievebidpopupheader);
	}

	// TC 13
	@FindBy(xpath = "//*[@id=\"newBidModal\"]/div/div/div[2]/div/div[1]")
	public List<WebElement> labels;
	public List<String> Actuallabels;

	public boolean checkRetrievebidpopuplabels() {
		Actuallabels = new ArrayList<>();
		for (WebElement option : labels) {
			String label = objaction.gettext(option);
			WbidBasepage.logger.info("🎯Labels:" + label);
			Actuallabels.add(label);
		}
		List<String> Expectedlabels = Arrays.asList("Base:", "Position:", "Round:", "Month:");
		if (Actuallabels.containsAll(Expectedlabels) && Expectedlabels.containsAll(Actuallabels)) {
			WbidBasepage.logger.pass("✅Labels are same");
			return true;
		} else {
			WbidBasepage.logger.fail("❌Labels are mismatch");
			return false;
		}
	}

	// TC 15
	@FindBy(xpath = "(//div[@class=\"modal-header text-center\"]/button[@class=\"close\"])[1]")
	public WebElement close_btn;

	public void click_closebtn() {
		objaction.click(close_btn);
	}

//TC16
	@FindBy(xpath = "//*[@id=\"newBidModal\"]/div/div/div[2]/div[1]/div[2]/button")
	public List<WebElement> basecities;

	public boolean checkcities_isenable() {
		boolean allEnabled = true;
		WbidBasepage.logger.info("✨Verifying all base cities:");

		for (WebElement option : basecities) {
			String cityName = option.getText();
			WbidBasepage.logger.info("✨City: " + cityName);

			objwait.waitForElemntTobeClickable(driver, option, 5);
			objaction.click(option);
			objwait.waitS(3000);
			String att = objaction.getAttribute(option, "class");

			if (att.contains("active-re")) {
				WbidBasepage.logger.pass("🎯City '" + cityName + "'✅ is enabled.");
			} else {
				WbidBasepage.logger.fail("🎯City '" + cityName + "'❌ is disabled.");
				allEnabled = false;
			}
		}

		if (allEnabled) {
			WbidBasepage.logger.pass("✅ All 14 base cities are enabled.");
		} else {
			WbidBasepage.logger.fail("❌ Some base cities are disabled.");
		}
		return allEnabled;
	}

//TC17
	@FindBy(xpath = "//*[@id=\"newBidModal\"]/div/div/div[2]/div[2]/div[2]/button")
	public List<WebElement> positionlist;

	public boolean checkposition_isenable() {
		boolean allEnabled = true;
		WbidBasepage.logger.info("✨Verifying all Positions:");

		for (WebElement option : positionlist) {
			String position = option.getText();
			WbidBasepage.logger.info("City: " + position);

			objwait.waitForElemntTobeClickable(driver, option, 5);
			objaction.click(option);
			objwait.waitS(3000);
			String att = objaction.getAttribute(option, "class");

			if (att.contains("active-re")) {
				WbidBasepage.logger.pass("✨Position'" + position + "' is enabled.");
			} else {
				WbidBasepage.logger.fail("✨Position '" + position + "' is disabled.");
				allEnabled = false;
			}
		}

		if (allEnabled) {
			WbidBasepage.logger.pass("✅ All 3 Positions are enabled.");
		} else {
			WbidBasepage.logger.fail("❌ Some Positions are disabled.");
		}
		return allEnabled;
	}

	/******************
	 * 🔥🎯💡✨🚀✅🔒❌Month Validation Checking❌🔒✅🔥🎯💡✨🚀
	 *********************/
	// TC18
	@FindBy(xpath = "//*[@id=\"newBidModal\"]/div/div/div[2]/div[4]/div[2]/button")
	public List<WebElement> monthlist;
	String monthName;

	public void formonthselection() {
		// Get next month in SHORT format (e.g., "Mar")
		nextMonth = LocalDate.now().plusMonths(1).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

		WbidBasepage.logger.info("Expected default selected month: " + nextMonth);

		// Check which month is selected in the dropdown
		for (WebElement option : monthlist) {
			monthName = option.getText().trim();
			String isSelected = option.getAttribute("selected"); // Or use appropriate attribute/class

			WbidBasepage.logger.info("Month: " + monthName + " | Selected: " + isSelected);

			if ("true".equals(isSelected) || option.getAttribute("class").contains("active")) {
				WbidBasepage.logger.info("Currently selected month: " + monthName);
				if (monthName.equalsIgnoreCase(nextMonth)) {
					WbidBasepage.logger.pass("✅ Next month '" + nextMonth + "' is correctly selected by default.");

				} else {
					WbidBasepage.logger.fail("❌ Expected '" + nextMonth + "', but found '" + monthName + "'.");

				}
			}
		}
	}

	public boolean isNextMonthSelectedByDefault() {
		// Get next month in SHORT format (e.g., "Mar")
		nextMonth = LocalDate.now().plusMonths(1).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

		WbidBasepage.logger.info("Expected default selected month: " + nextMonth);

		// Check which month is selected in the dropdown
		for (WebElement option : monthlist) {
			monthName = option.getText().trim();
			String isSelected = option.getAttribute("selected"); // Or use appropriate attribute/class

			WbidBasepage.logger.info("Month: " + monthName + " | Selected: " + isSelected);

			if ("true".equals(isSelected) || option.getAttribute("class").contains("active")) {
				WbidBasepage.logger.info("Currently selected month: " + monthName);
				if (monthName.equalsIgnoreCase(nextMonth)) {
					WbidBasepage.logger.pass("✅ Next month '" + nextMonth + "' is correctly selected by default.");
					return true;
				} else {
					WbidBasepage.logger.fail("❌ Expected '" + nextMonth + "', but found '" + monthName + "'.");
					return false;
				}
			}
		}

		WbidBasepage.logger.fail("❌ No month is selected by default.");
		return false;
	}

	// TC19
	public boolean checkdisablemonths() {
		int currentMonthValue = LocalDate.now().getMonthValue(); // 1 = January, 12 = December
		List<String> disabledMonthsList = new ArrayList<>();
		boolean isValidationSuccessful = true;

		WbidBasepage.logger.info("🔒 Checking and displaying only disabled months...");

		for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
			String expectedMonth = Month.of(monthIndex).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			boolean isMonthFound = false;

			for (WebElement option : monthlist) {
				String actualMonth = option.getText().trim();
				String classAttribute = option.getAttribute("class");
				String isDisabled = option.getAttribute("disabled");

				if (actualMonth.equalsIgnoreCase(expectedMonth)) {
					isMonthFound = true;
					boolean shouldBeDisabled = (monthIndex <= currentMonthValue);
					boolean isActuallyDisabled = (isDisabled != null && isDisabled.equalsIgnoreCase("true"))
							|| (classAttribute != null && classAttribute.toLowerCase().contains("disabled"));

					if (isActuallyDisabled) {
						disabledMonthsList.add(expectedMonth);
						WbidBasepage.logger.info("✅ Disabled Month: " + expectedMonth);
					}

					if (shouldBeDisabled != isActuallyDisabled) {
						WbidBasepage.logger.fail("❌ State mismatch for '" + expectedMonth + "'. Expected DISABLED: "
								+ shouldBeDisabled + " but found: " + isActuallyDisabled);
						isValidationSuccessful = false;
					}
					break;
				}
			}

			if (!isMonthFound) {
				WbidBasepage.logger.fail("⚠️ Month '" + expectedMonth + "' not found in the dropdown.");
				isValidationSuccessful = false;
			}
		}

		WbidBasepage.logger.info("📋 Disabled Months: " + String.join(", ", disabledMonthsList));

		if (isValidationSuccessful) {
			WbidBasepage.logger.pass("✅ Validation successful: All expected months are disabled.");
		} else {
			WbidBasepage.logger.fail("❌ Validation failed: One or more months are not in the expected state.");
		}

		return isValidationSuccessful;
	}
//TC20

	public boolean verifyNextAndUpcomingMonthsEnabled() {
		int currentMonthValue = LocalDate.now().getMonthValue(); // 1 = January, 12 = December
		List<String> enabledMonthsList = new ArrayList<>();
		boolean isValidationSuccessful = true;

		WbidBasepage.logger.info("🔎 Checking and displaying only enabled months (excluding 'active' months)...");

		for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
			String expectedMonth = Month.of(monthIndex).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			boolean isMonthFound = false;

			for (WebElement option : monthlist) {
				String actualMonth = option.getText().trim();
				String classAttribute = option.getAttribute("class");
				String isDisabled = option.getAttribute("disabled");

				if (actualMonth.equalsIgnoreCase(expectedMonth)) {
					isMonthFound = true;
					boolean shouldBeEnabled = (monthIndex > currentMonthValue);
					boolean isActuallyEnabled = (isDisabled == null || !isDisabled.equalsIgnoreCase("true"))
							&& (classAttribute == null || !classAttribute.toLowerCase().contains("disabled"));

					// 🚫 Exclude months with 'active' class
					if (classAttribute != null && classAttribute.contains("active")) {
						WbidBasepage.logger.info("⚠️ Skipping '" + expectedMonth + "' as it has 'active' class.");
						break;
					}

					// ✅ Only display enabled months in output
					if (isActuallyEnabled) {
						enabledMonthsList.add(expectedMonth);
						WbidBasepage.logger.info("✅ Enabled Month: " + expectedMonth);
					}

					// ❌ Validation: If a month expected to be enabled isn't, log failure
					if (shouldBeEnabled && !isActuallyEnabled) {
						WbidBasepage.logger
								.fail("❌ Month '" + expectedMonth + "' should be ENABLED but found DISABLED.");
						isValidationSuccessful = false;
					}
					break;
				}
			}

			if (!isMonthFound) {
				WbidBasepage.logger.fail("⚠️ Month '" + expectedMonth + "' not found in the dropdown.");
				isValidationSuccessful = false;
			}
		}

		WbidBasepage.logger
				.info("📋 Enabled Months Only (excluding 'active' months): " + String.join(", ", enabledMonthsList));

		if (isValidationSuccessful) {
			WbidBasepage.logger.pass("🎯 Validation successful: All upcoming months are enabled as expected.");
		} else {
			WbidBasepage.logger.fail("❌ Validation failed: One or more upcoming months are not enabled as expected.");
		}

		return isValidationSuccessful;
	}

//TC20
	@FindBy(xpath = "(//button[text()=\" ATL \"])[1]")
	public WebElement atl;
	@FindBy(xpath = "(//button[text()=\" CP \"])[1]")
	public WebElement cp;
	@FindBy(xpath = "(//button[text()=\" 1st Round \"])[1]")
	public WebElement firstround;
	@FindBy(xpath = "(//button[text()=\" Mar \"])[1]")
	public WebElement mar;

	// TC 21
	@FindBy(xpath = "//div[@class=\"swal2-html-container\"]")
	public WebElement warningpopup;
	@FindBy(xpath = "(//button[text()=\" Download \"])[1]")
	public WebElement download_btn;

	/***********
	 * Bid Download Cases
	 *************/
	int currentDay = LocalDate.now().getDayOfMonth();
	int lastDayOfMonth = LocalDate.now().lengthOfMonth();
	String nextMonth;

	/////////// Condition 1//////////
	public boolean checkCondition1DownloadBid() {
		WbidBasepage.logger
				.info("🎯 Verifying user can select round 1 and pilot (CP & FO) with all applicable domiciles:");
		boolean isDownloadEnabled = false;
		// 4th of the month
		if (currentDay >= 4 && currentDay <= lastDayOfMonth) {
			WbidBasepage.logger.info(
					"✅ Success: Report is available for viewing and download from the 4th day until the end of the month.");
			for (WebElement city : basecities) {
				String cityName = city.getText().trim();

				// Skip AUS and FLL for Pilot
				if (cityName.equalsIgnoreCase("AUS") || cityName.equalsIgnoreCase("FLL")) {
					WbidBasepage.logger.info("⚠️ Skipping domicile: " + cityName + " for Pilot as per scenario.");
					continue;
				}
				WbidBasepage.logger.info("✨ Domicile Selected: " + cityName);
				objwait.waitForElemntTobeClickable(driver, city, 5);
				objaction.click(city);
				for (WebElement position : positionlist) {
					String pos = position.getText().trim();

					// Select only Pilot positions (CP and FO)
					if (pos.equalsIgnoreCase("CP") || pos.equalsIgnoreCase("FO")) {
						WbidBasepage.logger.info("✨ Pilot Position Selected: " + pos);
						objwait.waitForElemntTobeClickable(driver, position, 5);
						objaction.click(position);

						// Select Round 1
						objwait.waitForElemntTobeClickable(driver, firstround, 5);
						objaction.click(firstround);
						WbidBasepage.logger.info("✅ Round 1 selected for " + pos + " at domicile " + cityName);
						// Get next month in SHORT format (e.g., "Mar")
						formonthselection();
						// ✅ Check if download button is enabled after first loop execution and return
						// result
						if (download_btn.isEnabled()) {
							WbidBasepage.logger.pass(
									"⬇️ Download button is enabled after selection for " + cityName + " - " + pos);
							isDownloadEnabled = true;
						} else {
							WbidBasepage.logger.fail(
									"🚫 Download button is NOT enabled after selection for " + cityName + " - " + pos);
						}
					}
				}
			}
		} else {
			WbidBasepage.logger.info("❌ Not able to download bid data");
		}
		return isDownloadEnabled;
	}

//TC23
	/////////// Condition 2////////////
	public boolean checkCondition2DownloadBid() {
		WbidBasepage.logger.info("🎯 Verifying user can select round 1 and pilot (FA) with all applicable domiciles:");
		boolean isDownloadEnabled = false;
		////// 2nd of month
		if (currentDay >= 2 && currentDay <= lastDayOfMonth) {
			WbidBasepage.logger.info(
					"✅ Success: Report is available for viewing and download from the 2nd day until the end of the month.");
			for (WebElement city : basecities) {
				String cityName = city.getText().trim();
				WbidBasepage.logger.info("✨ Domicile Selected: " + cityName);
				objwait.waitForElemntTobeClickable(driver, city, 5);
				objaction.click(city);
				for (WebElement position : positionlist) {
					String pos = position.getText().trim();

					if (pos.equalsIgnoreCase("FA")) {
						WbidBasepage.logger.info("✨ FA Position Selected: " + pos);
						objwait.waitForElemntTobeClickable(driver, position, 5);
						objaction.click(position);

						// Select Round 1
						objwait.waitForElemntTobeClickable(driver, firstround, 5);
						objaction.click(firstround);
						WbidBasepage.logger.info("✅ Round 1 selected for " + pos + " at domicile " + cityName);
						// Get next month in SHORT format (e.g., "Mar")
						formonthselection();
						// ✅ Check if download button is enabled after first loop execution and return
						// result
						if (download_btn.isEnabled()) {
							WbidBasepage.logger.pass(
									"⬇️ Download button is enabled after selection for " + cityName + " - " + pos);
							isDownloadEnabled = true;
						} else {
							WbidBasepage.logger.fail(
									"🚫 Download button is NOT enabled after selection for " + cityName + " - " + pos);
						}
					}
				}
			}
		} else {
			WbidBasepage.logger.info("❌ Not able to download bid data");
		}
		return isDownloadEnabled;
	}

	// TC 24
	@FindBy(xpath = "(//button[text()=\" 2nd Round \"])[1]")
	public WebElement secondround;

	///////// Condition 3/////////
	public boolean checkCondition3DownloadBid() {
		WbidBasepage.logger
				.info("🎯 Verifying user can select round 2 and pilot (CP & FO) with all applicable domiciles:");
		boolean isDownloadEnabled = false;
		// 4th of the month
		if (currentDay >= 17 && currentDay <= lastDayOfMonth) {
			WbidBasepage.logger.info(
					"✅ Success: Report is available for viewing and download from the 17th day until the end of the month.");
			for (WebElement city : basecities) {
				String cityName = city.getText().trim();

				// Skip AUS and FLL for Pilot
				if (cityName.equalsIgnoreCase("AUS") || cityName.equalsIgnoreCase("FLL")) {
					WbidBasepage.logger.info("⚠️ Skipping domicile: " + cityName + " for Pilot as per scenario.");
					continue;
				}
				WbidBasepage.logger.info("✨ Domicile Selected: " + cityName);
				objwait.waitForElemntTobeClickable(driver, city, 5);
				objaction.click(city);
				for (WebElement position : positionlist) {
					String pos = position.getText().trim();

					// Select only Pilot positions (CP and FO)
					if (pos.equalsIgnoreCase("CP") || pos.equalsIgnoreCase("FO")) {
						WbidBasepage.logger.info("✨ Pilot Position Selected: " + pos);
						objwait.waitForElemntTobeClickable(driver, position, 5);
						objaction.click(position);

						// Select Round 2
						objwait.waitForElemntTobeClickable(driver, secondround, 5);
						objaction.click(secondround);
						WbidBasepage.logger.info("✅ Round 2 selected for " + pos + " at domicile " + cityName);
						// Get next month in SHORT format (e.g., "Mar")
						formonthselection();
						// ✅ Check if download button is enabled after first loop execution and return
						// result
						if (download_btn.isEnabled()) {
							WbidBasepage.logger.pass(
									"⬇️ Download button is enabled after selection for " + cityName + " - " + pos);
							isDownloadEnabled = true;
						} else {
							WbidBasepage.logger.fail(
									"🚫 Download button is NOT enabled after selection for " + cityName + " - " + pos);
						}
					}
				}
			}
		} else {
			WbidBasepage.logger.info("❌ Not able to download bid data");
		}
		return isDownloadEnabled;
	}

	// TC 25
	////////////// Condition 4////////////////
	public boolean checkCondition4DownloadBid() {
		WbidBasepage.logger.info("🎯 Verifying user can select round 2 and pilot (FA) with all applicable domiciles:");
		boolean isDownloadEnabled = false;
		////// 11th of month
		if (currentDay >= 11 && currentDay <= lastDayOfMonth) {
			WbidBasepage.logger.info(
					"✅ Success: Report is available for viewing and download from the 11th day until the end of the month.");
			for (WebElement city : basecities) {
				String cityName = city.getText().trim();
				WbidBasepage.logger.info("✨ Domicile Selected: " + cityName);
				objwait.waitForElemntTobeClickable(driver, city, 5);
				objaction.click(city);
				for (WebElement position : positionlist) {
					String pos = position.getText().trim();

					if (pos.equalsIgnoreCase("FA")) {
						WbidBasepage.logger.info("✨ FA Position Selected: " + pos);
						objwait.waitForElemntTobeClickable(driver, position, 5);
						objaction.click(position);

						// Select Round 2
						objwait.waitForElemntTobeClickable(driver, secondround, 5);
						objaction.click(secondround);
						WbidBasepage.logger.info("✅ Round 2 selected for " + pos + " at domicile " + cityName);
						// Get next month in SHORT format (e.g., "Mar")
						formonthselection();
						// ✅ Check if download button is enabled after first loop execution and return
						// result
						if (download_btn.isEnabled()) {
							WbidBasepage.logger.pass(
									"⬇️ Download button is enabled after selection for " + cityName + " - " + pos);
							isDownloadEnabled = true;
						} else {
							WbidBasepage.logger.fail(
									"🚫 Download button is NOT enabled after selection for " + cityName + " - " + pos);
						}
					}
				}
			}
		} else {
			WbidBasepage.logger.info("❌ Not able to download bid data");
		}
		return isDownloadEnabled;
	}

	// TC 26
	@FindBy(xpath = "//h2[text()=\"Seniority List\"]")
	public WebElement sen_header;

	public void click_download() {
		objaction.click(download_btn);
	}

	public String fordisplay_seniority() {
		return objaction.gettext(sen_header);
	}

	public String Atl;
	public String Cp;
	public String selectedBase;
	public String selectedPosition;
	@FindBy(xpath = "//button[text()=\"Ok\"]")
	public WebElement popupokbtn;
	@FindBy(xpath = "//button[text()=\"OK\"]")
	public WebElement warningpopupokbtn;

	public boolean checkDownloadBid() {
		WbidBasepage.logger
				.info("🎯 Verifying user can select appropriate rounds and positions with all applicable domiciles:");
		boolean isAnyDownloadAttempted = false;
		int currentDay = LocalDate.now().getDayOfMonth();
		int lastDayOfMonth = LocalDate.now().lengthOfMonth();

		// Check applicable conditions for CP and FO only
		boolean firstRoundSuccess = (currentDay >= 4 && currentDay <= lastDayOfMonth)
				&& processDownload("CP", "FO", firstround);

		boolean secondRoundCP = (currentDay >= 17 && currentDay <= lastDayOfMonth)
				&& processDownload("CP", "FO", secondround);

		// Ensure all conditions are processed
		isAnyDownloadAttempted = firstRoundSuccess && secondRoundCP;

		if (!isAnyDownloadAttempted) {
			WbidBasepage.logger.info("❌ Not able to download bid data for all required conditions.");
		}

		return isAnyDownloadAttempted;
	}

	public boolean processDownload(String pos1, String pos2, WebElement round) {
		boolean isDownloadAttempted = false;
		boolean exitLoop = false; // Flag to break the outer loop

		WbidBasepage.logger.info("✅ Checking download attempt for " + pos1 + " and " + pos2);

		for (WebElement city : basecities) {
			if (exitLoop)
				break; // Break outer loop if needed

			String cityName = city.getText();

			// Skip AUS and FLL for Pilot
			if ((pos1.equals("CP") || pos1.equals("FO"))
					&& (cityName.equalsIgnoreCase("AUS") || cityName.equalsIgnoreCase("FLL"))) {
				WbidBasepage.logger.info("⚠️ Skipping domicile: " + cityName + " as per scenario.");
				continue;
			}

			WbidBasepage.logger.info("✨ Domicile Selected: " + cityName);
			objwait.waitForElemntTobeClickable(driver, city, 5);
			objaction.click(city);

			for (WebElement position : positionlist) {
				if (exitLoop)
					break; // Break inner loop if needed

				String pos = position.getText();

				if (pos.equalsIgnoreCase(pos1) || pos.equalsIgnoreCase(pos2)) {
					WbidBasepage.logger.info("✨ Position Selected: " + pos);
					objwait.waitForElemntTobeClickable(driver, position, 5);
					objaction.click(position);

					// Store selected base and position
					selectedBase = cityName;
					selectedPosition = pos;

					// Select round
					objwait.waitForElemntTobeClickable(driver, round, 5);
					objaction.click(round);
					WbidBasepage.logger.info("✅ Round selected for " + pos + " at domicile " + cityName);

					// Get next month in SHORT format (e.g., "Mar")
					formonthselection();

					// Click the download button regardless of its state
					WbidBasepage.logger.info("⬇️ Clicking the download button for " + cityName + " - " + pos);
					objaction.click(download_btn);

					if (objaction.fordisplay(popupokbtn)) {
						objwait.waitForElemntTobeClickable(driver, popupokbtn, 10);
						objaction.click(popupokbtn);
						objwait.waitForElemntTobeClickable(driver, warningpopupokbtn, 10);
						objaction.click(warningpopupokbtn);

						isDownloadAttempted = true;
					} else {
						WbidBasepage.logger.info("❌ No popup displayed, exiting the method.");
						objaction.fordisplay(loading);

						exitLoop = true; // Set flag to break both loops
						break;
					}
				}
			}
		}

		WbidBasepage.logger.info("📌 Final Selected Base: " + selectedBase);
		WbidBasepage.logger.info("📌 Final Selected Position: " + selectedPosition);

		return isDownloadAttempted;
	}

	// TC28
	@FindBy(xpath = "(//div[@class=\"pop-but-main\"]/button[2])[1]")
	public WebElement sen_cancel_btn;
	@FindBy(xpath = "(//div[@class=\"pop-but-main\"]/button[1])[1]")
	public WebElement viewsenlist_btn;

	public boolean fordisplaySEnbtninpopup() {
		return objaction.fordisplay(sen_cancel_btn) && objaction.fordisplay(viewsenlist_btn);
	}

	@FindBy(xpath = "//div[text()=\"Downloading Bid..\"]")
	public WebElement loading;

	public boolean fordisplayloadingicon() {
		return objaction.fordisplay(loading);
	}

	public void click_senioritylist() {
		objaction.click(viewsenlist_btn);
	}

	// TC29
	@FindBy(id = "fileContent-Section")
	public WebElement sen_list_content;

	public boolean checkMonthInSeniorityList() {
		String report = objaction.gettext(sen_list_content);
		String schedulePeriod = extractValue(report, "SCHEDULE PERIOD:");
		String currentmonth = nextMonth.toUpperCase() + String.valueOf(java.time.Year.now().getValue()).substring(2); // Format:
																														// Mar25
		WbidBasepage.logger.pass("Month : " + currentmonth);
		WbidBasepage.logger.pass("SCHEDULE PERIOD: " + schedulePeriod);
		if (currentmonth.trim().contains(schedulePeriod.trim())) {
			WbidBasepage.logger.pass("The seniority list is for the current month.");
			System.out.println("The seniority list is for the current month.");
			return true;
		} else {
			WbidBasepage.logger.fail("The seniority list is NOT for the current month.");
			System.out.println("The seniority list is NOT for the current month.");
			return false;
		}
	}

	public static String extractValue(String text, String key) {
		String[] lines = text.split("\\n");
		for (String line : lines) {
			if (line.contains(key)) {
				return line.split(key)[1].trim().split(" ")[0];
			}
		}
		return null;
	}

//Tc 30
	@FindBy(xpath = "(//div[@class=\"modal-header text-center justify-content-center\"]/button)[1]")
	public WebElement sen_close_btn;
	@FindBy(xpath = "//a[@class=\"dropdown-toggle\"]/img")
	public WebElement sen_share_btn;

	public boolean fordisplay_close_share_btn() {
		return objaction.fordisplay(sen_close_btn) && objaction.fordisplay(sen_share_btn);
	}

//Tc 31
	@FindBy(xpath = "//ul[@class=\"dropdown-menu show\"]/li[1]")
	public WebElement email_senlist;
	@FindBy(xpath = "//ul[@class=\"dropdown-menu show\"]/li[2]")
	public WebElement print_senlist;

	public boolean fordisplay_email_print_btn() {
		objaction.click(sen_share_btn);
		return objaction.fordisplay(email_senlist) && objaction.fordisplay(print_senlist);
	}

//Tc33
	@FindBy(xpath = "(//h2[@class=\"news-view-main\"])[1]")
	public WebElement latestnews_head;

	public void click_close_sen_btn() {
		objaction.click(sen_close_btn);
	}

	@FindBy(xpath = "( //button[text()=\" Cancel \"])[3]")
	public WebElement cancel_sen_btn;

	public void click_cancel_sen_btn() {
		objaction.click(cancel_sen_btn);
	}

	public String checklatestnew_header() {
		objwait.waitForElementTobeVisible(driver, latestnews_head, 10);
		String news = objaction.gettext(latestnews_head);
		return news;
	}

	// Tc35
	@FindBy(xpath = "(//*[@id=\"fullHeightModalRight\"]/div/div/div/div[1]/button)[1]")
	public WebElement news_closebtn;

	public void click_news_closebtn() {
		objaction.click(news_closebtn);
	}

	// TC36
	@FindBy(xpath = "//h2[text()=\"Cover Letter\"]")
	public WebElement coverletter_head;
	public String content;
	@FindBy(id = "fileContent-Section")
	public WebElement filecontent;

	public String checkcoverletter_head() {
		content = objaction.gettext(filecontent);
		objwait.waitForElementTobeVisible(driver, coverletter_head, 10);
		String news = objaction.gettext(coverletter_head);

		return news;
	}

	// TC37
	@FindBy(xpath = "(//*[@id=\"fullHeightModalRight\"]/div/div/div[1]/button)[1]")
	public WebElement coverltr_closebtn;

	public void click_close_coverletter() {
		objaction.click(coverltr_closebtn);
	}

	@FindBy(xpath = "//ul[@class=\"m-auto header-title-section\"]/li")
	public WebElement scrachpad_head;

	public boolean isvisible_scrachpad_head() {
		return objaction.fordisplay(scrachpad_head);
	}

	// TC39
	public boolean checkoffline() {
		String mode = objaction.gettext(scrachpad_head);
		if (mode.contains("Online")) {
			WbidBasepage.logger.pass("Network in Online ");
			return true;
		} else {
			WbidBasepage.logger.fail("Network in offline");
			return false;
		}
	}

	// TC40
	@FindBy(xpath = "//i[@class=\"fas fa-flag\"]")
	public WebElement flagicon;

	public boolean isvisibleflagicon() {
		return objaction.fordisplay(flagicon);
	}

	// TC41
	@FindBy(xpath = "(//mat-radio-group[@role=\"radiogroup\"])[1]")
	public WebElement herb_local_icon;

	public boolean isvisibleherb_local_icon() {
		return objaction.fordisplay(herb_local_icon);
	}

	// TC42
	@FindBy(xpath = "//i[@class=\"fas fa-cog\"]")
	public WebElement settingicon;

	public boolean isvisibleherbsettingicon() {
		return objaction.fordisplay(settingicon);
	}

	// TC43
	@FindBy(xpath = "//i[@class=\"fas fa-arrows-alt-v\"]")
	public WebElement scrolldown_icon;

	public boolean isvisiblescrolldown_icon() {
		return objaction.fordisplay(scrolldown_icon);
	}

	// TC44
	@FindBy(xpath = "//i[@class=\"fas fa-forward\"]")
	public WebElement move_icon;

	public boolean isvisiblemove_icon() {
		return objaction.fordisplay(move_icon);
	}

	// Tc 45
	@FindBy(xpath = "//i[@class=\"fas fa-home\"]")
	public WebElement home_icon;

	public boolean isvisiblehome_icon() {
		return objaction.fordisplay(home_icon);
	}

	// TC46
	@FindBy(xpath = "//i[@class=\"fas fa-question\"]")
	public WebElement help_icon;

	public boolean isvisiblehelp_icon() {
		return objaction.fordisplay(help_icon);
	}

	// TC47
	@FindBy(xpath = "//i[@class=\"fas fa-upload\"]")
	public WebElement bidaction_icon;

	public boolean isvisiblebidaction_icon() {
		return objaction.fordisplay(bidaction_icon);
	}

	// Tc48
	@FindBy(xpath = "//*[@id=\"navbarTogglerDemo03\"]/ul[1]/li[3]")
	public WebElement save_icon;

	public boolean checksaveisdisable() {
		String att = objaction.getAttribute(save_icon, "class");
		if (att.contains("disabled")) {
			WbidBasepage.logger.pass("Save icon is disable");
			return true;
		} else {
			WbidBasepage.logger.fail("Save icon is enable");
			return false;
		}
	}

	// Tc 49
	public boolean verifymovearrowclickable() {
		if (move_icon.isDisplayed() && move_icon.isEnabled()) {
			move_icon.click(); // Simulate user click
			System.out.println("Arrow button was clicked successfully.");
			return true;
		} else {
			System.out.println("Arrow button is not enabled or visible.");
			return false;
		}
	}

	// For line number from cover letter
	public String total;
	public String hard;
	public String reserve;
	public String blank;

	public void checklinenumber() {
		// Log selected values before proceeding
		WbidBasepage.logger.info("Selected Position: " + selectedPosition);
		WbidBasepage.logger.info("Selected Base: " + selectedBase);

		// Ensure selectedBase and selectedPosition are initialized
		if (selectedBase == null || selectedBase.trim().isEmpty() || selectedPosition == null
				|| selectedPosition.trim().isEmpty()) {
			System.out.println("⚠️ Error: Base or Position is empty. Please check inputs.");
			WbidBasepage.logger.fail("Base or Position is empty.");
			return;
		}

		// Convert to uppercase and normalize spaces
		selectedBase = selectedBase.trim().toUpperCase();
		selectedPosition = selectedPosition.trim().toUpperCase();

		// Special handling for "CP" -> "CA"
		if (selectedPosition.equalsIgnoreCase("CP")) {
			selectedPosition = "CA";
		}

		// Ensure content is available
		if (content == null || content.trim().isEmpty()) {
			System.out.println("⚠️ Error: Content is empty or null.");
			WbidBasepage.logger.fail("Content is empty or null.");
			return;
		}

		// Normalize content
		content = content.replaceAll("\\s+", " ").toUpperCase();

		// Regex to extract relevant numbers
		String regex = "\\b" + selectedBase + "\\s+" + selectedPosition
				+ "\\s+(\\d+)\\s+(?:\\d+[-])?(\\d+)\\s+\\d+-\\d+\\s+\\((\\d+)\\)\\s+\\d+-\\d+\\s+\\((\\d+)\\)";

		System.out.println("Regex: " + regex);
		WbidBasepage.logger.info("Regex: " + regex);

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		// Extract and log values
		if (matcher.find()) {
			total = matcher.group(1);
			hard = matcher.group(2);
			reserve = matcher.group(3);
			blank = matcher.group(4);

			String output = "Extracted numbers for " + selectedBase + " (" + selectedPosition + "):\n" + " 🔥 Total  : "
					+ total + "\n" + " 🔥 Hard   : " + hard + "\n" + " 🔥 Reserve: " + reserve + "\n" + " 🔥 Blank  : "
					+ blank;

			System.out.println(output);
			WbidBasepage.logger.pass(output);
		} else {
			System.out.println("No matching data found for " + selectedBase + " (" + selectedPosition + ").");
			WbidBasepage.logger.fail("No matching data found for " + selectedBase + " (" + selectedPosition + ").");
		}
	}

	@FindBy(xpath = "//h6[@class=\"scr-head\"]")
	public WebElement scratchpadlinenumber;
	public int extractedNumber;
	public int totalNumber;

	public String getreservevalue() {
		System.out.println(" Reserve value:" + reserve);
		return reserve;
	}
	public String getBlankvalue() {
		System.out.println(" Blank Lines:" + blank);
		return blank;
	}
	public boolean checkLineNumberFromScratchpad() {
		String text = objaction.gettext(scratchpadlinenumber);

		// Regex to find a number in the given text
		Pattern pattern = Pattern.compile("\\b(\\d+)\\b");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			extractedNumber = Integer.parseInt(matcher.group(1)); // Convert extracted number to int

			// Ensure `total` is not null before parsing
			if (total == null || total.trim().isEmpty()) {
				System.out.println("⚠️ Warning: `total` is null or empty. Skipping comparison.");
				return false;
			}

			totalNumber = Integer.parseInt(total.trim()); // Convert total to int

			System.out.println("Extracted Number: " + extractedNumber);
			System.out.println("Total: " + totalNumber);

			// Compare extracted number with totalNumber
			if (extractedNumber == totalNumber) {
				System.out.println("✅ Extracted number matches the total.");
				return true; // Return true if numbers match
			} else {
				System.out.println("❌ Extracted number does NOT match the total.");
				return false; // Return false if numbers do not match
			}
		} else {
			System.out.println("No number found.");
			return false; // Return false if no number is found
		}
	}

	////////// Check only FA condition
	public boolean checkDownloadBidFA() {
		WbidBasepage.logger
				.info("🎯 Verifying user can select appropriate rounds and positions with all applicable domiciles:");
		boolean isAnyDownloadAttempted = false;
		int currentDay = LocalDate.now().getDayOfMonth();
		int lastDayOfMonth = LocalDate.now().lengthOfMonth();

		// Check applicable conditions for FA only
		boolean firstRoundFA = (currentDay >= 2 && currentDay <= lastDayOfMonth)
				&& processDownloadFA("FA", "FA", firstround);

		boolean secondRoundFA = (currentDay >= 11 && currentDay <= lastDayOfMonth)
				&& processDownloadFA("FA", "FA", secondround);

		// Ensure all conditions are processed
		isAnyDownloadAttempted = firstRoundFA && secondRoundFA;

		if (!isAnyDownloadAttempted) {
			WbidBasepage.logger.info("❌ Not able to download bid data for all required conditions.");
		}

		return isAnyDownloadAttempted;
	}

	private boolean processDownloadFA(String pos1, String pos2, WebElement round) {
		boolean isDownloadAttempted = false;
		boolean exitLoop = false; // Flag to break the outer loop
		selectedBase = "";
		selectedPosition = "";

		WbidBasepage.logger.info("✅ Checking download attempt for " + pos1 + " and " + pos2);

		for (WebElement city : basecities) {
			if (exitLoop)
				break; // Break outer loop if needed

			String cityName = city.getText();

			WbidBasepage.logger.info("✨ Domicile Selected: " + cityName);
			objwait.waitForElemntTobeClickable(driver, city, 5);
			objaction.click(city);

			for (WebElement position : positionlist) {
				if (exitLoop)
					break; // Break inner loop if needed

				String pos = position.getText();

				if (pos.equalsIgnoreCase(pos1) || pos.equalsIgnoreCase(pos2)) {
					WbidBasepage.logger.info("✨ Position Selected: " + pos);
					objwait.waitForElemntTobeClickable(driver, position, 5);
					objaction.click(position);

					// Store selected base and position
					selectedBase = cityName;
					selectedPosition = pos;

					// Select round
					objwait.waitForElemntTobeClickable(driver, round, 5);
					objaction.click(round);
					WbidBasepage.logger.info("✅ Round selected for " + pos + " at domicile " + cityName);

					// Get next month in SHORT format (e.g., "Mar")
					formonthselection();

					// Click the download button
					WbidBasepage.logger.info("⬇️ Clicking the download button for " + cityName + " - " + pos);
					objaction.click(download_btn);

					if (objaction.fordisplay(popupokbtn)) {
						objwait.waitForElemntTobeClickable(driver, popupokbtn, 10);
						objaction.click(popupokbtn);
						objwait.waitForElemntTobeClickable(driver, warningpopupokbtn, 10);
						objaction.click(warningpopupokbtn);

						isDownloadAttempted = true;
					} else {
						WbidBasepage.logger.info("❌ No popup displayed, exiting the method.");
						objaction.fordisplay(loading);

						exitLoop = true; // Break both loops
						break;
					}
				}
			}
		}

		WbidBasepage.logger.info("📌 Final Selected Base: " + selectedBase);
		WbidBasepage.logger.info("📌 Final Selected Position: " + selectedPosition);

		return isDownloadAttempted;
	}
	// have to perform start over

	@FindBy(xpath = "//i[@class='fas fas fa-ellipsis-h']")
	public WebElement moreDot;

	@FindBy(xpath = "//li[@class='dropdown-item']/a[text()='Start Over']")
	public WebElement startOverBtn;

	@FindBy(xpath = "//button[text()='Yes']")
	public WebElement yesBtn;

	public void startOver() {
		objwait.waitForElementTobeVisible(driver, moreDot, 90);
		objaction.click(moreDot);
		objwait.waitForElementTobeVisible(driver, startOverBtn, 90);
		objaction.click(startOverBtn);
		objwait.waitForElementTobeVisible(driver, yesBtn, 90);
		objaction.click(yesBtn);
		objwait.waitForElementTobeVisible(driver, yesBtn, 90);
		objaction.click(yesBtn);
	}

	public boolean forvisibleellipisicon() {
		return objaction.fordisplay(moreDot);
	}

	public boolean checklinemovestobid() {
		if (extractedNumber == 0) {
			WbidBasepage.logger.pass("✅ Lines are moved");
			return true;
		} else {
			WbidBasepage.logger.fail("❌ Lines are not moved");
			return false;
		}
	}

	// Checking Reserve Lines
	@FindBy(xpath = "//div/span/small[text()=\"R\"]")
	public List<WebElement> reservelines;

	public String reserveCountUI;
	public boolean checkreservelines(String reserve) {
		String reserveCountUI = String.valueOf(reservelines.size());
		WbidBasepage.logger.info("💡Reserve lines: " + reserveCountUI);

		if (reserveCountUI.equals(reserve)) {
			System.out.println("Total Reserve Lines: " + reserveCountUI);
			WbidBasepage.logger.pass("✅ Total Reserve Lines: " + reserveCountUI);
			return true;
		} else {
			System.out.println("No Reserve Lines Found.");
			WbidBasepage.logger.fail("❌ No Reserve Lines Found.");
			return false;
		}
	}
	public boolean compareReserveLine(int reserveAPI, String reserveUI) {
	    if (String.valueOf(reserveAPI).equals(reserveUI)) {
	        System.out.println("✅ Total Reserve Lines API: " + reserveAPI + " | UI: " + reserveUI);
	        WbidBasepage.logger.pass("✅ Total Reserve Lines API: " + reserveAPI + " | UI: " + reserveUI);
	        return true;
	    } else {
	        System.out.println("No Reserve Lines Found.");
	        WbidBasepage.logger.fail("❌ No Reserve Lines Found. API: " + reserveAPI + " | UI: " + reserveUI);
	        return false;
	    }
	}

	
	
	@FindBy(xpath = "//span/small[text()=\"\"]")
	List<WebElement> blanklines;

	public boolean checkblankAPIline() {
		String count = String.valueOf(ScratchPadBlankReservedLines.blankcount);
		WbidBasepage.logger.info("💡Blank lines: " + count);

		if (count.equals(blank)) {
			System.out.println("Total Blank Lines: " + count);
			WbidBasepage.logger.pass("✅ Total Blank Lines: " + count);
			return true;
		} else {
			System.out.println("No Blank Lines Found.");
			WbidBasepage.logger.fail("❌ Blank Lines Found Mismatch." + count);
			return false;
		}
	}

	@FindBy(xpath = "//div/span/small")
	List<WebElement> linecount;
	@FindBy(xpath = "//td[contains(@class,\"ul-date seven-cols trip\")]")
	List<WebElement> tripday;
	@FindBy(xpath = "//div[@class=\"cala-view ng-star-inserted\"]")
	List<WebElement> calendar;

	public void linecount() {
		int count = linecount.size(); // Assuming 'linecount' is a list of WebElements
		WbidBasepage.logger.info("💡 Lines Count: " + count);

		for (int i = 1; i <= count; i++) {
			WbidBasepage.logger.info("💡 Value: " + i);

			boolean tripFound = false; // Flag to track if a trip is found
			int iterationCount = 0;
			for (WebElement option : tripday) {
				if (iterationCount >= 35) {
					WbidBasepage.logger.info("⏳ Skipping remaining days after 35 checks.");
					break; // Stop checking after 35 iterations
				}
				String att = objaction.getAttribute(option, "style");

				// Debug log to check what value is being retrieved
				WbidBasepage.logger.info("🔍 Retrieved 'style' attribute: " + (att.isEmpty() ? "EMPTY" : att));

				if (att != null && !att.isEmpty()
						&& (att.contains("background-color: rgb(220, 100, 5);")
								|| att.contains("background-color: rgb(255, 0, 0);")
								|| att.contains("background-color: rgb(185, 54, 16);")
								|| att.contains("background-color: rgb(17, 166, 124);"))) {

					WbidBasepage.logger.info("✅ This day contains the trip");
					tripFound = true;
					break; // Exit inner loop once a trip is found
				}
			}

			if (!tripFound) {
				WbidBasepage.logger.info("❌ This day does not have a trip");
			}
		}
	}

	// API Checking
	public boolean checkreserveAPIlines() {
		String count = String.valueOf(ScratchPadBlankReservedLines.reservecount);
		WbidBasepage.logger.info("💡Reserve lines: " + count);

		if (count.equals(reserve)) {
			System.out.println("Total Reserve Lines: " + count);
			WbidBasepage.logger.pass("✅ Total Reserve Lines: " + count);
			return true;
		} else {
			System.out.println("No Reserve Lines Found.");
			WbidBasepage.logger.fail("❌ No Reserve Lines Found.");
			return false;
		}
	}

	// LIne parameters
	@FindBy(xpath = "(//div[@class=\"cala-right\"])[1]")
	public WebElement lineparam;

	// TC 57
	@FindBy(xpath = "(//button[text()=\"No\"])[2]")
	public WebElement no_btn;

	public boolean checksavepopup() {
		objaction.click(home_icon);
		return objaction.fordisplay(no_btn);
	}

}
