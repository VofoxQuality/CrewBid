package pages;

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
	public String pageHeading() {
		String actualHeading = driver.getTitle();
		System.out.println(actualHeading);
		return actualHeading;
	}
//TC 2
	@FindBy(xpath = "//*[@data-placeholder='Employee Number']")
	public WebElement empNo;
	
	public boolean visibilityofusernamebox() {
		objwait.waitForElementTobeVisible(driver, empNo, 10);
		return empNo.isDisplayed();
	}
//TC 3
	@FindBy(xpath = "//input[@id='Password']")
	public WebElement password;
	
	public boolean visibilityofpasswordbox() {
		objwait.waitForElementTobeVisible(driver, password,30);
		return password.isDisplayed();
	}
//TC 4
	@FindBy(xpath = "//*[@id='loginbtn']")
	public WebElement login;

	public void login() {
		objaction.sendkey(empNo, String.valueOf((int) Double.parseDouble(WbidBasepage.username)));
		WbidBasepage.logger.log(Status.PASS, "Enter Employee Number Details Passed");
		password.click();
		password.clear();
		password.sendKeys(WbidBasepage.password);
		WbidBasepage.logger.log(Status.PASS, "Enter Password Details passed");
		objaction.JavaScriptclick(login);
		WbidBasepage.logger.log(Status.PASS, "Login Successfully");

	}
}
