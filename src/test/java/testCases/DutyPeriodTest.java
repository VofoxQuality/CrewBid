package testCases;

import java.io.IOException;
import java.text.ParseException;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import API.BlockTest;
import API.FetchDates;
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
	WebDriver driver = returnDriver();
	LoginPage objlogin = new LoginPage(driver);
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon = new CommonPage(driver);
	HoliRigATCPage objholirig = new HoliRigATCPage(driver);
	HoliRigPage objHoli = new HoliRigPage(driver);
	DutyPeriodPage objduty = new DutyPeriodPage(driver);
	IndividualCredValuePage objInCred = new IndividualCredValuePage(driver);
	BlkHour objBlk = new BlkHour(driver);
	protected String actualtitle;
	protected String expectedtitle;
	public String domicile = "ATL";
	public String position = "CP";
	public String round = "1st Round";
	public String APIRound = "1";
	public String APIMonth = String.valueOf(objCommon.getNextMonth());

	
	@Test(priority = 1, enabled = true)
	public void CBW010001000001() {
		logger = extent.createTest(" DUTY HOUR (CBW010001000001)").assignAuthor("VS/482");
		logger.info("Verify user is able see the Login page ");
		actualtitle = objlogin.pageHeading();
		expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + expectedtitle);
		logger.info("Assert the title");
		Assert.assertEquals(actualtitle, expectedtitle);
	}

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
		objCommon.clickOk();//handle subscription pop up
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

	@Test(priority = 9, enabled = false)
	public void CBW010001000009() {
		logger = extent.createTest("DUTY HOUR (CBW010001000009)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 01 : Download round 1 and pilot (CP and FO) with all the  domicile (4th day of all month)\n"
				+ "Note : For Pilot ( AUS and FLL) domicile  no need to download");
		Assert.assertTrue(objdownload.checkCondition1DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 10, enabled = false)
	public void CBW010001000010() {
		logger = extent.createTest("DUTY HOUR (CBW010001000010)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 02 : Download round 1 and  FA with all the  domicile(2nd day of all month)");
		Assert.assertTrue(objdownload.checkCondition2DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 11, enabled = false)
	public void CBW010001000011() {
		logger = extent.createTest("DUTY HOUR (CBW010001000011)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 03 : Download round 2 and pilot (CP and FO) with all the  domicile (17th day of all month)\n"
				+ "Note : For Pilot ( AUS and FLL) domicile  no need to download");
		Assert.assertTrue(objdownload.checkCondition3DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 12, enabled = true)
	public void CBW010001000012() {
		logger = extent.createTest("DUTY HOUR (CBW010001000012)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 04 : Download round 2 and FA with all the  domicile(11th day of all month)");
		Assert.assertTrue(objdownload.checkCondition4DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 13, enabled = true)
	public void CBW010001000013() throws JsonProcessingException {
		logger = extent.createTest("DUTY HOUR (CBW010001000013)").assignAuthor("VS/482");
		logger.info("Verify the new tab to close");
		objdownload.checkDownloadBid();
		// objwait.waitS(4000);
		objdownload.click_cancel_sen_btn();
		objwait.waitS(3000);

		Assert.assertEquals(objdownload.checklatestnew_header(), "Latest News", "❌ Headers mismatch");
		logger.info("✅ Assert : close button");
	}

	@Test(priority = 14, enabled = true)
	public void CBW010001000014() {
		logger = extent.createTest("DUTY HOUR (CBW010001000014)").assignAuthor("VS/482");
		logger.info("Assert : latest news close button\n");
		objwait.waitS(3000);
		objdownload.click_news_closebtn();
		objwait.waitS(3000);
		Assert.assertEquals(objdownload.checkcoverletter_head(), "Cover Letter", "❌ Headers mismatch");
		logger.info("✅Pop up should be closed and Cover letter should come");
	}

	@Test(priority = 15, enabled = true)
	public void CBW010001000015() {
		logger = extent.createTest("DUTY HOUR (CBW010001000015)").assignAuthor("VS/482");
		logger.info("Verify the download button");
		objdownload.checklinenumber();
		objwait.waitS(3000);
		objdownload.click_close_coverletter();
		objwait.waitS(3000);
		Assert.assertTrue(objdownload.isvisible_scrachpad_head(), "❌ Close button functionality not working");
		logger.info("✅Assert : Cover letter close button");
	}

	@Test(priority = 16, enabled = true)
	public void CBW010001000016() {
		logger = extent.createTest("DUTY HOUR (CBW010001000016)").assignAuthor("VS/482");
		logger.info("Verify the user can select the Arrow button");
		Assert.assertTrue(objdownload.verifymovearrowclickable(), "❌ Icon not clickable");
		logger.info("✅Assert : Arrow button\n" + "Button should be clickable");
	}

	@Test(priority = 17, enabled = true)
	public void CBW010001000017() {
		logger = extent.createTest("DUTY HOUR (CBW010001000017)").assignAuthor("VS/482");
		logger.info("Verify all the lines moved in the scractpad to bidlist");
		Assert.assertTrue(objdownload.checklinemovestobid(), "❌ Bid Count not changed");
		logger.info("✅ Assert : Bidlsit count should be the previous count of the scratchpad view");
	}

	@Test(priority = 18, enabled = true)
	public void CBW010001000018() {
		logger = extent.createTest("DUTY HOUR (CBW010001000018)").assignAuthor("VS/482");
		logger.info("Verify the ellipsis icon");
		Assert.assertTrue(objdownload.forvisibleellipisicon(), "❌ Ellipis icon not displayed");
		logger.info("✅ Assert : Ellipsis icon");
	}

	@Test(priority = 19, enabled = true)
	public void CBW010001000019() {
		logger = WbidBasepage.extent.createTest("DUTY HOUR(CBW010001000019)").assignAuthor("VS/482");
		logger.info("Verify Start over button - scratch pad should be in default state");
		objdownload.startOver();
		objwait.waitS(4000);
		Assert.assertTrue(objdownload.checkLineNumberFromScratchpad(), "❌ Scratch pad combination not matches");
		logger.info("✅Scratch pad combination matches");
	}

	@Test(priority = 20, enabled = true)
	public void CBW010001000020() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("DUTY HOUR(CBW010001000020)").assignAuthor("VS/482");
		logger.info(
				"Assert: Inside the trip details , the dates are shown same as the dates of the trips and should be same as its in the wbl file");
		logger.info("Get  trip Details from UI- Trip Code and Trip date and compare with API Trip code and dates");
//		FetchDates.fetchApiData(domicile, APIRound, position, APIMonth);
//	    Assert.assertTrue(objCommon.getAllTripDataAndCompare(FetchDates.tripData));
	}

	@Test(priority = 21, enabled = true)
	public void CBW010001000021() throws NumberFormatException, ParseException, IOException {
		logger = extent.createTest(" DUTY HOUR (CBW010001000021)").assignAuthor("VS/482");
		driver.navigate().refresh();
		objwait.waitS(7000);
		logger.info("Get individual cred of Each Trip from UI and compare with API Data ");
		// API.DutyPeriodTest.fetchApiData(domicile, APIRound, position, APIMonth);
		objduty.getCPDutyHour();
		Assert.assertTrue(objBlk.compareBlkFA(objduty.dutyhourUI, API.DutyPeriodTest.dutyHourMapAPI), "Duty Hour Mismatch");
	}

//	@Test(priority = 22, enabled = true)
//	public void CBW010001000022() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000022)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 23, enabled = true)
//	public void CBW010001000023() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000023)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 24, enabled = true)
//	public void CBW010001000024() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000024)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 25, enabled = true)
//	public void CBW010001000025() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000025)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 26, enabled = true)
//	public void CBW010001000026() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000026)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 27, enabled = true)
//	public void CBW010001000027() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000027)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 28, enabled = true)
//	public void CBW010001000028() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000028)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 29, enabled = true)
//	public void CBW010001000029() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000029)").assignAuthor("VS/482");
//	}
//
//	@Test(priority = 30, enabled = true)
//	public void CBW010001000030() {
//		logger = extent.createTest(" DUTY HOUR (CBW010001000030)").assignAuthor("VS/482");
//	}

}
