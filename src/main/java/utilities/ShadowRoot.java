package utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

public class ShadowRoot {
	WebDriver driver;
	WaitCondition objwait = new WaitCondition();
	public ShadowRoot(WebDriver driver) {

		this.driver = driver;
	}
	// To click shadowroot element
	public void Shadowclick(String elementScript) {
		try {
			WebElement root = ((WebElement) ((JavascriptExecutor) driver).executeScript(elementScript));
			root.click();
			WbidBasepage.logger.info("Clicked on shadow root element");
			objwait.waitS(2000);
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to click on shadow root element: " + e.getMessage());
		}
	}

	// To get text from shadow root element
	public String ShadowgetText(String elementScript) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement root = (WebElement) js.executeScript(elementScript);
			String text = (String) js.executeScript("return arguments[0].textContent;", root);
			WbidBasepage.logger.info("Retrieved text from shadow root element: " + text);
			objwait.waitS(2000);
			return text;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to retrieve text from shadow root element: " + e.getMessage());
			return null;
		}
	}

	// To check visibility of shadow root element
	public boolean Shadowelementvisibility(String Element) {

		//JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement root = ((WebElement) ((JavascriptExecutor) driver)
	            .executeScript(Element));
		
		boolean status=root.isDisplayed();
		objwait.waitS(2000);
		return status;
		
	}

	// To send keys to shadow root element
	public void Shadowrootsendkeys(String elementScript, String text) {
		try {
			WebElement root = ((WebElement) ((JavascriptExecutor) driver).executeScript(elementScript));
			root.sendKeys(text);
			WbidBasepage.logger.info("Entered text '" + text + "' into shadow root element");
			objwait.waitS(2000);
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to enter text into shadow root element: " + e.getMessage());
		}
	}

}
