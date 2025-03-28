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
import API.ScratchPadCountFA;
import API.TrialBidAPI;
import pages.BidDownloadPage;
import pages.CommonPage;
import pages.CredValuesPage;
import pages.HoliRigPage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class CommonTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	LoginPage objpage = new LoginPage(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon = new CommonPage(driver);
	HoliRigPage objHoli = new HoliRigPage(driver);
	CredValuesPage objCred = new CredValuesPage(driver);
	HashMap<String, String> testDataMap = testData("qa environment");

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
//	public static List<String> TripDates = new ArrayList<>();

	@Test(priority = 1, enabled = true)
	public void test1() {
		logger = WbidBasepage.extent.createTest("test1").assignAuthor("VS/483");
		logger.info("Login");
		objpage.updateVersionLogin();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(objpage.gettexthome(), "Home page header does not show updated version");
	}

	@Test(priority = 2, enabled = true)
	public void test2() {
		logger = WbidBasepage.extent.createTest("test2").assignAuthor("VS/483");
		logger.info("Get Login Version");
		actualVersion = objCommon.getVersionValue();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(actualVersion.contains(expectedVersion), "Home page header does not show updated version");
	}

	@Test(priority = 3, enabled = true)
	public void test3() {
		logger = WbidBasepage.extent.createTest("test3").assignAuthor("VS/483");
		logger.info("Bid download");
		logger.info("click Retreive button");
		objCommon.click_retrievedownload();
	}

	@Test(priority = 4, enabled = true)
	public void test4() {
		logger = WbidBasepage.extent.createTest("test4").assignAuthor("VS/483");
		logger.info("Verify the user able to select Retrieve new bid data button");
		objCommon.forclicknewbiddata();
	}

	@Test(priority = 5, enabled = true)
	public void test5() {
		logger = WbidBasepage.extent.createTest("test5").assignAuthor("VS/483");
		logger.info("Enter the User ID ");
		objCommon.enterempid();
	}

	@Test(priority = 6, enabled = false)
	public void test6() {
		logger = WbidBasepage.extent.createTest("test6").assignAuthor("VS/483");
		logger.info("selecting Base");
		objCommon.selectBase(domicile);
		Assert.assertTrue(objCommon.verifyBaseEnabled(domicile));
	}

	@Test(priority = 7, enabled = false)
	public void test7() {
		logger = WbidBasepage.extent.createTest("test7").assignAuthor("VS/483");
		logger.info("selecting Position");
		objCommon.selectPosition(position);
		Assert.assertTrue(objCommon.verifyPositionEnabled(position));
	}

	@Test(priority = 8, enabled = false)
	public void test8() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test8").assignAuthor("VS/483");
		logger.info("selecting Round");
		objCommon.selectRound(round);
		Assert.assertTrue(objCommon.verifyRoundEnabled(round));
	}

	@Test(priority = 9, enabled = true)
	public void test9() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test9").assignAuthor("VS/483");
		logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
		objCommon.selectOptions(domicile, position, round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile, position, round));
	}

	@Test(priority = 10, enabled = true)
	public void test10() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test10").assignAuthor("VS/483");
		logger.info("Click Download Bid");
		objCommon.clickDownload();
	}

	@Test(priority = 11, enabled = true)
	public void test11() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test11").assignAuthor("VS/483");
		logger.info("close Seniority list Pop Up");
		objCommon.clickSeniorityCancel();
	}

	@Test(priority = 12, enabled = true)
	public void test12() {
		logger = WbidBasepage.extent.createTest("test12").assignAuthor("VS/483");
		logger.info("close Latest newz pop Up");
		objCommon.clickLatestNwzClose();
	}

	@Test(priority = 13, enabled = true)
	public void test13() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test13").assignAuthor("VS/483");
		logger.info("close Cover Letter pop Up");
		objCommon.clickCoverLetterClose();
		Assert.assertTrue(objCommon.verifyScratchpadHeading(domicile, ScratchpadPosition, ScratchpadRound, year));
	}

	@Test(priority = 14, enabled = true)
	public void test14() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("test14").assignAuthor("VS/483");
		logger.info("Fetchinh API data");
		FetchDates.fetchApiData(domicile, APIRound, position, APIMonth);
		TrialBidAPI.fetchApiData(domicile, APIRound, position, APIMonth);
		// ScratchPadCountFA.fetchApiData(domicile, APIRound, position, APIMonth);
	}

	@Test(priority = 15, enabled = true)
	public void test15() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test15").assignAuthor("VS/483");
		logger.info("Start over ");
		objCommon.startOver();
	}

	@Test(priority = 16, enabled = true)
	public void test16() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test16").assignAuthor("VS/483");
		logger.info("Assert Total scratchpad Line count ");
		objCommon.scrLinesTotalCount(ScratchPadCountFA.linecount);
	}

	@Test(priority = 24, enabled = false)
	public void test24() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test24").assignAuthor("VS/483");
		logger.info("Get trip Code for one Trip Get  trip Details");
		String txt = objCommon.getTripCode();
		logger.info("Get  trip Details" + txt);
	}

	@Test(priority = 25, enabled = false)
	public void test25() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test25").assignAuthor("VS/483");
		logger.info("Get  All the trip Codes from UI ");
		TripCodes = objCommon.getAllTripCodes();
		logger.info("Fetching UI data" + TripCodes);

		logger.info("Compare All the trip Codes from UI with trip code fetched from API and get trip details ");
		// objCommon.compareTripCodesAndFetchData(TripCodes);
		Assert.assertTrue(objCommon.compareTripCodesAndFetchData(TripCodes));

	}

	@Test(priority = 26, enabled = false)
	public void test26() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test26").assignAuthor("VS/483");
		logger.info("Get  All the trip Codes from UI ");
		driver.navigate().refresh();
		objwait.waitS(5000);
		objCommon.getAllTripLinesValues();
	}

	@Test(priority = 27, enabled = false)
	public void test27() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test27").assignAuthor("VS/483");
		logger.info("Get  trip Details ");
		objCommon.getAllTripData();
		logger.info("Get  trip Details- date ");
		objCommon.getAllTripDates();
	}

	@Test(priority = 28, enabled = false)
	public void test28() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test28").assignAuthor("VS/483");

		logger.info("Get  trip Details from UI- Trip Code and Trip date and compare with API Trip code and dates");
		objCommon.getAllTripDataAndCompare(FetchDates.tripData);
	}

	@Test(priority = 29, enabled = false)
	public void test29() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test29").assignAuthor("VS/483");

		logger.info("Get  trip Details from UI- Trip Code and corresponding TAFB ");
		// objCommon.getAllTAFB();
		logger.info("Get  trip Details from UI- Trip Code and TAFB and compare with API Trip code and TAFB");
		// logger.info(" converted TAFB "+TrialBidAPI.tafbMapNew);
		Assert.assertTrue(objCommon.getAllTAFBAndCompare(TrialBidAPI.tafbMapNew));
	}

	@Test(priority = 30, enabled = false)
	public void test30() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("test30").assignAuthor("VS/483");
		logger.info("Get HoliRig Values from API ");
		JavaDirectHolirig.fetchParam(domicile, APIRound, position, APIMonth);
		logger.info("Get HoliRig Values from each line from UI ");
		objHoli.selectHoliRig();
		objHoli.getHoliRigVal();
		Assert.assertTrue(objHoli.isHoliRigDataMatching(JavaDirectHolirig.result));
	}

	@Test(priority = 31, enabled = true)
	public void test31() throws JsonProcessingException, ParseException {
		logger = WbidBasepage.extent.createTest("test31").assignAuthor("VS/483");
		logger.info("Get Cred Values from API ");

		logger.info("Get Cred Values from UI ");
		// objCred.getAllCred();
		logger.info("Get Cred Values from each line from UI ");
		// objCred.tripDataWithLine();
		objCred.getAllTripData();
		// Assert.assertTrue();
	}
}
