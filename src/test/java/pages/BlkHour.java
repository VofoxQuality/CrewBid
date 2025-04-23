package pages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

import API.BlockTest;
import API.GroundTest;
import utilities.ActionUtilities;
import utilities.WaitCondition;
import utilities.WbidBasepage;

public class BlkHour {
	WebDriver driver;
	ActionUtilities objaction;
	WaitCondition objwait = new WaitCondition();
	BidDownloadPage objdownload = new BidDownloadPage(driver);

	public BlkHour(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);// initial page factory
		objaction = new ActionUtilities(driver);
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
	@FindBy(xpath = "(//td[contains(@class,'left-side-radius')])[1]")
	public WebElement firstTrip;
	
	@FindBy(xpath = "//*[@id='fullHeightModalRight']/div/div/div/div/div[4]/div/pre")
	public WebElement tableHead;
	
	public boolean blkHeadVisible() {
		objwait.waitForElementTobeVisible(driver, firstTrip, 10);
		objaction.click(firstTrip);
		objwait.waitForElementTobeVisible(driver, tableHead, 10);
		String txt = objaction.gettext(tableHead);
		WbidBasepage.logger.pass("Head  :" + txt);
		boolean isVisible = txt.contains("Blk");
		WbidBasepage.logger.pass("Blk Head Visible :" + isVisible);
		return isVisible;
	}

	public boolean getBlkHour() {
		Map<String, Map<String, List<String>>> tripBlkMap = new LinkedHashMap<>();
		boolean found = false;
//		int i = 0;

		for (WebElement tripElement : tripList) {
			try {
//				if (i >= 5)
//					break; // Limit to 5 iterations
//				i++;

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

							// Extract all numeric values (include 1- to 4-digit numbers)
							Pattern numberPattern = Pattern.compile("\\b\\d{1,4}\\b");
							Matcher numberMatcher = numberPattern.matcher(tripDataText);

							List<String> numbers = new ArrayList<>();
							while (numberMatcher.find()) {
								numbers.add(numberMatcher.group());
							}

							String blkValue = null;
							if (numbers.size() >= 3) {
								blkValue = numbers.get(numbers.size() - 3); // 3rd last numeric value
								found = true;
							}

							if (blkValue != null) {
								String formattedBlkValue = convertToApiFormat(blkValue);

								WbidBasepage.logger.info("Trip Code: " + tripCode + " | Trip Date: " + tripDate
										+ " | Blk (formatted): " + formattedBlkValue);

								tripBlkMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
										.computeIfAbsent(tripDate, k -> new ArrayList<>()).add(formattedBlkValue);
							}
						}
					}
				}

			} catch (Exception e) {
				System.out.println("Error extracting Blk time: " + e.getMessage());
			}
		}

		WbidBasepage.logger.info("UI Blk Hours (Formatted): " + tripBlkMap);
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
	public boolean compareBlkHour() {
		Map<String, Map<String, List<String>>> tripBlkMap = new LinkedHashMap<>();
		  boolean isComparisonSuccessful = true;
	//	int i = 0;

		for (WebElement tripElement : tripList) {
			try {
//			if (i >= 5)break; // Limit to 5 iterations
//			i++;

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

			// Extract all numeric values (include 1- to 4-digit numbers)
							Pattern numberPattern = Pattern.compile("\\b\\d{1,4}\\b");
							Matcher numberMatcher = numberPattern.matcher(tripDataText);

							List<String> numbers = new ArrayList<>();
							while (numberMatcher.find()) {
								numbers.add(numberMatcher.group());
							}

							String blkValue = null;
							if (numbers.size() >= 3) {
								blkValue = numbers.get(numbers.size() - 3); // 3rd last numeric value
								
							}

							if (blkValue != null) {
								String formattedBlkValue = convertToApiFormat(blkValue);

								WbidBasepage.logger.info("Trip Code: " + tripCode + " | Trip Date: " + tripDate
										+ " | Blk (formatted): " + formattedBlkValue);

								tripBlkMap.computeIfAbsent(tripCode, k -> new LinkedHashMap<>())
										.computeIfAbsent(tripDate, k -> new ArrayList<>()).add(formattedBlkValue);
						}
					}
					
					 boolean isCurrentComparisonSuccessful = compareBlkData(tripBlkMap, BlockTest.apiBlk);
		                if (!isCurrentComparisonSuccessful) {
		                    isComparisonSuccessful = false;
		                }
					}
				}

			} catch (Exception e) {
				System.out.println("Error extracting Blk time: " + e.getMessage());
				 isComparisonSuccessful = false;
			}
		}

		//WbidBasepage.logger.info("UI Blk Hours (Formatted): " + tripBlkMap);
		return isComparisonSuccessful;
	}
*/
	public boolean compareBlkHour() {
	    Map<String, List<String>> tripBlkMap = new LinkedHashMap<>();
	    boolean isComparisonSuccessful = true;
//		int i = 0;
			for (WebElement tripElement : tripList) {
				try {
//				if (i >= 5)break; // Limit to 5 iterations
//				i++;

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

	            Pattern pattern = Pattern.compile("Trip\\s(\\w+)\\sDated");
	            Matcher matcher = pattern.matcher(tripSequenceText);

	            if (matcher.find()) {
	                String tripCode = matcher.group(1).trim();

	                for (WebElement tripEle : tripdata) {
	                    String tripDataText = objaction.gettext(tripEle).trim();

	                    if (tripDataText.startsWith("Rpt") || tripDataText.startsWith("TAFB") || tripDataText.startsWith("Totals")) {
	                        continue;
	                    }

	                    Pattern datePattern = Pattern.compile("^(\\d{2}[A-Za-z]{3})\\b");
	                    Matcher dateMatcher = datePattern.matcher(tripDataText);

	                    if (dateMatcher.find()) {
	                        Pattern numberPattern = Pattern.compile("\\b\\d{1,4}\\b");
	                        Matcher numberMatcher = numberPattern.matcher(tripDataText);

	                        List<String> numbers = new ArrayList<>();
	                        while (numberMatcher.find()) {
	                            numbers.add(numberMatcher.group());
	                        }

	                        String blkValue = null;
	                        if (numbers.size() >= 3) {
	                            blkValue = numbers.get(numbers.size() - 3); // 3rd last value is Blk
	                        }

	                        if (blkValue != null) {
	                            String formattedBlkValue = convertToApiFormat(blkValue);
	                            tripBlkMap.computeIfAbsent(tripCode, k -> new ArrayList<>()).add(formattedBlkValue);
	                        }
	                    }
	                }

	                // Now compare UI vs API for this trip
	                boolean currentComparisonSuccess = compareBlkData(tripCode, tripBlkMap.get(tripCode), BlockTest.apiBlk);
	                if (!currentComparisonSuccess) {
	                    isComparisonSuccessful = false;
	                }
	            }

	        } catch (Exception e) {
	        	WbidBasepage.logger.fail("Error while extracting Blk values: " + e.getMessage());
	            isComparisonSuccessful = false;
	        }
	    }
			//WbidBasepage.logger.pass("Blk Values "+tripBlkMap);
	    return isComparisonSuccessful;
	}

/*	public boolean compareBlkData(Map<String, Map<String, List<String>>> uiData,
			Map<String, Map<Integer, List<String>>> apiData) {
		boolean isMatch = true;

		for (String tripCode : uiData.keySet()) {
			if (!apiData.containsKey(tripCode)) {
				WbidBasepage.logger.fail("❌ Mismatch! UI has TripCode: " + tripCode + ", but API does not.");
				isMatch = false;
				continue;
			}

			Map<String, List<String>> uiDateMap = uiData.get(tripCode);
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

			Map<Integer, List<String>> apiDutySeqMap = apiData.get(tripCode);
			List<Integer> sortedApiDutySeq = new ArrayList<>(apiDutySeqMap.keySet());
			Collections.sort(sortedApiDutySeq);

			List<String> apiSequential = new ArrayList<>();
			for (Integer dutySeq : sortedApiDutySeq) {
				apiSequential.addAll(apiDutySeqMap.getOrDefault(dutySeq, new ArrayList<>()));
			}

			if (!uiGrndsSequential.equals(apiSequential)) {
				WbidBasepage.logger.fail("❌ Block Hour mismatch for TripCode: " + tripCode + " UI Blks (sequential): "
						+ uiGrndsSequential + " API Blks (sequential): " + apiSequential);
				isMatch = false;
			}
		}

		if (isMatch) {
			WbidBasepage.logger.info("✅ UI and API Blk values match perfectly!");
		} else {
			WbidBasepage.logger.fail("❌ Differences found in UI and API Blk hour data!");
			
		}

		return isMatch;
	}

	public boolean compareBlkData(Map<String, List<String>> tripBlkMapUI, Map<String, Map<Integer, List<String>>> tripBlkMapAPI) {
	    boolean isMatch = true;

	    for (Map.Entry<String, List<String>> entry : tripBlkMapUI.entrySet()) {
	        String tripCode = entry.getKey();
	        List<String> uiBlkValues = entry.getValue();

	        // Flatten API values for this tripCode
	        List<String> apiBlkValues = new ArrayList<>();

	        Map<Integer, List<String>> dutyMap = tripBlkMapAPI.get(tripCode);
	        if (dutyMap != null) {
	            for (Map.Entry<Integer, List<String>> dutyEntry : dutyMap.entrySet()) {
	                apiBlkValues.addAll(dutyEntry.getValue());
	            }
	        }

	        if (!uiBlkValues.equals(apiBlkValues)) {
	        	WbidBasepage.logger.fail("❌ Block Hour mismatch for TripCode: " + tripCode +
	                    " UI Blks (sequential): " + uiBlkValues +
	                    " API Blks (sequential): " + apiBlkValues);
	            isMatch = false;
	        } else {
	            WbidBasepage.logger.pass("✅ Block Hours match for TripCode: : " + tripCode +
	                    " UI Blks (sequential): " + uiBlkValues +
	                    " API Blks (sequential): " + apiBlkValues);
	        }
	    }

	    return isMatch;
	}
*/
	public boolean compareBlkData(String tripCode, List<String> uiBlkValues, Map<String, Map<Integer, List<String>>> tripBlkMapAPI) {
	    boolean isMatch = true;

	    List<String> apiBlkValues = new ArrayList<>();
	    Map<Integer, List<String>> dutyMap = tripBlkMapAPI.get(tripCode);
	    if (dutyMap != null) {
	        for (List<String> dutyBlkList : dutyMap.values()) {
	            apiBlkValues.addAll(dutyBlkList);
	        }
	    }

	    // Deduplicate UI blocks based on duty sizes
	    List<String> filteredUIBlocks = new ArrayList<>();
	    Set<List<String>> seenSequences = new HashSet<>();

	    if (dutyMap != null) {
	        int currentIndex = 0;

	        for (List<String> dutyBlkList : dutyMap.values()) {
	            int len = dutyBlkList.size();

	            if (currentIndex + len > uiBlkValues.size()) break;

	            List<String> candidateSeq = uiBlkValues.subList(currentIndex, currentIndex + len);

	            if (seenSequences.add(new ArrayList<>(candidateSeq))) {
	                filteredUIBlocks.addAll(candidateSeq);
	            }

	            currentIndex += len;
	        }
	    }

	    if (!filteredUIBlocks.equals(apiBlkValues)) {
	        WbidBasepage.logger.fail("❌ Block Hour mismatch for TripCode: " + tripCode +
	                " UI Blks (sequential): " + uiBlkValues +
	                " API Blks (sequential): " + apiBlkValues);
	        isMatch = false;
	    } else {
	        WbidBasepage.logger.pass("✅ Block Hours match for TripCode: " + tripCode+
	                " UI Blks (sequential): " + uiBlkValues +
	                " API Blks (sequential): " + apiBlkValues);
	    }

	    return isMatch;
	}

/*
	private Date convertDateToDate(String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMM", Locale.ENGLISH);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date(0);
		}
	}
	*/
}
