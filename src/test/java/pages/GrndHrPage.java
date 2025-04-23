package pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import API.GroundTest;

import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class GrndHrPage {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public GrndHrPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
	}

	@FindBy(xpath = "(//td[contains(@class,'left-side-radius')])[1]")
	public WebElement firstTrip;

	@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[4]/div/pre")
	public WebElement tableHead;

	public boolean grndHeadVisible() {
		objwait.waitForElementTobeVisible(driver, firstTrip, 10);
		objaction.click(firstTrip);
		objwait.waitForElementTobeVisible(driver, tableHead, 10);
		String txt = objaction.gettext(tableHead);
		WbidBasepage.logger.pass("Head  :" + txt);
		boolean isVisible = txt.contains("Grnd");
		WbidBasepage.logger.pass("Grnd Head Visible :" + isVisible);
		return isVisible;
	}

	@FindBy(xpath = "//div[contains(@class,'cala-view')]")
	public List<WebElement> tripLines; // Each 'div' contains trip details

	@FindBy(xpath = "//td[contains(@class,'left-side-radius')]")
	public List<WebElement> tripList;// Trip detail visible when we click tripList

	@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[1]/div/pre")
	public WebElement tripSequence;// Trip code Extracted from Trip Sequence

	@FindBy(xpath = "(//pre[@style='text-decoration: inherit;' and normalize-space(.)!=''])[position() > 2]")
	public List<WebElement> tripdata;// gets data starting from trip details skip 1st 2 lines which show trip code
										// and table header

	public String getTripXPath(int i) {
		return String.format(
				"(//div[contains(@class,'cala-view')])[%d]/div/div[2]/table/tr/td[contains(@class,'left-side-radius')]",
				i);
	}

	/*
	 * Method without grnd time formating with API format ie direct text value
	 * 045---not like 00:45 public boolean getGrndHour() { Map<String, Map<String,
	 * List<String>>> tripGrndMap = new LinkedHashMap<>(); boolean found = false;
	 * int i = 0;
	 * 
	 * for (WebElement tripElement : tripList) { try { if (i >= 5) break; // Limit
	 * to 5 iterations i++;
	 * 
	 * objwait.waitForElementTobeVisible(driver, tripElement, 90);
	 * ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
	 * tripElement); objwait.waitForElemntTobeClickable(driver, tripElement, 30);
	 * 
	 * try { tripElement.click(); } catch (Exception e) { ((JavascriptExecutor)
	 * driver).executeScript("arguments[0].click();", tripElement); }
	 * 
	 * objwait.waitForElementTobeVisible(driver, tripSequence, 90); String
	 * tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+",
	 * " ");
	 * 
	 * // Extract trip code Pattern pattern =
	 * Pattern.compile("Trip\\s(\\w+)\\sDated"); Matcher matcher =
	 * pattern.matcher(tripSequenceText);
	 * 
	 * if (matcher.find()) { String tripCode = matcher.group(1).trim();
	 * 
	 * for (WebElement tripEle : tripdata) { String tripDataText =
	 * objaction.gettext(tripEle).trim();
	 * 
	 * if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
	 * continue; }
	 * 
	 * // Extract trip date Pattern datePattern =
	 * Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b"); Matcher dateMatcher =
	 * datePattern.matcher(tripDataText);
	 * 
	 * if (dateMatcher.find()) { String tripDate = dateMatcher.group(1).trim();
	 * 
	 * // Extract Eqp (alphanumeric or numeric) Pattern eqpPattern =
	 * Pattern.compile("\\b(\\w{2,4})\\b"); Matcher eqpMatcher =
	 * eqpPattern.matcher(tripDataText); String eqp = eqpMatcher.find() ?
	 * eqpMatcher.group(1) : "";
	 * 
	 * // Extract all numbers excluding Eqp Pattern numberPattern =
	 * Pattern.compile("\\b(\\d{1,4})\\b"); Matcher numberMatcher =
	 * numberPattern.matcher(tripDataText);
	 * 
	 * List<String> numbers = new ArrayList<>(); while (numberMatcher.find()) {
	 * String num = numberMatcher.group(1); if (!num.equals(eqp)) { // Exclude Eqp
	 * if numeric numbers.add(num); } }
	 * 
	 * // Debug log // WbidBasepage.logger.info("Trip Code: " + tripCode +
	 * " | Trip Date: " + tripDate + " | Eqp: " + eqp + " | Numbers: " + numbers);
	 * 
	 * // Get Grnd value correctly String grndValue = null; if (numbers.size() >= 7)
	 * { grndValue = numbers.get(6); // 7th numeric value
	 * WbidBasepage.logger.info("Trip Code: " + tripCode + " | Trip Date: " +
	 * tripDate + " | Grnd: " + grndValue);
	 * 
	 * found = true; } else if (numbers.size() >= 2) { grndValue =
	 * numbers.get(numbers.size() - 2); // Second last value
	 * WbidBasepage.logger.info("Trip Code: " + tripCode + " | Trip Date: " +
	 * tripDate + " | Grnd (fallback): " + grndValue); found = true; }
	 * tripGrndMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
	 * .computeIfAbsent(tripDate, k -> new ArrayList<>()).add(grndValue);
	 * 
	 * } } }
	 * 
	 * } catch (Exception e) { System.out.println("Error extracting Grnd time: " +
	 * e.getMessage()); } }
	 * WbidBasepage.logger.info("UI Grnd  Hours: "+tripGrndMap); return found; }
	 */

	public boolean getGrndHour() {
		Map<String, Map<String, List<String>>> tripGrndMap = new LinkedHashMap<>();
		boolean found = false;
		int i = 0;

		for (WebElement tripElement : tripList) {
			try {
				if (i >= 5)
					break; // Limit to 5 iterations
				i++;

				objwait.waitForElementTobeVisible(driver, tripElement, 90);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						tripElement);
				objwait.waitForElemntTobeClickable(driver, tripElement, 30);

				try {
					tripElement.click();
				} catch (Exception e) {
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
				}

				objwait.waitForElementTobeVisible(driver, tripSequence, 90);
				String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");

				// Extract trip code
				Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
				Matcher matcher = pattern.matcher(tripSequenceText);

				if (matcher.find()) {
					String tripCode = matcher.group(1).trim();

					for (WebElement tripEle : tripdata) {
						String tripDataText = objaction.gettext(tripEle).trim();

						if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
							continue;
						}

						// Extract trip date
						Pattern datePattern = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
						Matcher dateMatcher = datePattern.matcher(tripDataText);

						if (dateMatcher.find()) {
							String tripDate = dateMatcher.group(1).trim();

							// Extract Eqp (alphanumeric or numeric)
							Pattern eqpPattern = Pattern.compile("\\b(\\w{2,4})\\b");
							Matcher eqpMatcher = eqpPattern.matcher(tripDataText);
							String eqp = eqpMatcher.find() ? eqpMatcher.group(1) : "";

							// Extract all numbers excluding Eqp
							Pattern numberPattern = Pattern.compile("\\b(\\d{1,4})\\b");
							Matcher numberMatcher = numberPattern.matcher(tripDataText);

							List<String> numbers = new ArrayList<>();
							while (numberMatcher.find()) {
								String num = numberMatcher.group(1);
								if (!num.equals(eqp)) { // Exclude Eqp if numeric
									numbers.add(num);
								}
							}

							String grndValue = null;
							if (numbers.size() >= 7) {
								grndValue = numbers.get(6); // 7th numeric value
								found = true;
							} else if (numbers.size() >= 2) {
								grndValue = numbers.get(numbers.size() - 2); // Second last value
								found = true;
							}

							if (grndValue != null) {
								String formattedGrndValue = convertToApiFormat(grndValue);

								WbidBasepage.logger.info("Trip Code: " + tripCode + " | Trip Date: " + tripDate
										+ " | Grnd (formatted): " + formattedGrndValue);

								tripGrndMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
										.computeIfAbsent(tripDate, k -> new ArrayList<>()).add(formattedGrndValue);
							}
						}
					}
				}

			} catch (Exception e) {
				System.out.println("Error extracting Grnd time: " + e.getMessage());
			}
		}

		WbidBasepage.logger.info("UI Grnd Hours (Formatted): " + tripGrndMap);
		return found;
	}

	// Utility to convert UI ground value (e.g., "125") into API format ("01:25")
	private String convertToApiFormat(String uiValue) {
		if (uiValue == null || uiValue.equals("0") || uiValue.equals("000")) {
			return "00:00";
		}
		int len = uiValue.length();
		int hours = Integer.parseInt(uiValue.substring(0, len - 2));
		int minutes = Integer.parseInt(uiValue.substring(len - 2));
		return String.format("%02d:%02d", hours, minutes);
	}

	/*
	 * 
	 * // Compare UI GRND and API GRND Hour values public boolean
	 * compareGrndData(Map<String, Map<String, List<String>>> uiGrndData,
	 * Map<String, Map<Integer, List<String>>> apiGrndData) { boolean isMatch =
	 * true;
	 * 
	 * for (String tripCode : uiGrndData.keySet()) { if
	 * (!apiGrndData.containsKey(tripCode)) {
	 * WbidBasepage.logger.fail("❌ Mismatch! UI has TripCode: " + tripCode +
	 * ", but API does not."); isMatch = false; continue; }
	 * 
	 * // Get and sort UI dates Map<String, List<String>> uiDateMap =
	 * uiGrndData.get(tripCode); List<String> sortedUiDates = new
	 * ArrayList<>(uiDateMap.keySet());
	 * sortedUiDates.sort(Comparator.comparing(this::convertDateToComparable));
	 * 
	 * // Flatten UI GRNDs in order List<String> uiGrndsSequential = new
	 * ArrayList<>(); for (String date : sortedUiDates) {
	 * uiGrndsSequential.addAll(uiDateMap.getOrDefault(date, new ArrayList<>())); }
	 * 
	 * // Get and sort API duty sequence numbers Map<Integer, List<String>>
	 * apiDutySeqMap = apiGrndData.get(tripCode); List<Integer> sortedApiDutySeq =
	 * new ArrayList<>(apiDutySeqMap.keySet()); Collections.sort(sortedApiDutySeq);
	 * 
	 * // Flatten API GRNDs in order List<String> apiGrndsSequential = new
	 * ArrayList<>(); for (Integer dutySeq : sortedApiDutySeq) {
	 * apiGrndsSequential.addAll(apiDutySeqMap.getOrDefault(dutySeq, new
	 * ArrayList<>())); }
	 * 
	 * WbidBasepage.logger.info("🔍 Comparing TripCode: " + tripCode);
	 * WbidBasepage.logger.info("UI GRNDs (sequential): " + uiGrndsSequential);
	 * WbidBasepage.logger.info("API GRNDs (sequential): " + apiGrndsSequential);
	 * 
	 * if (!uiGrndsSequential.equals(apiGrndsSequential)) {
	 * WbidBasepage.logger.fail("❌ Ground Hour mismatch for TripCode: " + tripCode);
	 * isMatch = false; } }
	 * 
	 * if (isMatch) {
	 * WbidBasepage.logger.info("✅ UI and API GRND values match perfectly!"); } else
	 * {
	 * WbidBasepage.logger.fail("❌ Differences found in UI and API GRND hour data!"
	 * ); }
	 * 
	 * return isMatch; }
	 * 
	 * //Converts "01May" to "May01" for consistent sorting private String
	 * convertDateToComparable(String date) { return date.substring(2) +
	 * date.substring(0, 2); }
	 * 
	 * 
	  // Compare UI GRND and API GRND Hour values
	public boolean compareGrndData(Map<String, Map<String, List<String>>> uiGrndData,
			Map<String, Map<Integer, List<String>>> apiGrndData) {
		boolean isMatch = true;

		for (String tripCode : uiGrndData.keySet()) {
			if (!apiGrndData.containsKey(tripCode)) {
				WbidBasepage.logger.fail("❌ Mismatch! UI has TripCode: " + tripCode + ", but API does not.");
				isMatch = false;
				continue;
			}

//  Get and sort UI dates (e.g., 01May, 02May, 31May, 01Jun)
			Map<String, List<String>> uiDateMap = uiGrndData.get(tripCode);
			List<String> sortedUiDates = new ArrayList<>(uiDateMap.keySet());
			sortedUiDates.sort(Comparator.comparing(t -> {
				try {
					return convertDateToDate(t);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			})); // Uses proper date comparison

// 🔹 Flatten UI GRNDs in order of sorted dates
			List<String> uiGrndsSequential = new ArrayList<>();
			for (String date : sortedUiDates) {
				uiGrndsSequential.addAll(uiDateMap.getOrDefault(date, new ArrayList<>()));
			}

// 🔹 Sort API duty sequence numbers (e.g., [1, 2, 3])
			Map<Integer, List<String>> apiDutySeqMap = apiGrndData.get(tripCode);
			List<Integer> sortedApiDutySeq = new ArrayList<>(apiDutySeqMap.keySet());
			Collections.sort(sortedApiDutySeq);

// 🔹 Flatten API GRNDs in order of sorted duty sequence numbers
			List<String> apiGrndsSequential = new ArrayList<>();
			for (Integer dutySeq : sortedApiDutySeq) {
				apiGrndsSequential.addAll(apiDutySeqMap.getOrDefault(dutySeq, new ArrayList<>()));
			}

// 🔍 Comparison log
			//WbidBasepage.logger.info("🔍 Comparing TripCode: " + tripCode);
			//WbidBasepage.logger.info("UI GRNDs (sequential): " + uiGrndsSequential);
			//WbidBasepage.logger.info("API GRNDs (sequential): " + apiGrndsSequential);

// 🔻 Compare flattened lists
			if (!uiGrndsSequential.equals(apiGrndsSequential)) {
				WbidBasepage.logger.fail("❌ Ground Hour mismatch for TripCode: " + tripCode+"UI GRNDs (sequential): " + uiGrndsSequential+"API GRNDs (sequential): " + apiGrndsSequential);
				isMatch = false;
			}
		}

// ✅ Final result
		if (isMatch) {
			WbidBasepage.logger.info("✅ UI and API GRND values match perfectly!");
		} else {
			WbidBasepage.logger.fail("❌ Differences found in UI and API GRND hour data!");
		}

		return isMatch;
	}

	private Date convertDateToDate(String dateStr) {
		try {
			// Assumes format like "01May", "31May", "01Jun" (ddMMM)
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMM", Locale.ENGLISH);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date(0); // fallback
		}
	}

	public boolean getGrndHrCompare() {
		boolean isComparisonSuccessful = true; // Assume success unless proven otherwise
		Map<String, Map<String, List<String>>> tripGrndMap = new LinkedHashMap<>();
		// int i = 0;

		for (WebElement tripElement : tripList) {
			try {
			//	 if (i >= 30) break; // Limit to 5 iterations
			//	i++;

				objwait.waitForElementTobeVisible(driver, tripElement, 90);
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						tripElement);
				objwait.waitForElemntTobeClickable(driver, tripElement, 30);

				try {
					tripElement.click();
				} catch (Exception e) {
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
				}

				objwait.waitForElementTobeVisible(driver, tripSequence, 90);
				String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");

				// Extract trip code
				Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
				Matcher matcher = pattern.matcher(tripSequenceText);

				if (matcher.find()) {
					String tripCode = matcher.group(1).trim();

					for (WebElement tripEle : tripdata) {
						String tripDataText = objaction.gettext(tripEle).trim();

						if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
							continue;
						}

						// Extract trip date
						Pattern datePattern = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
						Matcher dateMatcher = datePattern.matcher(tripDataText);

						if (dateMatcher.find()) {
							String tripDate = dateMatcher.group(1).trim();

							// Extract Eqp (alphanumeric or numeric)
							Pattern eqpPattern = Pattern.compile("\\b(\\w{2,4})\\b");
							Matcher eqpMatcher = eqpPattern.matcher(tripDataText);
							String eqp = eqpMatcher.find() ? eqpMatcher.group(1) : "";

							// Extract all numbers excluding Eqp
							Pattern numberPattern = Pattern.compile("\\b(\\d{1,4})\\b");
							Matcher numberMatcher = numberPattern.matcher(tripDataText);

							List<String> numbers = new ArrayList<>();
							while (numberMatcher.find()) {
								String num = numberMatcher.group(1);
								if (!num.equals(eqp)) { // Exclude Eqp if numeric
									numbers.add(num);
								}
							}

							String grndValue = null;
							if (numbers.size() >= 7) {
								grndValue = numbers.get(6); // 7th numeric value
							} else if (numbers.size() >= 2) {
								grndValue = numbers.get(numbers.size() - 2); // Second last value
							}

							if (grndValue != null) {
								String formattedGrndValue = convertToApiFormat(grndValue);

								// WbidBasepage.logger.info("Trip Code: " + tripCode + " | Trip Date: " +
								// tripDate + " | Grnd (formatted): " + formattedGrndValue);

								tripGrndMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
										.computeIfAbsent(tripDate, k -> new ArrayList<>()).add(formattedGrndValue);
							}
						}
					}
					boolean isCurrentComparisonSuccessful = compareGrndData(tripGrndMap, GroundTest.apiGrnd);
					if (!isCurrentComparisonSuccessful) {
						isComparisonSuccessful = false; // If any comparison fails, mark as unsuccessful

					}
				}

			} catch (Exception e) {
				WbidBasepage.logger.fail("Error extracting Grnd time: " + e.getMessage());
				isComparisonSuccessful = false; // Mark as unsuccessful if an error occurs
			}
		}
		//WbidBasepage.logger.info("UI Grnd Hours (Formatted): " + tripGrndMap);
		return isComparisonSuccessful;
	}*/
	public boolean getGrndHrCompare() {
	    boolean isComparisonSuccessful = true;
	    Map<String, Map<String, List<String>>> tripGrndMap = new LinkedHashMap<>();
	    Set<String> seenGrnds = new HashSet<>(); // To avoid duplicate entries

	    // int i = 0;
	    for (WebElement tripElement : tripList) {
	        try {
	        	// if (i >= 30) break; // Limit to 5 iterations
	        	//	i++;
	            objwait.waitForElementTobeVisible(driver, tripElement, 90);
	            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", tripElement);
	            objwait.waitForElemntTobeClickable(driver, tripElement, 30);

	            try {
	                tripElement.click();
	            } catch (Exception e) {
	                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", tripElement);
	            }

	            objwait.waitForElementTobeVisible(driver, tripSequence, 90);
	            String tripSequenceText = objaction.gettext(tripSequence).trim().replaceAll("\\s+", " ");

	            // Extract trip code
	            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
	            Matcher matcher = pattern.matcher(tripSequenceText);

	            if (matcher.find()) {
	                String tripCode = matcher.group(1).trim();

	                for (WebElement tripEle : tripdata) {
	                    String tripDataText = objaction.gettext(tripEle).trim();

	                    if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB")) {
	                        continue;
	                    }

	                    // Extract trip date
	                    Pattern datePattern = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
	                    Matcher dateMatcher = datePattern.matcher(tripDataText);

	                    if (dateMatcher.find()) {
	                        String tripDate = dateMatcher.group(1).trim();

	                        // Extract Eqp (alphanumeric or numeric)
	                        Pattern eqpPattern = Pattern.compile("\\b(\\w{1,4})\\b");
	                        Matcher eqpMatcher = eqpPattern.matcher(tripDataText);
	                        String eqp = eqpMatcher.find() ? eqpMatcher.group(1) : "";

	                        // Extract all numbers excluding Eqp
	                        Pattern numberPattern = Pattern.compile("\\b(\\d{1,4})\\b");
	                        Matcher numberMatcher = numberPattern.matcher(tripDataText);

	                        List<String> numbers = new ArrayList<>();
	                        while (numberMatcher.find()) {
	                            String num = numberMatcher.group(1);
	                            if (!num.equals(eqp)) {
	                                numbers.add(num);
	                            }
	                        }

	                        String grndValue = null;
	                        if (numbers.size() >= 7) {
	                            grndValue = numbers.get(6);
	                        } else if (numbers.size() >= 2) {
	                            grndValue = numbers.get(numbers.size() - 2);
	                        }

	                        if (grndValue != null) {
	                            String formattedGrndValue = convertToApiFormat(grndValue);
	                            String uniqueKey = tripCode + "_" + tripDate + "_" + formattedGrndValue;

	                            if (!seenGrnds.contains(uniqueKey)) {
	                                seenGrnds.add(uniqueKey);
	                                tripGrndMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
	                                           .computeIfAbsent(tripDate, k -> new ArrayList<>())
	                                           .add(formattedGrndValue);
	                            }
	                        }
	                    }
	                }

	                boolean isCurrentComparisonSuccessful = compareGrndData(tripGrndMap, GroundTest.apiGrnd);
	                if (!isCurrentComparisonSuccessful) {
	                    isComparisonSuccessful = false;
	                }
	            }

	        } catch (Exception e) {
	            WbidBasepage.logger.fail("Error extracting Grnd time: " + e.getMessage());
	            isComparisonSuccessful = false;
	        }
	    }

	    return isComparisonSuccessful;
	}

	public boolean compareGrndData(Map<String, Map<String, List<String>>> uiGrndData,
	                               Map<String, Map<Integer, List<String>>> apiGrndData) {
	    boolean isMatch = true;

	    for (String tripCode : uiGrndData.keySet()) {
	        if (!apiGrndData.containsKey(tripCode)) {
	            WbidBasepage.logger.fail("❌ Mismatch! UI has TripCode: " + tripCode + ", but API does not.");
	            isMatch = false;
	            continue;
	        }

	        Map<String, List<String>> uiDateMap = uiGrndData.get(tripCode);
	        List<String> sortedUiDates = new ArrayList<>(uiDateMap.keySet());
	        sortedUiDates.sort(Comparator.comparing(t -> {
	            try {
	                return convertDateToDate(t);
	            } catch (Exception e) {
	                e.printStackTrace();
	                return new Date(0);
	            }
	        }));

	        List<String> uiGrndsSequential = new ArrayList<>();
	        for (String date : sortedUiDates) {
	            uiGrndsSequential.addAll(uiDateMap.getOrDefault(date, new ArrayList<>()));
	        }

	        Map<Integer, List<String>> apiDutySeqMap = apiGrndData.get(tripCode);
	        List<Integer> sortedApiDutySeq = new ArrayList<>(apiDutySeqMap.keySet());
	        Collections.sort(sortedApiDutySeq);

	        List<String> apiGrndsSequential = new ArrayList<>();
	        for (Integer dutySeq : sortedApiDutySeq) {
	            apiGrndsSequential.addAll(apiDutySeqMap.getOrDefault(dutySeq, new ArrayList<>()));
	        }

	        if (!uiGrndsSequential.equals(apiGrndsSequential)) {
	            WbidBasepage.logger.fail("❌ Ground Hour mismatch for TripCode: " + tripCode +
	                " UI GRNDs (sequential): " + uiGrndsSequential +
	                " API GRNDs (sequential): " + apiGrndsSequential);
	            isMatch = false;
	        }
	    }

	    if (isMatch) {
	        WbidBasepage.logger.info("✅ UI and API GRND values match perfectly!");
	    } else {
	        WbidBasepage.logger.fail("❌ Differences found in UI and API GRND hour data!");
	    }

	    return isMatch;
	}

	private Date convertDateToDate(String dateStr) {
	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("ddMMM", Locale.ENGLISH);
	        return sdf.parse(dateStr);
	    } catch (ParseException e) {
	        e.printStackTrace();
	        return new Date(0);
	    }
	}

}
