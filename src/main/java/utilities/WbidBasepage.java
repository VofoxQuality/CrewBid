package utilities;

////////////////Nimishila Bhai: Created Datadriven Framework/////////////////////
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WbidBasepage {
	private WebDriver driver;
	public Properties objproperty;
	public static String username = "";
	public static String password = "";
	public static String downloadPath = System.getProperty("user.dir") + ".\\File Downloads";
	public static String url = "";
	public String screenshotpath = null;
	public static ExtentSparkReporter sparkall;
	public static ExtentSparkReporter sparkskip;
	public static ExtentSparkReporter sparkfailed;
	public static ExtentReports extent;
	public static ExtentTest logger;
	public WbidBasepage() {

		try {
			objproperty = new Properties();
			// Read Config File from .properties */
			FileInputStream src = new FileInputStream(
					System.getProperty("user.dir") + "\\src\\main\\resources\\Configfiles\\configfile.properties");
			try {
				objproperty.load(src);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/* Read Data from properties file */
			String browser_configfile = objproperty.getProperty("browser");
			String environment_configfile = objproperty.getProperty("environment");
			System.out.println(browser_configfile);
			/* Checking for the enviornment */
			if (environment_configfile.equalsIgnoreCase("qa")) {

				HashMap<String, String> testDataMap = testData("qa environment");

				// Taking values from hash map to script
				username = testDataMap.get("Usename"); // Retrieve value for the key "Name"
				System.out.println("Username from hashmap : " + username);

				password = testDataMap.get("Password");
				System.out.println("Password from hashmap : " + password);

				url = testDataMap.get("Url");
				System.out.println("QA url from hashmap : " + url);

				System.out.println("***************Running in QA environment***************");

			} else if (environment_configfile.equalsIgnoreCase("automation")) {

				HashMap<String, String> testDataMap = testData("automation environment");

				// Taking values from hash map to script
				username = testDataMap.get("Usename"); // Retrieve value for the key "Name"
				System.out.println("Username from hashmap : " + username);

				password = testDataMap.get("Password");
				System.out.println("Password from hashmap : " + password);

				url = testDataMap.get("Url");
				System.out.println("Automation url from hashmap : " + url);

				System.out.println("***************Running in Automation platform***************");
			}

			Browserlaunch();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public WebDriver returnDriver() {
		return driver;
	}

	/* To read excel data using HashMap (username,password,url) */

	public static HashMap testData(String env) {
		String fileName = System.getProperty("user.dir") + "\\src\\main\\resources\\Excelfiles\\Excelfile.xls"; // replace
																												// with
																												// your
		// Excel file path
		String sheetName = env; // replace with your sheet name
		HashMap<String, String> map = new HashMap();
		FileInputStream fis = null;
		HSSFWorkbook workbook = null;
		try {
			fis = new FileInputStream(new File(fileName));
			workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheet(sheetName);
			// skip header row
			for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Row row = sheet.getRow(rowIndex);
				if (row != null) {
					String key = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim();
					String value = "";
					if (row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getCellType() == CellType.NUMERIC) {
						value = String.valueOf(
								row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue());
					} else {
						value = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue().trim();
					}
					map.put(key, value);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (workbook != null) {
					workbook.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// print the HashMap
		return map;
	}

	/* To Browser launch */
	public void Browserlaunch() {
	    // Get the browser type from the properties file
	    String browser_configfile = objproperty.getProperty("browser");    
	    if (browser_configfile.equalsIgnoreCase("Chrome")) {
	        // Setup WebDriverManager for Chrome
	        WebDriverManager.chromedriver().setup();	        
	        // Create ChromeOptions to set preferences
	        ChromeOptions options = new ChromeOptions();	        
	        // Create a map to hold preferences
	        Map<String, Object> prefs = new HashMap<>();	        
	        // Set the default download directory
	        prefs.put("download.default_directory", downloadPath);	        
	        // Add preferences to ChromeOptions
	        options.setExperimentalOption("prefs", prefs);	        
	        // Add an argument to allow remote origins
	        options.addArguments("--remote-allow-origins=*");	        
	        // Create the ChromeDriver instance with the configured options
	        driver = new ChromeDriver(options);	        
	    } else if (browser_configfile.equalsIgnoreCase("Firefox")) {
	        // Setup WebDriverManager for Firefox and create FirefoxDriver
	        driver = WebDriverManager.firefoxdriver().create();	        
	    } else if (browser_configfile.equalsIgnoreCase("Edge")) {
	        // Setup WebDriverManager for Edge and create EdgeDriver
	        driver = WebDriverManager.edgedriver().create();	        
	    } else {
	        // Print an error message if no browser matches
	        System.out.println("No option for this Browser");
	    }
	    
	    // Maximize the browser window
	    driver.manage().window().maximize();
	    
	    // Delete all cookies
	    driver.manage().deleteAllCookies();
	    
	    // Open the specified URL
	    driver.get(url);
	}

	@AfterClass
	public void closebrowser() {
		driver.quit();
	}

	@BeforeSuite
	public void beforeSuite() {
		extent = new ExtentReports();
		sparkall = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/ExtentReport/AutomationExtentReport.html");
		extent.attachReporter(sparkall);
		extent.setSystemInfo("Host Name", "AUTOMATION TESTING");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("User Name", " LIYA ANN THOMAS, NIMISHILA BHAI N");
		sparkall.config().setDocumentTitle("Automation Extend Spark Report");
		// Name of the report
		sparkall.config().setReportName("MTSS");
		// Dark Theme
		sparkall.config().setTheme(Theme.DARK);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		try {
			screenshotpath = getScreenshot(driver, result.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (result.getStatus() == ITestResult.FAILURE) {
			logger.log(Status.FAIL, "<span style='color:red'>FAILED TEST CASE:  </span>" +"<span style='color:red'>"+ result.getName()+ "</span>");
			//logger.log(Status.FAIL, "FAILURE REASON:  " + result.getThrowable());
			logger.log(Status.FAIL,"<span style='color:red'>FAILURE REASON:  </span>"+"<span style='color:red'>" + result.getThrowable() + "</span>");
			logger.addScreenCaptureFromPath(screenshotpath, "Failed Test Screenshot");
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			logger.log(Status.PASS, "TEST CASE PASSED: " + result.getName());
		}else if (result.getStatus() == ITestResult.SKIP) {
		    logger.log(Status.SKIP, "TEST CASE SKIPPED: " + result.getName());
		    logger.log(Status.SKIP, "REASON: " + result.getThrowable());
		} 
	}


	@AfterSuite
	public void afterSuite() throws IOException {

		extent.flush();
	}

	///////////// TO get Screenshot/////////////
	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateName
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);
		return destination;
	}
}
