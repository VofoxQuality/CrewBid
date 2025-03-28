package testCases;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import com.fasterxml.jackson.core.JsonProcessingException;
import API.ScratchPadBlankReservedLines;
import pages.BidDownloadPage;
import pages.CommonPage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class BidDownloadTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	LoginPage objlogin = new LoginPage(driver);
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon = new CommonPage(driver);
	protected String actualtitle;
	protected String expectedtitle;
	public String domicile = "ATL";
	public String position = "CP";
	public String round = "1st Round";
	public String APIMonth = String.valueOf(objCommon.getNextMonth());

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

	@Test(priority = 2, enabled = true)
	public void CBW003001000002() {
		logger = extent.createTest(" BID DATA DOWNLOAD (CBW003001000001)").assignAuthor("VS/482");
		logger.info("Verify the update popup");
		objlogin.updateVersionLogin();
		// objwait.waitS(3000);
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(objlogin.gettexthome(), "Home page header does not show updated version");
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
		// objlogin.login();
		objwait.waitS(9000);
		logger.info("Assert the title : \"Crewbid\" in the top left");
		Assert.assertTrue(objdownload.fordisplaylogo(), "❌ Logo not display");
	}

	@Test(priority = 5, enabled = true)
	public void CBW003001000005() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000005)").assignAuthor("VS/482");
		logger.info("Verify the Retreive button");
		Assert.assertTrue(objdownload.fordisplayretrivedropdown(), "❌ Retrive button not displayed");
		logger.info("Assert : Retrieve new bid data and Retrieve Historical bid data");
		objdownload.click_retrievedownload();
		Assert.assertTrue(objdownload.fordisplaynewbiddata(),
				"❌ Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");
	}

	@Test(priority = 6, enabled = true)
	public void CBW003001000006() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000006)").assignAuthor("VS/482");
		logger.info("Verify the user able to select Retrieve new bid data button");
		objdownload.forclicknewbiddata();
		Assert.assertEquals(objdownload.checkEmpnumpopupheader(), "Enter Employee Number",
				"❌ Header mismatch or not displayed");
		logger.info("✅ Assert :  title in the new popup Alert");
	}

	@Test(priority = 8, enabled = true)
	public void CBW003001000007() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000007)").assignAuthor("VS/482");
		logger.info("Verify the Content correct in the \"Enter emplyoee number \" popup alert");
		Assert.assertEquals(objdownload.checkempPopupText(),
				"Please enter the employee number for whom the bid will be submitted (no 'e').", "❌ Text is mismatch");
		logger.info("✅ Assert: \"Please enter the employee number for whom the bid will be submitted (no 'e').\"");
	}

	@Test(priority = 9, enabled = true)
	public void CBW003001000008() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000008)").assignAuthor("VS/482");
		logger.info("Verify the Cancel button and OK button in the popup alert");
		Assert.assertTrue(objdownload.fordisplaybtninpopup(), "❌ Buttons not displayed");
		logger.info("✅ Assert : Cancel button and OK button");
	}

	@Test(priority = 10, enabled = true)
	public void CBW003001000009() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000009)").assignAuthor("VS/482");
		logger.info("Verify the user ID field with content \"Employee No:\"");
		Assert.assertTrue(objdownload.checkplaceholder(), "❌ Placeholder Mismatch or not displayed");
		logger.info("✅ Assert: \"Employee No:\" content in the field");
	}

	@Test(priority = 11, enabled = true)
	public void CBW003001000010() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000010)").assignAuthor("VS/482");
		logger.info("Verify the User ID ");
		objdownload.enterempid();
		Assert.assertEquals(objdownload.checkretrievebidpopupheader(), "Retrieve New Bid Period",
				"❌ Popup header mismatch or popup not displayed");
		logger.info("✅ Assert : Rediret to Retreive new bid perid popup page");
	}

	@Test(priority = 12, enabled = true)
	public void CBW003001000011() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000011)").assignAuthor("VS/482");
		logger.info("Verify the title in the Retrive new bid period popup alert");
		Assert.assertEquals(objdownload.checkretrievebidpopupheader(), "Retrieve New Bid Period",
				"❌ Popup header mismatch or popup not displayed");
		logger.info("✅ Assert :Title");
	}

	@Test(priority = 7, enabled = true)
	public void CBW003001000012() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000012)").assignAuthor("VS/482");
		logger.info("verify the Retreive new bid data title");
		Assert.assertEquals(objdownload.checkEmpnumpopupheader(), "Enter Employee Number",
				"❌ Header mismatch or not displayed");
		logger.info("✅ Assert : Enter employee number title in the new popup Alert");
	}

	@Test(priority = 13, enabled = true)
	public void CBW003001000013() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000013)").assignAuthor("VS/482");
		logger.info("Verify the title Base, Position , Round, MOnth  heading appers with double collon");
		Assert.assertTrue(objdownload.checkRetrievebidpopuplabels(), "❌ Labels mismatch");
		logger.info("✅ Assert : Subtitle : Base,Position , Round, MOnth  heading appers with double collon");
	}

	@Test(priority = 14, enabled = false)
	public void CBW003001000014() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000014)").assignAuthor("VS/482");
		logger.info("Verify the server sresponse");
	}

	@Test(priority = 15, enabled = true)
	public void CBW003001000015() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000015)").assignAuthor("VS/482");
		logger.info("To check the close button");
		objdownload.click_closebtn();
		Assert.assertEquals(objdownload.checkEmpnumpopupheader(), "Enter Employee Number",
				"❌ Header mismatch or not displayed");
		logger.info("✅ Assert : close button \n" + "Once click on the close button popup should be close");

	}

	@Test(priority = 16, enabled = true)
	public void CBW003001000016() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000016)").assignAuthor("VS/482");
		logger.info("Verify the all the base is enabled");
		objdownload.forclickokbtn();
		objwait.waitS(3000);
		Assert.assertTrue(objdownload.checkcities_isenable(), "❌ Base not enable");
		logger.info("✅ Assert : All the base cities");
	}

	@Test(priority = 17, enabled = true)
	public void CBW003001000017() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000017)").assignAuthor("VS/482");
		logger.info("To check the positions are enabled state");
		Assert.assertTrue(objdownload.checkposition_isenable(), "❌Positions are disable");
		logger.info("✅ Assert : CP , FO and FA");
	}

	@Test(priority = 18, enabled = true)
	public void CBW003001000018() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000018)").assignAuthor("VS/482");
		logger.info("To check the next month in default condition");
		Assert.assertTrue(objdownload.isNextMonthSelectedByDefault(), "❌ The next month is not selected by default.");
		logger.info("✅ Assert: Next month is selected by default.");
	}

	@Test(priority = 19, enabled = true)
	public void CBW003001000019() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000019)").assignAuthor("VS/482");
		logger.info("Verify the Current month to previous months in disable state");
		Assert.assertTrue(objdownload.checkdisablemonths(),
				"❌ Some previous months are not in the disabled state as expected.");
		logger.info("✅ Assert: All previous months are in the disabled state.");

	}

	@Test(priority = 20, enabled = true)
	public void CBW003001000020() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000020)").assignAuthor("VS/482");
		logger.info("Verify the next month to upcoming month of the current year in enabled state");
		Assert.assertTrue(objdownload.verifyNextAndUpcomingMonthsEnabled(),
				"❌ Validation failed: Upcoming months are not enabled as expected.");
		logger.info("✅ Assert: Displayed only the enabled upcoming months successfully.");
	}

	@Test(priority = 21, enabled = true)
	public void CBW003001000021() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000021)").assignAuthor("VS/482");

	}

	@Test(priority = 22, enabled = true)
	public void CBW003001000022() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000022)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 01 : Download round 1 and pilot (CP and FO) with all the  domicile (4th day of all month)\n"
				+ "Note : For Pilot ( AUS and FLL) domicile  no need to download");
		Assert.assertTrue(objdownload.checkCondition1DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 23, enabled = true)
	public void CBW003001000023() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000023)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 02 : Download round 1 and  FA with all the  domicile(2nd day of all month)");
		Assert.assertTrue(objdownload.checkCondition2DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 24, enabled = true)
	public void CBW003001000024() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000024)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 03 : Download round 2 and pilot (CP and FO) with all the  domicile (17th day of all month)\n"
				+ "Note : For Pilot ( AUS and FLL) domicile  no need to download");
		Assert.assertTrue(objdownload.checkCondition3DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 25, enabled = true)
	public void CBW003001000025() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000025)").assignAuthor("VS/482");
		logger.info("Verify user can able to select any one of the below condition \n"
				+ "Condition 04 : Download round 2 and FA with all the  domicile(11th day of all month)");
		Assert.assertTrue(objdownload.checkCondition4DownloadBid(), "❌Download button is disable");
		logger.info("✅Assert: Download button is enabled ");
	}

	@Test(priority = 26, enabled = true)
	public void CBW003001000026() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000026)").assignAuthor("VS/482");
		logger.info("Verify user can able to view the \"Seniority list\" popup");
		objdownload.checkDownloadBid();
		objwait.waitS(5000);
		logger.info("Verify the loader is appers while download the bid");
		SoftAssert soft = new SoftAssert();
		// soft.assertTrue(objdownload.fordisplayloadingicon(), "❌ Load Icon not
		// display");
		objwait.waitS(5000);
		soft.assertEquals(objdownload.fordisplay_seniority(), "Seniority List", "❌ Mismatch header");
		logger.info("✅Assert : Seniority list popup appers");
		soft.assertAll();
	}

	@Test(priority = 27, enabled = true)
	public void CBW003001000027() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000027)").assignAuthor("VS/482");
		logger.info("Verify the Seniority list Title");
		Assert.assertEquals(objdownload.fordisplay_seniority(), "Seniority List", "❌ Mismatch header");
		logger.info("✅Assert : Seniority list Title");
	}

	@Test(priority = 28, enabled = true)
	public void CBW003001000028() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000028)").assignAuthor("VS/482");
		logger.info("Verify the buttons are appears");
		Assert.assertTrue(objdownload.fordisplaySEnbtninpopup(), "❌ Button not visible");
		logger.info("✅ Assert : View Seniority list and Cancel button appears");
	}

	@Test(priority = 29, enabled = true)
	public void CBW003001000029() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000029)").assignAuthor("VS/482");
		logger.info("Verify to expand the seniority list and check whether the seniority list is current month or not");
		objdownload.click_senioritylist();
		Assert.assertTrue(objdownload.checkMonthInSeniorityList(), "❌ Month in seniority list is different");
		logger.info("✅ Assert : check the seniority list is current month or not");
	}

	@Test(priority = 30, enabled = true)
	public void CBW003001000030() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000030)").assignAuthor("VS/482");
		logger.info("Verify the close button and share button in the Seniority list");
		Assert.assertTrue(objdownload.fordisplay_close_share_btn(), "❌Buttons not displayed");
		logger.info("✅Assert : close button, and share button \n");
	}

	@Test(priority = 31, enabled = true)
	public void CBW003001000031() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000031)").assignAuthor("VS/482");
		logger.info("Verify the print seniority list");
		Assert.assertTrue(objdownload.fordisplay_close_share_btn(), "❌Buttons not displayed");
		logger.info("✅ Assert : Email seniority list and Print Seniority list\n");
	}

	@Test(priority = 32, enabled = false)
	public void CBW003001000032() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000032)").assignAuthor("VS/482");
		logger.info("");
		logger.info("");
	}

	@Test(priority = 33, enabled = true)
	public void CBW003001000033() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000033)").assignAuthor("VS/482");
		logger.info("Verify the new tab to close");
		objdownload.click_close_sen_btn();
		objwait.waitS(3000);
		Assert.assertEquals(objdownload.checklatestnew_header(), "Latest News", "❌ Headers mismatch");
		logger.info("✅ Assert : close button");
	}

	@Test(priority = 34, enabled = true)
	public void CBW003001000034() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000034)").assignAuthor("VS/482");
		logger.info("Verify the latest news popup appers");
		Assert.assertEquals(objdownload.checklatestnew_header(), "Latest News", "❌ Headers mismatch");
		logger.info("✅Assert : Latest news ");
	}

	@Test(priority = 35, enabled = true)
	public void CBW003001000035() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000035)").assignAuthor("VS/482");
		logger.info("Assert : latest news close button\n");
		objwait.waitS(3000);
		objdownload.click_news_closebtn();
		objwait.waitS(3000);
		Assert.assertEquals(objdownload.checkcoverletter_head(), "Cover Letter", "❌ Headers mismatch");
		logger.info("✅Pop up should be closed and Cover letter should come");
	}

	@Test(priority = 36, enabled = true)
	public void CBW003001000036() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000036)").assignAuthor("VS/482");
		logger.info("Verify the cover letter Title");
		Assert.assertEquals(objdownload.checkcoverletter_head(), "Cover Letter", "❌ Headers mismatch");
		logger.info("✅Assert : cover letter");
	}

	@Test(priority = 37, enabled = true)
	public void CBW003001000037() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000037)").assignAuthor("VS/482");
		logger.info("Verify the cover letter Close button");
		objdownload.checklinenumber();
		objwait.waitS(3000);
		objdownload.click_close_coverletter();
		objwait.waitS(3000);
		Assert.assertTrue(objdownload.isvisible_scrachpad_head(), "❌ Close button functionality not working");
		logger.info("✅Assert : Cover letter close button");
	}

	@Test(priority = 38, enabled = true)
	public void CBW003001000038() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000038)").assignAuthor("VS/482");
		logger.info(
				"To check the heading label \"(version number) SelectedMonth selectedyear Captain Round 1 - Network Status\"");
		Assert.assertTrue(objdownload.isvisible_scrachpad_head(), "❌Scratchpad head not visible");
		logger.info(
				"✅Assert the Heading \"(version number) SelectedMonth selectedyear Captain Round 1 - Online\" and Colour (White)");
	}

	@Test(priority = 39, enabled = true)
	public void CBW003001000039() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000039)").assignAuthor("VS/482");
		logger.info("To check the network status in Online mode");
		Assert.assertTrue(objdownload.checkoffline(), "❌Network in offline mode ");
		logger.info("✅Assert : Scratch pad title with Online should show");
	}

	@Test(priority = 40, enabled = true)
	public void CBW003001000040() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000040)").assignAuthor("VS/482");
		logger.info("To check the flag icon is visible");
		Assert.assertTrue(objdownload.isvisibleflagicon(), "❌ Icon not visible");
		logger.info("✅ Assert : flag icon is visible");
	}

	@Test(priority = 41, enabled = true)
	public void CBW003001000041() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000041)").assignAuthor("VS/482");
		logger.info("To check the local/herb icon ");
		Assert.assertTrue(objdownload.isvisibleherb_local_icon(), "❌ Icon not visible");
		logger.info("✅Assert : Herb/local button");
	}

	@Test(priority = 42, enabled = true)
	public void CBW003001000042() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000042)").assignAuthor("VS/482");
		logger.info("To check the Settings icon is visible");
		Assert.assertTrue(objdownload.isvisibleherbsettingicon(), "❌ Icon not visible");
		logger.info("✅ Assert : Settings menu icon visible");
	}

	@Test(priority = 43, enabled = true)
	public void CBW003001000043() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000043)").assignAuthor("VS/482");
		logger.info("To check to scroll icon is visible");
		Assert.assertTrue(objdownload.isvisiblescrolldown_icon(), "❌ Icon not visible");
		logger.info("✅ Assert : Scroll to line icin visible");
	}

	@Test(priority = 44, enabled = true)
	public void CBW003001000044() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000044)").assignAuthor("VS/482");
		logger.info("To check the Move all the line icon is visible");
		Assert.assertTrue(objdownload.isvisiblemove_icon(), "❌ Icon not visible");
		logger.info("✅ Assert : Move all the line is visible");
	}

	@Test(priority = 45, enabled = true)
	public void CBW003001000045() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000045)").assignAuthor("VS/482");
		logger.info("To check the home button icon is visble");
		Assert.assertTrue(objdownload.isvisiblehome_icon(), "❌ Icon not visible");
		logger.info("✅ Assert : home button is visible");
	}

	@Test(priority = 46, enabled = true)
	public void CBW003001000046() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000046)").assignAuthor("VS/482");
		logger.info("To check the help menu icon is visible");
		Assert.assertTrue(objdownload.isvisiblehelp_icon(), "❌ Icon not visible");
		logger.info("✅ Assert : help menu icon");
	}

	@Test(priority = 47, enabled = true)
	public void CBW003001000047() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000047)").assignAuthor("VS/482");
		logger.info("To check the bid action button is visible");
		Assert.assertTrue(objdownload.isvisiblebidaction_icon(), "❌ Icon not visible");
		logger.info("✅ Assert : bid action button");
	}

	@Test(priority = 48, enabled = true)
	public void CBW003001000048() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000048)").assignAuthor("VS/482");
		logger.info("To check the save button is default disabled state");
		Assert.assertTrue(objdownload.checksaveisdisable(), "❌ Save icon is enable");
		logger.info("✅ Assert : save button\n" + "Disabled");
	}

	@Test(priority = 49, enabled = true)
	public void CBW003001000049() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000049)").assignAuthor("VS/482");
		logger.info("Verify the user can select the Arrow button");
		Assert.assertTrue(objdownload.verifymovearrowclickable(), "❌ Icon not clickable");
		logger.info("✅Assert : Arrow button\n" + "Button should be clickable");
	}

	@Test(priority = 50, enabled = true)
	public void CBW003001000050() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000050)").assignAuthor("VS/482");
		logger.info("Verify all the lines moved in the scractpad to bidlist");
		Assert.assertTrue(objdownload.checklinemovestobid(), "❌ Bid Count not changed");
		logger.info("✅ Assert : Bidlsit count should be the previous count of the scratchpad view");
	}

	@Test(priority = 51, enabled = true)
	public void CBW003001000051() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000051)").assignAuthor("VS/482");
		logger.info("Verify the ellipsis icon");
		Assert.assertTrue(objdownload.forvisibleellipisicon(), "❌ Ellipis icon not displayed");
		logger.info("✅ Assert : Ellipsis icon");
	}

	@Test(priority = 52, enabled = true)
	public void CBW003001000052() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000052)").assignAuthor("VS/482");
		logger.info("Check line Number");
		objdownload.startOver();
		objwait.waitS(4000);
		Assert.assertTrue(objdownload.checkLineNumberFromScratchpad(), "❌ Scratch pad combination not matches");
		logger.info("✅Scratch pad combination matches");
	}

	@Test(priority = 53, enabled = true)
	public void CBW003001000053() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000053)").assignAuthor("VS/482");
		logger.info("Check Reserve lines count");
		Assert.assertTrue(objdownload.checkreservelines(), "❌ No Reserve Lines Found.");
		logger.info("✅ Reserve line count matches ");
	}

	@Test(priority = 54, enabled = true)
	public void CBW003001000054() throws JsonProcessingException {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000054)").assignAuthor("VS/482");
		logger.info("Check Blank lines count");
		ScratchPadBlankReservedLines.fetchApiData("ATL", "1", "CP", "4");
		Assert.assertTrue(objdownload.checkblankAPIline(), "❌ No Blank Lines Found.");
		logger.info("✅ Blank line count matches ");
	}

	@Test(priority = 55, enabled = true)
	public void CBW003001000055() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000055)").assignAuthor("VS/482");
		logger.info("");
		// objdownload.linecount();
		logger.info("✅");
	}

	@Test(priority = 56, enabled = true)
	public void CBW003001000056() {
		logger = extent.createTest("BID DATA DOWNLOAD (CBW003001000056)").assignAuthor("VS/482");
		logger.info("Check Reserve lines count");
		Assert.assertTrue(objdownload.checkreserveAPIlines(), "❌ No Reserve Lines Found.");
		logger.info("✅ Reserve line count matches ");
	}

}
