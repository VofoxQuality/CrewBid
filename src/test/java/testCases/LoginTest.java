package testCases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
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
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (CBW002001000001)").assignAuthor("VS/483") ;
	 logger.info("To check that CrewBid Login page is displaying when entering the Url");
		actualtitle = objpage.pageHeading();
		expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + expectedtitle);
		logger.info("Assert the title");
		Assert.assertEquals(actualtitle, expectedtitle);

  }
  @Test(priority = 2, enabled = true)
  public void CBW002001000002() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (CBW002001000002)").assignAuthor("VS/483") ;
	 logger.info("To check that Employee Number Text Box");
	 Assert.assertTrue(objpage.visibilityofusernamebox(), "Employee Number Textbox Not visible");
  }
  @Test(priority = 3, enabled = false)
  public void CBW002001000003() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (CBW002001000003)").assignAuthor("VS/483") ;
	 logger.info("To check that Password Text Box");
	 Assert.assertTrue(objpage.visibilityofpasswordbox(), "Password Textbox Not visible");

  }
  @Test(priority = 4, enabled = true)
  public void CBW002001000004() {
	 logger = WbidBasepage.extent.createTest(" LOGIN PAGE (CBW002001000004)").assignAuthor("VS/483") ;
	 logger.info("To check Login functionality");
	 objpage.login();
	 objwait.waitS(9000);
	
  }
}
