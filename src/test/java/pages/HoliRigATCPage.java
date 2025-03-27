package pages;

import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class HoliRigATCPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();

	public HoliRigATCPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}

	// Line parameters
	@FindBy(xpath = "(//div[@class=\"cala-right\"])[1]")
	public WebElement lineparam;
	@FindBy(xpath = "//button[@class=\"btn resetbtn btn-sm\"]")
	public WebElement reset_btn;
	@FindBy(xpath = "//div[contains(@class,\"selected-col\")]")
	public List<WebElement> linevalues;
	@FindBy(xpath = "//label[text()=\" HoliRig \"]")
	public WebElement holirig;
	@FindBy(xpath = "//span[text()=\"HoliRig\"]")
	public WebElement scratchpadholirig;
	@FindBy(xpath = "//div[@class=\"checkbox-animated\"]/label")
	public List<WebElement> line;
	@FindBy(xpath = "//label[text()=\" Aircraft Changes \"]")
	public WebElement aircraft;

	public boolean forClickHoliRig() {
	    objaction.click(lineparam);
	    objwait.waitS(3000); // Wait for all elements to be available

	    WbidBasepage.logger.pass("Total Line Values Retrieved: " + linevalues.size());

	    for (WebElement option : linevalues) {
	        String text = objaction.gettext(option).trim();  // Trim spaces
	        WbidBasepage.logger.pass("🔹 Checking Line Value: [" + text + "]");

	        if (text.equalsIgnoreCase("HoliRig".trim())) {  // Case-insensitive check
	            String att = objaction.getAttribute(option, "class").trim();
	            WbidBasepage.logger.pass("🔹 Attribute Value: " + att);

	            if (att.contains("selected")) {
	                WbidBasepage.logger.pass("✅ Blue Tick displayed");
	                return true;
	            } else {
	                WbidBasepage.logger.fail("❌ Blue Tick not displayed");
	                return false;
	            }
	        }
	    }

	    WbidBasepage.logger.warning("⚠️ 'HoliRig' option not found in the list");
	    return false;
	}

	public boolean fordisplayholirig() {
		objwait.waitForElementTobeVisible(driver, holirig, 60);
		return objaction.fordisplay(scratchpadholirig);
	}

	public void pressEscapeKey() {
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.ESCAPE).perform();
		WbidBasepage.logger.info("Escape key pressed.");
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

}
