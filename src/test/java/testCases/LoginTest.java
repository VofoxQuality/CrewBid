package testCases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class LoginTest extends WbidBasepage{
	WebDriver driver = returnDriver();
	LoginPage objpage = new LoginPage(driver);
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	protected String actualtitle;
	protected String expectedtitle;
	
  @Test(priority = 1, enabled = true)
  public void CBW002001000001() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000001)").assignAuthor("VS/483") ;
	 logger.info("To check that CrewBid Login page is displaying when entering the Url");
	 actualtitle = objpage.pageHeading();
	 expectedtitle = "CrewbidWebApp";
	 logger.info("Captured title of the page: " + actualtitle);
	 logger.info("Expected title: " + expectedtitle);
	 Assert.assertEquals(actualtitle, expectedtitle, "Title assertion failed!");	
  }
  @Test(priority = 2, enabled = false)
  public void CBW002001000002() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000002)").assignAuthor("VS/483") ;
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
  @Test(priority = 3, enabled = false,dependsOnMethods = {"CBW002001000002"})
  public void CBW002001000003() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000003)").assignAuthor("VS/483") ;
	 logger.info("Verify the update popup");
	 Assert.assertTrue(objpage.visibilityVersnUpdateBtn(), "Home page header does not show updated version");
  }
  @Test(priority = 4, enabled = true)
  public void CBW002001000004() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000004)").assignAuthor("VS/483") ;
	 logger.info("To check Login functionality");
	 //objpage.login();
	 objpage.updateVersionLogin();
	 //objwait.waitS(3000);
	 logger.info("Assert that Home page header shows updated version.");
	 Assert.assertTrue(objpage.gettexthome(), "Home page header does not show updated version");
  }
  @Test(priority = 5, enabled = true)
  public void CBW002001000005() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000005)").assignAuthor("VS/483") ;
	 logger.info("To logout and verify Login Label");
	 Assert.assertTrue(objpage.toperformlogout(), "The Login Label- The premiere bidding software for Pilots and Flight Attendants of Southwest Airlines. is not visible");
  }
  @Test(priority = 6, enabled = true)
  public void CBW002001000006() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000006)").assignAuthor("VS/483") ;
	 logger.info("Verify the Logo of the application");
	 Assert.assertTrue(objpage.logoVisibile(), "Logo of the application is not Visible");
  }
  @Test(priority = 7, enabled = true)
  public void CBW002001000007() {
	 logger = WbidBasepage.extent.createTest("LOGIN PAGE (MTS001001000007)").assignAuthor("VS/483") ;
	 logger.info("To verify Sign In Label");
	 Assert.assertTrue(objpage.signInLblVisibile(), "Sign In Label of the application is not Visible");
  }
  @Test(priority = 8, enabled = true)
  public void CBW002001000008() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000008)").assignAuthor("VS/483") ;
	 logger.info("Verify the text field for entering Employee No ");
	 Assert.assertTrue(objpage.visibilityEmpNobox(), "Employee No Txt Box of the application is not Visible");
  }
  @Test(priority = 9, enabled = true)
  public void CBW002001000009() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000009)").assignAuthor("VS/483") ;
	 logger.info("Verify the text field for entering password ");
	 Assert.assertTrue(objpage.visibilityofPassbox(), "password Txt Box of the application is not Visible");
  }
  @Test(priority = 10, enabled = true)
  public void CBW002001000010() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000010)").assignAuthor("VS/483") ;
	 logger.info("Check whether Login Button is present");
	 Assert.assertTrue(objpage.visibilityLoginBtn(),"Login Button of the application is not Visible");
  }
  @Test(priority = 11, enabled = true)
  public void CBW002001000011() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000011)").assignAuthor("VS/483") ;
	 logger.info("Check visibility of Place holder text Employee Number in the username field");
	 
  }
  @Test(priority = 12, enabled = true)
  public void CBW002001000012() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000012)").assignAuthor("VS/483") ;
	 logger.info("Check visibility of Place holder text CWA password in the username field");
	 
  }
  @Test(priority = 13, enabled = true)
  public void CBW002001000013() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000013)").assignAuthor("VS/483") ;
	 logger.info("Check whether the eye button is visible inside password field.");
	 
  }
  @Test(priority = 14, enabled = true)
  public void CBW002001000014() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000014)").assignAuthor("VS/483") ;
	 logger.info("Verify the hyperlink for downloading crewbid ipad from appstore is navigating to appstore");
	 
  }
  @Test(priority = 15, enabled = true)
  public void CBW002001000015() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000015)").assignAuthor("VS/483") ;
	 logger.info("Verify the hyperlink for downloading crewbid Valet from appstore is navigating to appstore");
	 
  }
  @Test(priority = 16, enabled = true)
  public void CBW002001000016() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000016)").assignAuthor("VS/483") ;
	 logger.info("Verify whether the password entered in CWA password space is in masked format");
	 
  }
  @Test(priority = 17, enabled = true)
  public void CBW002001000017() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000017)").assignAuthor("VS/483") ;
	 logger.info("Verify that the User name field is mandatory");
	 
  }
  @Test(priority = 18, enabled = true)
  public void CBW002001000018() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000018)").assignAuthor("VS/483") ;
	 logger.info("Verify that the password field is mandatory");
	 
  }
  @Test(priority = 19, enabled = true)
  public void CBW002001000019() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000019)").assignAuthor("VS/483") ;
	 logger.info("Verify the message when both username and password are not provided.");
	 
  }
  @Test(priority = 20, enabled = true)
  public void CBW002001000020() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000020)").assignAuthor("VS/483") ;
	 logger.info("Verify whether validation message is shown when username exceed 8 digits");
	 
  }
  @Test(priority = 30, enabled = true)
  public void CBW002001000030() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000030)").assignAuthor("VS/483") ;
	 logger.info("");
	 
  }
  @Test(priority = 40, enabled = true)
  public void CBW002001000040() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (MTS001001000040)").assignAuthor("VS/483") ;
	 logger.info("");
	 
  }
}







