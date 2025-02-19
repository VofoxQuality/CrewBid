package testCases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.BidDownloadPage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class BidDownloadTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	LoginPage objlogin = new LoginPage(driver);
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	BidDownloadPage objdownload=new BidDownloadPage(driver);
	protected String actualtitle;
	protected String expectedtitle;

	@Test(priority = 1, enabled = true)
	public void CBW003001000001() {
		logger = extent.createTest(" BID DATA DOWNLOAD (CBW003001000001)").assignAuthor("VS/482");
		logger.info("To check that CrewBid Login page is displaying when entering the Url");
		actualtitle = objlogin.pageHeading();
		expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + expectedtitle);
		logger.info("Assert the title");
		Assert.assertEquals(actualtitle, expectedtitle);
	}

	@Test(priority = 2, enabled = false)
	public void CBW003001000002() {
		logger = extent.createTest(" BID DATA DOWNLOAD (CBW003001000001)").assignAuthor("VS/482");
		logger.info("Verify the update popup");
		
	}

	@Test(priority = 3, enabled = false)
	public void CBW003001000003() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000003)").assignAuthor("VS/482");
	    logger.info("Verify the subscription expired");
	}

	@Test(priority = 4, enabled = true)
	public void CBW003001000004() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000004)").assignAuthor("VS/482");
	    logger.info("Verify the login (subscribed user)");
	    objlogin.login();
		objwait.waitS(9000);
		logger.info("Assert the title : \"Crewbid\" in the top left");
		Assert.assertTrue(objdownload.fordisplaylogo(), "Logo not display");
	}

	@Test(priority = 5, enabled = true)
	public void CBW003001000005() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000005)").assignAuthor("VS/482");
	    logger.info("Verify the Retreive button");
	    Assert.assertTrue(objdownload.fordisplayretrivedropdown(),"Retrive button not displayed");
	    logger.info("Assert : Retrieve new bid data and Retrieve Historical bid data");
	    objdownload.click_retrievedownload();
	    Assert.assertTrue(objdownload.fordisplaynewbiddata(),"Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");
	}

	@Test(priority = 6, enabled = true)
	public void CBW003001000006() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000006)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 7, enabled = true)
	public void CBW003001000007() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000007)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 8, enabled = true)
	public void CBW003001000008() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000008)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 9, enabled = true)
	public void CBW003001000009() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000009)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 10, enabled = true)
	public void CBW003001000010() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000010)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 11, enabled = true)
	public void CBW003001000011() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000011)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 12, enabled = true)
	public void CBW003001000012() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000012)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 13, enabled = true)
	public void CBW003001000013() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000013)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 14, enabled = true)
	public void CBW003001000014() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000014)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 15, enabled = true)
	public void CBW003001000015() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000015)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 16, enabled = true)
	public void CBW003001000016() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000016)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 17, enabled = true)
	public void CBW003001000017() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000017)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 18, enabled = true)
	public void CBW003001000018() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000018)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 19, enabled = true)
	public void CBW003001000019() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000019)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 20, enabled = true)
	public void CBW003001000020() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000020)").assignAuthor("VS/482");
	    logger.info("");
	}
	@Test(priority = 21, enabled = true)
	public void CBW003001000021() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000021)").assignAuthor("VS/482");
	    logger.info("");
	}

	@Test(priority = 22, enabled = true)
	public void CBW003001000022() {
	    logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000022)").assignAuthor("VS/482");
	    logger.info("");
	}

}
