package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.Status;

public class commonFunctionalities {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	@FindBy(xpath = "//*[@data-testid='NavigateNextIcon']//parent::button")
	public WebElement nextButton;

	public commonFunctionalities(WebDriver driver) {
		// TODO Auto-generated constructor stub
		this.driver = driver;
		PageFactory.initElements(driver, this);/// initial page factory
		objaction = new ActionUtilities(driver);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////
	public <T extends Comparable<? super T>> List<T> retrieveData(List<WebElement> elementsPath) {
		List<T> items = new ArrayList<>();
		try {
			for (WebElement element : elementsPath) {
				String text = element.getText();
				if (!text.equals("-") && !text.isEmpty()) {
					items.add((T) text);
				}
			}

			if (objaction.fordisplay(nextButton)) {
				String nextButtonAttri = nextButton.getAttribute("class");
				while (!nextButtonAttri.contains("disabled")) {
					objaction.click(nextButton);
					for (WebElement element2 : elementsPath) {
						String text1 = element2.getText();
						if (!text1.equals("-") && !text1.isEmpty()) {
							items.add((T) text1);
						}
					}
					nextButtonAttri = nextButton.getAttribute("class");
				}
			}
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred during data retrieval: " + e.getMessage());
		}
		return items;
	}

	public <T extends Comparable<? super T>> List<T> sortData(List<T> items, boolean ascending) {
		try {
			if (ascending) {
				items = items.stream()
						.sorted((s1, s2) -> String.CASE_INSENSITIVE_ORDER.compare(s1.toString(), s2.toString()))
						.collect(Collectors.toList()); // Ascending sorting
			} else {
				items = items.stream()
						.sorted((s1, s2) -> String.CASE_INSENSITIVE_ORDER.compare(s2.toString(), s1.toString()))
						.collect(Collectors.toList()); // Descending sorting
			}
			System.out.println(items); // Printing the sorted list
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL, "An error occurred during sorting: " + e.getMessage());
		}
		return items; // Returning the sorted list
	}

	public <T extends Comparable<? super T>> Boolean genericSort(List<WebElement> elementsPath, boolean ascending) {
		try {
			WbidBasepage.logger.info("Performing generic sorting operation:");

			// Retrieve data
			List<T> items = retrieveData(elementsPath);

			WbidBasepage.logger.info("Original list: " + items);
			System.out.println("List=" + items);

			// Sorting the data
			List<T> sortedList = sortData(new ArrayList<>(items), ascending);

			WbidBasepage.logger.info((ascending ? "Ascending" : "Descending") + " sorted list: " + sortedList);
			System.out.println((ascending ? "Ascending" : "Descending") + " sorted list=" + sortedList);

			// Checking if the list is sorted
			boolean sortingStatus = items.equals(sortedList);
			WbidBasepage.logger.info("Sorting status: " + (sortingStatus ? "PASS" : "FAIL"));

			return sortingStatus;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred during generic sorting operation: " + e.getMessage());
			return false;
		}
	}

	public Boolean statusSort(List<WebElement> elementsPath, boolean ascending) {
		try {
			WbidBasepage.logger.info("Performing status [Ascending:" + ascending + "] sorting operation:");

			// List to store the elements
			List<String> originalItems = new ArrayList<>();

			// Adding elements to the list
			for (WebElement element : elementsPath) {
				String attribute = element.getAttribute("aria-label");
				if (attribute != null && !attribute.isEmpty()) {
					originalItems.add(attribute);
				}
			}

			// Checking if pagination is available
			if (objaction.fordisplay(nextButton)) {
				String nextButtonAttri = nextButton.getAttribute("class");
				while (!nextButtonAttri.contains("disabled")) {
					objaction.click(nextButton);
					for (WebElement element2 : elementsPath) {
						String attribute = element2.getAttribute("aria-label"); // Assuming "status" is the attribute
						// name
						if (attribute != null && !attribute.isEmpty()) {
							originalItems.add(attribute);
						}
					}
					nextButtonAttri = nextButton.getAttribute("class");
				}
			}
			// Creating a copy of originalItems to sort
			List<String> sortedItems = new ArrayList<>(originalItems);
			try {
				if (ascending) {
					// Sorting the list based on attribute value
					sortedItems.sort(Comparator.naturalOrder());

				} else {
					sortedItems.sort(Comparator.reverseOrder()); // Descending sorting
				}
				System.out.println(sortedItems); // Printing the sorted list
			} catch (Exception e) {
				WbidBasepage.logger.log(Status.FAIL, "An error occurred during sorting: " + e.getMessage());
			}
			WbidBasepage.logger.info("Original list: " + originalItems);
			System.out.println("Original list=" + originalItems);

			WbidBasepage.logger.info("Sorted list: " + sortedItems);
			System.out.println("Sorted list=" + sortedItems);

			// Checking if the original list and sorted list are equal
			boolean sortingStatus = originalItems.equals(sortedItems);
			WbidBasepage.logger.info("Sorting status: " + (sortingStatus ? "PASS" : "FAIL"));

			return sortingStatus;
		} catch (Exception e) {
			WbidBasepage.logger.log(Status.FAIL,
					"An error occurred during generic sorting operation: " + e.getMessage());
			return false;
		}
	}

}
