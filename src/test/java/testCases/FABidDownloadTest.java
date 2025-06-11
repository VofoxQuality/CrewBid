package testCases;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.BidDownloadPage;
import pages.CommonPage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class FABidDownloadTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	LoginPage objlogin = new LoginPage(driver);
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon = new CommonPage(driver);
	protected String actualtitle;
	protected String expectedtitle;
	HashMap<String, String> testDataMap = testData("qa environment");

	public String actualVersion;
	public String expectedVersion = testDataMap.get("Version");

	@Test(priority = 1, enabled = true)
	public void test1() {
		logger = WbidBasepage.extent.createTest("test1").assignAuthor("VS/482");
		logger.info("Login");
		objlogin.updateVersionLogin();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(objlogin.gettexthome(), "Home page header does not show updated version");
	}

	@Test(priority = 2, enabled = true)
	public void test2() {
		logger = WbidBasepage.extent.createTest("test2").assignAuthor("VS/482");
		logger.info("Get Login Version");
		actualVersion = objCommon.getVersionValue();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(actualVersion.contains(expectedVersion), "Home page header does not show updated version");
	}

	@Test(priority = 3, enabled = true)
	public void test3() {
		logger = WbidBasepage.extent.createTest("test3").assignAuthor("VS/482");
		objCommon.clickOk();//handle subscription pop up
		logger.info("Bid download");
		logger.info("click Retreive button");
		objdownload.click_retrievedownload();
		objdownload.forclicknewbiddata();
		logger.info("Enter the User ID ");
		objwait.waitS(3000);
		objdownload.enterempid();
		objwait.waitS(3000);
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 02 : Download round 1 and  FA with all the  domicile(2nd day of all month)");
		Assert.assertTrue(objdownload.checkCondition2DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 4, enabled = true)
	public void test4() {
		logger = WbidBasepage.extent.createTest("test4").assignAuthor("VS/482");
		objwait.waitS(5000);
		objdownload.checkDownloadBidFA();
		objwait.waitS(5000);
		Assert.assertEquals(objdownload.fordisplay_seniority(), "Seniority List", "❌ Mismatch header");
		logger.info("✅Assert : Seniority list popup appears");
	}

	@Test(priority = 5, enabled = true)
	public void test5() {
		logger = WbidBasepage.extent.createTest("test5").assignAuthor("VS/482");
	}

	@Test(priority = 6, enabled = true)
	public void test6() {
		logger = WbidBasepage.extent.createTest("test6").assignAuthor("VS/482");
	}

	@Test(priority = 7, enabled = true)
	public void test7() {
		logger = WbidBasepage.extent.createTest("test7").assignAuthor("VS/482");
	}

	@Test(priority = 8, enabled = true)
	public void test8() {
		logger = WbidBasepage.extent.createTest("test8").assignAuthor("VS/482");
	}

	@Test(priority = 9, enabled = true)
	public void test9() {
		logger = WbidBasepage.extent.createTest("test9").assignAuthor("VS/482");
	}

	@Test(priority = 10, enabled = true)
	public void test10() {
		logger = WbidBasepage.extent.createTest("test10").assignAuthor("VS/482");
	}

	@Test(priority = 11, enabled = true)
	public void test11() {
	    logger = WbidBasepage.extent.createTest("test11").assignAuthor("VS/482");
	}
}
