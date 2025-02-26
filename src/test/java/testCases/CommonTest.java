package testCases;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import API.TrialBidAPI;
import pages.BidDownloadPage;
import pages.CommonPage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;


public class CommonTest extends WbidBasepage{
	WebDriver driver = returnDriver();
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	LoginPage objpage = new LoginPage(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon=new CommonPage(driver);
	
	public String actualVersion;
	public String expectedVersion="10.4.16.2";
	public String domicile="ATL";
	public String position="CP";
	public String round="1st Round";
	public String ScratchpadPosition="Captain";
	public String ScratchpadRound="Round 1";
	public String year=objCommon.getNextMonthAndCurrentYear();
	
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
	  actualVersion=objCommon.getVersionValue();
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
  public void test8() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test8").assignAuthor("VS/483");
	  logger.info("selecting Round");
	  objCommon.selectRound(round);
	  Assert.assertTrue(objCommon.verifyRoundEnabled(round));
	 }
  @Test(priority = 9, enabled = true)
  public void test9() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test9").assignAuthor("VS/483");
	  logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
	  objCommon.selectOptions(domicile,position,round);
	  Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile,position,round));
	 }
  @Test(priority = 10, enabled = true)
  public void test10() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test10").assignAuthor("VS/483");
	  logger.info("Click Download Bid");
	  objCommon.clickDownload();
	 }
  @Test(priority = 11, enabled = true)
  public void test11() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test11").assignAuthor("VS/483");
	  logger.info("close Seniority list Pop Up");
	  objCommon.clickSeniorityCancel();
	 }
  @Test(priority = 12, enabled = true)
  public void test12() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test12").assignAuthor("VS/483");	 
	  logger.info("close Latest newz pop Up");
	  objCommon.clickLatestNwzClose();
	   }
  @Test(priority = 13, enabled = true)
  public void test13() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test13").assignAuthor("VS/483");
	  logger.info("close Cover Letter pop Up");
	  objCommon.clickCoverLetterClose();
	  Assert.assertTrue(objCommon.verifyScratchpadHeading(domicile,ScratchpadPosition,ScratchpadRound,year));
	  }
  @Test(priority = 14, enabled = true)
  public void test14() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test14").assignAuthor("VS/483");
	  logger.info("Fetchinh API data");
	  TrialBidAPI.fetchApiData(); 
	  }
  public static List<String> TripCodes = new ArrayList<>();
  @Test(priority = 15, enabled = true)
  public void test15() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test15").assignAuthor("VS/483");
	  logger.info("Get  trip Details");
	  String txt=objCommon.getTripCode();
	  logger.info("Get  trip Details"+ txt);
	   }
  @Test(priority = 16, enabled = true)
  public void test16() throws JsonProcessingException  {
	  logger = WbidBasepage.extent.createTest("test16").assignAuthor("VS/483");		  
	  logger.info("Get  All the trip Codes from UI ");
	  TripCodes=objCommon.getAllTripCodes();
	  logger.info("Fetching UI data"+TripCodes);
	  
	  logger.info("Compare All the trip Codes from UI with trip code fetched from API and get trip details ");
	  //objCommon.compareTripCodesAndFetchData(TripCodes);
	  Assert.assertTrue(objCommon.compareTripCodesAndFetchData(TripCodes));
	  
	  }
}
