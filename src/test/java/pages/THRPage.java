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

public class THRPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public THRPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
////THR-line parameter ////
	
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

		@FindBy(xpath = "(//*[@class='checkbox-animated'])[46]")
		public WebElement THRLineBtn;

		public void clickTHRLineBtn() {
			objaction.scrollToElement(THRLineBtn);
			objwait.waitForElementTobeVisible(driver, THRLineBtn, 90);
			objaction.click(THRLineBtn);
		}

		public void selectTHRLine() {
			clickLineValue();// open lines modal
			clickResetLinevalues();// Reset line values so by default 5 default lines selected
			clickAirCrftLineBtn();// uncheck Air Craft changes line
			clickTHRLineBtn();// check THR
			// Close modal properly
			try {
				Actions actions = new Actions(driver);
				actions.sendKeys(Keys.ESCAPE).perform();
			} catch (Exception e) {
				WbidBasepage.logger.info("Modal close action failed, continuing.");
			}
		}

		@FindBy(xpath = "//*[text()='rigTHR']")
		public WebElement THRTxt;

		@FindBy(xpath = "//*[text()='rigTHR']/following-sibling::span")
		public List<WebElement> THRLineVal;
		
		public boolean fordisplayTHR() {
			objwait.waitForElementTobeVisible(driver, THRTxt, 60);
			return objaction.fordisplay(THRTxt);
		}
		
		//Get THR data  from UI
		public List<Map<String, Object>> THRLineValueUI = new ArrayList<>();

		public boolean getTHRLineVal() {
		    try {
		        objwait.waitForElementTobeVisible(driver, THRTxt, 90);

		        THRLineValueUI.clear(); // Clear previous data before populating new

		        if (!THRLineVal.isEmpty()) {
		            for (int i = 0; i < THRLineVal.size(); i++) {
		                String THRValue = THRLineVal.get(i).getText().trim();
		                WbidBasepage.logger.info("Lines: " + (i + 1) + " THRLine Value: " + THRValue);

		                Map<String, Object> map = new LinkedHashMap<>();
		                map.put("Lines", i + 1);
		                map.put("THR", THRValue);
		                THRLineValueUI.add(map);
		            }

		            WbidBasepage.logger.info("THR Line Parameter UI with Lines: " + THRLineValueUI);
		            return true;
		        } else {
		            WbidBasepage.logger.fail("No THR Line Parameter values found!");
		            return false;
		        }
		    } catch (Exception e) {
		        WbidBasepage.logger.fail("Exception while retrieving THR Line Parameter values: " + e.getMessage());
		        return false;
		    }
		}

		// Compare THR data from UI and API
		public boolean isTHRLineValCompare(List<Map<String, Object>> thrLineAPI) {
		    boolean allMatch = true;

		    if (thrLineAPI.size() != THRLineValueUI.size()) {
		        WbidBasepage.logger.fail("Mismatch in THR data size! API size: " + thrLineAPI.size() + ", UI size: " + THRLineValueUI.size());
		        allMatch = false;
		    } else {
		        WbidBasepage.logger.info("API and UI THR data sizes match. API size: " + thrLineAPI.size() + " UI size: " + THRLineValueUI.size());
		    }

		    int loopSize = Math.min(thrLineAPI.size(), THRLineValueUI.size());

		    for (int i = 0; i < loopSize; i++) {
		        Map<String, Object> apiData = thrLineAPI.get(i);
		        Map<String, Object> uiData = THRLineValueUI.get(i);

		        int apiLine = convertToInteger(apiData.get("Lines"));
		        int uiLine = convertToInteger(uiData.get("Lines"));

		        Double apiValue = convertToDouble(apiData.get("RigTafbInBp"));
		        Double uiValue = convertToDouble(uiData.get("THR"));

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
		        WbidBasepage.logger.info("API and UI THR Line data match perfectly!");
		    } else {
		        WbidBasepage.logger.fail("One or more mismatches found in THR Line data.");
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


