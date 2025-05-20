package pages;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class DutyPeriodPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait=new WaitCondition();
	
	public DutyPeriodPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);/// initial page factory
		objaction = new ActionUtilities(driver);
	}
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
	//Get individual Duty hour in each trip

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
		
}
