package pages;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import API.FetchDates.TripEntry;
import API.TrialBidAPI;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class CommonPage {
	//Common page created
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public CommonPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}
	@FindBy(xpath = "//*[contains(@id,'navbarToggler')]/ul[2]/li")
	public WebElement homeTxt;
	
	public String getVersionValue() {
		objwait.waitForElementTobeVisible(driver, homeTxt, 30);
        String hometext = objaction.gettext(homeTxt).trim();
        WbidBasepage.logger.info("Full Home Text: " + hometext);

        // Updated regex pattern to handle spaces within parentheses
        String version = null;
        String regex = "\\(\\s*(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s*\\)";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(regex).matcher(hometext);
        if (matcher.find()) {
            version = matcher.group(1).trim(); // Get the version part
            WbidBasepage.logger.info("Extracted Version: " + version);
        } else {
            WbidBasepage.logger.info("Version not found in home text.");
        } 
        return version;
    }

	@FindBy(id = "navbardrop")
	public WebElement retrivedropdown;

	public void click_retrievedownload() {
		objaction.click(retrivedropdown);
	}
	@FindBy(xpath = "//a[text()='Retrieve New BidData']")
	public WebElement retrievenewbiddata;

	public void forclicknewbiddata() {
		objaction.click(retrievenewbiddata);
	}
	// Enter Emplyee ID
	@FindBy(name = "empNum")
	public WebElement empplaceholder;

	@FindBy(xpath = "//button[@class='submit-bid btn']")
	public WebElement okBtn;

	public void enterempid() {
		objwait.waitForElementTobeVisible(driver, empplaceholder, 60);
		objaction.sendkey(empplaceholder, String.valueOf((int) Double.parseDouble(WbidBasepage.username)));
		objaction.click(okBtn);
	}
//selecting and verifying Base
	@FindBy(xpath = "//*[@id='newBidModal']/div/div/div[2]/div[1]/div[2]/button")
	public List<WebElement> basecities;

	public void selectBase(String domicile) {
		WbidBasepage.logger.info("Attempting to select base city: " + domicile);
		for (WebElement option : basecities) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String cityName = option.getText().trim();
			if (cityName.equalsIgnoreCase(domicile)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				objaction.click(option);
				WbidBasepage.logger.pass("City" + cityName + "clicked successfully");
			}
		}
	}
	public boolean verifyBaseEnabled(String domicile) {
		boolean isEnabled = false;
		WbidBasepage.logger.info("Verifying base city: " + domicile);
		for (WebElement option : basecities) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String cityName = option.getText().trim();
			WbidBasepage.logger.info("City: " + cityName);
			if (cityName.equalsIgnoreCase(domicile)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				String att = objaction.getAttribute(option, "class");
				if (att.contains("active-re")) {
					WbidBasepage.logger.pass("City" + cityName + "' ✅ is enabled.");
					isEnabled = true;
				} else {
					WbidBasepage.logger.fail("City" + cityName + "' ❌ is disabled.");
				}
				break;
			}
		}
		if (!isEnabled) {
			WbidBasepage.logger.fail("City" + domicile + "' not found or is disabled.");
		}
		return isEnabled;
	}
//selecting  and verifying Position
	@FindBy(xpath = "//*[@id='newBidModal']/div/div/div[2]/div[2]/div[2]/button")
	public List<WebElement> positionList;

	public void selectPosition(String position) {
		WbidBasepage.logger.info("Attempting to select Position: " + position);
		for (WebElement option : positionList) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String positionName = option.getText().trim();
			if (positionName.equalsIgnoreCase(position)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				objaction.click(option);
				WbidBasepage.logger.pass("Position" + position + "clicked successfully");
			}
		}
	}
	public boolean verifyPositionEnabled(String position) {
		boolean isEnabled = false;
		WbidBasepage.logger.info("Verifying base city: " + position);
		for (WebElement option : positionList) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String positionName = option.getText().trim();
			WbidBasepage.logger.info("position: " + position);
			if (positionName.equalsIgnoreCase(position)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				String att = objaction.getAttribute(option, "class");
				if (att.contains("active-re")) {
					WbidBasepage.logger.pass("Pposition" + position + "' ✅ is enabled.");
					isEnabled = true;
				} else {
					WbidBasepage.logger.fail("Position" + position + "' ❌ is disabled.");
				}
				break;
			}
		}
		if (!isEnabled) {
			WbidBasepage.logger.fail("position" + position + "' not found or is disabled.");
		}
		return isEnabled;
	}
//selecting  and verifying Round
	@FindBy(xpath = "//*[@id='newBidModal']/div/div/div[2]/div[3]/div[2]/button")
	public List<WebElement> roundList;

	public void selectRound(String Round) {
		WbidBasepage.logger.info("Attempting to select Round: " + Round);
		for (WebElement option : roundList) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String RoundName = option.getText().trim();
			if (RoundName.equalsIgnoreCase(Round)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				objaction.click(option);
				WbidBasepage.logger.pass("Round" + Round + "clicked successfully");
			}
		}
	}
	public boolean verifyRoundEnabled(String Round) {
		boolean isEnabled = false;
		WbidBasepage.logger.info("Verifying Round selected: " + Round);
		for (WebElement option : roundList) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String RoundName = option.getText().trim();
			WbidBasepage.logger.info("Round: " + Round);
			if (RoundName.equalsIgnoreCase(Round)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				String att = objaction.getAttribute(option, "class");
				if (att.contains("active-re")) {
					WbidBasepage.logger.pass("Round" + Round + "' ✅ is enabled.");
					isEnabled = true;
				} else {
					WbidBasepage.logger.fail("Round" + Round + "' ❌ is disabled.");
				}
				break;
			}
		}
		if (!isEnabled) {
			WbidBasepage.logger.fail("Round" + Round + " not found or is disabled.");
		}
		return isEnabled;
	}
//Download Bid Button
	@FindBy(xpath = "(//button[text()=' Download '])[1]")
	public WebElement downloadBtn;

	public void clickDownload() {
		objwait.waitForElemntTobeClickable(driver, downloadBtn, 60);
		objaction.click(downloadBtn);
	}
// common method to select Domicile, Position and Round
	public void selectOptions(String domicile, String position, String round) {
		WbidBasepage.logger.info(
				"Attempting to select options - Base: " + domicile + ", Position: " + position + ", Round: " + round);
		selectFromList(basecities, domicile, "Base City");
		selectFromList(positionList, position, "Position");
		selectFromList(roundList, round, "Round");
	}
	private void selectFromList(List<WebElement> elements, String value, String elementType) {
		for (WebElement option : elements) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String optionText = option.getText().trim();

			if (optionText.equalsIgnoreCase(value)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				objaction.click(option);
				WbidBasepage.logger.pass(elementType + " '" + value + "' clicked successfully.");
				return;
			}
		}
		WbidBasepage.logger.fail("❌ " + elementType + " '" + value + "' not found in the list.");
	}
//common method to Verify  Domicile, Position and Round anre enabled
	public boolean verifyOptionsEnabled(String domicile, String position, String round) {
		WbidBasepage.logger.info("Verifying if options are enabled - Base: " + domicile + ", Position: " + position
				+ ", Round: " + round);

		boolean isDomicileEnabled = verifyElementEnabled(basecities, domicile, "Base City");
		boolean isPositionEnabled = verifyElementEnabled(positionList, position, "Position");
		boolean isRoundEnabled = verifyElementEnabled(roundList, round, "Round");

		return isDomicileEnabled && isPositionEnabled && isRoundEnabled;
	}
	private boolean verifyElementEnabled(List<WebElement> elements, String value, String elementType) {
		boolean isEnabled = false;
		WbidBasepage.logger.info("Verifying " + elementType + ": " + value);
		for (WebElement option : elements) {
			objwait.waitForElementTobeVisible(driver, option, 60);
			String optionText = option.getText().trim();
			WbidBasepage.logger.info(elementType + ": " + optionText);
			if (optionText.equalsIgnoreCase(value)) {
				objwait.waitForElemntTobeClickable(driver, option, 5);
				String att = objaction.getAttribute(option, "class");
				if (att.contains("active-re")) {
					WbidBasepage.logger.pass(elementType + " '" + value + "' ✅ is enabled.");
					isEnabled = true;
				} else {
					WbidBasepage.logger.fail(elementType + " '" + value + "' ❌ is disabled.");
				}
				break;
			}
		}
		if (!isEnabled) {
			WbidBasepage.logger.fail("❌ " + elementType + " '" + value + "' not found or is disabled.");
		}
		return isEnabled;
	}
//Seniority List
	@FindBy(xpath = "//h2[text()='Seniority List']")
	public WebElement seniorityHead;
	
	@FindBy(xpath = "//button[text()=' View Seniority List ']/following-sibling::button[@class='submit-cancel']")
	public WebElement seniorityCancel;
	
	public void clickSeniorityCancel() {
		objwait.waitForElemntTobeClickable(driver, seniorityCancel, 90);
		objaction.click(seniorityCancel);
	}
//Latest News
	@FindBy(xpath = "//h2[text()='Latest News']")
	public WebElement latestNwzHead;
	
	@FindBy(xpath = "//h2[text()='Latest News']/following-sibling::button[@aria-label='Close']")
	public WebElement latestNwzClose;
	
	public void clickLatestNwzClose() {
		objwait.waitForElemntTobeClickable(driver, latestNwzClose, 90);
		objaction.click(latestNwzClose);
	}
//Cover Letter
		@FindBy(xpath = "//h2[text()='Cover Letter']")
		public WebElement coverLetterHead;
		
		@FindBy(xpath = "//h2[text()='Cover Letter']/following-sibling::button[@aria-label='Close']")
		public WebElement coverLetterClose;
		
		public void clickCoverLetterClose() {
			objwait.waitForElemntTobeClickable(driver, coverLetterClose, 90);
			objaction.click(coverLetterClose);
		}
		@FindBy(xpath = "(//button[@class='submit-cancel'])[7]")
		public WebElement cancelFA;
//Close cover letter FA
		public void CoverLetterCloseFA() {
			objwait.waitForElemntTobeClickable(driver, coverLetterClose, 90);
			objaction.click(coverLetterClose);
			objaction.click(yesBtn);
			objaction.click(cancelFA);
		}
//Cover Letter content in FA	
		@FindBy(xpath = "//*[@id='fileContent-Section']")
		public WebElement coverLetterFA;
//Cover Letter content in FA A-B-C position lines		
		public boolean getABCPositionLinesFA(int abcLines) {
		    objwait.waitForElementTobeVisible(driver, coverLetterFA, 90);
		    objwait.waitS(3000);
		    String actualText = coverLetterFA.getText().trim();
		   
		    // Extracting the specific content
		    Pattern pattern = Pattern.compile("Number of A-B-C position lines:\\s*(\\d+)");
		    Matcher matcher = pattern.matcher(actualText);
		   
		    if (matcher.find()) {
		        String abcPositionLines = matcher.group(1);
		        WbidBasepage.logger.pass("Extracted Number of A-B-C position lines:  UI:-" + abcPositionLines);
		     // Check if the extracted value matches the expected dLines and return result
		        boolean isMatch = abcPositionLines.equals(String.valueOf(abcLines));
		        if (isMatch) {
		            WbidBasepage.logger.pass("Expected and extracted ABC position lines match: API:- " + abcLines);
		        } else {
		            WbidBasepage.logger.fail("Mismatch! Expected: " + abcLines + ", Extracted: " + abcPositionLines);
		        }
		        return isMatch;
		    } else {
		        WbidBasepage.logger.fail("Failed to extract Number of ABC position lines. Text was: " + actualText);
		        return false;
		    }
		}
//Cover Letter content in FA-B-C position lines	
		public boolean getBCPositionLinesFA(int bcLines) {
		    objwait.waitForElementTobeVisible(driver, coverLetterFA, 90);
		    String actualText = coverLetterFA.getText().trim();
		    // Extracting the specific content
		    Pattern pattern = Pattern.compile("Number of B-C position lines:\\s*(\\d+)");
		    Matcher matcher = pattern.matcher(actualText);
		   
		    if (matcher.find()) {
		        String bcdPositionLines = matcher.group(1);
		        WbidBasepage.logger.pass("Extracted Number of B-C position lines: UI:- " + bcdPositionLines);
		     // Check if the extracted value matches the expected dLines and return result
		        boolean isMatch = bcdPositionLines.equals(String.valueOf(bcLines));
		        if (isMatch) {
		            WbidBasepage.logger.pass("Expected and extracted BC position lines match: API:-" + bcLines);
		        } else {
		            WbidBasepage.logger.fail("Mismatch! Expected: " + bcLines + ", Extracted: " + bcdPositionLines);
		        }
		        return isMatch;
		    } else {
		        WbidBasepage.logger.fail("Failed to extract Number of BC position lines. Text was: " + actualText);
		        return false;
		    }
		}
//Cover Letter content in FA A-B-C-D position lines
		public boolean getABCDPositionLinesFA(int abcdLines) {
		    objwait.waitForElementTobeVisible(driver, coverLetterFA, 90);
		    String actualText = coverLetterFA.getText().trim();
		    // Extracting the specific content
		    Pattern pattern = Pattern.compile("Number of A-B-C-D position lines:\\s*(\\d+)");
		    Matcher matcher = pattern.matcher(actualText);
		    if (matcher.find()) {
		        String abcdPositionLines = matcher.group(1);
		        WbidBasepage.logger.pass("Extracted Number of A-B-C-D position lines: UI:- " + abcdPositionLines);
		     // Check if the extracted value matches the expected dLines and return result
		        boolean isMatch = abcdPositionLines.equals(String.valueOf(abcdLines));
		        if (isMatch) {
		            WbidBasepage.logger.pass("Expected and extracted ABCD position lines match: API:- " + abcdLines);
		        } else {
		            WbidBasepage.logger.fail("Mismatch! Expected: " + abcdLines + ", Extracted: " + abcdPositionLines);
		        }
		        return isMatch;
		    } else {
		        WbidBasepage.logger.fail("Failed to extract Number of ABCD position lines. Text was: " + actualText);
		        return false;
		    }
		}
//Cover Letter content in FA D position lines
		public boolean getDPositionLinesFA(int dLines) {
		    objwait.waitForElementTobeVisible(driver, coverLetterFA, 90);
		    String actualText = coverLetterFA.getText().trim();
		    // Extracting the specific content using regex
		    Pattern pattern = Pattern.compile("Number of D-ONLY position lines:\\s*(\\d+)");
		    Matcher matcher = pattern.matcher(actualText);
		    if (matcher.find()) {
		        String dPositionLines = matcher.group(1);
		        WbidBasepage.logger.pass("Extracted Number of D-ONLY position lines: UI :-" + dPositionLines);
		        // Check if the extracted value matches the expected dLines and return result
		        boolean isMatch = dPositionLines.equals(String.valueOf(dLines));
		        if (isMatch) {
		            WbidBasepage.logger.pass("Expected and extracted D-ONLY position lines match: API:- " + dLines);
		        } else {
		            WbidBasepage.logger.fail("Mismatch! Expected: " + dLines + ", Extracted: " + dPositionLines);
		        }
		        return isMatch;
		    } else {
		        WbidBasepage.logger.fail("Failed to extract Number of D-ONLY position lines. Text was: " + actualText);
		        return false;
		    }
		}
//ScratchPad
		@FindBy(xpath = "//ul[@class='m-auto header-title-section']/li")
		public WebElement scratchpadHead;
		
		public boolean verifyScratchpadHeading(String expectedDomicile, String expectedRound, String expectedPosition, String expectedMonthYear) {
			objwait.waitForElementTobeVisible(driver, scratchpadHead, 90);
			String actualText = scratchpadHead.getText().trim();
	        WbidBasepage.logger.pass("scratchpad Heading : "+actualText );
	        boolean isDomicileMatched = actualText.contains(expectedDomicile);
	        boolean isRoundMatched = actualText.contains(expectedRound);
	        boolean isPositionMatched = actualText.contains(expectedPosition);
	        boolean isMonthYearMatched = actualText.contains(expectedMonthYear);
	        // Return true only if all conditions are met
	        return isDomicileMatched && isRoundMatched && isPositionMatched && isMonthYearMatched;
	    }
		public String getNextMonthAndCurrentYear() {
	        LocalDate nextMonth = LocalDate.now().plusMonths(1);
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
	        return nextMonth.format(formatter);
	    }
		public String getNextMonth() {
	        LocalDate nextMonth = LocalDate.now().plusMonths(1); // Get next month
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM"); // Format as "MM"
	        return nextMonth.format(formatter);
	    }
//have to perform start over
		@FindBy(xpath = "//i[@class='fas fa-forward']")
		public WebElement moveIcon;
		
		@FindBy(xpath = "//i[@class='fas fas fa-ellipsis-h']")
		public WebElement moreDot;
		
		@FindBy(xpath = "//li[@class='dropdown-item']/a[text()='Start Over']")
		public WebElement startOverBtn;
		
		@FindBy(xpath = "//button[text()='Yes']")
		public WebElement yesBtn;
		
		public void startOver() {
			objwait.waitForElementTobeVisible(driver, moveIcon, 90);
			objaction.click(moveIcon);
			objwait.waitForElementTobeVisible(driver, moreDot, 90);
			objaction.click(moreDot);
			objwait.waitForElementTobeVisible(driver, startOverBtn, 90);
			objaction.click(startOverBtn);
			objwait.waitForElementTobeVisible(driver, yesBtn, 90);
			objaction.click(yesBtn);
			objwait.waitForElementTobeVisible(driver, yesBtn, 90);
			objaction.click(yesBtn);
		}
//Get total ScratchPad line count 
		@FindBy(xpath = "//h6[@class='scr-head']")
		public WebElement scrLinesHead;
		
		public boolean scrLinesTotalCount(int total) {
		    objwait.waitForElementTobeVisible(driver, scrLinesHead, 90);
		    String text = scrLinesHead.getText().trim();
		    WbidBasepage.logger.pass("scratchpad Lines total count UI: " + text);
		    WbidBasepage.logger.pass("scratchpad Lines total count API: " + text);
		    return text.contains(String.valueOf(total));
		}
//Get trip details-Trip Code
		@FindBy(xpath = "//td[contains(@class,'left-side-radius')]")
		public List<WebElement> tripList;//Trip detail visible when we click tripList
		
		@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[1]/div/pre")
		public WebElement tripSequence;//Trip code Extracted from Trip Sequence
//Get trip Code for one Trip		
		public String getTripCode() {
		    // Click on the first element in the trip list
		    if (!tripList.isEmpty()) {
		        tripList.get(0).click();
		    } 
		    // Wait for the trip sequence element to be visible
		    objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		    
		    // Get the text from the trip sequence element
		    String tripSequenceText = objaction.gettext(tripSequence).trim();
		    WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);
		    // Normalize whitespace and extract the trip code using regex
		    tripSequenceText = tripSequenceText.replaceAll("\\s+", " "); // Replace multiple spaces with a single space
		    Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
		    Matcher matcher = pattern.matcher(tripSequenceText);
		    if (matcher.find()) {
		        String tripCode = matcher.group(1).trim();
		        WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);
		        return tripCode;
		    }
		    WbidBasepage.logger.fail("Trip code not found in the trip sequence text.");
		    return null;
		}
//Get trip Code for all the Trips
		public List<String> getAllTripCodes() {
		    List<String> tripCodes = new ArrayList<>();
		    
		    for (WebElement tripElement : tripList) {
		        try {
		            objwait.waitForElementTobeVisible(driver, tripElement, 90);
		            // Scroll into view with an offset to avoid being covered by headers
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		            objwait.waitForElemntTobeClickable(driver, tripElement, 30);	            
		            // Use JavaScript click to avoid "element click intercepted" issues
		            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);		            
		            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		            
		            String tripSequenceText = objaction.gettext(tripSequence).trim();
		            WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);
		            
		            tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
		            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
		            Matcher matcher = pattern.matcher(tripSequenceText);
		            
		            if (matcher.find()) {
		                String tripCode = matcher.group(1).trim();
		                WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);
		                tripCodes.add(tripCode);
		                
		                Actions actions = new Actions(driver);
		                actions.sendKeys(Keys.ESCAPE).perform();
		                
		            } else {
		                WbidBasepage.logger.fail("Trip code not found in the trip sequence text.");
		            }
		            
		        } catch (Exception e) {
		            WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
		        }
		    }
		    return tripCodes;
		}
//Get compare trip Code from UI to tripCode fromAPI responses
		public boolean compareTripCodesAndFetchData(List<String> tripCodesFromUI) {
		    WbidBasepage.logger.info("Trip Codes from UI: " + tripCodesFromUI);
		    boolean allMatched = true; // Assume all matches are found initially

		    for (String tripCode : tripCodesFromUI) {
		        boolean found = false;
		        for (String dynamicData : TrialBidAPI.dynamicArray) {
		            if (dynamicData.startsWith(tripCode)) {
		                found = true;
		                WbidBasepage.logger.info("Matching Trip Code found: " + tripCode);
		                WbidBasepage.logger.info("Corresponding Data: " + dynamicData);		            
		                break;
		            }
		        }
		        if (!found) {
		            WbidBasepage.logger.fail("Trip Code not found in dynamic array: " + tripCode);
		            allMatched = false; // Set to false if any trip code is not found
		        }
		    }		    
		    return allMatched;
		}
//Get trip Details 
		@FindBy(xpath = "(//pre[@style='text-decoration: inherit;' and normalize-space(.)!=''])[position() > 2]")
		public List<WebElement> tripdata;//gets data starting  from trip details skip 1st 2 lines which show trip code and table header
		
		public List<String> getAllTripDates() {
		    List<String> tripDates = new ArrayList<>();
		    for (WebElement tripElement : tripList) {  
		        try {
		            objwait.waitForElementTobeVisible(driver, tripElement, 90);
		            // Scroll into view
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		            objwait.waitForElemntTobeClickable(driver, tripElement, 30);
		            // Try normal click first, fallback to JavaScript click
		            try {
		                tripElement.click();
		            } catch (Exception e) {
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
		            }
		            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		            String tripSequenceText = objaction.gettext(tripSequence).trim();
		            WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);
		            
		            tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
		            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
		            Matcher matcher = pattern.matcher(tripSequenceText);
		            
		            if (matcher.find()) {
		                String tripCode = matcher.group(1).trim();
		                WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);
		              //  tripCodes.add(tripCode);

		            for (WebElement tripEle : tripdata) {
		                String tripDataText = objaction.gettext(tripEle).trim();
		                WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

		                // Ignore trip data starting with "Rpt" or "TAFB"
		                if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
		                    WbidBasepage.logger.info("Ignoring trip data: " + tripDataText);
		                    continue;
		                }
		                tripDataText = tripDataText.replaceAll("\\s+", " ");		                
		                // Extract date (e.g., "01Apr", "15May")
		                Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
		                Matcher matcherD = patternD.matcher(tripDataText);

		                if (matcherD.find()) {
		                    String tripDate = matcherD.group(1).trim();
		                    System.out.println("Extracted Trip Code :"+tripCode+"and Trip Date: "+ tripDate);
		                    WbidBasepage.logger.info("Extracted Trip Code UI:"+tripCode+"and Trip Date: "+ tripDate);
		                    tripDates.add(tripDate);
		                     } else {
		                    WbidBasepage.logger.fail("Trip code not found in the trip sequence text.");
		                }
		            }
		            // Close modal properly
		            try {
		                Actions actions = new Actions(driver);
		                actions.sendKeys(Keys.ESCAPE).perform();
		            } catch (Exception e) {
		                WbidBasepage.logger.info("Modal close action failed, continuing.");
		            }
		        } 
		        }catch (Exception e) {
		            WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
		        }
		    }
		    return tripDates;
		}
//Get all Trip data
		public Map<String, String> getAllTripData() {
		    Map<String, String> tripDataMap = new HashMap<>(); // Stores (Trip Code -> Date)

		    for (WebElement tripElement : tripList) {  
		        try {
		            objwait.waitForElementTobeVisible(driver, tripElement, 90);

		            // Scroll into view
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		            objwait.waitForElemntTobeClickable(driver, tripElement, 30);

		            // Try normal click first, fallback to JavaScript click
		            try {
		                tripElement.click();
		            } catch (Exception e) {
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
		            }

		            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		            String tripSequenceText = objaction.gettext(tripSequence).trim();
		            WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);
		            
		            tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
		            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
		            Matcher matcher = pattern.matcher(tripSequenceText);
		            
		            if (matcher.find()) {
		                String tripCode = matcher.group(1).trim();
		                WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);

		                for (WebElement tripEle : tripdata) {
		                    String tripDataText = objaction.gettext(tripEle).trim();
		                    WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

		                    // Ignore unwanted trip data
		                    if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
		                        WbidBasepage.logger.info("Ignoring trip data: " + tripDataText);
		                        continue;
		                    }

		                    tripDataText = tripDataText.replaceAll("\\s+", " ");
		                    
		                    // Extract date (e.g., "01Apr", "15May")
		                    Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
		                    Matcher matcherD = patternD.matcher(tripDataText);

		                    if (matcherD.find()) {
		                        String tripDate = matcherD.group(1).trim();
		                        WbidBasepage.logger.info("Extracted Trip Code: " + tripCode + " and Trip Date: " + tripDate);

		                        // Store the trip code and date in the map
		                        tripDataMap.put(tripCode, tripDate);
		                        
		                    } else {
		                        WbidBasepage.logger.fail("Trip date not found in the trip sequence text.");
		                    }
		                }
		            }

		            // Close modal properly
		            try {
		                new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		            } catch (Exception e) {
		                WbidBasepage.logger.info("Modal close action failed, continuing.");
		            }
		        } catch (Exception e) {
		            WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
		        }
		    }
		    return tripDataMap; // Return trip code and corresponding date
		}
//using TreeSet get trip code and corresponding date from UI to API

		public boolean getAllTripDataAndCompare(List<TripEntry> apiTripList) {
		    Map<String, Set<String>> uiTripDataMap = new LinkedHashMap<>(); // Stores (Trip Code -> Dates)
		    Map<String, Set<String>> apiTripMap = new LinkedHashMap<>();  // Convert API List<TripEntry> to Map
		    boolean isDataMatching = true; // Assume data matches initially

		    // ✅ Convert API List to Map<String, Set<String>> to prevent duplicates
		    for (TripEntry entry : apiTripList) {
		        String tripCode = entry.getTripCode().trim().toUpperCase();
		        if (tripCode.length() >= 4) {
		            tripCode = tripCode.substring(0, 4); // ✅ Extract first 4 characters
		        }
		        String tripDate = entry.getTripDate().trim().replaceAll("\\s+", ""); // Normalize API date format
		        apiTripMap.computeIfAbsent(tripCode, k -> new TreeSet<>()).add(tripDate); // ✅ Use TreeSet for auto-sorting
		    }
		 //   int i = 0;
		    for (WebElement tripElement : tripList) {  
		        try {
		  //          i++;
		   //         if (i <= 5)
		        	{
		                objwait.waitForElementTobeVisible(driver, tripElement, 90);
		                // Scroll into view
		                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		                objwait.waitForElemntTobeClickable(driver, tripElement, 30);

		                // Try normal click first, fallback to JavaScript click
		                try {
		                    tripElement.click();
		                } catch (Exception e) {
		                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
		                }
		                objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		                String tripSequenceText = objaction.gettext(tripSequence).trim();
		              //  WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);

		                tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
		                Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
		                Matcher matcher = pattern.matcher(tripSequenceText);

		                if (matcher.find()) {
		                    String tripCode = matcher.group(1).trim();
		                  //  WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);

		                    for (WebElement tripEle : tripdata) {
		                    	 objwait.waitForElementTobeVisible(driver, tripEle, 90);
		                        String tripDataText = objaction.gettext(tripEle).trim();
		                     //   WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

		                        // Ignore unwanted trip data
		                        if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
		                           // WbidBasepage.logger.info("Ignoring trip data: " + tripDataText);
		                            continue;
		                        }
		                        tripDataText = tripDataText.replaceAll("\\s+", " ");
		                        // Extract date (e.g., "01Apr", "15May")
		                        Pattern patternD = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
		                        Matcher matcherD = patternD.matcher(tripDataText);

		                        if (matcherD.find()) {
		                            String tripDate = matcherD.group(1).trim();
		                        //    WbidBasepage.logger.info("Extracted Trip Code UI: " + tripCode + " and Trip Date UI: " + tripDate);

		                            // ✅ Store dates in Set inside Map (Avoids duplicates)
		                            uiTripDataMap.computeIfAbsent(tripCode, k -> new TreeSet<>()).add(tripDate);
		                        } else {
		                            WbidBasepage.logger.fail("Trip date not found in the trip sequence text.");
		                            isDataMatching = false;
		                        }
		                    }
		                }
		                // Close modal properly
		                try {
		                    new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		                } catch (Exception e) {
		                    WbidBasepage.logger.info("Modal close action failed, continuing.");
		                }
		            }
		        } catch (Exception e) {
		            WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
		            isDataMatching = false;
		        }
		    }
		    // ✅ Compare UI vs API Dates AFTER collecting all data
		    for (String tripCode : apiTripMap.keySet()) {
		        Set<String> apiDates = apiTripMap.getOrDefault(tripCode, new TreeSet<>());
		        Set<String> uiDates = uiTripDataMap.getOrDefault(tripCode, new TreeSet<>());

		        if (!uiDates.equals(apiDates)) {
		            WbidBasepage.logger.fail("Mismatch Found: Trip Code: " + tripCode + 
		                " | UI Dates: " + uiDates + " | API Dates: " + apiDates);
		            isDataMatching = false;
		        } else {
		            WbidBasepage.logger.info("Matching Data: Trip Code: " + tripCode + " | Dates: " + uiDates);
		        }
		    }
		    // ✅ Final Check: Trips in API but Missing in UI
		    for (String apiTripCode : apiTripMap.keySet()) {
		        if (!uiTripDataMap.containsKey(apiTripCode)) {
		            WbidBasepage.logger.fail("Trip Code " + apiTripCode + " found in API but missing in UI!");
		            isDataMatching = false;
		        }
		    }
		    return isDataMatching; // True if all data matches, false if mismatches are found
		}
//Get TAFB from UI for each Trip(corresponding TipCode)
		@FindBy(xpath = "(//pre[@style='text-decoration: inherit;' and normalize-space(.)!=''])[last()]")
		public WebElement tripTAFB;//get last data starting containing TAFB
		
		public Map<String, Double> getAllTAFB() {
		    Map<String, String> tripTAFBMap = new HashMap<>(); 
			Map<String, Double> tafbMapUI = new HashMap<>();
           int i=0;
		    for (WebElement tripElement : tripList) {  
		        try {
		        	i++;
		        if(i<=5) {
		            objwait.waitForElementTobeVisible(driver, tripElement, 90);
		            // Scroll into view
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		            objwait.waitForElemntTobeClickable(driver, tripElement, 30);
		            // Try normal click first, fallback to JavaScript click
		            try {
		                tripElement.click();
		            } catch (Exception e) {
		                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
		            }

		            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		            String tripSequenceText = objaction.gettext(tripSequence).trim();
		          //  WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);

		            tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
		            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
		            Matcher matcher = pattern.matcher(tripSequenceText);

		            if (matcher.find()) {
		                String tripCode = matcher.group(1).trim();
		            //    WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);

		              
		                    String tripDataText = objaction.gettext(tripTAFB).trim();
		           //         WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

		                    // Look for TAFB data
		                    if (tripDataText.startsWith("TAFB")) {
		                    //    WbidBasepage.logger.info("trip data: " + tripDataText);
		                        tripDataText = tripDataText.replaceAll("\\s+", " ");

		                        // Extract TAFB value (e.g., "TAFB 5250")
		                        Pattern patternD = Pattern.compile("TAFB\\s+(\\d+)");
		                        Matcher matcherD = patternD.matcher(tripDataText);

		                        if (matcherD.find()) {
		                            String tripTAFB = matcherD.group(1).trim();
		                            WbidBasepage.logger.info("UI Extracted Trip Code: " + tripCode + " UI Trip TAFB: " + tripTAFB);

		                            // Store the trip code and TAFB in the map
		                                if (tripTAFBMap.containsKey(tripCode)) {
		                                tafbMapUI.put(tripCode, Double.parseDouble(tripTAFBMap.get(tripTAFB)));
		                            }
		                        } else {
		                            WbidBasepage.logger.fail("TAFB value not found in trip data.");
		                        }
		                    }
		                }
		            }

		            // Close modal properly after processing the trip
		            try {
		                new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		            } catch (Exception e) {
		                WbidBasepage.logger.info("Modal close action failed, continuing.");
		            }

		        
		        } catch (Exception e) {
		            WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
		        }
		    }

		    return tafbMapUI; // Return trip code and corresponding TAFB value
		}
//Compare TAFB with API data
		public boolean getAllTAFBAndCompare(Map<String, Double> apiTAFBMap) {
		    Map<String, Double> tafbMapUI = new HashMap<>();
		    boolean isDataMatching = true; // Assume data matches initially
		 //   int i = 0;

		    for (WebElement tripElement : tripList) {  
		        try {
		         //   i++;
		          //  if (i <= 5) { // Process only the first 5 trips
		                objwait.waitForElementTobeVisible(driver, tripElement, 90);
		                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		                objwait.waitForElemntTobeClickable(driver, tripElement, 30);

		                // Try normal click, fallback to JavaScript click
		                try {
		                    tripElement.click();
		                } catch (Exception e) {
		                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
		                }

		                objwait.waitForElementTobeVisible(driver, tripSequence, 90);
		                String tripSequenceText = objaction.gettext(tripSequence).trim();
		            //    WbidBasepage.logger.info("Trip Sequence Text: " + tripSequenceText);

		                tripSequenceText = tripSequenceText.replaceAll("\\s+", " ");
		                Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated"); // Extract Trip Code
		                Matcher matcher = pattern.matcher(tripSequenceText);

		                if (matcher.find()) {
		                    String tripCode = matcher.group(1).trim();
		              //      WbidBasepage.logger.info("Extracted Trip Code: " + tripCode);

		                    // Extract TAFB from UI
		                    String tripDataText = objaction.gettext(tripTAFB).trim();
		             //       WbidBasepage.logger.info("Trip Data Text: " + tripDataText);

		                    if (tripDataText.startsWith("TAFB")) {
		                        tripDataText = tripDataText.replaceAll("\\s+", " ");

		                        // Extract TAFB value (supporting decimals)
		                        Pattern patternD = Pattern.compile("TAFB\\s+(\\d+\\.?\\d*)");
		                        Matcher matcherD = patternD.matcher(tripDataText);

		                        if (matcherD.find()) {
		                            String tripTAFB = matcherD.group(1).trim();
		                            Double tafbValue = Double.parseDouble(tripTAFB);
		                            WbidBasepage.logger.info("UI Extracted Trip Code: " + tripCode + " | UI Trip TAFB: " + tafbValue);

		                            // Store in UI Map
		                            tafbMapUI.put(tripCode, tafbValue);
		                        } else {
		                            WbidBasepage.logger.fail("TAFB value not found for Trip Code: " + tripCode);
		                        }
		                    }
		                }

		                // Close modal properly after processing the trip
		                try {
		                    new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		                } catch (Exception e) {
		                    WbidBasepage.logger.info("Modal close action failed, continuing.");
		                }
		           // }
		      } catch (Exception e) {
		            WbidBasepage.logger.fail("Failed to interact with trip element: " + e.getMessage());
		        }
		    }
		 // ✅ **Compare API vs UI TAFB Values**
		    for (String tripCode : apiTAFBMap.keySet()) {
		        Double apiTAFB = apiTAFBMap.get(tripCode);
		        Double uiTAFB = tafbMapUI.get(tripCode);

		        if (uiTAFB == null) {
		            WbidBasepage.logger.fail("Trip Code " + tripCode + " found in API but missing in UI!");
		            isDataMatching = false;
		            continue;
		        }

		        // ✅ Compare with tolerance
		        if (Math.abs(uiTAFB - apiTAFB) > 0.0001) {  // EPSILON = 0.0001
		            WbidBasepage.logger.fail("TAFB Mismatch for Trip " + tripCode +
		                    " | UI TAFB: " + uiTAFB + " | API TAFB: " + apiTAFB);
		            isDataMatching = false;
		        } else {
		            WbidBasepage.logger.info("TAFB Match for Trip " + tripCode +
		                    " | UI TAFB: " + uiTAFB + " | API TAFB: " + apiTAFB);
		        }
		    }

		    // ✅ Check for Trips in UI but Missing in API
		    for (String uiTripCode : tafbMapUI.keySet()) {
		        if (!apiTAFBMap.containsKey(uiTripCode)) {
		            WbidBasepage.logger.fail("Trip Code " + uiTripCode + " found in UI but missing in API!");
		            isDataMatching = false;
		        }
		    }

		    return isDataMatching; // Returns false if any mismatch is found
		}
//Get cred value
		@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div/div/pre")
		public List<WebElement> tripdataLines;
		
		public List<String> getAllTripLinesValues() {
		   // List<String> tripCodes = new ArrayList<>();
		    List<String> tripLines = new ArrayList<>();
		    
		    for (WebElement tripElement : tripList) {
		            objwait.waitForElementTobeVisible(driver, tripElement, 90);
		            // Scroll into view with an offset to avoid being covered by headers
		            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
		            objwait.waitForElemntTobeClickable(driver, tripElement, 30);
		            // Use JavaScript click to avoid "element click intercepted" issues
		            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
		            		            
		            for(WebElement tripLine:tripdataLines ) {
		            	objwait.waitForElementTobeVisible(driver, tripLine, 90);  
			            String tripLineText = objaction.gettext(tripLine).trim();
			            WbidBasepage.logger.info("Trip Line Text: " + tripLineText);
			            tripLines.add(tripLineText);
		            }
		            Actions actions = new Actions(driver);
		            actions.sendKeys(Keys.ESCAPE).perform();      
		        }
		    return tripLines;
		}

		
//		Get Trip Start date 
		
		@FindBy(xpath = "//*[contains(@class, 'top-wrapper') and normalize-space(.)='2']/preceding::td[contains(@class, 'ul-date')]//p")
		public List<WebElement> allDateElements;
		
		@FindBy(xpath = "//*[contains(@class, 'top-wrapper') and normalize-space(.)='2']/preceding::td[contains(@class, 'trip') and not(contains(@style,'background-color'))and not(contains(@class,'disabled '))]//p")
		public List<WebElement> datesWithoutTrip;
		
		public void getDatesWithoutTrip() {
			System.out.println("datesWithoutTrip Count: " + datesWithoutTrip.size());
	        for (WebElement trip : datesWithoutTrip) {
	        	WbidBasepage.logger.info("Dates Without Trip : " + trip.getText());
	        	
	        	
	        }
	    }
		@FindBy(xpath = "//*[contains(@class, 'top-wrapper') and normalize-space(.)='2']/preceding::td[contains(@class,'left-side-radius')]")
		public List<WebElement> tripStartDates;
		
		@FindBy(xpath = "//*[contains(@class, 'top-wrapper') and normalize-space(.)='2']/preceding::td[contains(@class,'right-side-radius')]")
		public List<WebElement> tripEndDates;
		
		@FindBy(xpath = "//*[contains(@class, 'top-wrapper') and normalize-space(.)='2']/preceding::td[contains(@class,'left-side-radius right-side-radius')]")
		public List<WebElement> tripStartEndSameDate;
		
		@FindBy(xpath = "//*[contains(@class, 'top-wrapper') and normalize-space(.)='2']/preceding::td[contains(@class, 'trip1_1')]")
		public List<WebElement> calandercount;

		public void getTripStartDates() {
		    List<Integer> visibleDates = new ArrayList<>();

		    // Get next month's year and month
		    LocalDate today = LocalDate.now();
		    LocalDate nextMonthDate = today.plusMonths(1);
		    int nextMonthDays = YearMonth.of(nextMonthDate.getYear(), nextMonthDate.getMonthValue()).lengthOfMonth();

		    WbidBasepage.logger.info("Next Month: " + nextMonthDate.getMonth() + " Days: " + nextMonthDays);

		    // Extract visible dates (where no trips are present)
		    for (WebElement date : datesWithoutTrip) {
		        String text = date.getText().trim();
		        if (!text.isEmpty() && text.matches("\\d+")) {  // Ensure it's a valid date
		            int dateValue = Integer.parseInt(text);
		            visibleDates.add(dateValue);
		            WbidBasepage.logger.info("Date Without Trip: " + dateValue);
		        }
		    }

		    // Get the total number of trip elements (to ensure correct trip count)
		    int totalTrips = calandercount.size();
		    WbidBasepage.logger.info("Total Trips Count: " + totalTrips);

		    // Sort visible dates
		    Collections.sort(visibleDates);

		    // Find missing dates (trip start dates)
		    List<Integer> tripStartDatesDerived = new ArrayList<>();
		    for (int i = 1; i <= nextMonthDays; i++) {  // Use next month's days limit
		        if (!visibleDates.contains(i)) {
		            tripStartDatesDerived.add(i); // Missing dates = Trip start dates
		        }
		    }

		    // Log all derived trip start dates
		    System.out.println("Derived Trip Start Dates: " + tripStartDatesDerived);
		    WbidBasepage.logger.info("Derived Trip Start Dates: " + tripStartDatesDerived);
		}
		
}
