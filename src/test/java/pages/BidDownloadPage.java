package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;

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
	@FindBy(xpath="//a[text()=\"Retrieve Historical BidData\"]")
	public WebElement retrievehistoricalbiddata;

	public boolean fordisplaynewbiddata() {
		return objaction.fordisplay(retrievenewbiddata)&& objaction.fordisplay(retrievehistoricalbiddata);
	}
	@FindBy(xpath="")
	public WebElement oopsOkbtn;

}
