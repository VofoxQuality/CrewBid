package testCases;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import API.FetchDates;
import API.JavaDirectHolirig;
import API.LineParameterDirect;
import API.ScratchPadBlankReservedLines;
import API.TrialBidAPI;
import pages.ADGPage;
import pages.BidDownloadPage;
import pages.BlkHour;
import pages.CommonPage;
import pages.CredValuesPage;
import pages.DHRPage;
import pages.DPMPage;
import pages.GrndHrPage;
import pages.HoliRigPage;
import pages.IndividualCredValuePage;
import pages.LoginPage;
import pages.TAFBPage;
import pages.THRPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class RigDPM extends WbidBasepage{
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
	HashMap<String, String> testDataMap = testData("qa environment");
	TAFBPage objTafb=new TAFBPage(driver);
	DPMPage objDPM= new DPMPage(driver);
	DHRPage objDHR= new DHRPage(driver);
	ADGPage objADG= new ADGPage(driver);
	THRPage objTHR= new THRPage(driver);
	
	public String actualVersion;
	public String expectedVersion = testDataMap.get("Version");
	public String domicile = "ATL";
	public String position = "CP";
	public String round = "1st Round";
	public String ScratchpadPosition = "Captain";
	public String ScratchpadRound = "Round 1";
	public String year = objCommon.getNextMonthAndCurrentYear();
	public String APIRound = "1";
	public String APIMonth = String.valueOf(objCommon.getNextMonth());
	public static List<String> TripCodes = new ArrayList<>();
	
	
  @Test
  public void CBW010004000001() {
	  	logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400001").assignAuthor("VS/483");
		logger.info("To check that CrewBid Login page is displaying when entering the Url");
		String actualtitle = objlogin.pageHeading();
		String expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + actualtitle);
		logger.info("Expected title: " + expectedtitle);
		Assert.assertEquals(actualtitle, expectedtitle, "Title assertion failed!");
  }
  @Test
  public void CBW010004000002() {
	  	logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400002").assignAuthor("VS/483");
	  	logger.info("When Click on update button-Automatically redirect to landing page");
		objlogin.updateVersionLogin();
		logger.info("Assert that Home page header shows updated version.");
		actualVersion = objCommon.getVersionValue();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(actualVersion.contains(expectedVersion), "Home page header does not show updated version");
	 }
  @Test(priority = 3, enabled = true)
	public void CBW010004000003() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400003").assignAuthor("VS/483");
		logger.info(
				"Verify the login-Assert the title : Crewbid in the top left-Assert the image : Crewbid icon in the top left");
		Assert.assertTrue(objInCred.logoVisibile(), "Crewbid Logo not display");
	}

	@Test(priority = 4, enabled = true)
	public void CBW010004000004() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400004").assignAuthor("VS/483");
		logger.info("Verify the Retreive button-Assert : Retrieve new bid data and Retrieve Historical bid data");
		logger.info("click Retreive button");
		objCommon.click_retrievedownload();
		Assert.assertTrue(objInCred.isVisibleBiddata(),
				"Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");

	}

	@Test(priority = 5, enabled = true)
	public void CBW010004000005() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400005").assignAuthor("VS/483");
		logger.info("Verify the user able to select Retrieve new bid data button ");
		objCommon.forclicknewbiddata();
		Assert.assertEquals(objInCred.checkEmpnumHeader(), "Enter Employee Number", "Header mismatch or not displayed");
	}

	@Test(priority = 6, enabled = true)
	public void CBW010004000006() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400006").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		logger.info("Enter the User ID ");
		objCommon.enterempid();
		logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
		objCommon.selectOptions(domicile, position, round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile, position, round));
	}

	@Test(priority = 7, enabled = true)
	public void CBW010004000007() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400007").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		Assert.assertTrue(objInCred.downloadEnable());
	}

	boolean isDownloadSuccessful = false; // Flag to track download status

	@Test(priority = 8, enabled = true)
	public void CBW010004000008() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400008").assignAuthor("VS/483");
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

	@Test(priority = 9, dependsOnMethods = { "CBW010004000008" }, alwaysRun = true)
	public void CBW010004000009() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW01000400009").assignAuthor("VS/483");
		if (isDownloadSuccessful) {
			logger.info("Download was successful, skipping warning popup verification.");
		} else {
			logger.info("Executing CBW010004000009: Verifying Early Bid Warning Popup");
			logger.info("Early Bid Warning popup is displayed.");
			Assert.assertTrue(objInCred.visibleEarlyBidPopup(), "Early Bid Warning popup should be displayed.");
		}
	}

	@Test(priority = 10, dependsOnMethods = { "CBW010004000008" }, alwaysRun = true)
	public void CBW0100040000010() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000010").assignAuthor("VS/483");
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

	@Test(priority = 11, dependsOnMethods = { "CBW010004000009", "CBW0100040000010" }, alwaysRun = true)
	public void CBW0100040000011() throws Exception {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000011").assignAuthor("VS/483");
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

	@Test(priority = 12, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000012() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000012").assignAuthor("VS/483");
		logger.info("Fetchinh API data");
		ScratchPadBlankReservedLines.fetchApiData(domicile, APIRound, position, APIMonth);
		TrialBidAPI.fetchApiData(domicile, APIRound, position, APIMonth);
		logger.info("Verify the subscription expiring alert - Expiring alert  not visible ");
	}

	@Test(priority = 13, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000013() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000013").assignAuthor("VS/483");
		logger.info("Verify the user is able to close the subscription expire pop up alert ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 14, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000014() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000014").assignAuthor("VS/483");
		logger.info("Verify the subscription Expired Popup ");
		logger.info("Expiring alert  not visible");
	}

	@Test(priority = 15, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000015() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000015").assignAuthor("VS/483");
		logger.info("Verify user can able to view the Seniority list popup");
		Assert.assertEquals(objInCred.displaySeniority(), "Seniority List", "Seniority list popup not visible");

	}

	@Test(priority = 16, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000016() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000016").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the 'Latest news' popup");
		logger.info("close Seniority list Pop Up");
		objCommon.clickSeniorityCancel();
		logger.info("Verify the user can able to view the 'Latest news' popup");
		Assert.assertEquals(objInCred.latestnewHeader(), "Latest News", "Headers mismatch");

	}

	@Test(priority = 17, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000017() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000017").assignAuthor("VS/483");
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

	@Test(priority = 18, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000018() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000018").assignAuthor("VS/483");
		logger.info("User can able to start over the bid");
		logger.info("Verify the user can select the Arrow button");
		initialScratchPadCount = objInCred.scrLinesTotalCount();
		Assert.assertTrue(objInCred.movearrowclickable(), "Icon not clickable");
	}

	@Test(priority = 19, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000019() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000019").assignAuthor("VS/483");
		logger.info("Bidlsit count should be the previous count count of the scratchpad view");
		Assert.assertEquals(objInCred.bidListHeadCount(), initialScratchPadCount,"Bidlsit count not same as previous count count of the scratchpad view");
	}

	@Test(priority = 20, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000020() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000020").assignAuthor("VS/483");
		logger.info("Verify the ellipsis icon");
		Assert.assertTrue(objInCred.ellipisIconVisible(), "Ellipis icon not displayed");
	}

	@Test(priority = 21, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000021() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000021").assignAuthor("VS/483");
		logger.info("Verify Start over button - scratch pad should be in default state");
		objInCred.startOver();
		objwait.waitS(5000);
		logger.info(
				"Compare with Scratchpad count from the UI and the  Line count from the cover letter API should be same");
		Assert.assertTrue(objCommon.scrLinesTotalCount(ScratchPadBlankReservedLines.linecount));
	}

	@Test(priority = 22,enabled=false, dependsOnMethods = { "CBW0100040000011" })
	public void CBW0100040000022() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000022").assignAuthor("VS/483");
		logger.info(
				"Assert: Inside the trip details , the dates are shown same as the dates of the trips and should be same as its in the wbl file");
		logger.info("Get  trip Details from UI- Trip Code and Trip date and compare with API Trip code and dates");
		FetchDates.fetchApiData(domicile, APIRound, position, APIMonth);
		Assert.assertTrue(objCommon.getAllTripDataAndCompare(FetchDates.tripData));
	}
	@Test(priority = 23, enabled = true)
	public void CBW0100040000023() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000023").assignAuthor("VS/483");
		logger.info("User can able to select the rigDPM  parameter from line parameter  ");
		logger.info("Assert : rigDPM parameter along with the value displayed in the line paramter of each line");
		objDPM.selectDPMLine();
		Assert.assertTrue(objDPM.fordisplayDPM(), "❌ DPM in the line paramter of each line not displayed");
	}
	@Test(priority = 24, enabled = true)
	public void CBW0100040000024() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000024").assignAuthor("VS/483");
		logger.info("check after selecting DPM parameter, when clicking the line parameter area and looking DPM parameter, the DPM parameter is showed as selected by blue tick mark");
		Assert.assertTrue(objDPM.getDPMLineVal(),"❌ After selecting Rig DPM parameter all DPM line values not found");
	}
	@Test(priority = 25, enabled = true)
	public void CBW0100040000025() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000025").assignAuthor("VS/483");
		logger.info("Verify the DPM parameter  showing is correct or not ");
		logger.info("Assert the value shows in the parameter in the UI is  the sum of all the TAFB  values of each  trip calculated from ourside   and  should be same as its in the file from the server  (WBL File) ");
		logger.info("Get DPM Values from API ");
		LineParameterDirect.fetchParam(domicile, APIRound, position, APIMonth);
		Assert.assertTrue(objDPM.isDPMLineValCompare(LineParameterDirect.dpmLineAPI));
		
	}
	@Test(priority = 26, enabled = true)
	public void CBW0100040000026() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000026").assignAuthor("VS/483");
		logger.info("User can able to select the rigDHR  parameter from line parameter  ");
		logger.info("Assert : rigDHR parameter along with the value displayed in the line paramter of each line");
		objDHR.selectDHRLine();
		Assert.assertTrue(objDHR.fordisplayDHR(), "❌ DHR in the line paramter of each line not displayed");
	}
	@Test(priority = 27, enabled = true)
	public void CBW0100040000027() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000027").assignAuthor("VS/483");
		logger.info("check after selecting DHR parameter, when clicking the line parameter area and looking DHR parameter, the DHR parameter is showed as selected by blue tick mark");
		Assert.assertTrue(objDHR.getDHRLineVal(),"❌ After selecting Rig DHR parameter all DHR line values not found");
	}
	@Test(priority = 28, enabled = true)
	public void CBW0100040000028() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000028").assignAuthor("VS/483");
		logger.info("Verify the DHR parameter  showing is correct or not ");
		logger.info("Assert the value shows in the parameter in the UI is  the sum of all the DHR  values of each  trip calculated from ourside   and  should be same as its in the file from the server  (WBL File) ");
		Assert.assertTrue(objDHR.isDHRLineValCompare(LineParameterDirect.dhrLineAPI));
		
	}
	@Test(priority = 29, enabled = true)
	public void CBW0100040000029() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000029").assignAuthor("VS/483");
		logger.info("User can able to select the rigADG  parameter from line parameter  ");
		logger.info("Assert : rigADG parameter along with the value displayed in the line paramter of each line");
		objADG.selectADGLine();
		Assert.assertTrue(objADG.fordisplayADG(), "❌ ADG in the line paramter of each line not displayed");
	}
	@Test(priority = 30, enabled = true)
	public void CBW0100040000030() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000030").assignAuthor("VS/483");
		logger.info("check after selecting ADG parameter, when clicking the line parameter area and looking ADG parameter, the ADG parameter is showed as selected by blue tick mark");
		Assert.assertTrue(objADG.getADGLineVal(),"❌ After selecting Rig ADG parameter all DHR line values not found");
	}
	@Test(priority = 31, enabled = true)
	public void CBW0100040000031() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000031").assignAuthor("VS/483");
		logger.info("Verify the ADG parameter  showing is correct or not ");
		logger.info("Assert the value shows in the parameter in the UI is  the sum of all the ADG  values of each  trip calculated from ourside   and  should be same as its in the file from the server  (WBL File) ");
		Assert.assertTrue(objADG.isADGLineValCompare(LineParameterDirect.adgLineAPI));
		
	}
	@Test(priority = 32, enabled = true)
	public void CBW0100040000032() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000032").assignAuthor("VS/483");
		logger.info("User can able to select the rigTHR  parameter from line parameter  ");
		logger.info("Assert : rigTHR parameter along with the value displayed in the line paramter of each line");
		objTHR.selectTHRLine();
		Assert.assertTrue(objTHR.fordisplayTHR(), "❌ THR in the line paramter of each line not displayed");
	}
	@Test(priority = 33, enabled = true)
	public void CBW0100040000033() {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000033").assignAuthor("VS/483");
		logger.info("check after selecting THR parameter, when clicking the line parameter area and looking THR parameter, the THR parameter is showed as selected by blue tick mark");
		Assert.assertTrue(objTHR.getTHRLineVal(),"❌ After selecting Rig THR parameter all DHR line values not found");
	}
	@Test(priority = 34, enabled = true)
	public void CBW0100040000034() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("ATL-CP-Round 1-Rig ADG,DHR,THR,DPM - CBW010004000034").assignAuthor("VS/483");
		logger.info("Verify the ADG parameter  showing is correct or not ");
		logger.info("Assert the value shows in the parameter in the UI is  the sum of all the THR  values of each  trip calculated from ourside   and  should be same as its in the file from the server  (WBL File) ");
		Assert.assertTrue(objTHR.isTHRLineValCompare(LineParameterDirect.thrLineAPI));
		
	}
}
