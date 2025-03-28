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
	public static List<Map<String, Object>> DirectHolirigresultReturned = new ArrayList<>();

	@Test(priority = 1, enabled = false)
	public void f() throws JsonProcessingException, ParseException {
		WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("Cred Values in an array");
		// WbidBasepage.logger = WbidBasepage.extent.createTest("Bid Download
		// API").assignAuthor("VS/445");
		// ScratchPadBlankReservedLines.fetchApiData("ATL", "1", "CP", "3");
		JavaDirectHolirig.fetchParam("ATL", "1", "CP", "4");
		// logger.info("Success");
		// HoliRig.fetchApiData("ATL", "1", "CP", "4");
	}

	@Test(priority = 2, enabled = true)
	public void g() throws ParseException, NumberFormatException, IOException {
		WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("HoliRig values ");
		// WbidBasepage.logger = WbidBasepage.extent.createTest("Bid Download
		// API").assignAuthor("VS/445");
		// ScratchPadBlankReservedLines.fetchApiData("ATL", "1", "CP", "3");
		DirectHolirigresultReturned = JavaDirectHolirig.fetchParam("ATL", "1", "CP", "4");
		// logger.info("Success");
		// HoliRig.fetchApiData("ATL", "1", "CP", "4");
		holirigResultReturned = HoliRigCPFO.fetchApiData("ATL", "1", "CP", "4");
		// HoliRigCPFOModified.fetchApiData("ATL", "1", "CP", "4");
	}

	@Test(priority = 3, enabled = true)
	public void h() throws JsonProcessingException, ParseException {
		WbidBasepage.logger = extent.createTest("Bid Download API").assignAuthor("VS/445");

		logger.info("Comparison of calcuated Holirig and Direct Holirig");
		// boolean abc=JavaDirectHolirig.compareLists(HoliRig.holirigResult,
		// JavaDirectHolirig.result);
		boolean matched = JavaDirectHolirig.compareLists(holirigResultReturned, DirectHolirigresultReturned);
		Assert.assertTrue(matched);
		System.out.println("Matching is:"+matched);
		// Assert.assertTrue(abc);

	}
}
