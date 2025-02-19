package pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class BidDownloadPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();

	public BidDownloadPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}

	@FindBy(xpath = "//nav[@class=\"navbar navbar-expand-sm navbar-light\"]/img")
	public WebElement logo;
	@FindBy(id = "navbardrop")
	public WebElement retrivedropdown;

	public boolean fordisplaylogo() {
		return objaction.fordisplay(logo);
	}

	public boolean fordisplayretrivedropdown() {
		return objaction.fordisplay(retrivedropdown);
	}

	public void click_retrievedownload() {
		objaction.click(retrivedropdown);
	}

	@FindBy(xpath = "//a[text()=\"Retrieve New BidData\"]")
	public WebElement retrievenewbiddata;
	@FindBy(xpath = "//a[text()=\"Retrieve Historical BidData\"]")
	public WebElement retrievehistoricalbiddata;

	public boolean fordisplaynewbiddata() {
		return objaction.fordisplay(retrievenewbiddata) && objaction.fordisplay(retrievehistoricalbiddata);
	}

	// TC 7
	public void forclicknewbiddata() {
		objaction.click(retrievenewbiddata);
	}

	@FindBy(xpath = "//h2[text()=\"Enter Employee Number\"]")
	public WebElement empnumberpopupheader;

	public String checkEmpnumpopupheader() {
		return objaction.gettext(empnumberpopupheader);
	}

	// TC 8
	@FindBy(xpath = "(//p[@class=\"text-center\"])[2]")
	public WebElement emppopuptext;

	public String checkempPopupText() {
		return objaction.gettext(emppopuptext);
	}

	// TC 9
	@FindBy(xpath = "//button[@class=\"submit-cancel btn\"]")
	public WebElement cancel_btn;
	@FindBy(xpath = "//button[@class=\"submit-bid btn\"]")
	public WebElement ok_btn;

	public boolean fordisplaybtninpopup() {
		return objaction.fordisplay(cancel_btn) && objaction.fordisplay(ok_btn);
	}

	// Tc 10
	@FindBy(name = "empNum")
	public WebElement empplaceholder;

	public boolean checkplaceholder() {
		String att = objaction.getAttribute(empplaceholder, "placeholder");
		WbidBasepage.logger.info(att);
		if (att.contains("Employee No:")) {
			WbidBasepage.logger.pass("Placeholder is same");
			return true;
		} else {
			WbidBasepage.logger.fail("Placeholder mismatch");
			return false;
		}
	}

	// TC 11
	public void enterempid() {
		objaction.sendkey(empplaceholder, "21221");
		objaction.click(ok_btn);
	}

	public void forclickokbtn() {
		objaction.click(ok_btn);
	}

	@FindBy(xpath = "//h2[text()=\"Retrieve New Bid Period\"]")
	public WebElement retrievebidpopupheader;

	public String checkretrievebidpopupheader() {
		return objaction.gettext(retrievebidpopupheader);
	}

	// TC 13
	@FindBy(xpath = "//*[@id=\"newBidModal\"]/div/div/div[2]/div/div[1]")
	public List<WebElement> labels;
	public List<String> Actuallabels;

	public boolean checkRetrievebidpopuplabels() {
		Actuallabels = new ArrayList<>();
		for (WebElement option : labels) {
			String label = objaction.gettext(option);
			WbidBasepage.logger.info("Labels:" + label);
			Actuallabels.add(label);
		}
		List<String> Expectedlabels = Arrays.asList("Base:", "Position:", "Round:", "Month:");
		if (Actuallabels.containsAll(Expectedlabels) && Expectedlabels.containsAll(Actuallabels)) {
			WbidBasepage.logger.pass("Labels are same");
			return true;
		} else {
			WbidBasepage.logger.fail("Labels are mismatch");
			return false;
		}
	}

	// TC 15
	@FindBy(xpath = "(//div[@class=\"modal-header text-center\"]/button[@class=\"close\"])[1]")
	public WebElement close_btn;

	public void click_closebtn() {
		objaction.click(close_btn);
	}

//TC16
	@FindBy(xpath = "//*[@id=\"newBidModal\"]/div/div/div[2]/div[1]/div[2]/button")
	public List<WebElement> basecities;

	public boolean checkcities_isenable() {
		boolean allEnabled = true;
		WbidBasepage.logger.info("Verifying all base cities:");

		for (WebElement option : basecities) {
			String cityName = option.getText();
			WbidBasepage.logger.info("City: " + cityName);

			objwait.waitForElemntTobeClickable(driver, option, 5);
			objaction.click(option);
			objwait.waitS(3000);
			String att = objaction.getAttribute(option, "class");

			if (att.contains("active-re")) {
				WbidBasepage.logger.pass("City '" + cityName + "' is enabled.");
			} else {
				WbidBasepage.logger.fail("City '" + cityName + "' is disabled.");
				allEnabled = false;
			}
		}

		if (allEnabled) {
			WbidBasepage.logger.pass("✅ All 14 base cities are enabled.");
		} else {
			WbidBasepage.logger.fail("❌ Some base cities are disabled.");
		}
		return allEnabled;
	}
//TC17
	@FindBy(xpath="//*[@id=\"newBidModal\"]/div/div/div[2]/div[2]/div[2]/button")
	public List<WebElement> positionlist;
	public boolean checkposition_isenable() {
		boolean allEnabled = true;
		WbidBasepage.logger.info("Verifying all Positions:");

		for (WebElement option : positionlist) {
			String position = option.getText();
			WbidBasepage.logger.info("City: " + position);

			objwait.waitForElemntTobeClickable(driver, option, 5);
			objaction.click(option);
			objwait.waitS(3000);
			String att = objaction.getAttribute(option, "class");

			if (att.contains("active-re")) {
				WbidBasepage.logger.pass("Position'" + position + "' is enabled.");
			} else {
				WbidBasepage.logger.fail("Position '" + position + "' is disabled.");
				allEnabled = false;
			}
		}

		if (allEnabled) {
			WbidBasepage.logger.pass("✅ All 3 Positions are enabled.");
		} else {
			WbidBasepage.logger.fail("❌ Some Positions are disabled.");
		}
		return allEnabled;
	}
}
