package pages;

import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
		objwait.implicitWait(driver);
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
	@FindBy(xpath = "//input[@data-placeholder='CWA Password']")
	public WebElement passTxtBx;
//TC 9
	public boolean visibilityofPassbox() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    boolean visible = (Boolean) js.executeScript("return arguments[0].offsetParent !== null && window.getComputedStyle(arguments[0]).display !== 'none';", passTxtBx);
	    WbidBasepage.logger.log(Status.INFO, "Password txt Box Visible: " + visible);
	    return visible;
	}
//TC 10
	public boolean visibilityLoginBtn() {
		objwait.waitForElementTobeVisible(driver, login, 30);
		WbidBasepage.logger.log(Status.INFO, "Employee No txt Box Visible: " + login.isDisplayed());
		return login.isDisplayed();
	}
//TC 11
	public String empNoPlaceholder() {
		String txt = objaction.getAttribute(empNo, "data-placeholder");
		WbidBasepage.logger.pass("placeholder: " + txt);
		return txt;
	}
//TC 12
	public String pswdPlaceholder() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("arguments[0].click();", password); // Click using JS
	    String txt = (String) js.executeScript("return arguments[0].getAttribute('data-placeholder');", passTxtBx);
	    WbidBasepage.logger.pass("placeholder: " + txt);
	    return txt;
	}

//TC 13
	@FindBy(xpath = "//i[@class='fa fa-eye-slash']")
	public WebElement eyeBtn;
	
	public boolean visibilityeyeBtn() {
		objwait.waitForElementTobeVisible(driver, eyeBtn, 30);
		WbidBasepage.logger.log(Status.INFO, "Eye Button Visible: " + eyeBtn.isDisplayed());
		return login.isDisplayed();
	}
//TC 14
	@FindBy(xpath = "//p[text()='Crewbid-iPad']/following::a[1]")
	public WebElement link;
	
	public void clickLink() {
		objaction.scrolldown(driver);
		objwait.waitForElementTobeVisible(driver, link, 30);
		WbidBasepage.logger.log(Status.INFO, " Click on the hyperlink available in the login page of the application (crewbid ipad )");
		objaction.JavaScriptclick(link);
	}
	public boolean linkNavi() {
	    objaction.switchToWindowByIndex(1);
	    objwait.waitS(2000);
	    // Replace non-breaking spaces with regular spaces and trim whitespace
	    String actualTitle = pageHeading().replace("\u00A0", " ").trim();                                          
	    String expectedTitle = "CrewBid on the App Store".trim();
	    WbidBasepage.logger.info("Captured title of the page: " + actualTitle);
	    WbidBasepage.logger.info("Expected title: " + expectedTitle);
	    boolean isNavigated = actualTitle.equals(expectedTitle);
	    if (isNavigated) {
	        WbidBasepage.logger.info("Navigation to the App Store was successful.");
	    } else {
	        WbidBasepage.logger.fail("Navigation failed: Expected [" + expectedTitle + "] but found [" + actualTitle + "]");
	    }
	    // Switch back to the main window regardless of the result and close extra tabs
	    objaction.switchToWindowByIndex(0);
	    objaction.CheckExtraTabs();
	    return isNavigated;
	}
//TC 15
	@FindBy(xpath = "//p[text()='Crewbid2']/following::a[1]")
	public WebElement iPadLink;
	
	public void clickiPadLink() {
		objaction.scrolldown(driver);
		objwait.waitForElementTobeVisible(driver, iPadLink, 30);
		WbidBasepage.logger.log(Status.INFO, " Click on the hyperlink available in the login page of the application (crewbid ipad )");
		objaction.click(iPadLink);;
	}
	public boolean hyperLinkNavi() {
	    objaction.switchToWindowByIndex(1);
	    objwait.waitS(2000);
	    // Replace non-breaking spaces with regular spaces and trim whitespace
	    String actualTitle = pageHeading().replace("\u00A0", " ").trim();                                          
	    String expectedTitle = "CrewBid2 on the App Store".trim();
	    WbidBasepage.logger.info("Captured title of the page: " + actualTitle);
	    WbidBasepage.logger.info("Expected title: " + expectedTitle);
	    boolean isNavigated = actualTitle.equals(expectedTitle);
	    if (isNavigated) {
	        WbidBasepage.logger.info("Navigation to the App Store was successful.");
	    } else {
	        WbidBasepage.logger.fail("Navigation failed: Expected [" + expectedTitle + "] but found [" + actualTitle + "]");
	    }
	    // Switch back to the main window regardless of the result and close extra tabs
	    objaction.switchToWindowByIndex(0);
	    objaction.CheckExtraTabs();
	    return isNavigated;
	}
//TC 16
		@FindBy(xpath = "//p[text()='CrewbidValet']/following::a[1]")
		public WebElement valetLink;
		
		public void clickvaletLink() {
			objaction.scrolldown(driver);
			objwait.waitForElementTobeVisible(driver, valetLink, 30);
			WbidBasepage.logger.log(Status.INFO, " Click on the hyperlink available in the login page of the application (crewbid ipad )");
			objaction.click(valetLink);
		}
		public boolean valetLinkLinkNavi() {
		    objaction.switchToWindowByIndex(1);
		    objwait.waitS(2000);
		    // Replace non-breaking spaces with regular spaces and trim whitespace
		    String actualTitle = pageHeading().replace("\u00A0", " ").trim();                                          
		    String expectedTitle = "CrewBid Valet on the App Store".trim();
		    WbidBasepage.logger.info("Captured title of the page: " + actualTitle);
		    WbidBasepage.logger.info("Expected title: " + expectedTitle);
		    boolean isNavigated = actualTitle.equals(expectedTitle);
		    if (isNavigated) {
		        WbidBasepage.logger.info("Navigation to the App Store was successful.");
		    } else {
		        WbidBasepage.logger.fail("Navigation failed: Expected [" + expectedTitle + "] but found [" + actualTitle + "]");
		    }
		    // Switch back to the main window regardless of the result and close extra tabs
		    objaction.switchToWindowByIndex(0);
		    objaction.CheckExtraTabs();
		    return isNavigated;
		}
//TC 17
		@FindBy(xpath = "//p[contains(@class,'fake-input-text')]")
		public WebElement maskedPswd;
		
		public boolean pswdMasked() {
		    password.click();
		    password.clear();
		    password.sendKeys(WbidBasepage.password);
		 // Retrieve the displayed text from the fake input element
		    String displayedText = maskedPswd.getText().trim();
		    WbidBasepage.logger.log(Status.PASS, "Displayed Password Text: " + displayedText);
		    // Check if the displayed text is in a masked format 
		    if (displayedText.matches("[•*]+")) {
		        System.out.println("The password is displayed in a masked format.");
		        return true;
		    } else {
		        System.out.println("The password is not displayed in a masked format.");
		        return false;
		    }
		}
//TC 18
		@FindBy(xpath = "//*[text()=' This field is required ']")
		public WebElement mandatoryEmpMssg;
		
		public boolean empNoAlertMssg() {
			empNo.click();
			password.click();
			String txt = objaction.gettext(mandatoryEmpMssg);
			WbidBasepage.logger.pass("EmpNo Alert Mssg: " + txt);
			boolean val=objaction.fordisplay(mandatoryEmpMssg);
			return val;
		}
//TC 19
		public boolean pswdAlertMssg() {
		    password.click();
		    String currentText = WbidBasepage.password;
		    int textLength = currentText.length();
		    // Clear the text using BACK_SPACE
		    for (int i = 0; i < textLength; i++) {
		        password.sendKeys(Keys.BACK_SPACE);
		    }
		    objaction.sendkey(empNo, String.valueOf((int) Double.parseDouble(WbidBasepage.username)));
		    WbidBasepage.logger.log(Status.PASS, "Enter Employee Number Details Passed");
		    empNo.click();
		    String txt = objaction.gettext(mandatoryEmpMssg);
		    WbidBasepage.logger.pass("Password Alert Mssg: " + txt);
		    boolean val = objaction.fordisplay(mandatoryEmpMssg);
		    return val;
		}
//TC 20
		@FindBy(xpath = "//*[text()=' This field is required ']")
		public List<WebElement> alertMssgs;
		
		public boolean mandatoryMssgs() {
			empNo.click();
		    String currentText = WbidBasepage.username;
		    int textLength = currentText.length();
		    // Clear the text using BACK_SPACE
		    for (int i = 0; i < textLength; i++) {
		    	empNo.sendKeys(Keys.BACK_SPACE);
		    }
		    password.click();
		 // Check if both alert messages are displayed
		    boolean alertsVisible = alertMssgs.size() >= 2;
		    // Logging alert messages for debugging
		    if (alertsVisible) {
		        for (WebElement alert : alertMssgs) {
		            String alertText = alert.getText().trim();
		            WbidBasepage.logger.pass("Alert Message: " + alertText);
		        }
		    } else {
		        WbidBasepage.logger.fail("Not all mandatory field alerts are visible.");
		    }

		    return alertsVisible;
		}
//TC 21
		@FindBy(xpath = "//*[text()=' Employee Number cannot exceed 8 digits. ']")
		public WebElement empLimitMssgs;
		
		public void enterEmpNo(String user) {
			try {
				WbidBasepage.logger.info("Entering Emp No");
				objaction.sendkey(empNo, user);
				WbidBasepage.logger.pass("Emp No entered successfully");
			} catch (Exception e) {
				WbidBasepage.logger.fail("An error occurred while entering Emp No: " + e.getMessage());
				throw e;
			}
		}
		public boolean empNoValidationPrsnt() {
			boolean isPresent = false;
			try {
				WbidBasepage.logger.info("Checking if Emp No validation message is present when limit exceed 8 digit");
				isPresent = objaction.fordisplay(empLimitMssgs);
				WbidBasepage.logger.pass("Emp No validation message presence check completed successfully");
			} catch (Exception e) {
				WbidBasepage.logger
						.fail("An error occurred while checking Emp No validation message presence: " + e.getMessage());
				throw e;
			}
			return isPresent;
		}
//TC 22
		public void clearuser() {
			empNo.click();
		    String currentText = objaction.getValue(empNo);
		    int textLength = currentText.length();
		    // Clear the text using BACK_SPACE
		    for (int i = 0; i < textLength; i++) {
		    	empNo.sendKeys(Keys.BACK_SPACE);
		    }
		}
		public boolean empNoalphPrsnt() {
			boolean isPresent = false;
			try {
				WbidBasepage.logger.info("Provide username with first letter as any letter other than 'x' or 'e'");
				String txt = objaction.getValue(empNo);
				isPresent=txt.isEmpty();
				WbidBasepage.logger.pass("validation  successfully");
			} catch (Exception e) {
				WbidBasepage.logger
						.fail("An error occurred : " + e.getMessage());
				throw e;
			}
			return isPresent;
		}
		public void enterPassword(String pswrd) {
			try {
				WbidBasepage.logger.info("Entering password");
				objaction.sendkey(password, pswrd);
				WbidBasepage.logger.pass("Entered password successfully");
			} catch (Exception e) {
				WbidBasepage.logger.fail("An error occurred while entering password: " + e.getMessage());
				throw e;
			}
		}

	        
		}

	    
	


