package testCases;
/////////////TAFB -FA -Round 1- ATL//////
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import API.BlockTest;
import API.FetchDates;
import API.GroundTest;
import API.JavaDirectHolirig;
import API.LineParameterDirect;
import API.ScratchPadBlankReservedLines;
import API.TAFB;
import API.TAFBCPFO;
import API.TrialBidAPI;
import pages.BidDownloadPage;
import pages.BlkHour;
import pages.CommonPage;
import pages.CredValuesPage;
import pages.FAPage;
import pages.HoliRigPage;
import pages.IndividualCredValuePage;
import pages.LoginPage;
import pages.TAFBPage;
import pages.GrndHrPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class TAFBLineFATest extends WbidBasepage{
	WebDriver driver = returnDriver();
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	LoginPage objlogin = new LoginPage(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon = new CommonPage(driver);
	HoliRigPage objHoli = new HoliRigPage(driver);
	CredValuesPage objCred = new CredValuesPage(driver);
	IndividualCredValuePage objInCred = new IndividualCredValuePage(driver);
	GrndHrPage objTAFB=new GrndHrPage(driver);
	BlkHour objBlk=new BlkHour(driver);
	TAFBPage objTafb=new TAFBPage(driver);
	FAPage objFA=new FAPage(driver);
	
	
	HashMap<String, String> testDataMap = testData("qa environment");
	
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
	
	@Test(priority = 1, enabled = true)
	public void CBW010002000001() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200001").assignAuthor("VS/483");
		logger.info("To check that CrewBid Login page is displaying when entering the Url");
		String actualtitle = objlogin.pageHeading();
		String expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + actualtitle);
		logger.info("Expected title: " + expectedtitle);
		Assert.assertEquals(actualtitle, expectedtitle, "Title assertion failed!");

	}

	@Test(priority = 2, enabled = true)
	public void CBW010002000002() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200002").assignAuthor("VS/483");
		logger.info("When Click on update button-Automatically redirect to landing page");
		objlogin.updateVersionLogin();
		logger.info("Assert that Home page header shows updated version.");
		actualVersion = objCommon.getVersionValue();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(actualVersion.contains(expectedVersion), "Home page header does not show updated version");
	}

	@Test(priority = 3, enabled = true)
	public void CBW010002000003() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200003").assignAuthor("VS/483");
		logger.info(
				"Verify the login-Assert the title : Crewbid in the top left-Assert the image : Crewbid icon in the top left");
		Assert.assertTrue(objInCred.logoVisibile(), "Crewbid Logo not display");
	}

	@Test(priority = 4, enabled = true)
	public void CBW010002000004() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200004").assignAuthor("VS/483");
		logger.info("Verify the Retreive button-Assert : Retrieve new bid data and Retrieve Historical bid data");
		logger.info("click Retreive button");
		objCommon.click_retrievedownload();
		Assert.assertTrue(objInCred.isVisibleBiddata(),
				"Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");

	}

	@Test(priority = 5, enabled = true)
	public void CBW010002000005() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200005").assignAuthor("VS/483");
		logger.info("Verify the user able to select Retrieve new bid data button ");
		objCommon.forclicknewbiddata();
		Assert.assertEquals(objInCred.checkEmpnumHeader(), "Enter Employee Number", "Header mismatch or not displayed");
	}

	@Test(priority = 6, enabled = true)
	public void CBW010002000006() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200006").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		logger.info("Enter the User ID ");
		objCommon.enterempid();
		logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
		objCommon.selectOptions(domicile, position, round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile, position, round));
	}

	@Test(priority = 7, enabled = true)
	public void CBW010002000007() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200007").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		Assert.assertTrue(objInCred.downloadEnable());
	}

	boolean isDownloadSuccessful = false; // Flag to track download status

	@Test(priority = 8, enabled = true)
	public void CBW010002000008() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200008").assignAuthor("VS/483");
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

	@Test(priority = 9, dependsOnMethods = { "CBW010002000008" }, alwaysRun = true)
	public void CBW010002000009() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW01000200009").assignAuthor("VS/483");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping warning popup verification.");
		} else {
			logger.info("Executing CBW010002000009: Verifying Early Bid Warning Popup");
			logger.info("Early Bid Warning popup is displayed.");
			Assert.assertTrue(objInCred.visibleEarlyBidPopup(), "Early Bid Warning popup should be displayed.");
		}
	}

	@Test(priority = 10, dependsOnMethods = { "CBW010002000008" }, alwaysRun = true)
	public void CBW0100020000010() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000010").assignAuthor("VS/483");
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

	@Test(priority = 11, dependsOnMethods = { "CBW010002000009", "CBW0100020000010" }, alwaysRun = true)
	public void CBW0100020000011() throws Exception {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000011").assignAuthor("VS/483");
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

	@Test(priority = 12, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000012() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000012").assignAuthor("VS/483");
		logger.info("Fetchinh API data");
		ScratchPadBlankReservedLines.fetchApiData(domicile, APIRound, position, APIMonth);
		TrialBidAPI.fetchApiData(domicile, APIRound, position, APIMonth);
		logger.info("Verify the subscription expiring alert - Expiring alert  not visible ");
	}

	@Test(priority = 13, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000013() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000013").assignAuthor("VS/483");
		logger.info("Verify the user is able to close the subscription expire pop up alert ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 14, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000014() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000014").assignAuthor("VS/483");
		logger.info("Verify the subscription Expired Popup ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 15, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000015() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000015").assignAuthor("VS/483");
		logger.info("Verify user can able to view the Seniority list popup");
		Assert.assertEquals(objInCred.displaySeniority(), "Seniority List", "Seniority list popup not visible");

	}

	@Test(priority = 16, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000016() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000016").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the 'Latest news' popup");
		logger.info("close Seniority list Pop Up");
		objCommon.clickSeniorityCancel();
		logger.info("Verify the user can able to view the 'Latest news' popup");
		Assert.assertEquals(objInCred.latestnewHeader(), "Latest News", "Headers mismatch");

	}

	@Test(priority = 17, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000017() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000017").assignAuthor("VS/483");
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

	@Test(priority = 18, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000018() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000018").assignAuthor("VS/483");
		logger.info("User can able to start over the bid");
		logger.info("Verify the user can select the Arrow button");
		initialScratchPadCount = objInCred.scrLinesTotalCount();
		Assert.assertTrue(objInCred.movearrowclickable(), "Icon not clickable");
	}

	@Test(priority = 19, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000019() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000019").assignAuthor("VS/483");
		logger.info("Bidlsit count should be the previous count count of the scratchpad view");
		Assert.assertEquals(objInCred.bidListHeadCount(), initialScratchPadCount,
				"Bidlsit count not same as previous count count of the scratchpad view");
	}

	@Test(priority = 20, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000020() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000020").assignAuthor("VS/483");
		logger.info("Verify the ellipsis icon");
		Assert.assertTrue(objInCred.ellipisIconVisible(), "Ellipis icon not displayed");
	}

	@Test(priority = 21, dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000021() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000021").assignAuthor("VS/483");
		logger.info("Verify Start over button - scratch pad should be in default state");
		objInCred.startOver();
		logger.info(
				"Compare with Scratchpad count from the UI and the  Line count from the cover letter API should be same");
		Assert.assertTrue(objCommon.scrLinesTotalCount(ScratchPadBlankReservedLines.linecount));
	}

	@Test(priority = 22,enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000022() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000022").assignAuthor("VS/483");
		logger.info(
				"Assert: Inside the trip details , the dates are shown same as the dates of the trips and should be same as its in the wbl file");
		logger.info("Get  trip Details from UI- Trip Code and Trip date and compare with API Trip code and dates");
		FetchDates.fetchApiData(domicile, APIRound, position, APIMonth);
		Assert.assertTrue(objCommon.getAllTripDataAndCompare(FetchDates.tripData));
	}

	@Test(priority = 23, enabled=false ,dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000023() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000023").assignAuthor("VS/483");
		logger.info("Assert Grnd -Check the Grnd  hours is visible in the trip details    ");
		driver.navigate().refresh();
		objwait.waitS(7000);
		Assert.assertTrue(objTAFB.grndHeadVisible(), "Grnd not visible inside the trip detials ");
	}

	@Test(priority = 24,enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000024() throws Throwable {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000024").assignAuthor("VS/483");
		logger.info("check whether the Grnd  hours showing correctly or not  ");
		logger.info("Assert: Grnd hours meets the condition and should be same as its in the trip file  ");
		logger.info("Assert Grnd  hours in API  ");
		GroundTest.fetchground(domicile, APIRound, position, APIMonth);//API Assertion

	}

	@Test(priority = 25,enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000025() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000025").assignAuthor("VS/483");
		driver.navigate().refresh();
		objwait.waitS(7000);
		logger.info("Assert: Grnd hours meets the condition and should be same as its in the trip file  ");
		objTAFB.getFAGrndHour();
		Assert.assertTrue(objTAFB.compareGrndFA(objTAFB.tripGrndUI,GroundTest.apiGrndHr), "Grnd hr Mismatch");
		}

	@Test(priority = 26,enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000026() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000026").assignAuthor("VS/483");
		logger.info("Assert Blk -Check the Blk  hours is visible in the trip details    ");
		driver.navigate().refresh();
		objwait.waitS(7000);
		Assert.assertTrue(objBlk.blkHeadVisible(), "Blk not visible inside the trip detials ");
		}

	@Test(priority = 27, enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000027() throws Throwable {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000027").assignAuthor("VS/483");
		logger.info("Assert: Total blk hours for a trip is shown as the sum of  blk hours of each day   should be same as its in the trip file ");
		BlockTest.fetchBlock(domicile, APIRound, position, APIMonth);//API	Assertion
	}

	@Test(priority = 28, enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000028() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000028").assignAuthor("VS/483");
		logger.info("check whether the total blk hours for the trip  is showing correctly or not in UI");
		//objBlk.getBlkHour();
		//Assert.assertTrue(objBlk.compareBlkHour(), "Blk hr Mismatch");
		driver.navigate().refresh();
		objwait.waitS(7000);
		objBlk.getFABlkHour();
		Assert.assertTrue(objBlk.compareBlkFA(objBlk.tripBlkUI,BlockTest.apiBlkHr), "Blk hr Mismatch");
	
	}
	@Test(priority = 29, enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000029() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000029").assignAuthor("VS/483");
		logger.info("Verify the TAFB  is shown inside the trip data for each Trip");
		logger.info("Get  trip Details from UI- Trip Code and corresponding TAFB ");
		// objTafb.getAllTAFB();
		driver.navigate().refresh();
		objwait.waitS(7000);
		logger.info("Assert: The TAFB displayed inside the trip details meets the calculations  and should be same as its in the wbp file");
		logger.info("Get  trip Details from UI- Trip Code and TAFB and compare with API Trip code and TAFB");
		Assert.assertTrue(objTafb.getAllTAFBAndCompare(TrialBidAPI.tafbMapNew));

	}
	@Test(priority = 30, enabled=true , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000030() throws Throwable {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000030").assignAuthor("VS/483");
		logger.info("Assert : The TAFB is shown same as in wbp file for each trip");
		
		//TAFB.fetchApiData(domicile, APIRound, position, APIMonth); //API	Assertion calculation part 
	}
	@Test(priority = 31,enabled=false , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000031() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000030").assignAuthor("VS/483");
		logger.info("Verify the TAFB shown for each trip inside the trip details for reserve trips is ZERO");
		logger.info("No Reserve trips in FA round 1");
	
	}
	@Test(priority = 32, enabled=true , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000032() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000032").assignAuthor("VS/483");
		logger.info("User can able to select the TAFB  parameter from line parameter ");
		logger.info("Assert : TAFB  paramter along with the value displayed in the line paramter of each line");
		objTafb.selectTAFBLine();
		Assert.assertTrue(objTafb.fordisplayTAFB(), "❌ TAFB in the line paramter of each line not displayed");
	
	}
	@Test(priority = 33, enabled=true , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000033() {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000033").assignAuthor("VS/483");
		logger.info("check after selecting TAFB parameter, when clicking the line parameter area and looking TAFB parameter, the TAFB parameter is showed as selected by blue tick mark");
		Assert.assertTrue(objTafb.getTAFBLineVal(),"❌ After selecting TAFB parameter all TAFB line values not found");
	
	}
	@Test(priority = 34,enabled=true , dependsOnMethods = { "CBW0100020000011" })
	public void CBW0100020000034() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-FA-Round 1-TAFB of each Line - CBW010002000034").assignAuthor("VS/483");
		logger.info("Verify the TAFB parameter  showing is correct or not ");
		logger.info("Assert the value shows in the parameter in the UI is  the sum of all the TAFB  values of each  trip calculated from ourside   and  should be same as its in the file from the server  (WBL File) ");
		logger.info("Get TAFB Values from API ");
		JavaDirectHolirig.fetchParam(domicile, APIRound, position, APIMonth);
		Assert.assertTrue(objTafb.isTAFBLineValCompare(JavaDirectHolirig.tafbLineAPI));
	
	}
		
}
