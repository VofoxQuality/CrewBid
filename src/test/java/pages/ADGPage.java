package pages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class ADGPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public ADGPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
////ADG-line parameter ////
	
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

		@FindBy(xpath = "(//*[@class='checkbox-animated'])[43]")
		public WebElement ADGLineBtn;

		public void clickADGLineBtn() {
			objaction.scrollToElement(ADGLineBtn);
			objwait.waitForElementTobeVisible(driver, ADGLineBtn, 90);
			objaction.click(ADGLineBtn);
		}

		public void selectADGLine() {
			clickLineValue();// open lines modal
			clickResetLinevalues();// Reset line values so by default 5 default lines selected
			clickAirCrftLineBtn();// uncheck Air Craft changes line
			clickADGLineBtn();// check ADG
			// Close modal properly
			try {
				Actions actions = new Actions(driver);
				actions.sendKeys(Keys.ESCAPE).perform();
			} catch (Exception e) {
				WbidBasepage.logger.info("Modal close action failed, continuing.");
			}
		}

		@FindBy(xpath = "//*[text()='rigADG']")
		public WebElement ADGTxt;

		@FindBy(xpath = "//*[text()='rigADG']/following-sibling::span")
		public List<WebElement> ADGLineVal;
		
		public boolean fordisplayADG() {
			objwait.waitForElementTobeVisible(driver, ADGTxt, 60);
			return objaction.fordisplay(ADGTxt);
		}
		
		//Get ADG data  from UI
		public List<Map<String, Object>> ADGLineValueUI = new ArrayList<>();

		public boolean getADGLineVal() {
		    try {
		        objwait.waitForElementTobeVisible(driver, ADGTxt, 90);

		        ADGLineValueUI.clear(); // Clear previous data before populating new

		        if (!ADGLineVal.isEmpty()) {
		            for (int i = 0; i < ADGLineVal.size(); i++) {
		                String ADGValue = ADGLineVal.get(i).getText().trim();
		                WbidBasepage.logger.info("Lines: " + (i + 1) + " ADGLine Value: " + ADGValue);

		                Map<String, Object> map = new LinkedHashMap<>();
		                map.put("Lines", i + 1);
		                map.put("ADG", ADGValue);
		                ADGLineValueUI.add(map);
		            }

		            WbidBasepage.logger.info("ADG Line Parameter UI with Lines: " + ADGLineValueUI);
		            return true;
		        } else {
		            WbidBasepage.logger.fail("No ADG Line Parameter values found!");
		            return false;
		        }
		    } catch (Exception e) {
		        WbidBasepage.logger.fail("Exception while retrieving ADG Line Parameter values: " + e.getMessage());
		        return false;
		    }
		}

		// Compare ADG data from UI and API
		public boolean isADGLineValCompare(List<Map<String, Object>> adgLineAPI) {
		    boolean allMatch = true;

		    if (adgLineAPI.size() != ADGLineValueUI.size()) {
		        WbidBasepage.logger.fail("Mismatch in ADG data size! API size: " + adgLineAPI.size() + ", UI size: " + ADGLineValueUI.size());
		        allMatch = false;
		    } else {
		        WbidBasepage.logger.info("API and UI ADG data sizes match. API size: " + adgLineAPI.size() + " UI size: " + ADGLineValueUI.size());
		    }

		    int loopSize = Math.min(adgLineAPI.size(), ADGLineValueUI.size());

		    for (int i = 0; i < loopSize; i++) {
		        Map<String, Object> apiData = adgLineAPI.get(i);
		        Map<String, Object> uiData = ADGLineValueUI.get(i);

		        int apiLine = convertToInteger(apiData.get("Lines"));
		        int uiLine = convertToInteger(uiData.get("Lines"));

		        Double apiValue = convertToDouble(apiData.get("RigAdgInLine"));
		        Double uiValue = convertToDouble(uiData.get("ADG"));

		        if (apiLine != uiLine || Math.abs(apiValue - uiValue) > 0.01) {
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
		        WbidBasepage.logger.info("API and UI ADG Line data match perfectly!");
		    } else {
		        WbidBasepage.logger.fail("One or more mismatches found in ADG Line data.");
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

		private double convertToDouble(Object obj) {
		    try {
		        return Double.parseDouble(String.valueOf(obj).trim());
		    } catch (Exception e) {
		        return 0.0;
		    }
		}

	}

