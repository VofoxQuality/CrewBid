package API;

import java.text.ParseException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import utilities.WbidBasepage;

public class ParameterCall extends WbidBasepage{
  @Test
  public void f() throws JsonProcessingException, ParseException {
	  WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("Cred Values in an array");
	  //WbidBasepage.logger = WbidBasepage.extent.createTest("Bid Download API").assignAuthor("VS/445");
	 // ScratchPadBlankReservedLines.fetchApiData("ATL", "1", "CP", "3");
	  JavaDirectHolirig.fetchParam("ATL", "1", "CP", "4");
	 // logger.info("Success");
	//  HoliRig.fetchApiData("ATL", "1", "CP", "4");
  }
  @Test
  public void g() throws JsonProcessingException, ParseException {
	  WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("Cred Values in an array");
	  //WbidBasepage.logger = WbidBasepage.extent.createTest("Bid Download API").assignAuthor("VS/445");
	 // ScratchPadBlankReservedLines.fetchApiData("ATL", "1", "CP", "3");
	  //JavaDirectHolirig.fetchParam("ATL", "1", "CP", "4");
	 // logger.info("Success");
	  HoliRig.fetchApiData("ATL", "1", "CP", "4");
  }
  @Test
  public void h() throws JsonProcessingException, ParseException {
	  WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("Comparison of Holirig");
		boolean abc=JavaDirectHolirig.compareLists(HoliRig.holirigResult, JavaDirectHolirig.result);
		Assert.assertTrue(abc);
		
	  
  }
}
