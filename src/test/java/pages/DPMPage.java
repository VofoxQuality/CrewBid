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

public class DPMPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public DPMPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
////DPM-line parameter ////
	
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

		@FindBy(xpath = "(//*[@class='checkbox-animated'])[45]")
		public WebElement DPMLineBtn;

		public void clickDPMLineBtn() {
			objaction.scrollToElement(DPMLineBtn);
			objwait.waitForElementTobeVisible(driver, DPMLineBtn, 90);
			objaction.click(DPMLineBtn);
		}

		public void selectDPMLine() {
			clickLineValue();// open lines modal
			clickResetLinevalues();// Reset line values so by default 5 default lines selected
			clickAirCrftLineBtn();// uncheck Air Craft changes line
			clickDPMLineBtn();// check DPM
			// Close modal properly
			try {
				Actions actions = new Actions(driver);
				actions.sendKeys(Keys.ESCAPE).perform();
			} catch (Exception e) {
				WbidBasepage.logger.info("Modal close action failed, continuing.");
			}
		}

		@FindBy(xpath = "//*[text()='rigDPM']")
		public WebElement DPMTxt;

		@FindBy(xpath = "//*[text()='rigDPM']/following-sibling::span")
		public List<WebElement> DPMLineVal;
		
		public boolean fordisplayDPM() {
			objwait.waitForElementTobeVisible(driver, DPMTxt, 60);
			return objaction.fordisplay(DPMTxt);
		}
		
		//Get DPM data  from UI
		public List<Map<String, Object>> DPMLineValueUI = new ArrayList<>();

		public boolean getDPMLineVal() {
		    try {
		        objwait.waitForElementTobeVisible(driver, DPMTxt, 90);

		        DPMLineValueUI.clear(); // Clear previous data before populating new

		        if (!DPMLineVal.isEmpty()) {
		            for (int i = 0; i < DPMLineVal.size(); i++) {
		                String DPMValue = DPMLineVal.get(i).getText().trim();
		                WbidBasepage.logger.info("Lines: " + (i + 1) + " DPMLine Value: " + DPMValue);

		                Map<String, Object> map = new LinkedHashMap<>();
		                map.put("Lines", i + 1);
		                map.put("DPM", DPMValue);
		                DPMLineValueUI.add(map);
		            }

		            WbidBasepage.logger.info("DPM Line Parameter UI with Lines: " + DPMLineValueUI);
		            return true;
		        } else {
		            WbidBasepage.logger.fail("No DPM Line Parameter values found!");
		            return false;
		        }
		    } catch (Exception e) {
		        WbidBasepage.logger.fail("Exception while retrieving DPM Line Parameter values: " + e.getMessage());
		        return false;
		    }
		}

		// Compare DPM data from UI and API
		public boolean isDPMLineValCompare(List<Map<String, Object>> dpmLineAPI) {
		    boolean allMatch = true;

		    if (dpmLineAPI.size() != DPMLineValueUI.size()) {
		        WbidBasepage.logger.fail("Mismatch in DPM data size! API size: " + dpmLineAPI.size() + ", UI size: " + DPMLineValueUI.size());
		        allMatch = false;
		    } else {
		        WbidBasepage.logger.info("API and UI DPM data sizes match. API size: " + dpmLineAPI.size() + " UI size: " + DPMLineValueUI.size());
		    }

		    int loopSize = Math.min(dpmLineAPI.size(), DPMLineValueUI.size());

		    for (int i = 0; i < loopSize; i++) {
		        Map<String, Object> apiData = dpmLineAPI.get(i);
		        Map<String, Object> uiData = DPMLineValueUI.get(i);

		        int apiLine = convertToInteger(apiData.get("Lines"));
		        int uiLine = convertToInteger(uiData.get("Lines"));

		        Double apiValue = convertToDouble(apiData.get("RigDailyMinInLine"));
		        Double uiValue = convertToDouble(uiData.get("DPM"));

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
		        WbidBasepage.logger.info("API and UI DPM Line data match perfectly!");
		    } else {
		        WbidBasepage.logger.fail("One or more mismatches found in DPM Line data.");
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

