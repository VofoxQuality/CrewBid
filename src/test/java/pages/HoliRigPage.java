package pages;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class HoliRigPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public List<Map<String, Object>> holirigValueUI = new ArrayList<>();

	public HoliRigPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}

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

	@FindBy(xpath = "(//*[@class='checkbox-animated'])[26]")
	public WebElement holirigLineBtn;

	public void clickHolirigLineBtn() {
		objaction.scrollToElement(holirigLineBtn);
		objwait.waitForElementTobeVisible(driver, holirigLineBtn, 90);
		objaction.click(holirigLineBtn);
	}

	public void selectHoliRig() {
		clickLineValue();// open lines modal
		clickResetLinevalues();// Reset line values so by default 5 default lines selected
		clickAirCrftLineBtn();// uncheck Air Craft changes line
		clickHolirigLineBtn();// check HoliRig
		// Close modal properly
		try {
			Actions actions = new Actions(driver);
			actions.sendKeys(Keys.ESCAPE).perform();
		} catch (Exception e) {
			WbidBasepage.logger.info("Modal close action failed, continuing.");
		}
	}

	@FindBy(xpath = "//*[text()='HoliRig']")
	public WebElement holiRigTxt;

	@FindBy(xpath = "//*[text()='HoliRig']/following-sibling::span")
	public List<WebElement> holiRigVal;

//Get holirig data  from UI

	public void getHoliRigVal() {
		objwait.waitForElementTobeVisible(driver, holiRigTxt, 90);

		// Ensure holiRigVal is not empty to avoid unnecessary processing
		if (!holiRigVal.isEmpty()) {
			for (int i = 0; i < holiRigVal.size(); i++) { // FIXED: Use '<' instead of '<='
				String holiRigValue = holiRigVal.get(i).getText().trim(); // Get HoliRig value
				// WbidBasepage.logger.info("Lines: " + (i + 1) + " HolRig Value: " +
				// holiRigValue);

				// Store values in a Map
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("Lines", i + 1); // Line number
				map.put("HolRig", holiRigValue); // HoliRig value
				holirigValueUI.add(map);
			}

			// Print results to verify
			System.out.println(holirigValueUI);
			WbidBasepage.logger.info("Holirig UI with Lines: " + holirigValueUI);
		} else {
			WbidBasepage.logger.fail("No HoliRig values found!");
		}
	}

//Compare holirig data  from UI	 and API
	public boolean isHoliRigDataMatching(List<Map<String, Object>> result) {
		if (result.size() != holirigValueUI.size()) {
			WbidBasepage.logger
					.fail("Mismatch in data size! API size: " + result.size() + ", UI size: " + holirigValueUI.size());
			return false;
		}
		WbidBasepage.logger
				.info("API and UI data sizes match. API size: " + result.size() + " UI size: " + holirigValueUI.size());

		DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Ensures consistent formatting (e.g., "0.0" -> "0")

		for (int i = 0; i < result.size(); i++) {
			Map<String, Object> apiData = result.get(i);
			Map<String, Object> uiData = holirigValueUI.get(i);

			// Convert "Lines" value safely to Integer
			int apiLine = convertToInteger(apiData.get("Lines"));
			int uiLine = convertToInteger(uiData.get("Lines"));

			// Normalize "HolRig" values
			String apiValue = normalizeValue(apiData.get("HolRig"), decimalFormat);
			String uiValue = normalizeValue(uiData.get("HolRig"), decimalFormat);

			if (apiLine != uiLine || !apiValue.equals(uiValue)) {
				WbidBasepage.logger.fail("Mismatch at Line " + (i + 1) + " | API -> Line: " + apiLine + ", Value: "
						+ apiValue + " | UI -> Line: " + uiLine + ", Value: " + uiValue);
				return false;
			} else
				WbidBasepage.logger.pass("API -> Line: " + apiLine + ", Value: " + apiValue + " UI -> Line: " + uiLine
						+ ", Value: " + uiValue);
		}

		WbidBasepage.logger.info("API and UI HoliRig data match perfectly!");
		return true;
	}

	/**
	 * Utility method to safely convert Object to Integer.
	 */
	private int convertToInteger(Object value) {
		if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof String) {
			try {
				return Integer.parseInt((String) value);
			} catch (NumberFormatException e) {
				WbidBasepage.logger.fail("Invalid number format: " + value);
			}
		}
		return -1; // Return an invalid number if conversion fails
	}

	/**
	 * Utility method to normalize numeric values.
	 */
	private String normalizeValue(Object value, DecimalFormat decimalFormat) {
		if (value instanceof Number) {
			return decimalFormat.format(value); // Format numbers (e.g., 0.0 -> 0)
		} else if (value instanceof String) {
			try {
				double num = Double.parseDouble((String) value);
				return decimalFormat.format(num);
			} catch (NumberFormatException e) {
				return ((String) value).trim(); // Return original string if it's not a number
			}
		}
		return String.valueOf(value).trim();
	}

}
