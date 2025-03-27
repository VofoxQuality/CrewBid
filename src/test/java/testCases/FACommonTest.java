package testCases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import API.ScratchPadCountFA;
import pages.BidDownloadPage;
import pages.CommonPage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class FACommonTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	LoginPage objpage = new LoginPage(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon = new CommonPage(driver);
	HashMap<String, String> testDataMap = testData("qa environment");

	public String actualVersion;
	public String expectedVersion = testDataMap.get("Version");
	public String domicile = "ATL";
	public String position = "FA";
	public String round = "1st Round";
	public String ScratchpadPosition = "Flight Attendant";
	public String ScratchpadRound = "Round 1";
	public String year = objCommon.getNextMonthAndCurrentYear();
	public static List<String> TripCodes = new ArrayList<>();
	public String APIRound = "1";
	public String APIMonth = String.valueOf(objCommon.getNextMonth());

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

	@Test(priority = 6, enabled = true)
	public void test6() {
		logger = WbidBasepage.extent.createTest("test6").assignAuthor("VS/483");
		logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
		objCommon.selectOptions(domicile, position, round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile, position, round));
	}

	@Test(priority = 7, enabled = true)
	public void test7() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test7").assignAuthor("VS/483");
		logger.info("Click Download Bid");
		objCommon.clickDownload();
		logger.info("Fetchinh API data");
		// TrialBidAPI.fetchApiData();
		ScratchPadCountFA.fetchApiData(domicile, APIRound, position, APIMonth);

	}

	@Test(priority = 8, enabled = true)
	public void test8() {
		logger = WbidBasepage.extent.createTest("test7").assignAuthor("VS/483");
		logger.info("close Seniority list Pop Up");
		objCommon.clickSeniorityCancel();
	}

	@Test(priority = 9, enabled = true)
	public void test9() {
		logger = WbidBasepage.extent.createTest("test9").assignAuthor("VS/483");
		logger.info("close Latest newz pop Up");
		objCommon.clickLatestNwzClose();
	}

	@Test(priority = 10, enabled = true)
	public void test10() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test10").assignAuthor("VS/483");
		logger.info("Get text from Cover Letter -FA");
		logger.info("Get FA- ABC position Lines");
		Assert.assertTrue(objCommon.getABCPositionLinesFA(ScratchPadCountFA.ABCcount));
	}

	@Test(priority = 11, enabled = true)
	public void test11() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test11").assignAuthor("VS/483");
		logger.info("Get text from Cover Letter -FA");
		logger.info("Get FA- BC position Lines");
		Assert.assertTrue(objCommon.getBCPositionLinesFA(ScratchPadCountFA.BCcount));
	}

	@Test(priority = 12, enabled = true)
	public void test12() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test12").assignAuthor("VS/483");
		logger.info("Get text from Cover Letter -FA");
		logger.info("Get FA- ABCD position Lines");
		Assert.assertTrue(objCommon.getABCDPositionLinesFA(ScratchPadCountFA.ABCDcount));
	}

	@Test(priority = 13, enabled = true)
	public void test13() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test13").assignAuthor("VS/483");
		logger.info("Get text from Cover Letter -FA");
		logger.info("Get FA- D only position Lines");
		Assert.assertTrue(objCommon.getDPositionLinesFA(ScratchPadCountFA.Dcount));
	}

	@Test(priority = 14, enabled = true)
	public void test14() {
		logger = WbidBasepage.extent.createTest("test14").assignAuthor("VS/483");
		logger.info("close Cover Letter pop Up");
		objCommon.CoverLetterCloseFA();
		Assert.assertTrue(objCommon.verifyScratchpadHeading(domicile, ScratchpadPosition, ScratchpadRound, year));
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

	@Test(priority = 17, enabled = false)
	public void test17() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test17").assignAuthor("VS/483");
		logger.info("Get start date of Trip ");
		objCommon.getDatesWithoutTrip();
	}

	@Test(priority = 18, enabled = true)
	public void test18() throws JsonProcessingException {
		logger = WbidBasepage.extent.createTest("test18").assignAuthor("VS/483");
		logger.info("Get start date of Trip ");
		objCommon.getTripStartDates();

//objCommon.findTripStartDatesForEachCalendar();
	}
}
