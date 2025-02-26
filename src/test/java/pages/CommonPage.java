package pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

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
//Get trip details-Trip Code
		@FindBy(xpath = "//td[contains(@class,'left-side-radius trip-text-color ng-star-inserted')]")
		public List<WebElement> tripList;
		
		@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[1]/div/pre")
		public WebElement tripSequence;
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
//Get compare trip Code from UI to tripCode fromAPI response
		public void UITripCodesAndFetchDataFromAPI(List<String> tripCodesFromUI) {
		    // Get all trip codes from the UI
		    //List<String> tripCodesFromUI = getAllTripCodes();
		    WbidBasepage.logger.info("Trip Codes from UI: " + tripCodesFromUI);

		    // Loop through each trip code
		    for (String tripCode : tripCodesFromUI) {
		        boolean found = false;

		        // Compare each trip code with the dynamic array
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
		        }
		    }
		}
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


}
