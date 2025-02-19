package pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import com.aventstack.extentreports.Status;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class LoginPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
//TC 1
	public String pageHeading() {
		String actualHeading = driver.getTitle();
		return actualHeading;
	}
	@FindBy(xpath = "//h2[text()='Welcome to CrewBid!']")
	public WebElement welcomeHead;
	
	public String welcomeMssg() {
		String actualHeading = objaction.gettext(welcomeHead);
		return actualHeading;
	}
	
//Emp No and Password
	@FindBy(xpath = "//*[@data-placeholder='Employee Number']")
	public WebElement empNo;
	@FindBy(xpath = "//input[@id='Password']")
	public WebElement password;
//Login
	@FindBy(xpath = "//*[@id='loginbtn']")
	public WebElement login;

	public void login() {
		objwait.waitForElementTobeVisible(driver, empNo, 60);
		objaction.sendkey(empNo, String.valueOf((int) Double.parseDouble(WbidBasepage.username)));
		WbidBasepage.logger.log(Status.PASS, "Enter Employee Number Details Passed");
		password.click();
		password.clear();
		password.sendKeys(WbidBasepage.password);
		WbidBasepage.logger.log(Status.PASS, "Enter Password Details passed");
		objaction.JavaScriptclick(login);
		WbidBasepage.logger.log(Status.PASS, "Login Successfully");
	}
//TC 2 Version update 
	@FindBy(xpath = "//*[contains(text(),'You do not have the newest Version of Crewbid')]")
	public List<WebElement> versionPopup;
	
	public boolean visibilityversionPopup() {
		WbidBasepage.logger.log(Status.INFO, "Check Version Pop up ");
		return !versionPopup.isEmpty();
	}
	@FindBy(xpath = "//button[text()='Update Now']")
	public WebElement versnUpdateBtn;
	
	public boolean visibilityVersnUpdateBtn() {
		objwait.waitForElementTobeVisible(driver, versnUpdateBtn,30);
		return versnUpdateBtn.isDisplayed();
	}
	
// Version update Login method	
	public void updateVersionLogin() {
	    if (!versionPopup.isEmpty()) {
	        WebElement popupMessage = versionPopup.get(0); // Get the first matching element
	        objwait.waitForElementTobeVisible(driver, popupMessage, 30);
	        objaction.click(versnUpdateBtn);
	        WbidBasepage.logger.log(Status.PASS, "Version update initiated.");
	        login(); // Proceed with login
	    } else {
	        login(); // Proceed with login if the popup is not present
	    }
	}
	@FindBy(xpath = "//*[contains(@id,'navbarToggler')]/ul[2]/li")
	public WebElement homeTxt;
	
	public boolean gettexthome() {
		objwait.waitForElementTobeVisible(driver, homeTxt, 30);
		String hometext = objaction.gettext(homeTxt);
		WbidBasepage.logger.log(Status.INFO, "Get Home Text and version :  " + hometext);
		return hometext.contains("Home");
	}
//TC 5 Logout
	@FindBy(xpath = "//a[text()='Logout']")
	public WebElement logoutBtn;
	
	@FindBy(xpath = "//button[text()='Yes']")
	public WebElement yesLogout;
	
	@FindBy(xpath = "//p[contains(text(),'The premiere bidding software for Pilots and Flight Attendants of Southwest Airlines')]")
	public WebElement loginLbl;
	
	public boolean toperformlogout() {		
		// perform Logout functionality
		objwait.waitForElementTobeVisible(driver, logoutBtn, 30);
		WbidBasepage.logger.info("click Logout Button");
		objaction.click(logoutBtn);
		WbidBasepage.logger.info("click Ok");
		objwait.waitForElementTobeVisible(driver, yesLogout, 30);
		objaction.click(yesLogout);
		WbidBasepage.logger.info("*Assert CrewBid Login Label-The premiere bidding software for Pilots and Flight Attendants of Southwest Airlines.");
		objwait.waitForElementTobeVisible(driver, loginLbl, 30);
		boolean loginLabel=objaction.fordisplay(loginLbl);		
		return loginLabel;
	}
//TC 6
	@FindBy(xpath = "//*[@class='log-logo']/img")
	public WebElement logo;
	
	public boolean logoVisibile() {		
		objwait.waitForElementTobeVisible(driver, logo, 30);
		boolean logo=objaction.fordisplay(loginLbl);
		WbidBasepage.logger.log(Status.INFO, "Logo of the application Visible: " + logo);
		return logo;
	}
//TC 7
	@FindBy(xpath = "//h5[text()='Sign into your account']")
	public WebElement signInLbl;
	
	@FindBy(xpath = "(//p[@class='login-dis'])[1]")
	public WebElement signInDis;
	
	public boolean signInLblVisibile() {		
		objwait.waitForElementTobeVisible(driver, signInLbl, 30);
		boolean lbl=objaction.fordisplay(signInLbl)&&objaction.fordisplay(signInDis);
		WbidBasepage.logger.log(Status.INFO, "signin Label of the application Visible: " + lbl);
		String text = objaction.gettext(signInDis);
		WbidBasepage.logger.log(Status.INFO, "signin Discription: " + text);
		return lbl;
	}
//TC 8
	public boolean visibilityEmpNobox() {
		objwait.waitForElementTobeVisible(driver, empNo, 30);
		WbidBasepage.logger.log(Status.INFO, "Employee No txt Box Visible: " + empNo.isDisplayed());
		return empNo.isDisplayed();
	}
	@FindBy(xpath = "(//div[contains(@class,'mat-form-field-wrapper')])[2]")
	public WebElement passTxtBx;
//TC 9
	public boolean visibilityofPassbox() {
		passTxtBx.click();
		boolean visibile=objaction.fordisplay(passTxtBx);
		WbidBasepage.logger.log(Status.INFO, "Password txt Box Visible: " + visibile);
		return visibile;
	}
//TC 10
	public boolean visibilityLoginBtn() {
		objwait.waitForElementTobeVisible(driver, login, 30);
		WbidBasepage.logger.log(Status.INFO, "Employee No txt Box Visible: " + login.isDisplayed());
		return login.isDisplayed();
	}
}
