package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class IndividualCredValuePage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public IndividualCredValuePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
//TC 3
	@FindBy(xpath = "//img[@class='manin-logo']")
	public WebElement logo;
	
	public boolean logoVisibile() {
		objwait.waitForElementTobeVisible(driver, logo, 30);
		boolean logoVisible = objaction.fordisplay(logo);
		WbidBasepage.logger.log(Status.INFO, "Logo of the application Visible: " + logoVisible);
		return logoVisible;
	}
//TC 4
	@FindBy(xpath = "//a[text()='Retrieve New BidData']")
	public WebElement retrievenewbiddata;
	@FindBy(xpath = "//a[text()='Retrieve Historical BidData']")
	public WebElement retrievehistoricalbiddata;

	public boolean isVisibleBiddata() {
		objwait.waitForElementTobeVisible(driver, retrievenewbiddata, 30);
		String txt1=objaction.gettext(retrievenewbiddata);
		String txt2=objaction.gettext(retrievehistoricalbiddata);
		boolean isVisible = objaction.fordisplay(retrievenewbiddata) && objaction.fordisplay(retrievehistoricalbiddata);
		WbidBasepage.logger.log(Status.INFO, "Retrive Button Visible: " + txt1+ " & "+txt2 );
		return isVisible;
	}
//TC 5
	@FindBy(xpath = "//h2[text()='Enter Employee Number']")
	public WebElement empNo;
	
	public String checkEmpnumHeader() {
		objwait.waitForElementTobeVisible(driver, empNo, 60);
		String headerName = empNo.getText().trim();
		WbidBasepage.logger.pass("popup appears with Header :"+headerName);
		return headerName;
	}
//TC 7
	@FindBy(xpath = "(//button[text()=' Download '])[1]")
	public WebElement downloadBtn;
	
	public boolean downloadEnable() {
		boolean isDownloadEnabled;
	if (downloadBtn.isEnabled()) {
		isDownloadEnabled = true;
		WbidBasepage.logger.pass("Download button is enabled :"+isDownloadEnabled);
	} else {
		isDownloadEnabled = false;
		WbidBasepage.logger.fail("Download button is NOT enabled ");
	}
	return isDownloadEnabled;
	}
//TC 8
		@FindBy(xpath = "//div[text()='Downloading Bid..']")
		public WebElement loading;

		public boolean displayloadingicon() {
			boolean isPresent =objaction.fordisplay(loading);
			WbidBasepage.logger.pass("Loading appears :"+isPresent);
			return isPresent;
		}
//TC 9
	@FindBy(xpath = "//h2[text()='Early Bid Warning']")
	public WebElement earlyBidPopup;
	
	public boolean visibleEarlyBidPopup() {
		objwait.waitForElementTobeVisible(driver, earlyBidPopup, 30);
		boolean isPopupPresent =earlyBidPopup.isDisplayed();
		WbidBasepage.logger.pass("popup appears :"+isPopupPresent);
		return isPopupPresent;
	}
	@FindBy(xpath = "//button[text()='Ok']")
	public WebElement okBtn;

	public void clickOkEarlyBid() {
		objwait.waitForElementTobeVisible(driver, okBtn, 60);
		objaction.click(okBtn);
	}
//TC 10
	@FindBy(xpath = "//div[text()='Bid Data is not available. Please try again later.']")
	public WebElement bidNotAvailable;
	
	public boolean bidNotAvailableBidPopup() {
		objwait.waitForElementTobeVisible(driver, bidNotAvailable, 30);
		boolean isPopupPresent =bidNotAvailable.isDisplayed();
		WbidBasepage.logger.pass("popup appears :"+isPopupPresent);
		return isPopupPresent;
	}
	@FindBy(xpath = "//button[text()='OK']")
	public WebElement bidNotAvailableokBtn;
	
	public void clickOkbidNotAvailable() {
		objwait.waitForElementTobeVisible(driver, bidNotAvailableokBtn, 60);
		objaction.click(bidNotAvailableokBtn);
	}
//TC 11
	@FindBy(xpath = "(//button[@class='submit-cancel'])[1]")
	public WebElement cancelBtn;
	
	public void clickcancelBtn() {
		objwait.waitForElementTobeVisible(driver, cancelBtn, 60);
		objaction.click(cancelBtn);
	}

// TC 15 Seniority List 
		@FindBy(xpath = "//h2[text()='Seniority List']")
		public WebElement seniorityHead;
	
		public String displaySeniority() {
			WbidBasepage.logger.pass("Seniority Pop Up header :"+objaction.gettext(seniorityHead));
			return objaction.gettext(seniorityHead);
		}
//TC 16
		@FindBy(xpath = "//h2[text()='Latest News']")
		public WebElement latestNwzHead;
		
		public String latestnewHeader() {
			objwait.waitForElementTobeVisible(driver, latestNwzHead, 70);
			String news = objaction.gettext(latestNwzHead);
			WbidBasepage.logger.pass("Latest News Pop Up header :"+news);
			return news;
		}
//TC 17
		@FindBy(xpath = "//h2[text()='Cover Letter']")
		public WebElement coverLetterHead;
		
		public String coverLetterHeader() {
			objwait.waitForElementTobeVisible(driver, coverLetterHead, 10);
			String news = objaction.gettext(coverLetterHead);
			WbidBasepage.logger.pass("Cover Letter Pop Up header :"+news);
			return news;
		}
}
