package API;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import utilities.WbidBasepage;

public class ParameterCall extends WbidBasepage {

	public static List<Map<String, Object>> holirigResultReturned = new ArrayList<>();
	public static List<Map<String, Object>> holirigFAResultReturned = new ArrayList<>();
	public static List<Map<String, Object>> DirectHolirigresultReturned = new ArrayList<>();

	@Test(priority = 1, enabled = true)
	public void f() throws Throwable {
		//WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		//logger.info("ScratchPad Blank and Reserved");
		
		// ScratchPadBlankReservedLines.fetchApiData("ATL", "1", "CP", "3");
		//TAFBCPFO.fetchTafb("ATL", "1", "CP", "6");
		TAFB_FA.fetchTafb("ATL", "1", "FA", "6");
		
	}

	@Test(priority = 2, enabled = false)
	public void g() throws Throwable {
		WbidBasepage.logger = extent.createTest("Direct HoliRig").assignAuthor("VS/445");

		logger.info("HoliRig values ");
		
		// TrialBidAPI.fetchApiData("ATL", "1", "CP", "4");
		// ScratchPadBlankReservedLines.fetchApiData("ATL", "1", "CP", "3");
		
		// DutyPeriodTest.fetchApiData("ATL", "1", "FA", "7");
		// GroundTest.fetchground("ATL", "1", "FA", "7");
		//BlockTest.fetchBlock("ATL", "1", "FA", "7");
		
		
		///////////*Calling method to get Direct holirig CP,FO and FA*//////////
		DirectHolirigresultReturned = JavaDirectHolirig.fetchParam("ATL", "1", "FA", "5");
		
		
		
		///////////*Calling method to get calculated holirig CP,FO*//////////
		// holirigResultReturned = HoliRigCPFO.fetchApiData("ATL", "1", "CP", "4");
		
		
		///////////*Calling method to get calculated holirig FA*//////////
		holirigFAResultReturned = HoliRigFA.fetchApiData("ATL", "1", "FA", "5");
		//holirigFAResultReturned = HoliRigFA_Copy.fetchApiData("ATL", "1", "FA", "7");
	}

	@Test(priority = 3, enabled = false)
	public void h() throws JsonProcessingException, ParseException {
		WbidBasepage.logger = extent.createTest("HoliRig Comparison").assignAuthor("VS/445");

		logger.info("Comparison of calcuated Holirig and Direct Holirig");
		// boolean abc=JavaDirectHolirig.compareLists(HoliRig.holirigResult,
		// JavaDirectHolirig.result);
		
		///////////*Calling comparison method for holirig CP and FO*//////////
		// boolean matched = JavaDirectHolirig.compareLists(holirigResultReturned, DirectHolirigresultReturned);
		
		///////////*Calling comparison method for holirig for FA*//////////
		boolean matched = JavaDirectHolirig.compareListsFA(holirigFAResultReturned, DirectHolirigresultReturned);
		
		Assert.assertTrue(matched);
		System.out.println("Matching is:" + matched);
		

	}
}
