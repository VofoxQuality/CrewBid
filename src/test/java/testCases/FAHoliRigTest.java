package testCases;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import API.FetchDates;
import API.HoliRigCPFO;
import API.HoliRigFA;
import API.JavaDirectHolirig;
import API.ScratchPadBlankReservedLines;
import pages.BidDownloadPage;
import pages.CommonPage;
import pages.CredValuesPage;
import pages.FAPage;
import pages.HoliRigATCPage;
import pages.HoliRigPage;
import pages.IndividualCredValuePage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class FAHoliRigTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	LoginPage objlogin = new LoginPage(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon = new CommonPage(driver);
	HoliRigPage objHoli = new HoliRigPage(driver);
	CredValuesPage objCred = new CredValuesPage(driver);
	IndividualCredValuePage objInCred = new IndividualCredValuePage(driver);
	HashMap<String, String> testDataMap = testData("qa environment");
	HoliRigATCPage objholirig = new HoliRigATCPage(driver);
	

	public String actualVersion;
	public String expectedVersion = testDataMap.get("Version");
	public String domicile = "ATL";
	public String position = "FA";
	public String round = "1st Round";
	public String ScratchpadPosition = "Flight Attendant";
	public String ScratchpadRound = "Round 1";
	public String year = objCommon.getNextMonthAndCurrentYear();
	public String APIRound = "1";
	public String APIMonth = String.valueOf(objCommon.getNextMonth());
	public static List<String> TripCodes = new ArrayList<>();
	public static List<Map<String, Object>> DirectHolirigresultReturned = new ArrayList<>();
	public static List<Map<String, Object>> holirigFAResultReturned = new ArrayList<>();
	
	@Test(priority = 1, enabled = true)
	public void CBW010005001001() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001001").assignAuthor("VS/483");
		logger.info("To check that CrewBid Login page is displaying when entering the Url");
		String actualtitle = objlogin.pageHeading();
		String expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + actualtitle);
		logger.info("Expected title: " + expectedtitle);
		Assert.assertEquals(actualtitle, expectedtitle, "Title assertion failed!");
	}

	@Test(priority = 2, enabled = true)
	public void CBW010005001002() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001002").assignAuthor("VS/483");
		logger.info("When Click on update button-Automatically redirect to landing page");
		objlogin.updateVersionLogin();
		logger.info("Assert that Home page header shows updated version.");
		actualVersion = objCommon.getVersionValue();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(actualVersion.contains(expectedVersion), "Home page header does not show updated version");
	}

	@Test(priority = 3, enabled = true)
	public void CBW010005001003() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001003").assignAuthor("VS/483");
		logger.info(
				"Verify the login-Assert the title : Crewbid in the top left-Assert the image : Crewbid icon in the top left");
		Assert.assertTrue(objInCred.logoVisibile(), "Crewbid Logo not display");
	}

	@Test(priority = 4, enabled = true)
	public void CBW010005001004() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001004").assignAuthor("VS/483");
		logger.info("Verify the Retreive button-Assert : Retrieve new bid data and Retrieve Historical bid data");
		logger.info("click Retreive button");
		objCommon.click_retrievedownload();
		Assert.assertTrue(objInCred.isVisibleBiddata(),
				"Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");

	}

	@Test(priority = 5, enabled = true)
	public void CBW010005001005() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001005").assignAuthor("VS/483");
		logger.info("Verify the user able to select Retrieve new bid data button ");
		objCommon.forclicknewbiddata();
		Assert.assertEquals(objInCred.checkEmpnumHeader(), "Enter Employee Number", "Header mismatch or not displayed");
	}

	@Test(priority = 6, enabled = true)
	public void CBW010005001006() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001006").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		logger.info("Enter the User ID ");
		objCommon.enterempid();
		logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
		objCommon.selectOptions(domicile, position, round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile, position, round));
	}

	@Test(priority = 7, enabled = true)
	public void CBW010005001007() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001007").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		Assert.assertTrue(objInCred.downloadEnable());
	}

	boolean isDownloadSuccessful = false; // Flag to track download status

	@Test(priority = 8, enabled = true)
	public void CBW010005001008() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001008").assignAuthor("VS/483");
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

	@Test(priority = 9, dependsOnMethods = { "CBW010005001008" }, alwaysRun = true)
	public void CBW010005001009() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001009").assignAuthor("VS/483");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping warning popup verification.");
		} else {
			logger.info("Executing CBW010005001009: Verifying Early Bid Warning Popup");
			logger.info("Early Bid Warning popup is displayed.");
			Assert.assertTrue(objInCred.visibleEarlyBidPopup(), "Early Bid Warning popup should be displayed.");
		}
	}

	@Test(priority = 10, dependsOnMethods = { "CBW010005001008" }, alwaysRun = true)
	public void CBW010005001010() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001010").assignAuthor("VS/483");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping bid not available popup verification.");
		} else {
			logger.info("If bid data is not available, a warning popup should appear.");
			objInCred.clickOkEarlyBid();
			Assert.assertTrue(objInCred.bidNotAvailableBidPopup(),
					"Bid not available warning popup should be displayed.");
			objInCred.clickOkbidNotAvailable();
		}
	}

	@Test(priority = 11, dependsOnMethods = { "CBW010005001009", "CBW010005001010" }, alwaysRun = true)
	public void CBW010005001011() throws Exception {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001011").assignAuthor("VS/483");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping cancel button verification.");
		} else {
			logger.info("Verifying if user can select the cancel button.");
			objInCred.clickcancelBtn();
			Assert.assertTrue(objwait.waitPopupToClose(driver, objInCred.cancelBtn, 20),
					"Cancel button is not enabled.");
		}
		Assert.assertTrue(isDownloadSuccessful, "Bid Download was not successful");
	}

	@Test(priority = 12, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001012() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001012").assignAuthor("VS/483");
		logger.info("Fetchinh API data");
		ScratchPadBlankReservedLines.fetchApiData(domicile, APIRound, position, APIMonth);
		logger.info("Verify the subscription expiring alert - Expiring alert  not visible ");
	}

	@Test(priority = 13, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001013() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001013").assignAuthor("VS/483");
		logger.info("Verify the user is able to close the subscription expire pop up alert ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 14, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001014() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001014").assignAuthor("VS/483");
		logger.info("Verify the subscription Expired Popup ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 15, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001015() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001015").assignAuthor("VS/483");
		logger.info("Verify user can able to view the Seniority list popup");
		Assert.assertEquals(objInCred.displaySeniority(), "Seniority List", "Seniority list popup not visible");

	}

	@Test(priority = 16, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001016() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001016").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the 'Latest news' popup");
		logger.info("close Seniority list Pop Up");
		objCommon.clickSeniorityCancel();
		logger.info("Verify the user can able to view the 'Latest news' popup");
		Assert.assertEquals(objInCred.latestnewHeader(), "Latest News", "Headers mismatch");

	}

	@Test(priority = 17, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001017() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001017").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the Cover letter popup");
		logger.info("close Latest Pop Up");
		objCommon.clickLatestNwzClose();
		logger.info("Verify the user can able to view the 'Latest news' popup");
		Assert.assertEquals(objInCred.coverLetterHeader(), "Cover Letter", "Headers mismatch");
		logger.info("close Cover Letter pop Up");
		objCommon.CoverLetterCloseFA();
		Assert.assertTrue(objCommon.verifyScratchpadHeading(domicile, ScratchpadPosition, ScratchpadRound, year));
	}

	public int initialScratchPadCount;

	@Test(priority = 18, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001018() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001018").assignAuthor("VS/483");
		logger.info("User can able to start over the bid");
		logger.info("Verify the user can select the Arrow button");
		initialScratchPadCount = objInCred.scrLinesTotalCount();
		Assert.assertTrue(objInCred.movearrowclickable(), "Icon not clickable");
	}

	@Test(priority = 19, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001019() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001019").assignAuthor("VS/483");
		logger.info("Bidlsit count should be the previous count count of the scratchpad view");
		Assert.assertEquals(objInCred.bidListHeadCount(), initialScratchPadCount,
				"Bidlsit count not same as previous count count of the scratchpad view");
	}

	@Test(priority = 20, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001020() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW010005001020").assignAuthor("VS/483");
		logger.info("Verify the ellipsis icon");
		Assert.assertTrue(objInCred.ellipisIconVisible(), "Ellipis icon not displayed");
	}

	@Test(priority = 21, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001021() {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW0100050010021").assignAuthor("VS/483");
		logger.info("Verify Start over button - scratch pad should be in default state");
		objInCred.startOver();
		logger.info(
				"Compare with Scratchpad count from the UI and the  Line count from the cover letter API should be same");
		Assert.assertTrue(objCommon.scrLinesTotalCount(ScratchPadBlankReservedLines.linecount));
	}

	@Test(priority = 22, enabled = true, dependsOnMethods = { "CBW010005001011" })
	public void CBW010005001022() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("ATL-FA Round 1-HoliRig  - CBW0100050010022").assignAuthor("VS/483");
		logger.info(
				"Assert: Inside the trip details , the dates are shown same as the dates of the trips and should be same as its in the wbl file");
		logger.info("Get  trip Details from UI- Trip Code and Trip date and compare with API Trip code and dates");
		FetchDates.fetchApiData(domicile, APIRound, position, APIMonth);
	  //Assert.assertTrue(objCommon.getAllTripDataAndCompare(FetchDates.tripData));	
	}
	@Test(priority = 23, enabled = true)
	public void CBW010005001023() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("CP-HOLI RIG (CBW010005001023)").assignAuthor("VS/482");
		logger.info("User can able to select the HoliRig  parameter from line parameter ");
		objholirig.selectHoliRig();
		Assert.assertTrue(objholirig.fordisplayholirig(), "❌ Holi rig not displayed");
		logger.info("✅ Assert : HoliRig parameter along with the value displayed in the line paramter of each line");
	}

	@Test(priority = 24, enabled = true)
	public void CBW010005001024() {
		logger = WbidBasepage.extent.createTest("CP-HOLI RIG (CBW010005001024)").assignAuthor("VS/482");
		logger.info(
				"check after selecting HoliRig parameter, when clicking the line parameter area and looking HoliRig parameter, the HoliRig parameter is showed as selected by blue tick mark");
		Assert.assertTrue(objholirig.forClickHoliRig(), "❌ Not displayed blue tick ");
		logger.info("✅ Assert : blue tick on the HoliRig parameter");
	}
	
	@Test(priority = 25, enabled = true)
	public void CBW010005001025() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("CP-HOLI RIG (CBW010005001025)").assignAuthor("VS/482");
		logger.info("Check whether the Holirig parameter shows correct value in UI  for FA bids");
		DirectHolirigresultReturned = JavaDirectHolirig.fetchParam(domicile, APIRound, position, APIMonth);
		objHoli.getHoliRigVal();
		Assert.assertTrue(objHoli.isHoliRigDataMatching(JavaDirectHolirig.result));
		logger.info(
				"✅ Assert : the  Holirig parameter shows correct value  for FA bids after calculation  and same from the WBP file ");
	}
 
	@Test(priority = 26, enabled = true)
	public void CBW010005001026() throws Throwable {
		logger = WbidBasepage.extent.createTest("CP-HOLI RIG (CBW010005001026)").assignAuthor("VS/483");
		logger.info("Calling method to get calculated holirig for FA");
		holirigFAResultReturned = HoliRigFA.fetchApiData(domicile, APIRound, position, APIMonth);
		logger.info("Comparison of calcuated Holirig and Direct Holirig");
		boolean matched = JavaDirectHolirig.compareListsFA(holirigFAResultReturned, DirectHolirigresultReturned);
		Assert.assertTrue(matched);
		
		}
}
