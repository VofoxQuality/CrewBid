package utilities;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitCondition {
/////////////////////////// Implicit wait///////////////////////////////
	public void implicitWait(WebDriver driver) {
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

/////////////////////// Explicit wait for element to be
/////////////////////// visible///////////////////////
	public void waitForElementTobeVisible(WebDriver driver, WebElement element, long seconds) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
		wait.until(ExpectedConditions.visibilityOf(element));
	}

/////////////////// Explicit wait for element to be
/////////////////// clickable///////////////////////////
	public void waitForElemntTobeClickable(WebDriver driver, WebElement element, long i) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(i));
		wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	////////////////until the element is not displayed////
	public boolean waitPopupToClose(WebDriver driver, WebElement closeButton, long seconds) throws Exception {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds)); // Adjust timeout as needed
	    try {
	        // Wait until the close button is not displayed
	        wait.until(ExpectedConditions.invisibilityOf(closeButton));
	        return true; // Popup is closed
	    } catch (Exception e) {
	        return false; // Popup is still open or close button not found
	    }
	}

///////////////////// Wait//////////////////////
	public void waitS(long timeout) {
		try {
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

////////////////////////////// Fluent Wait//////////////////////////////////
	public void fluentWait(WebDriver driver, WebElement element, String attribute, String attributeValue) {
		Wait<WebDriver> fwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMillis(4000))
				.pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);
		fwait.until(ExpectedConditions.attributeToBe(element, attribute, attributeValue));
	}

	public void fluentWait(WebDriver driver, WebElement element) {
		Wait<WebDriver> fwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMillis(5000))
				.pollingEvery(Duration.ofMillis(250)).ignoring(NoSuchElementException.class);
		fwait.until(ExpectedConditions.visibilityOf(element));
	}
	
	
}
