package testCases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class LoginTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	LoginPage objpage = new LoginPage(driver);
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	protected String actualtitle;
	protected String expectedtitle;

	@Test(priority = 1, enabled = true)
	public void CBW002001000001() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000001)").assignAuthor("VS/483");
		logger.info("To check that CrewBid Login page is displaying when entering the Url");
		actualtitle = objpage.pageHeading();
		expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + actualtitle);
		logger.info("Expected title: " + expectedtitle);
		Assert.assertEquals(actualtitle, expectedtitle, "Title assertion failed!");
	}

	@Test(priority = 2, enabled = false)
	public void CBW002001000002() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000002)").assignAuthor("VS/483");
		logger.info("Verify invalid version case");
		boolean isPopupPresent = objpage.visibilityversionPopup();
		if (isPopupPresent) {
			logger.info("Version update popup is displayed.");
			Assert.assertTrue(isPopupPresent, "Version update popup should be displayed.");
		} else {
			logger.info("Version update popup is NOT displayed. Skipping further assertions.");
			throw new SkipException("Skipping test as version update popup did not appear.");
		}
	}

	@Test(priority = 3, enabled = false, dependsOnMethods = { "CBW002001000002" })
	public void CBW002001000003() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000003)").assignAuthor("VS/483");
		logger.info("Verify the update popup");
		Assert.assertTrue(objpage.visibilityVersnUpdateBtn(), "Home page header does not show updated version");
	}

	@Test(priority = 4, enabled = true)
	public void CBW002001000004() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000004)").assignAuthor("VS/483");
		logger.info("To check Login functionality");
		// objpage.login();
		objpage.updateVersionLogin();
		// objwait.waitS(3000);
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(objpage.gettexthome(), "Home page header does not show updated version");
	}

	@Test(priority = 5, enabled = true)
	public void CBW002001000005() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000005)").assignAuthor("VS/483");
		logger.info("To logout and verify Login Label");
		Assert.assertTrue(objpage.toperformlogout(),
				"The Login Label- The premiere bidding software for Pilots and Flight Attendants of Southwest Airlines. is not visible");
	}

	@Test(priority = 6, enabled = true)
	public void CBW002001000006() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000006)").assignAuthor("VS/483");
		logger.info("Verify the Logo of the application");
		Assert.assertTrue(objpage.logoVisibile(), "Logo of the application is not Visible");
	}

	@Test(priority = 7, enabled = true)
	public void CBW002001000007() {
		logger = WbidBasepage.extent.createTest("LOGIN PAGE (MTS001001000007)").assignAuthor("VS/483");
		logger.info("To verify Sign In Label");
		Assert.assertTrue(objpage.signInLblVisibile(), "Sign In Label of the application is not Visible");
	}

	@Test(priority = 8, enabled = true)
	public void CBW002001000008() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000008)").assignAuthor("VS/483");
		logger.info("Verify the text field for entering Employee No ");
		Assert.assertTrue(objpage.visibilityEmpNobox(), "Employee No Txt Box of the application is not Visible");
	}

	@Test(priority = 9, enabled = true)
	public void CBW002001000009() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000009)").assignAuthor("VS/483");
		logger.info("Verify the text field for entering password ");
		Assert.assertTrue(objpage.visibilityofPassbox(), "password Txt Box of the application is not Visible");
	}

	@Test(priority = 10, enabled = true)
	public void CBW002001000010() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000010)").assignAuthor("VS/483");
		logger.info("Check whether Login Button is present");
		Assert.assertTrue(objpage.visibilityLoginBtn(), "Login Button of the application is not Visible");
	}

	@Test(priority = 11, enabled = true)
	public void CBW002001000011() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000011)").assignAuthor("VS/483");
		logger.info("Check visibility of Place holder text Employee Number in the username field");
		Assert.assertEquals(objpage.empNoPlaceholder(), "Employee Number",
				"Place holder text Employee Number not visible");
	}

	@Test(priority = 12, enabled = true)
	public void CBW002001000012() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000012)").assignAuthor("VS/483");
		logger.info("Check visibility of Place holder text CWA password in the username field");
		Assert.assertEquals(objpage.pswdPlaceholder(), "CWA Password", "Place holder text CWA password not visible");
	}

	@Test(priority = 13, enabled = true)
	public void CBW002001000013() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000013)").assignAuthor("VS/483");
		logger.info("Check whether the eye button is visible inside password field.");
		Assert.assertTrue(objpage.visibilityeyeBtn(), "Eye Button is not Visible inside password field");
	}

	@Test(priority = 14, enabled = true)
	public void CBW002001000014() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000014)").assignAuthor("VS/483");
		logger.info("Verify the hyperlink for downloading crewbid ipad from appstore is navigating to appstore");
		objpage.clickLink();
		objwait.waitS(2000);
		Assert.assertTrue(objpage.linkNavi(), "Application does not navigates to the appstore-CrewBid");
	}

	@Test(priority = 15, enabled = true)
	public void CBW002001000015() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000015)").assignAuthor("VS/483");
		logger.info("Verify the hyperlink for downloading crewbid2 from appstore is navigating to appstore");
		objpage.clickiPadLink();
		objwait.waitS(2000);
		Assert.assertTrue(objpage.hyperLinkNavi(), "Application does not navigates to the appstore-CrewBid2");
	}

	@Test(priority = 16, enabled = true)
	public void CBW002001000016() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000016)").assignAuthor("VS/483");
		logger.info("Verify the hyperlink for downloading crewbid Valet from appstore is navigating to appstore");
		objpage.clickvaletLink();
		objwait.waitS(2000);
		Assert.assertTrue(objpage.valetLinkLinkNavi(), "Application does not navigates to the appstore-crewbid Valet");
	}

	@Test(priority = 17, enabled = true)
	public void CBW002001000017() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000017)").assignAuthor("VS/483");
		logger.info("Verify whether the password entered in CWA password space is in masked format");
		Assert.assertTrue(objpage.pswdMasked(), "the password entered in CWA password space is not in masked format");
	}

	@Test(priority = 18, enabled = true)
	public void CBW002001000018() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000018)").assignAuthor("VS/483");
		logger.info("Verify that the User name field is mandatory");
		Assert.assertTrue(objpage.empNoAlertMssg(), "User name field is mandatory not visible");
	}

	@Test(priority = 19, enabled = true)
	public void CBW002001000019() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000019)").assignAuthor("VS/483");
		logger.info("Verify that the password field is mandatory");
		Assert.assertTrue(objpage.pswdAlertMssg(), "Password field is mandatory not visible");
	}

	@Test(priority = 20, enabled = true)
	public void CBW002001000020() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000020)").assignAuthor("VS/483");
		logger.info("Verify the message when both username and password are not provided.");
		Assert.assertTrue(objpage.mandatoryMssgs(), "username and password  field is mandatory not visible");

	}

	@Test(priority = 21, enabled = true)
	public void CBW002001000021() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000021)").assignAuthor("VS/483");
		logger.info("Verify whether validation message is shown when username exceed 8 digits");
		objpage.enterEmpNo("22121121212");
		Assert.assertTrue(objpage.empNoValidationPrsnt(),
				"validation message is not shown when username exceed 8 digits");
	}

	@Test(priority = 22, enabled = true)
	public void CBW002001000022() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000022)").assignAuthor("VS/483");
		logger.info("Verify whether username field permits characters other than 'x' and 'e' as first character");
		objpage.clearuser();
		objpage.enterEmpNo("a");
		Assert.assertTrue(objpage.empNoalphPrsnt(), "username field permits characters");
	}

	@Test(priority = 23, enabled = true)
	public void CBW002001000023() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000023)").assignAuthor("VS/483");
		logger.info("Verify whether username field permits characters 'x' as first letter");
		objpage.enterEmpNo("x21221");
		Assert.assertTrue(objpage.getEmpNo("x21221"), "username field does not permit characters 'x'");
	}

	@Test(priority = 24, enabled = true)
	public void CBW002001000024() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000024)").assignAuthor("VS/483");
		logger.info("Verify whether username field permits characters 'e' as first letter");
		objpage.clearuser();
		objpage.enterEmpNo("e21221");
		Assert.assertTrue(objpage.getEmpNo("e21221"), "username field does not permit characters 'e'");
	}

	@Test(priority = 25, enabled = true)
	public void CBW002001000025() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000025)").assignAuthor("VS/483");
		logger.info("Verify the login functionality by entering correct username and incorrect password");
		objpage.clearuser();
		objaction.sendkey(objpage.empNo, String.valueOf((int) Double.parseDouble(WbidBasepage.username)));
		WbidBasepage.logger.log(Status.PASS, "Enter Employee Number Details Passed");
		objpage.enterPswd("abc@123");
		// objaction.JavaScriptclick(objpage.login);
		objpage.clickLoginBtn();
		Assert.assertTrue(objpage.visibilityofoopsPopUp(), "pop up not visible for incorrect password");
	}

	@Test(priority = 26, enabled = true)
	public void CBW002001000026() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000026)").assignAuthor("VS/483");
		logger.info(
				"Verify the login functionality by entering incorrect username and correct password(unauthourized user)");
		objpage.clickOk();
		objpage.clearuser();
		objpage.enterEmpNo("x");
		objpage.clearPswd();
		objpage.enterPswd(WbidBasepage.password);
		WbidBasepage.logger.log(Status.PASS, "Enter Password Details passed");
		Assert.assertTrue(objpage.invalidvadlidation(), "validation message is not shown");
	}

	@Test(priority = 27, enabled = true)
	public void CBW002001000027() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000027)").assignAuthor("VS/483");
		logger.info("Verify the login functionality by entering incorrect username and password");
		objpage.clearuser();
		objpage.enterEmpNo("x");
		objpage.clearPswd();
		objpage.enterPswd("abc@123");
		WbidBasepage.logger.log(Status.PASS, "Enter invalid user and Password Details passed");
		Assert.assertTrue(objpage.loginBtnDisable(), "Login button is not disabled");
	}

	@Test(priority = 28, enabled = true)
	public void CBW002001000028() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000028)").assignAuthor("VS/483");
		logger.info(
				"Verify the login functionality by entering username and password with correct formats but are invalid ");
		objpage.clearuser();
		objpage.enterEmpNo("x56383");
		WbidBasepage.logger.log(Status.PASS, "Enter Employee Number Details Passed");
		objpage.enterPswd("abc@123");
		objpage.clickLoginBtn();
		Assert.assertTrue(objpage.visibilityofoopsPopUp(), "pop up not visible for incorrect password");
	}

	@Test(priority = 29, enabled = true)
	public void CBW002001000029() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000029)").assignAuthor("VS/483");
		logger.info(
				"Verify that the application navigates to the home page of crewbid web app when correct username and password entered");
		objpage.clickOk();
		objpage.clearuser();
		objpage.clearPswd();
		objpage.login();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(objpage.gettexthome(), "Home page header does not show updated version");
	}

	@Test(priority = 30, enabled = true)
	public void CBW002001000030() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000030)").assignAuthor("VS/483");
		logger.info("Verify whether the password entered is visible after clicking on eye button in password field");
		objpage.logout();
		objpage.pswdMasked();
		objpage.clickEyeSlash();
		Assert.assertTrue(objpage.pswdVisible(), "the password entered is not visible");
	}

	@Test(priority = 31, enabled = true)
	public void CBW002001000031() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000031)").assignAuthor("VS/483");
		logger.info("Verify the password character limit");
		objpage.clickEye();
		objpage.clearPswd();
		objpage.enterPswd("zxcvbasdfgqwert@123456789012345zqwe");
		objaction.click(objpage.empNo);
		Assert.assertTrue(objpage.pswdValidationPrsnt(),
				"password validation message is not present when limit 1 to 30 characters");
	}

	@Test(priority = 32, enabled = true)
	public void CBW002001000032() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000032)").assignAuthor("VS/483");
		logger.info("Verify the website is using https protocol");
		String url = driver.getCurrentUrl();
		logger.info("website is using https protocol: " + url);
		Assert.assertTrue(url.contains("https"), "website is not using https protocol");
	}

	@Test(priority = 33, enabled = true)
	public void CBW002001000033() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000033)").assignAuthor("VS/483");
		logger.info("To verify invalid subscription- ");

	}

	@Test(priority = 34, enabled = true)
	public void CBW002001000034() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000034)").assignAuthor("VS/483");
		logger.info("To verifywhether website is running when internet is blocked - Automation not possible");

	}

	@Test(priority = 35, enabled = true)
	public void CBW002001000035() throws Exception {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000035)").assignAuthor("VS/483");
		logger.info("To verify PWA Installer icon");
		Assert.assertTrue(objpage.visibilitypwaInstaller(), "PWA Installer icon is not visible");
		WbidBasepage.logger.pass("PWA Installer icon visible");
	}

	@Test(priority = 36, enabled = true)
	public void CBW002001000036() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000036)").assignAuthor("VS/483");
		logger.info("To verify Username field while offline -Automation not possible");

	}

	@Test(priority = 37, enabled = true)
	public void CBW002001000037() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000037)").assignAuthor("VS/483");
		logger.info("To verify login in offline mode-Automation not possible");

	}

	@Test(priority = 38, enabled = true)
	public void CBW002001000038() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000038)").assignAuthor("VS/483");
		logger.info("To verify captcha loading");
		Assert.assertTrue(objpage.visibilitycaptchaCanvas(), "Captcha Canvas is not visible");
		WbidBasepage.logger.pass("Captcha Canvas is visible");
	}

	@Test(priority = 39, enabled = true)
	public void CBW002001000039() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000039)").assignAuthor("VS/483");
		logger.info("To verify Captcha Refresh button");
		Assert.assertTrue(objpage.captchaRefreshPresent(), "Captcha Refresh button is not visible");
		WbidBasepage.logger.pass("Captcha Canvas Refresh button is visible");
	}

	@Test(priority = 40, enabled = true)
	public void CBW002001000040() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000040)").assignAuthor("VS/483");
		logger.info(
				"To verify login with wrong captcha- Automation not possible- checking enter random text in captcha text box");
		Assert.assertTrue(objpage.VerifyenterCaptcha("KjTha"), "Captcha connot be entered");
		WbidBasepage.logger.pass("Can enter random text in captcha text box");
	}

	@Test(priority = 41, enabled = true)
	public void CBW002001000041() {
		logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000041)").assignAuthor("VS/483");
		logger.info("To verify support email link");
		Assert.assertTrue(objpage.supportLinkPresent(), "support email link is not visible");
		WbidBasepage.logger.pass("support email link is visible");
	}
}
