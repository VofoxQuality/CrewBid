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

public class HolirigAssertion extends WbidBasepage {

	public static List<Map<String, Object>> holirigResultReturned = new ArrayList<>();
	public static List<Map<String, Object>> holirigFAResultReturned = new ArrayList<>();
	public static List<Map<String, Object>> DirectHolirigresultReturned = new ArrayList<>();

	

	@Test(priority = 1, enabled = true)
	public void getHolirigResults() throws Throwable {
		WbidBasepage.logger = extent.createTest("Direct HoliRig").assignAuthor("VS/445");

		logger.info("HoliRig values ");
		
		
		///////////*Calling method to get Direct holirig CP,FO and FA*//////////
		DirectHolirigresultReturned = JavaDirectHolirig.fetchParam("ATL", "1", "FA", "5");		
		
		///////////*Calling method to get calculated holirig CP,FO*//////////
		// holirigResultReturned = HoliRigCPFO.fetchApiData("ATL", "1", "CP", "4");
		
		
		///////////*Calling method to get calculated holirig FA*//////////
		holirigFAResultReturned = HoliRigFA.fetchApiData("ATL", "1", "FA", "5");
		
	}

	@Test(priority = 2, enabled = true)
	public void compareHolirigResults() throws JsonProcessingException, ParseException {
		WbidBasepage.logger = extent.createTest("HoliRig Comparison").assignAuthor("VS/445");

		logger.info("Comparison of calcuated Holirig and Direct Holirig");
		
		
		///////////*Calling comparison method for holirig CP and FO*//////////
		// boolean matched = JavaDirectHolirig.compareLists(holirigResultReturned, DirectHolirigresultReturned);
		
		///////////*Calling comparison method for holirig for FA*//////////
		boolean matched = JavaDirectHolirig.compareListsFA(holirigFAResultReturned, DirectHolirigresultReturned);
		
		Assert.assertTrue(matched);
		System.out.println("Matching is:" + matched);
		

	}
}
