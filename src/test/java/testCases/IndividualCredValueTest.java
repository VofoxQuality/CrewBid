package testCases;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import pages.BidDownloadPage;
import pages.CommonPage;
import pages.CredValuesPage;
import pages.HoliRigPage;
import pages.IndividualCredValuePage;
import pages.LoginPage;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class IndividualCredValueTest extends WbidBasepage {
	WebDriver driver = returnDriver();
	WaitCondition objwait = new WaitCondition();
	ActionUtilities objaction = new ActionUtilities(driver);
	LoginPage objlogin = new LoginPage(driver);
	BidDownloadPage objdownload = new BidDownloadPage(driver);
	CommonPage objCommon=new CommonPage(driver);
	HoliRigPage objHoli=new HoliRigPage(driver);
	CredValuesPage objCred=new CredValuesPage(driver);
	IndividualCredValuePage objInCred =new IndividualCredValuePage(driver);
	HashMap<String, String> testDataMap = testData("qa environment");
	
	
	public String actualVersion;
	public String expectedVersion=testDataMap.get("Version");
	public String domicile="ATL";
	public String position="CP";
	public String round="1st Round";
	public String ScratchpadPosition="Captain";
	public String ScratchpadRound="Round 1";
	public String year=objCommon.getNextMonthAndCurrentYear();
	public String APIRound="1";
	public String APIMonth=String.valueOf(objCommon.getNextMonth());
	public static List<String> TripCodes = new ArrayList<>();
	
  @Test(priority = 1, enabled = true)
  public void CBW010003000001 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000001").assignAuthor("VS/483");
		logger.info("To check that CrewBid Login page is displaying when entering the Url");
		String actualtitle = objlogin.pageHeading();
		String expectedtitle = "CrewbidWebApp";
		logger.info("Captured title of the page: " + actualtitle);
		logger.info("Expected title: " + expectedtitle);
		Assert.assertEquals(actualtitle, expectedtitle, "Title assertion failed!");
	
  }
  @Test(priority = 2, enabled = true)
  public void CBW010003000002 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000002").assignAuthor("VS/483");
		logger.info("When Click on update button-Automatically redirect to landing page");
		objlogin.updateVersionLogin();
		logger.info("Assert that Home page header shows updated version.");
		actualVersion=objCommon.getVersionValue();
		logger.info("Assert that Home page header shows updated version.");
		Assert.assertTrue(actualVersion.contains(expectedVersion), "Home page header does not show updated version");	
  }
  @Test(priority = 3, enabled = true)
  public void CBW010003000003 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000003").assignAuthor("VS/483");
		logger.info("Verify the login-Assert the title : Crewbid in the top left-Assert the image : Crewbid icon in the top left");
		Assert.assertTrue(objInCred.logoVisibile(), "Crewbid Logo not display");
  }
  @Test(priority = 4, enabled = true)
  public void CBW010003000004 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000004").assignAuthor("VS/483");
		logger.info("Verify the Retreive button-Assert : Retrieve new bid data and Retrieve Historical bid data");
		logger.info("click Retreive button");
		objCommon.click_retrievedownload();
		Assert.assertTrue(objInCred.isVisibleBiddata(),"Retrieve new bid data and Retrieve Historical bid data are not displayed in dropdown");

  }
  @Test(priority = 5, enabled = true)
  public void CBW010003000005 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000005").assignAuthor("VS/483");
		logger.info("Verify the user able to select Retrieve new bid data button ");
		objCommon.forclicknewbiddata();
		Assert.assertEquals(objInCred.checkEmpnumHeader(), "Enter Employee Number","Header mismatch or not displayed");	
  }
  @Test(priority = 6, enabled = true)
  public void CBW010003000006 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000006").assignAuthor("VS/483");
		logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
		logger.info("Enter the User ID ");
		objCommon.enterempid(); 
		logger.info("selecting and Verifying Base, Position and Round, (Month- selected as default) highlighted");
		objCommon.selectOptions(domicile,position,round);
		Assert.assertTrue(objCommon.verifyOptionsEnabled(domicile,position,round));	
  }
  @Test(priority = 7, enabled = true)
  public void CBW010003000007 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000007").assignAuthor("VS/483");
	  logger.info("Verify user can able to select any condition  Assert: Download button is enabled");
	 // objCommon.selectOptions("AUS","CP","2nd Round");
	  Assert.assertTrue(objInCred.downloadEnable());
  }
  boolean isDownloadSuccessful = false; // Flag to track download status
  @Test(priority = 8, enabled = true)
  public void CBW010003000008 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000008").assignAuthor("VS/483");
	  logger.info("Verify the download button - Scratch pad view is visible");
      logger.info("Click Download Bid");
      objCommon.clickDownload();
      logger.info("Verify if the loader appears while downloading the bid");
      boolean isLoadingIconDisplayed = objdownload.fordisplayloadingicon();
      if (isLoadingIconDisplayed) {
    	  Assert.assertTrue(isLoadingIconDisplayed, "Loading icon not displayed");
          isDownloadSuccessful = true;
      }else {
    	  logger.info("Download was not successful, warning popup should be visible."); 
      }
  }
  @Test(priority = 9, dependsOnMethods = {"CBW010003000008"}, alwaysRun = true)
  public void CBW010003000009 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW010003000009").assignAuthor("VS/483");
	  if (isDownloadSuccessful) {
          logger.info("Download was successful, skipping warning popup verification.");
      } else {
          logger.info("Executing CBW010003000009: Verifying Early Bid Warning Popup");
      	  logger.info("Early Bid Warning popup is displayed.");
      	  Assert.assertTrue(objInCred.visibleEarlyBidPopup(), "Early Bid Warning popup should be displayed.");	
           }	
  }
  @Test(priority = 10, dependsOnMethods = {"CBW010003000008"}, alwaysRun = true)
  public void CBW0100030000010 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000010").assignAuthor("VS/483");
	  if (isDownloadSuccessful) {
          logger.info("Download was successful, skipping bid not available popup verification.");
      } else {
          logger.info("If bid data is not available, a warning popup should appear.");
          objInCred.clickOkEarlyBid();
          Assert.assertTrue(objInCred.bidNotAvailableBidPopup(), "Bid not available warning popup should be displayed.");
          objInCred.clickOkbidNotAvailable();
      }
  }
  @Test(priority = 11, dependsOnMethods = {"CBW010003000009", "CBW0100030000010"}, alwaysRun = true)
  public void CBW0100030000011 () throws Exception {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000011").assignAuthor("VS/483");
	  if (isDownloadSuccessful) {
          logger.info("Download was successful, skipping cancel button verification.");
      } else {
          logger.info("Verifying if user can select the cancel button.");
          objInCred.clickcancelBtn();
          Assert.assertTrue(objwait.waitPopupToClose(driver, objInCred.cancelBtn, 20),"Cancel button is not enabled.");  
      }
	  Assert.assertTrue(isDownloadSuccessful,"Bid Download was not successful");
  }
  @Test(priority = 12, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000012 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000012").assignAuthor("VS/483");
		logger.info("Verify the subscription expiring alert ");
		logger.info("Expiring alert  not visible");
  }
  @Test(priority = 13, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000013 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000013").assignAuthor("VS/483");
		logger.info("Verify the user is able to close the subscription expire pop up alert ");
		logger.info("Expiring alert  not visible");	
  }
  @Test(priority = 14, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000014 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000014").assignAuthor("VS/483");
		logger.info("Verify the subscription Expired Popup ");
		logger.info("Expiring alert  not visible");
  }
  @Test(priority = 15, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000015 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000015").assignAuthor("VS/483");
	  
		logger.info("Verify user can able to view the Seniority list popup");
		Assert.assertEquals(objInCred.displaySeniority(), "Seniority List", "Seniority list popup not visible");
	 
  }
  @Test(priority = 16, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000016 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000016").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the 'Latest news' popup");
		 if (isDownloadSuccessful) {
			 logger.info("close Seniority list Pop Up");
			 objCommon.clickSeniorityCancel();
			 logger.info("Verify the user can able to view the 'Latest news' popup");
			 Assert.assertEquals(objInCred.latestnewHeader(), "Latest News", "Headers mismatch");
		 }
  }
  @Test(priority = 17, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000017 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000017").assignAuthor("VS/483");
		logger.info("Verify the user can able to view the Cover letter popup");
		if (isDownloadSuccessful) {
			 logger.info("close Latest Pop Up");
			 objCommon.clickLatestNwzClose();
			 logger.info("Verify the user can able to view the 'Latest news' popup");
			 Assert.assertEquals(objInCred.coverLetterHeader(), "Cover Letter", "Headers mismatch");
		 }
	
  }
  @Test(priority = 18, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000018 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000018").assignAuthor("VS/483");
		logger.info("User can able to start over the bid");
			
	
  }
  @Test(priority = 19, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000019 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000019").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 20, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000020 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000020").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 21, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000021 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000021").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 22, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000022 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000022").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 23, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000023 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000023").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 24, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000024 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000024").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 25, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000025 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000025").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 26, dependsOnMethods = {"CBW0100030000011"})
  public void CBW0100030000026 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000026").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 27, enabled = true)
  public void CBW0100030000027 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000027").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  @Test(priority = 28, enabled = true)
  public void CBW0100030000028 () {
	  logger = WbidBasepage.extent.createTest("Individual Cred Value Page - CBW0100030000028").assignAuthor("VS/483");
		logger.info("");
			
	
  }
  
}
