package pages;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class BidDownloadPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();

	public BidDownloadPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}

	@FindBy(xpath = "//nav[@class=\"navbar navbar-expand-sm navbar-light\"]/img")
	public WebElement logo;
	@FindBy(id = "navbardrop")
	public WebElement retrivedropdown;

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

	public boolean isNextMonthSelectedByDefault() {
		// Get next month in SHORT format (e.g., "Mar")
		String nextMonth = LocalDate.now().plusMonths(1).getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

		WbidBasepage.logger.info("Expected default selected month: " + nextMonth);

		// Check which month is selected in the dropdown
		for (WebElement option : monthlist) {
			String monthName = option.getText().trim();
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
//	@FindBy(xpath="(//button[text()=\" ATL \"])[1]")
//	public WebElement atl;
//	@FindBy(xpath="(//button[text()=\" CP \"])[1]")
//	public WebElement cp;
	@FindBy(xpath = "(//button[text()=\" 1st Round \"])[1]")
	public WebElement firstround;
//	@FindBy(xpath="(//button[text()=\" Mar \"])[1]")
//	public WebElement mar;
//	public boolean checkbiddownloadsteps() {
//		objaction.click(atl);
//		objaction.click(cp);
//		objaction.click(firstround);
//		
//	}
	// TC 21
	@FindBy(xpath = "//div[@class=\"swal2-html-container\"]")
	public WebElement warningpopup;

	/*********** Bid Download Cases *************/
	public void checkCondition1DownloadBid() {
		WbidBasepage.logger
				.info("🎯 Verifying user can select round 1 and pilot (CP & FO) with all applicable domiciles:");

		for (WebElement city : basecities) {
			String cityName = city.getText().trim();

			// ❌ Skip AUS and FLL for Pilot
			if (cityName.equalsIgnoreCase("AUS") || cityName.equalsIgnoreCase("FLL")) {
				WbidBasepage.logger.info("⚠️ Skipping domicile: " + cityName + " for Pilot as per scenario.");
				continue;
			}

			WbidBasepage.logger.info("✨ Domicile Selected: " + cityName);
			objwait.waitForElemntTobeClickable(driver, city, 5);
			objaction.click(city);

			for (WebElement position : positionlist) {
				String pos = position.getText().trim();

				// ✅ Select only Pilot positions (CP and FO)
				if (pos.equalsIgnoreCase("CP") || pos.equalsIgnoreCase("FO")) {
					WbidBasepage.logger.info("✨ Pilot Position Selected: " + pos);
					objwait.waitForElemntTobeClickable(driver, position, 5);
					objaction.click(position);

					// ✅ Select Round 1
					objwait.waitForElemntTobeClickable(driver, firstround, 5);
					objaction.click(firstround);
					WbidBasepage.logger.info("✅ Round 1 selected for " + pos + " at domicile " + cityName);
					// Get next month in SHORT format (e.g., "Mar")
					String nextMonth = LocalDate.now().plusMonths(1).getMonth().getDisplayName(TextStyle.SHORT,
							Locale.ENGLISH);

					WbidBasepage.logger.info("Expected default selected month: " + nextMonth);

					// Check which month is selected in the dropdown
					for (WebElement option : monthlist) {
						String monthName = option.getText().trim();
						String isSelected = option.getAttribute("selected"); // Or use appropriate attribute/class
						if ("true".equals(isSelected) || option.getAttribute("class").contains("active")) {
							WbidBasepage.logger.info("Currently selected month: " + monthName);
							if (monthName.equalsIgnoreCase(nextMonth)) {
								WbidBasepage.logger
										.pass("✅ Next month '" + nextMonth + "' is correctly selected by default.");
							} else {
								WbidBasepage.logger
										.fail("❌ Expected '" + nextMonth + "', but found '" + monthName + "'.");
							}
						}
					}
				}
			}
		}
	}

}
