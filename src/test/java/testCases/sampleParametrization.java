package testCases;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import API.CPBIDCalculation;
import API.ScratchPadBlankReservedLines;
import pages.BidDownloadPage;
import pages.CommonPage;
import pages.CredValuesPage;
import pages.FAPage;
import pages.HoliRigPage;
import pages.IndividualCredValuePage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class sampleParametrization extends WbidBasepage {
	WebDriver driver;
	WaitCondition objwait;
	ActionUtilities objaction;
	LoginPage objlogin;
	BidDownloadPage objdownload;
	CommonPage objCommon;
	HoliRigPage objHoli;
	CredValuesPage objCred;
	IndividualCredValuePage objInCred;
	FAPage objFACred;
	HashMap<String, String> testDataMap;
	

	public String actualVersion;
	public String expectedVersion;
	public String domicile;
	public String position = "CP";
	public String round = "1st Round";
	public String ScratchpadPosition = "Captain";
	public String ScratchpadRound = "Round 1";
	public String year;
	public String APIRound = "1";
	public String APIMonth;
	public static List<String> TripCodes = new ArrayList<>();
	
	@Parameters({ "domicile" })  // this tells TestNG to inject the parameter
	@BeforeClass(alwaysRun = true)
	public void setup(@Optional("ATL") String domicile) {
		this.domicile = domicile;

		// Now safely initialize after domicile is set
		this.driver = returnDriver(); 
		this.objwait = new WaitCondition();
		this.objaction = new ActionUtilities(driver);
		this.objlogin = new LoginPage(driver);
		this.objdownload = new BidDownloadPage(driver);
		this.objCommon = new CommonPage(driver);
		this.objHoli = new HoliRigPage(driver);
		this.objCred = new CredValuesPage(driver);
		this.objInCred = new IndividualCredValuePage(driver);
		this.objFACred = new FAPage(driver);

		this.testDataMap = testData("qa environment");
		this.expectedVersion = testDataMap.get("Version");
		this.year = objCommon.getNextMonthAndCurrentYear();
		this.APIMonth = String.valueOf(objCommon.getNextMonth());
	}


	

	@Test(priority = 1, enabled = true)
	public void CBW010003000001() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300001").assignAuthor("VS/483");
		logger.info("To check that CrewBid Login page is displaying when entering the Url");
		String actualtitle = objlogin.pageHeading();
		String expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + actualtitle);
		logger.info("Expected title: " + expectedtitle);
		Assert.assertEquals(actualtitle, expectedtitle, "Title assertion failed!");

	}

	@Test(priority = 2, enabled = true)
	public void CBW010003000002() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300002").assignAuthor("VS/483");
		logger.info("When Click on update button-Automatically redirect to landing page");
		objlogin.updateVersionLogin();
		logger.info("Assert that Home page header shows updated version.");
		actualVersion = objCommon.getVersionValue();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(actualVersion.contains(expectedVersion), "Home page header does not show updated version");
	}

	@Test(priority = 3, enabled = true)
	public void CBW010003000003() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300003").assignAuthor("VS/483");
		objCommon.clickOk();//handle subscription pop up
		logger.info(
				"Verify the login-Assert the title : Crewbid in the top left-Assert the image : Crewbid icon in the top left");
		Assert.assertTrue(objInCred.logoVisibile(), "Crewbid Logo not display");
	}

	@Test(priority = 4, enabled = true)
	public void CBW010003000004() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300004").assignAuthor("VS/483");
		logger.info("Verify the Retreive button-Assert : Retrieve new bid data and Retrieve Historical bid data");
		logger.info("click Retreive button");
		objCommon.click_retrievedownload();
		Assert.assertTrue(objInCred.isVisibleBiddata(),
				"Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");

	}

	@Test(priority = 5, enabled = true)
	public void CBW010003000005() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300005").assignAuthor("VS/483");
		logger.info("Verify the user able to select Retrieve new bid data button ");
		objCommon.forclicknewbiddata();
		Assert.assertEquals(objInCred.checkEmpnumHeader(), "Enter Employee Number", "Header mismatch or not displayed");
	}

	@Test(priority = 6, enabled = true)
	public void CBW010003000006() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300006").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		logger.info("Enter the User ID ");
		objCommon.enterempid();
		logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
		objCommon.selectOptions(domicile, position, round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile, position, round));
	}

	@Test(priority = 7, enabled = true)
	public void CBW010003000007() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300007").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		// objCommon.selectOptions("AUS","CP","2nd Round");
		Assert.assertTrue(objInCred.downloadEnable());
	}

	boolean isDownloadSuccessful = false; // Flag to track download status

	@Test(priority = 8, enabled = true)
	public void CBW010003000008() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300008").assignAuthor("VS/483");
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

	@Test(priority = 9, dependsOnMethods = { "CBW010003000008" }, alwaysRun = true)
	public void CBW010003000009() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW01000300009").assignAuthor("VS/483");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping warning popup verification.");
		} else {
			logger.info("Executing CBW010003000009: Verifying Early Bid Warning Popup");
			logger.info("Early Bid Warning popup is displayed.");
			Assert.assertTrue(objInCred.visibleEarlyBidPopup(), "Early Bid Warning popup should be displayed.");
		}
	}

	@Test(priority = 10, dependsOnMethods = { "CBW010003000008" }, alwaysRun = true)
	public void CBW0100030000010() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000010").assignAuthor("VS/483");
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

	@Test(priority = 11, dependsOnMethods = { "CBW010003000009", "CBW0100030000010" }, alwaysRun = true)
	public void CBW0100030000011() throws Exception {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000011").assignAuthor("VS/483");
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

	@Test(priority = 12, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000012() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000012").assignAuthor("VS/483");
		logger.info("Fetchinh API data");
		ScratchPadBlankReservedLines.fetchApiData(domicile, APIRound, position, APIMonth);
		logger.info("Verify the subscription expiring alert - Expiring alert  not visible ");
	}

	@Test(priority = 13, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000013() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000013").assignAuthor("VS/483");
		logger.info("Verify the user is able to close the subscription expire pop up alert ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 14, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000014() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000014").assignAuthor("VS/483");
		logger.info("Verify the subscription Expired Popup ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 15, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000015() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000015").assignAuthor("VS/483");
		logger.info("Verify user can able to view the Seniority list popup");
		Assert.assertEquals(objInCred.displaySeniority(), "Seniority List", "Seniority list popup not visible");

	}

	@Test(priority = 16, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000016() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000016").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the 'Latest news' popup");
		logger.info("close Seniority list Pop Up");
		objCommon.clickSeniorityCancel();
		logger.info("Verify the user can able to view the 'Latest news' popup");
		Assert.assertEquals(objInCred.latestnewHeader(), "Latest News", "Headers mismatch");

	}

	@Test(priority = 17, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000017() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000017").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the Cover letter popup");
		logger.info("close Latest Pop Up");
		objCommon.clickLatestNwzClose();
		logger.info("Verify the user can able to view the 'Latest news' popup");
		Assert.assertEquals(objInCred.coverLetterHeader(), "Cover Letter", "Headers mismatch");
		logger.info("close Cover Letter pop Up");
		objCommon.clickCoverLetterClose();
		Assert.assertTrue(objCommon.verifyScratchpadHeading(domicile, ScratchpadPosition, ScratchpadRound, year));
	}

	public int initialScratchPadCount;

	@Test(priority = 18, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000018() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000018").assignAuthor("VS/483");
		logger.info("User can able to start over the bid");
		logger.info("Verify the user can select the Arrow button");
		initialScratchPadCount = objInCred.scrLinesTotalCount();
		Assert.assertTrue(objInCred.movearrowclickable(), "Icon not clickable");
	}

	@Test(priority = 21,enabled = false, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000019() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000019").assignAuthor("VS/483");
		logger.info("Bidlsit count should be the previous count count of the scratchpad view");
		Assert.assertEquals(objInCred.bidListHeadCount(), initialScratchPadCount,
				"Bidlsit count not same as previous count count of the scratchpad view");
	}

	@Test(priority = 19, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000020() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000020").assignAuthor("VS/483");
		logger.info("Verify the ellipsis icon");
		Assert.assertTrue(objInCred.ellipisIconVisible(), "Ellipis icon not displayed");
	}

	@Test(priority = 20, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000021() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000021").assignAuthor("VS/483");
		logger.info("Verify Start over button - scratch pad should be in default state");
		objInCred.startOver();
		logger.info(
				"Compare with Scratchpad count from the UI and the  Line count from the cover letter API should be same");
		Assert.assertTrue(objCommon.scrLinesTotalCount(ScratchPadBlankReservedLines.linecount));
	}

	@Test(priority = 22, enabled = false,dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000022() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000022").assignAuthor("VS/483");
		logger.info(
				"Assert: Inside the trip details , the dates are shown same as the dates of the trips and should be same as its in the wbl file");
		logger.info("Get  trip Details from UI- Trip Code and Trip date and compare with API Trip code and dates");
//		FetchDates.fetchApiData(domicile, APIRound, position, APIMonth);
//		Assert.assertTrue(objCommon.getAllTripDataAndCompare(FetchDates.tripData));
	}

	@Test(priority = 23,enabled = false, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000023() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000023").assignAuthor("VS/483");
		logger.info("Assert the 'cred' inside the trip detials ");
		driver.navigate().refresh();
		objwait.waitS(7000);
		Assert.assertTrue(objInCred.CredHeadVisible(), "cred not visible inside the trip detials ");
	}

	@Test(priority = 24, enabled = false,dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000024() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000024").assignAuthor("VS/483");
		logger.info(
				"Verify the Individual cred value in the trip details  in same as its in the wbp file for each leg ");
		//TrialBidAPI.fetchApiData(domicile, APIRound, position, APIMonth);
		CPBIDCalculation.fetchApiData(domicile, APIRound, position, APIMonth);
		logger.info("Get individual cred of Each Trip from UI and compare with API Data ");
		objFACred.getFAindiCredHour();
		Assert.assertTrue(objFACred.compareindiCredFA(objFACred.tripindiCredUI,CPBIDCalculation.apiCred), "cred not same as API cred");
	
	}

	@Test(priority = 25,enabled = false, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000025() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000025").assignAuthor("VS/483");
		logger.info("Verify the sum of individual cred value (total Cred value) for each day is shown in cred values ");
		driver.navigate().refresh();
		objwait.waitS(7000);
		logger.info("Get Total cred of Each Trip from UI and compare with API Data ");
		Assert.assertTrue(objInCred.totalCredCompareAPICP(), "cred not same as API cred");
	}
	@Test(priority = 26,enabled = false, dependsOnMethods = { "CBW0100030000011" })
	public void CBW0100030000026() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000026").assignAuthor("VS/483");
		driver.navigate().refresh();
		objwait.waitS(7000);
		logger.info(
				"AM:-Verify in cp and fo bid ,in reserve line  the Individual cred value in the trip details  is 6 and should be  same as its in the wbp file ");
		Assert.assertTrue(objInCred.ReserveLinesAMCred(), "cred not same as 600 for CP and FO");
	}

	@Test(priority = 27, enabled = false)
	public void CBW0100030000027() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000027").assignAuthor("VS/483");
		logger.info(
				"PM:-Verify in cp and fo bid ,in reserve line  the Individual cred value in the trip details  is 6 and should be  same as its in the wbp file ");
		driver.navigate().refresh();
		objwait.waitS(7000);
		Assert.assertTrue(objInCred.ReserveLinesPMCred(), "cred not same as 600 for CP and FO");

	}

	@Test(priority = 28, enabled = true)
	public void CBW0100030000028() {
		logger = WbidBasepage.extent.createTest("CP-Round 1-Individual Cred Value Page - CBW010003000028").assignAuthor("VS/483");
		logger.info(
				"Summary report of theIndividual cred value and total cred value  of each domicile with Position, round , date and time");
		logger.info("Implementation and Execution done seperately for each Domicile, position and Round ");

	}

}
