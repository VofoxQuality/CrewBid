package testCases;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.json.JsonException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import API.ScratchPadBlankReservedLines;
import pages.BidDownloadPage;
import pages.BlkHour;
import pages.CommonPage;
import pages.DutyPeriodPage;
import pages.HoliRigATCPage;
import pages.HoliRigPage;
import pages.IndividualCredValuePage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class DutyPeriodTest extends WbidBasepage {
	WebDriver driver;
	LoginPage objlogin;
	WaitCondition objwait;
	ActionUtilities objaction;
	BidDownloadPage objdownload;
	CommonPage objCommon;
	HoliRigATCPage objholirig;
	HoliRigPage objHoli;
	DutyPeriodPage objduty;
	IndividualCredValuePage objInCred;
	BlkHour objBlk;

	public String actualtitle;
	public String expectedtitle;
	public String domicile;
	public String position = "CP";

	public String round = "1st Round";
	public String APIRound = "1";
	public HashMap<String, String> testDataMap;
	public String APIMonth;

	@Parameters({ "domicile" })
	@BeforeClass(alwaysRun = true)
	public void setup(@Optional("ATL") String domicile) {
		this.domicile = domicile;

		this.driver = returnDriver();// ✅ always get the correct driver from ThreadLocal

		this.objwait = new WaitCondition();
		this.objaction = new ActionUtilities(driver);
		this.objlogin = new LoginPage(driver);
		this.objdownload = new BidDownloadPage(driver);
		this.objCommon = new CommonPage(driver);
		this.objHoli = new HoliRigPage(driver);
		this.objholirig = new HoliRigATCPage(driver);
		this.objduty = new DutyPeriodPage(driver); // Pass driver to constructor

		this.objInCred = new IndividualCredValuePage(driver);
		this.objBlk = new BlkHour(driver);

		this.testDataMap = testData("qa environment");
		this.APIMonth = String.valueOf(objCommon.getNextMonth());
	}

	@Test(priority = 1)
	public void CBW010001000001() {
		// ExtentTest logger = loggerthread.get();
		logger = extent.createTest(" DUTY HOUR (CBW010001000001)").assignAuthor("VS/482");
		logger.info("Verify user is able see the Login page ");
		actualtitle = objlogin.pageHeading();
		expectedtitle = "CrewbidWebApp";
		logger.info("Captured title: " + actualtitle);
		Assert.assertEquals(actualtitle, expectedtitle, "Page title mismatch!");
	}

	// ✅ Add more tests here

	@Test(priority = 2, enabled = true)
	public void CBW010001000002() {
		logger = extent.createTest(" DUTY HOUR (CBW010001000002)").assignAuthor("VS/482");
		logger.info("Verify the update popup");
		objlogin.updateVersionLogin();
		// objwait.waitS(3000);
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(objlogin.gettexthome(), "Home page header does not show updated version");

	}

	@Test(priority = 3, enabled = true)
	public void CBW010001000003() {
		logger = extent.createTest("DUTY HOUR(CBW010001000003)").assignAuthor("VS/482");
		logger.info("Verify the login (subscribed user)");
		objCommon.clickOk();// handle subscription pop up
		objwait.waitS(9000);
		logger.info("Assert the title : \"Crewbid\" in the top left");
		Assert.assertTrue(objdownload.fordisplaylogo(), "❌ Logo not display");
	}

	@Test(priority = 4, enabled = true)
	public void CBW010001000004() {
		logger = extent.createTest("DUTY HOUR(CBW010001000004)").assignAuthor("VS/482");
		logger.info("Verify the Retreive button");
		Assert.assertTrue(objdownload.fordisplayretrivedropdown(), "❌ Retrive button not displayed");
		logger.info("Assert : Retrieve new bid data and Retrieve Historical bid data");
		objdownload.click_retrievedownload();
		Assert.assertTrue(objdownload.fordisplaynewbiddata(),
				"❌ Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");
	}

	@Test(priority = 5, enabled = true)
	public void CBW010001000005() {
		logger = extent.createTest("DUTY HOUR(CBW010001000005)").assignAuthor("VS/482");
		logger.info("verify the Retreive new bid data title");
		objdownload.forclicknewbiddata();
		Assert.assertEquals(objdownload.checkEmpnumpopupheader(), "Enter Employee Number",
				"❌ Header mismatch or not displayed");
		logger.info("✅ Assert :  title in the new popup Alert");
	}

	@Test(priority = 6, enabled = true)
	public void CBW010001000006() {
		logger = extent.createTest("DUTY HOUR(CBW010001000006)").assignAuthor("VS/482");
		logger.info("Verify the User ID ");
		objdownload.enterempid();
		Assert.assertEquals(objdownload.checkretrievebidpopupheader(), "Retrieve New Bid Period",
				"❌ Popup header mismatch or popup not displayed");
		logger.info("✅ Assert : Rediret to Retreive new bid perid popup page");
	}

	@Test(priority = 7, enabled = true)
	public void CBW010001000007() {
		logger = extent.createTest("DUTY HOUR(CBW010001000007)").assignAuthor("VS/482");
		logger.info("Verify the title in the Retrive new bid period popup alert");
		Assert.assertEquals(objdownload.checkretrievebidpopupheader(), "Retrieve New Bid Period",
				"❌ Popup header mismatch or popup not displayed");
		logger.info("✅ Assert :Title");
	}

	@Test(priority = 8, enabled = true)
	public void CBW010001000008() {
		logger = extent.createTest("DUTY HOUR(CBW010001000008)").assignAuthor("VS/482");
		logger.info("Verify the all the base is enabled");
		objdownload.forclickokbtn();
		objwait.waitS(3000);
		// Assert.assertTrue(objdownload.checkcities_isenable(), "❌ Base not enable");
		logger.info("✅ Assert : All the base cities");
	}

	@Test(priority = 9, enabled = true)
	public void CBW010001000009() {
		logger = extent.createTest("DUTY HOUR (CBW010001000009)").assignAuthor("VS/482");
		logger.info("Verify user can select any condition. Assert: Download button is enabled");
		objCommon.enterempid();
		objCommon.selectOptions(domicile, position, round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile, position, round));

	}

	@Test(priority = 10, enabled = true)
	public void CBW010001000010() {
		logger = extent.createTest("DUTY HOUR (CBW010001000010)").assignAuthor("VS/482");
		logger.info("Verify Download button is enabled");
		Assert.assertTrue(objInCred.downloadEnable());
		logger.info("✅Assert: Download button is enabled ");
	}

	boolean isDownloadSuccessful = false;

	@Test(priority = 11, enabled = true)
	public void CBW010001000011() {
		logger = extent.createTest("DUTY HOUR (CBW010001000011)").assignAuthor("VS/482");
		logger.info("Verify the download button - Scratch pad view is visible");
		logger.info("Click Download Bid");
		objCommon.clickDownload();
		logger.info("Verify if the loader appears while downloading the bid");
		boolean isLoadingIconDisplayed = objdownload.fordisplayloadingicon();
		if (isLoadingIconDisplayed) {
			Assert.assertTrue(isLoadingIconDisplayed, "Loading icon not displayed");
			isDownloadSuccessful = true;
		} else {
			logger.info("Download was not successful, warning popup should be visible.");
		}
	}

	@Test(priority = 12, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000012() {
		logger = extent.createTest("DUTY HOUR (CBW010001000012)").assignAuthor("VS/482");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping warning popup verification.");
		} else {
			logger.info("Verifying Early Bid Warning Popup");
			logger.info("Early Bid Warning popup is displayed.");
			Assert.assertTrue(objInCred.visibleEarlyBidPopup(), "Early Bid Warning popup should be displayed.");
			logger.info("If bid data is not available, a warning popup should appear.");
			objInCred.clickOkEarlyBid();
			Assert.assertTrue(objInCred.bidNotAvailableBidPopup(),
					"Bid not available warning popup should be displayed.");
			objInCred.clickOkbidNotAvailable();
		}
	}

	@Test(priority = 13, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000013() throws Exception {
		logger = extent.createTest("DUTY HOUR (CBW010001000013)").assignAuthor("VS/482");
		SoftAssert objsoft = new SoftAssert();
		logger.info("Verify the new tab to close");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping cancel button verification.");
		} else {
			logger.info("Verifying if user can select the cancel button.");
			objInCred.clickcancelBtn();
			Assert.assertTrue(objwait.waitPopupToClose(driver, objInCred.cancelBtn, 20),
					"Cancel button is not enabled.");
		}
		objsoft.assertTrue(isDownloadSuccessful, "Bid Download was not successful");
		objwait.waitS(3000);
		ScratchPadBlankReservedLines.fetchApiData(domicile, APIRound, position, APIMonth);
		objdownload.click_cancel_sen_btn();
		objwait.waitS(3000);
		objsoft.assertEquals(objdownload.checklatestnew_header(), "Latest News", "❌ Headers mismatch");
		logger.info("✅ Assert : close button");
		objsoft.assertAll();
	}

	@Test(priority = 14, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000014() {
		logger = extent.createTest("DUTY HOUR (CBW010001000014)").assignAuthor("VS/482");
		logger.info("Assert : latest news close button\n");
		objwait.waitS(3000);
		objdownload.click_news_closebtn();
		objwait.waitS(3000);
		Assert.assertEquals(objdownload.checkcoverletter_head(), "Cover Letter", "❌ Headers mismatch");
		logger.info("✅Pop up should be closed and Cover letter should come");
	}

	@Test(priority = 15, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000015() {
		logger = extent.createTest("DUTY HOUR (CBW010001000015)").assignAuthor("VS/482");
		logger.info("Verify the download button");
		objdownload.checklinenumber(domicile, position);
		objwait.waitS(3000);
		objdownload.click_close_coverletter();
		objwait.waitS(3000);
		Assert.assertTrue(objdownload.isvisible_scrachpad_head(), "❌ Close button functionality not working");
		logger.info("✅Assert : Cover letter close button");
	}

	@Test(priority = 16, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000016() {
		logger = extent.createTest("DUTY HOUR (CBW010001000016)").assignAuthor("VS/482");
		logger.info("Verify the user can select the Arrow button");
		Assert.assertTrue(objdownload.verifymovearrowclickable(), "❌ Icon not clickable");
		logger.info("✅Assert : Arrow button\n" + "Button should be clickable");
	}

	@Test(priority = 17, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000017() {
		logger = extent.createTest("DUTY HOUR (CBW010001000017)").assignAuthor("VS/482");
		logger.info("Verify all the lines moved in the scractpad to bidlist");
		Assert.assertTrue(objdownload.checklinemovestobid(), "❌ Bid Count not changed");
		logger.info("✅ Assert : Bidlsit count should be the previous count of the scratchpad view");
	}

	@Test(priority = 18, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000018() {
		logger = extent.createTest("DUTY HOUR (CBW010001000018)").assignAuthor("VS/482");
		logger.info("Verify the ellipsis icon");
		Assert.assertTrue(objdownload.forvisibleellipisicon(), "❌ Ellipis icon not displayed");
		logger.info("✅ Assert : Ellipsis icon");
	}

	@Test(priority = 19, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000019() {
		logger = WbidBasepage.extent.createTest("DUTY HOUR(CBW010001000019)").assignAuthor("VS/482");
		logger.info("Verify Start over button - scratch pad should be in default state");
		objdownload.startOver();
		objwait.waitS(4000);
		Assert.assertTrue(objdownload.checkLineNumberFromScratchpad(), "❌ Scratch pad combination not matches");
		logger.info("✅Scratch pad combination matches");
	}

	@Test(priority = 20, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000020() throws JsonException {
		logger = WbidBasepage.extent.createTest("DUTY HOUR(CBW010001000020)").assignAuthor("VS/482");
		logger.info(
				"Assert: Inside the trip details , the dates are shown same as the dates of the trips and should be same as its in the wbl file");
		logger.info("Get  trip Details from UI- Trip Code and Trip date and compare with API Trip code and dates");
//		FetchDates.fetchApiData(domicile, APIRound, position, APIMonth);
//	    Assert.assertTrue(objCommon.getAllTripDataAndCompare(FetchDates.tripData));
	}

	@Test(priority = 21, enabled = true, dependsOnMethods = "CBW010001000011", alwaysRun = true)
	public void CBW010001000021() throws NumberFormatException {
		logger = extent.createTest(" DUTY HOUR (CBW010001000021)").assignAuthor("VS/482");
		driver.navigate().refresh();
		objwait.waitS(7000);
		logger.info("Get individual cred of Each Trip from UI and compare with API Data ");
		// API.DutyPeriodTest.fetchApiData(domicile, APIRound, position, APIMonth);
		objduty.getCPDutyHour();
		Assert.assertTrue(objBlk.compareBlkFA(objduty.dutyhourUI, API.DutyPeriodTest.dutyHourMapAPI),
				"Duty Hour Mismatch");
	}
}
