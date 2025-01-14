package utilities;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;

public class ActionUtilities {
	WebDriver driver;
	Alert objalert;
	Select objdrop;
	Actions objaction;
	WaitCondition objwait = new WaitCondition();

	public ActionUtilities(WebDriver driver) {

		this.driver = driver;
	}

///////////////Shadow Root//////////////////////
	public void Shadowclick(String Element) {

//JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement root = ((WebElement) ((JavascriptExecutor) driver).executeScript(Element));
		root.click();
		objwait.waitS(2000);
	}

	public boolean Shadowelementvisibility(String Element) {

		// JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement root = ((WebElement) ((JavascriptExecutor) driver).executeScript(Element));

		boolean status = root.isDisplayed();
		objwait.waitS(2000);
		return status;

	}

	// To get text from shadow root element
	public String ShadowgetText(String Element) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement root = (WebElement) js.executeScript(Element);
		String text = (String) js.executeScript("return arguments[0].textContent;", root);
		objwait.waitS(2000);
		return text;
	}

	public void Shadowrootsendkeys(String Element, String text) {

		// JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement root = ((WebElement) ((JavascriptExecutor) driver).executeScript(Element));
		root.sendKeys(text);
		objwait.waitS(2000);
	}

////////////////////Actions//////////////////////////
	public void click(WebElement value) {
		for (int i = 0; i < 5; i++) {
			try {
				objwait.waitForElemntTobeClickable(driver, value, 20);
				value.click();
				WbidBasepage.logger.log(Status.PASS, "Successfully clicked on the element");
				break;
			} catch (ElementNotInteractableException ex) { // from org.openqa.selenium.ElementNotInteractableException
				WbidBasepage.logger.log(Status.INFO,
						"Element not interactable or stale, retrying click attempt: " + (i + 1));
				continue;
			} catch (Exception e) { // Fail on all other exceptions
				System.out.println(e);
				WbidBasepage.logger.fail(e);
				WbidBasepage.logger.log(Status.FAIL, "Unable to click on the element");
				break;
			}
		}
	}

	public void interactable(WebElement element) {
		try {
			objwait.waitForElemntTobeClickable(driver, element, 20);
			if (element.isDisplayed() && element.isEnabled()) {
				element.click();
				WbidBasepage.logger.info("Clicked on the element");
			} else {
				WbidBasepage.logger.log(Status.INFO, "Element is not clickable");
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to interact with the element: " + e.getMessage());
		}
	}

	// To click on the javascript click
	public void JavaScriptclick(WebElement value) {
		for (int i = 0; i < 5; i++) {
			try {
				objwait.waitForElemntTobeClickable(driver, value, 20);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click()", value);
				WbidBasepage.logger.log(Status.PASS, "Successfully clicked on the element using JavaScript");
				break;
			} catch (ElementNotInteractableException ex) { // from org.openqa.selenium.ElementNotInteractableException
				WbidBasepage.logger.log(Status.INFO,
						"Element not interactable retrying JavaScript click attempt:" + (i + 1));
				continue;
			} catch (Exception e) { // Fail on all other exceptions
				System.out.println(e);
				WbidBasepage.logger.fail(e);
				WbidBasepage.logger.log(Status.FAIL, "Unable to click on the element using JavaScript");
				break;
			}
		}
	}

	public void clear(WebElement value) {
		try {
			value.clear();
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to clear the input field. Error: " + e.getMessage());
		}
	}

	public void sendkey(WebElement value, String data) {
		try {
			if (value.isDisplayed()) {
				value.clear();
				value.sendKeys(data);
				WbidBasepage.logger.log(Status.PASS, "Successfully entered value: " + data);
				System.out.println("Successfully entered value: " + data);
			} else {
				System.out.println("Element is not displayed");
				WbidBasepage.logger.log(Status.FAIL, "Element is not displayed");
			}
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			System.out.println("Element not found");
			WbidBasepage.logger.log(Status.FAIL, "Element not found");
		}
	}

	public boolean type(WebElement ele, String text) {
		boolean flag = false;
		try {
			flag = ele.isDisplayed();
			ele.clear();
			ele.sendKeys(text);
			// logger.info("Entered text :"+text);
			flag = true;
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.info("Location Not found");
			System.out.println("Location Not found");
			flag = false;
		} finally {
			if (flag) {
				WbidBasepage.logger.log(Status.PASS, "Successfully entered value");
				System.out.println("Successfully entered value");
			} else {
				System.out.println("Unable to enter value");
				WbidBasepage.logger.log(Status.FAIL, "Unable to enter value");
			}
		}
		return flag;
	}

	public String gettext(WebElement value) {
		try {
			objwait.waitForElementTobeVisible(driver, value, 100);
			return value.getText();
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.info("Element not visible or stale");
			System.out.println("Element not visible or stale");
			WbidBasepage.logger.log(Status.FAIL, "Element not visible or stale");
			return ""; // or any default value or indication of failure
		}
	}

	//////////// Label checking////////////////////
	public boolean Labelchecking(String expectedlabel, WebElement path) {
		try {
			String actualHeading = gettext(path);
			boolean result = actualHeading.contains(expectedlabel);
			if (result) {
				WbidBasepage.logger.log(Status.PASS, "Label check successful. Expected label: '" + expectedlabel + "'");
			} else {
				WbidBasepage.logger.log(Status.FAIL, "Label check failed. Expected label: '" + expectedlabel
						+ "', Actual label: '" + actualHeading + "'");
			}
			return result;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An unexpected error occurred during label check. Error: " + e.getMessage());
			return false;
		}
	}

	// To get the attribute value
	public String getValue(WebElement element) {
		try {
			objwait.waitForElementTobeVisible(driver, element, 100);
			return element.getAttribute("value");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.info("Element not visible or stale or Unable to get attribute value");
			System.out.println("Element not visible or stale or Unable to get attribute value");
			WbidBasepage.logger.log(Status.FAIL, "Element not visible or stale");
			return ""; // or any default value or indication of failure
		}
	}

	//////// To get CSS value////////////////
	public String getCssvalue(WebElement locator, String Cssname) {
		// return locator.getCssValue(Cssname);
		try {
			objwait.waitForElementTobeVisible(driver, locator, 20);
			String cssValue = locator.getCssValue(Cssname);
			WbidBasepage.logger.log(Status.PASS,
					"Successfully retrieved CSS value for property '" + Cssname + "': " + cssValue);
			return cssValue;
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Unable to retrieve CSS value for property '" + Cssname + "'");
			return ""; // or any default value or indication of failure
		}
	}

	// To get the attribute details
	public String getAttribute(WebElement element, String Attribute) {
		try {
			objwait.waitForElementTobeVisible(driver, element, 20);
			String attributeValue = element.getAttribute(Attribute);
			WbidBasepage.logger.log(Status.PASS,
					"Successfully retrieved attribute '" + Attribute + "' value: " + attributeValue);
			return attributeValue;
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Unable to retrieve attribute '" + Attribute + "' value");
			return ""; // or any default value or indication of failure
		}
	}

//////////////////// Key Actions///////////////////////
	public void keyEnter(WebElement value) {
		try {
			value.sendKeys(Keys.ENTER);
			WbidBasepage.logger.log(Status.PASS, "Pressed ENTER key on the element");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Failed to press ENTER key on the element");
		}
	}

	public void keyArrowDown(WebElement value) {
		try {
			value.sendKeys(Keys.ARROW_DOWN);
			WbidBasepage.logger.log(Status.PASS, "Pressed ARROW_DOWN key on the element");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Failed to press ARROW_DOWN key on the element");
		}
	}

	public void keyBackspace(WebElement value) {
		try {
			value.sendKeys(Keys.BACK_SPACE);
			WbidBasepage.logger.log(Status.PASS, "Pressed BACK_SPACE key on the element");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Failed to press BACK_SPACE key on the element");
		}
	}

	public void keydelete(WebElement value) {
		try {
			value.sendKeys(Keys.CONTROL, "a");
			value.sendKeys(Keys.DELETE);
			WbidBasepage.logger.log(Status.PASS, "Pressed DELETE key on the element");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Failed to press DELETE key on the element");
		}
	}

	public void keytab(WebElement value) {
		try {
			value.sendKeys(Keys.TAB);
			WbidBasepage.logger.log(Status.PASS, "Pressed TAB key on the element");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Failed to press TAB key on the element");
		}
	}

	public void keycopy(WebElement value) {
		try {
			value.sendKeys(Keys.CONTROL, "a");
			value.sendKeys(Keys.CONTROL, "c");
			WbidBasepage.logger.log(Status.PASS, "Copy the element");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Failed to copy the element");
		}
	}

	public void keypaste(WebElement value) {
		try {
			value.sendKeys(Keys.CONTROL, "v");
			WbidBasepage.logger.log(Status.PASS, "Paste the value of element");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Failed to paste the value of element");
		}
	}

////////////////// Alert manager/////////////////////

	public boolean isAlertPresents() {
		try {
			driver.switchTo().alert();
			WbidBasepage.logger.log(Status.PASS, "Alert is present");
			return true;
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Exception while checking for alert presence");
			return false;
		}
	}

	public void alertaccept() throws InterruptedException {
		try {
			objalert = driver.switchTo().alert();
			objalert.accept();
			WbidBasepage.logger.log(Status.PASS, "Alert accepted");
		} catch (NoAlertPresentException e) {
			WbidBasepage.logger.log(Status.INFO, "No alert present to accept");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Exception while accepting alert");
		}
	}

	public void alertdismiss() throws InterruptedException {
		try {
			objalert = driver.switchTo().alert();
			objalert.dismiss();
			WbidBasepage.logger.log(Status.PASS, "Alert dismissed");
		} catch (NoAlertPresentException e) {
			WbidBasepage.logger.log(Status.INFO, "No alert present to dismiss");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Exception while dismissing alert");
		} finally {
			driver.switchTo().defaultContent();
		}
	}

	public String alertgettext() throws InterruptedException {
		try {
			if (isAlertPresents()) {
				objalert = driver.switchTo().alert();
				String message = objalert.getText();
				System.out.println(message);
				WbidBasepage.logger.log(Status.INFO, "Alert text retrieved: " + message);
				return message;
			} else {
				WbidBasepage.logger.info("No alert present to retrieve text");
				return null;
			}
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Exception while retrieving alert text");
			return null;
		}
	}

	public void alertsendkeys(String value) throws InterruptedException {
		try {
			objalert = driver.switchTo().alert();
			objalert.sendKeys(value);
			WbidBasepage.logger.log(Status.PASS, "Sent keys to alert: " + value);
		} catch (NoAlertPresentException e) {
			WbidBasepage.logger.log(Status.INFO, "No alert present to send keys");
		} catch (Exception e) {
			WbidBasepage.logger.fail(e);
			WbidBasepage.logger.log(Status.FAIL, "Exception while sending keys to alert");
		}
	}
///////////////// Drop down manager///////////////////

	public void dropvalue(WebElement drop, String dvalue) {
		try {
			Select objdrop = new Select(drop);
			objdrop.selectByValue(dvalue);
			WbidBasepage.logger.log(Status.PASS, "Selected value '" + dvalue + "' from dropdown");
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found" + e.getMessage());
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
		}
	}

	public void droptext(WebElement drop, String text) {
		try {
			Select objdrop = new Select(drop);
			objdrop.selectByVisibleText(text);
			WbidBasepage.logger.log(Status.PASS, "Selected text '" + text + "' from dropdown");
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found" + e.getMessage());
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
		}
	}

	public void dropindex(WebElement drop, int index) {
		try {
			Select objdrop = new Select(drop);
			objdrop.selectByIndex(index);
			WbidBasepage.logger.log(Status.PASS, "Selected option at index '" + index + "' from dropdown");
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found" + e.getMessage());
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
		}
	}

	// To select the drop down options
	public void select(WebElement drop, String optionLocator) {
		try {
			Select select = new Select(drop);

			if (optionLocator.startsWith("index=")) {
				int index = Integer.parseInt(optionLocator.substring("index=".length())) - 1;
				select.selectByIndex(index); // selectByIndex is zero-based, input is 1-based
				WbidBasepage.logger.log(Status.PASS, "Selected option at index '" + index + "' from dropdown");
			} else if (optionLocator.startsWith("value=")) {
				select.selectByValue(optionLocator.substring("value=".length()));
				WbidBasepage.logger.log(Status.PASS, "Selected option with value '"
						+ optionLocator.substring("value=".length()) + "' from dropdown");
			} else if (optionLocator.startsWith("label=")) {
				select.selectByVisibleText(optionLocator.substring("label=".length()));
				WbidBasepage.logger.log(Status.PASS, "Selected option with label '"
						+ optionLocator.substring("label=".length()) + "' from dropdown");
			} else {
				select.selectByVisibleText(optionLocator);
				WbidBasepage.logger.log(Status.PASS, "Selected option with text '" + optionLocator + "' from dropdown");
			}
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found" + e.getMessage());
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
		}
	}
//////////////////Checks whether the provided WebElement is selected or
////////////////// not.//////////////////////////////

	public boolean isSelected(WebElement element) {
		try {
			boolean isSelected = element.isSelected();
			WbidBasepage.logger.info("Element selection status checked successfully.");
			return isSelected;
		} catch (Exception e) {
			WbidBasepage.logger.fail("Failed to check element selection status: " + e.getMessage());
			return false;
		}
	}

	////////////// Total no.of web elements by same
	////////////// locator.////////////////////////////
	public int TotalElementsCount(List<WebElement> elements) {
		try {
			int count = elements.size();
			WbidBasepage.logger.info("Total elements count: " + count);
			return count;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to get total elements count: " + e.getMessage());
			return 0;
		}
	}

	// To check selected drop down value.
	public String DropselectedValue(WebElement element) {
		try {
			Select selectElement = new Select(element);
			WebElement selectedOption = selectElement.getFirstSelectedOption();
			String selectedValue = selectedOption.getText();
			WbidBasepage.logger.log(Status.PASS, "Selected value from dropdown: '" + selectedValue + "'");
			return selectedValue;
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found");
			return null;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
			return null;
		}
	}

//////////////Verify dropdown options list and total no.of list
//////////////Items/////////////////////
	public boolean VerifyDropdownOptions(WebElement locator, String[] Listlabels) {
		try {
			int totalitems = 0, totalwebelmts = 0;
			Select select = new Select(locator);
			WbidBasepage.logger.log(Status.INFO, "Dropdown options verification started");
			List<WebElement> options = select.getOptions();

			for (WebElement we : options) {
				totalwebelmts++;
				for (int i = 0; i < Listlabels.length; i++) {
					if (we.getText().trim().equals(Listlabels[i])) {
						totalitems++;
						WbidBasepage.logger.log(Status.INFO, "Option '" + Listlabels[i] + "' found");
						break;
					}
				}
			}
			WbidBasepage.logger.log(Status.INFO, "Total List Items: " + Listlabels.length);
			WbidBasepage.logger.log(Status.INFO, "Total Web Elements: " + totalwebelmts);
			WbidBasepage.logger.log(Status.INFO, "Total Match Elements: " + totalitems);

			if (totalitems == Listlabels.length && totalwebelmts == Listlabels.length) {
				WbidBasepage.logger.log(Status.PASS,
						"All dropdown options and total number of list items verified successfully");
				return true;
			} else {
				WbidBasepage.logger.log(Status.FAIL,
						"Dropdown options or total number of list items verification failed");
				return false;
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred during dropdown options verification: " + e.getMessage());
			return false;
		}
	}
/////////////// displayed,enable,select/////////////////

	public boolean fordisplay(WebElement ele) {
		boolean flag = false;
		try {
			flag = ele.isDisplayed();
			if (flag) {
				// Assuming MTSSBasepage.logger is an instance of ExtentReports logger
			} else {
				System.out.println("The element is not Displayed");
				WbidBasepage.logger.log(Status.FAIL, "The element is not Displayed");
			}
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.info("Element not found");
		}
		return flag;
	}

	public void forenable(WebElement value) {
		try {
			if (value.isEnabled()) {
				WbidBasepage.logger.log(Status.PASS, "The element is enabled");
			} else {
				System.out.println("The element is not enabled");
				WbidBasepage.logger.log(Status.FAIL, "The element is not enabled");
			}
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found" + e.getMessage());
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
		}
	}

	public boolean fordisabled(WebElement element) {
		try {
			return !element.isEnabled();
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found: " + e.getMessage());
			return false;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
			return false;
		}
	}

	public void forselect(WebElement value) {
		try {
			if (value.isSelected()) {
				WbidBasepage.logger.log(Status.PASS, "The element is selected");
			} else {
				System.out.println("The element is not selected");
				WbidBasepage.logger.log(Status.FAIL, "The element is not selected");
			}
		} catch (NoSuchElementException e) {
			WbidBasepage.logger.log(Status.FAIL, "Element not found" + e.getMessage());
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An unexpected error occurred: " + e.getMessage());
		}
	}

///////////////////Mouse actions//////////////////////

	public void mouseHover(WebElement element) {
		try {
			objaction = new Actions(driver);
			objaction.moveToElement(element).perform();
			WbidBasepage.logger.log(Status.PASS, "Mouse hovered over the element successfully");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to perform mouse hover. Error: " + e.getMessage());
		}
	}

	public void mouseRightclick(WebElement element) {
		try {
			objaction = new Actions(driver);
			objaction.contextClick(element).perform();
			WbidBasepage.logger.log(Status.PASS, "Performed right-click successfully");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to perform right-click. Error: " + e.getMessage());
		}
	}

	public void mouseDoubleclick(WebElement element) {
		try {
			objaction = new Actions(driver);
			objaction.doubleClick(element).perform();
			WbidBasepage.logger.log(Status.PASS, "Performed double-click successfully");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to perform double-click. Error: " + e.getMessage());
		}
	}
/////////////////Checking the element///////////////

	// To check the checkbox is not selected
	public void selectCheckbox(WebElement element) {
		try {
			if (!element.isSelected()) {
				element.click();
				WbidBasepage.logger.log(Status.PASS, "Checkbox selected successfully");
			} else {
				WbidBasepage.logger.log(Status.PASS, "Checkbox is already selected");
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to select checkbox. Error: " + e.getMessage());
		}
	}

	// To check the checkbox is uncheck
	public void uncheck(WebElement element) {
		try {
			if (element.isSelected()) {
				element.click();
				WbidBasepage.logger.log(Status.PASS, "Checkbox unchecked successfully");
			} else {
				WbidBasepage.logger.log(Status.PASS, "Checkbox is already unchecked");
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to uncheck checkbox. Error: " + e.getMessage());
		}
	}

	// To check the radiobutton is not selected
	public void selectRadiobutton(WebElement element) {
		try {
			if (!element.isEnabled()) {
				element.click();
				WbidBasepage.logger.log(Status.PASS, "Radio button selected successfully");
			} else {
				WbidBasepage.logger.log(Status.PASS, "Radio button is already selected");
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to select radio button. Error: " + e.getMessage());
		}
	}

	// To check the radiobutton is disable
	public void disableRadiobutton(WebElement element) {
		try {
			if (element.isEnabled()) {
				element.click();
				WbidBasepage.logger.log(Status.PASS, "Radio button disabled successfully");
			} else {
				WbidBasepage.logger.log(Status.PASS, "Radio button is already disabled");
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to disable radio button. Error: " + e.getMessage());
		}
	}

	// To Button is displayed
	public void button(WebElement element) {
		try {
			if (element.isDisplayed()) {
				element.click();
				WbidBasepage.logger.log(Status.PASS, "Button clicked successfully");
			} else {
				WbidBasepage.logger.log(Status.FAIL, "Button is not displayed");
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to click the button. Error: " + e.getMessage());
		}
	}

//////////////Window Handling//////////////////
/////////////////// To switch to the window using index///////////////////////////////
	public void switchToWindow_index(int windowNumber) {
		try {
			Set<String> s = driver.getWindowHandles();
			Iterator<String> ite = s.iterator();
			int i = 1;
			while (ite.hasNext() && i < 10) {
				String popupHandle = ite.next().toString();
				driver.switchTo().window(popupHandle);
				WbidBasepage.logger.log(Status.INFO, "Switched to window with title: " + driver.getTitle());
				if (i == windowNumber)
					break;
				i++;
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred while switching to window by index: " + e.getMessage());
		}
	}

////////////To switch to the window using index/////////////////
	public void switchToWindowByIndex(int windowIndex) {
		try {
			Set<String> windowHandles = driver.getWindowHandles();
			int currentIndex = 0;

			for (String handle : windowHandles) {
				if (currentIndex == windowIndex) {
					driver.switchTo().window(handle);
					WbidBasepage.logger.info("Switched to window with title: " + driver.getTitle());
					return; // Exit loop once desired window is found and switched to
				}
				currentIndex++;
			}

// If loop completes without switching, window with given index was not found
			WbidBasepage.logger.info("Window with index " + windowIndex + " not found.");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to switch to window by index: " + e.getMessage());
		}
	}

////////////To switch to the window using name/////////////////
	public void switchToWindowByName(String windowName) {
		try {
			String currentWindowHandle = driver.getWindowHandle();
			Set<String> windowHandles = driver.getWindowHandles();

			for (String handle : windowHandles) {
				driver.switchTo().window(handle);
				if (driver.getTitle().equals(windowName)) {
					WbidBasepage.logger.info("Switched to window with title: " + driver.getTitle());
					return;
				}
			}

// If loop completes without switching, window with given name was not found
			driver.switchTo().window(currentWindowHandle);
			WbidBasepage.logger.info("Window with name '" + windowName + "' not found.");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to switch to window by name: " + e.getMessage());
		}
	}

//To check whether extra tabs present.
	public void CheckExtraTabs() {
		try {
			String originalHandle = driver.getWindowHandle();
// Do something to open new tabs
			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(originalHandle)) {
					WbidBasepage.logger.log(Status.INFO, "Switching to extra tab");
					driver.switchTo().window(handle);
					WbidBasepage.logger.log(Status.INFO, "Closing extra tab");
					driver.close();
				}
			}
			driver.switchTo().window(originalHandle);
			WbidBasepage.logger.log(Status.INFO, "Switched back to original tab");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while checking extra tabs: " + e.getMessage());
		}
	}

///////////////Iframe/////////////////////
	public void SwitchFrame(WebElement frameelement) {
		try {
			driver.switchTo().frame(frameelement);
			WbidBasepage.logger.log(Status.INFO, "Switched to frame: " + frameelement);
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while switching to frame: " + e.getMessage());
		}
	}

	public void switchBackFromFrame() {
		try {
			driver.switchTo().defaultContent();
			WbidBasepage.logger.log(Status.INFO, "Switched back from the frame");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred while switching back from the frame: " + e.getMessage());
		}
	}

////////////To Scroll Window /////////////
	/* Scroll by Element */
	public void scrollToElement(WebElement element) {
		try {
			objwait.waitForElementTobeVisible(driver, element, 100);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while scrolling to element: " + e.getMessage());
		}
	}

	/* Scroll by pixel */
	public void scrollpixel(int a, int b, WebDriver driver) {
		try {
			((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(" + a + "," + b + ")");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while scrolling by pixel: " + e.getMessage());
		}
	}

	/* Scroll down */
	public void scrolldown(WebDriver driver) {
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollBy(0,2000)", "");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while scrolling down: " + e.getMessage());
		}
	}

	/* Scroll to top */
	public void scrollTop(WebDriver driver) {
		try {
			((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while scrolling to top: " + e.getMessage());
		}
	}

	// To find the element in the DOM Structure
	public WebElement find(WebElement locator) {
		By by = parseLocator(locator);
		List<WebElement> elements = driver.findElements(by);

		if (elements.size() == 0) {
			throw new AssertionError("Could not find any element matching '" + locator + "'");
			// return null;
		} else if (elements.size() > 1) {
			System.err.println("Found " + elements.size() + " elements matching '" + locator + "'");
		}
		return elements.get(0);
	}

	// check whether element exist or not in the DOM
	public boolean exists(WebElement element) {
		try {
			By by = parseLocator(element);
			boolean exists = driver.findElements(by).size() > 0;
			if (!exists) {
				WbidBasepage.logger.log(Status.FAIL, "Element does not exist in the DOM");
			}
			return exists;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred while checking element existence: " + e.getMessage());
			return false;
		}
	}

	// check element present
	public boolean isElementPresent(WebElement locator) {
		try {
			boolean present = exists(locator);
			if (!present) {
				WbidBasepage.logger.log(Status.FAIL, "Element is not present");
			}
			return present;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred while checking element presence: " + e.getMessage());
			return false;
		}
	}

///////////////Random Numbers and alphabets////////////////

	// To generate random data.
	public String generateRandomData() {
		try {
			final String alphabet = "0123456789111213141516171819abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			final int N = alphabet.length();
			char[] data = new char[10];
			Random r = new Random();
			for (int i = 0; i < 10; i++) {
				data[i] = alphabet.charAt(r.nextInt(N));
			}
			String RandomData = String.valueOf(data);
			WbidBasepage.logger.log(Status.INFO, "Generated random data: " + RandomData);
			return "QA" + RandomData;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while generating random data: " + e.getMessage());
			return null;
		}
	}

	// To generate random number
	public String generateRandomNumber() {
		try {
			final String number = "0123456789";
			final int N = number.length();
			char[] data = new char[10];
			Random r = new Random();
			for (int i = 0; i < 7; i++) {
				data[i] = number.charAt(r.nextInt(N));
			}
			String RandomData = String.valueOf(data);
			WbidBasepage.logger.log(Status.INFO, "Generated random number: " + RandomData);
			return RandomData;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while generating random number: " + e.getMessage());
			return null;
		}
	}

	// To generate random numbers within 100
	public static String getRandomNumber() {
		try {
			Random random = new Random();
			int number = random.nextInt(100);
			return String.valueOf(number);
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while generating random number: " + e.getMessage());
			return null;
		}
	}
////////////////////Parsing locator//////////////////////////////

	// Parsing the element from the parameter
	public By parseLocator(WebElement element) {
		By by;
		String locator = element.toString();
		if (locator.startsWith("xpath=")) {
			String xpath = locator.substring("xpath=".length());
			by = By.xpath(xpath);
		} else if (locator.startsWith("//")) { // only implicit xpath supported
			by = By.xpath(locator);
		} else if (locator.startsWith("#") && locator.indexOf(" ") < 0 && locator.indexOf(".") < 0
				&& locator.indexOf(",") < 0) {
			by = By.id(locator.substring(1));
		} else {
			by = By.cssSelector(locator);
		}
		return by;
	}

	// To support any type of date format
	public String getFormattedDate(String dateFormat) {
		try {
			// Get today's date
			LocalDate today = LocalDate.now();
			// Define date format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
			// Format date
			String formattedDate = today.format(formatter);
			// Return formatted date
			return formattedDate;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred while formatting the date: " + e.getMessage());
			return null;
		}
	}

///////////////////File ///////////////////////


	// To delete the file in the folder
	public void FileDelete() {

		File dir = new File(WbidBasepage.downloadPath);
		if (!dir.exists()) {

			File theFile = new File(WbidBasepage.downloadPath);
			theFile.mkdirs();
		}
		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {

			if (files == null || files.length == 0) {
				break;
			} else {
				files[i].delete();
			}
		}

	}

	// To verify the file extension
	protected boolean isFileDownloaded_Ext(String ext) {
		boolean flag = false;
		File dir = new File(WbidBasepage.downloadPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			flag = false;
		}

		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(ext)) {
				flag = true;
			}
		}
		return flag;
	}

	// To wait download file
	public void waitFor_downloadfile() {
		try {
			int i = 3000;
			int j = 0;
			File dir = new File(WbidBasepage.downloadPath);
			File[] files;
			do {
				Thread.sleep(i);
				files = dir.listFiles();

				j = j + 3000;
				System.out.println(j);
				if (j > 200000) {
					System.out.println("The maximum Time limit has Reached to 200000");
					break;
				}

			} while (files == null || files.length == 0);
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// To get downloaded file name
	public String getLatestDownloadedFileName() {
		try {
			File folder = new File(WbidBasepage.downloadPath);
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles != null || listOfFiles.length == 0) {
				for (File file : listOfFiles) {
					if (file.isFile()) {
						return file.getName();
					}
				}
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred while getting the latest downloaded file name: " + e.getMessage());
		}
		return null; // If no file is found or error occurs
	}

/////////////////// CSV File count actions///////////////////
// returns the total count of rows in a csv
	public int csv_row_count() throws Exception {
		try {
			File folder = new File(WbidBasepage.downloadPath);
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.getName().contains(".csv")) {
					BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry));
					String input;
					int count = 0;
					while ((input = bufferedReader.readLine()) != null) {
						count++;
					}
					return count;
				}
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred while counting rows in the CSV file: " + e.getMessage());
		}
		return 0;
	}


////////////////////////Move to element/////////////////////////
	public void click_svg_element(WebElement element) {
		try {
			objaction = new Actions(driver);
			objaction.moveToElement(element).click().build().perform();
			WbidBasepage.logger.log(Status.PASS, "Performed click successfully");
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "Failed to perform click Error: " + e.getMessage());
		}
	}
}
