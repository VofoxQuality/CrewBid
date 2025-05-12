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

public class DHRPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public DHRPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
////DHR-line parameter ////
	
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

		@FindBy(xpath = "(//*[@class='checkbox-animated'])[44]")
		public WebElement DHRLineBtn;

		public void clickDHRLineBtn() {
			objaction.scrollToElement(DHRLineBtn);
			objwait.waitForElementTobeVisible(driver, DHRLineBtn, 90);
			objaction.click(DHRLineBtn);
		}

		public void selectDHRLine() {
			clickLineValue();// open lines modal
			clickResetLinevalues();// Reset line values so by default 5 default lines selected
			clickAirCrftLineBtn();// uncheck Air Craft changes line
			clickDHRLineBtn();// check DHR
			// Close modal properly
			try {
				Actions actions = new Actions(driver);
				actions.sendKeys(Keys.ESCAPE).perform();
			} catch (Exception e) {
				WbidBasepage.logger.info("Modal close action failed, continuing.");
			}
		}

		@FindBy(xpath = "//*[text()='rigDHR']")
		public WebElement DHRTxt;

		@FindBy(xpath = "//*[text()='rigDHR']/following-sibling::span")
		public List<WebElement> DHRLineVal;
		
		public boolean fordisplayDHR() {
			objwait.waitForElementTobeVisible(driver, DHRTxt, 60);
			return objaction.fordisplay(DHRTxt);
		}
		
		//Get DHR data  from UI
		public List<Map<String, Object>> DHRLineValueUI = new ArrayList<>();

		public boolean getDHRLineVal() {
		    try {
		        objwait.waitForElementTobeVisible(driver, DHRTxt, 90);

		        DHRLineValueUI.clear(); // Clear previous data before populating new

		        if (!DHRLineVal.isEmpty()) {
		            for (int i = 0; i < DHRLineVal.size(); i++) {
		                String DHRValue = DHRLineVal.get(i).getText().trim();
		                WbidBasepage.logger.info("Lines: " + (i + 1) + " DHRLine Value: " + DHRValue);

		                Map<String, Object> map = new LinkedHashMap<>();
		                map.put("Lines", i + 1);
		                map.put("DHR", DHRValue);
		                DHRLineValueUI.add(map);
		            }

		            WbidBasepage.logger.info("DHR Line Parameter UI with Lines: " + DHRLineValueUI);
		            return true;
		        } else {
		            WbidBasepage.logger.fail("No DHR Line Parameter values found!");
		            return false;
		        }
		    } catch (Exception e) {
		        WbidBasepage.logger.fail("Exception while retrieving DHR Line Parameter values: " + e.getMessage());
		        return false;
		    }
		}

		// Compare DHR data from UI and API
		public boolean isDHRLineValCompare(List<Map<String, Object>> dhrLineAPI) {
		    boolean allMatch = true;

		    if (dhrLineAPI.size() != DHRLineValueUI.size()) {
		        WbidBasepage.logger.fail("Mismatch in DHR data size! API size: " + dhrLineAPI.size() + ", UI size: " + DHRLineValueUI.size());
		        allMatch = false;
		    } else {
		        WbidBasepage.logger.info("API and UI DHR data sizes match. API size: " + dhrLineAPI.size() + " UI size: " + DHRLineValueUI.size());
		    }

		    int loopSize = Math.min(dhrLineAPI.size(), DHRLineValueUI.size());

		    for (int i = 0; i < loopSize; i++) {
		        Map<String, Object> apiData = dhrLineAPI.get(i);
		        Map<String, Object> uiData = DHRLineValueUI.get(i);

		        int apiLine = convertToInteger(apiData.get("Lines"));
		        int uiLine = convertToInteger(uiData.get("Lines"));

		        Double apiValue = convertToDouble(apiData.get("RigDhrInLine"));
		        Double uiValue = convertToDouble(uiData.get("DHR"));

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
		        WbidBasepage.logger.info("API and UI DHR Line data match perfectly!");
		    } else {
		        WbidBasepage.logger.fail("One or more mismatches found in DHR Line data.");
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
