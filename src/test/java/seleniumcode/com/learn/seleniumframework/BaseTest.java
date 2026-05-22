package seleniumcode.com.learn.seleniumframework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import seleniumcode.com.learn.seleniumframework.pageobjects.Landingpage;
import seleniumcode.com.learn.seleniumframework.reports.ExtentReportManager;
import seleniumcode.com.learn.seleniumframework.resources.ConfigReader;
import seleniumcode.com.learn.seleniumframework.utils.ScreenshotUtil;

public class BaseTest {

	private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
	protected ConfigReader configReader;
	


	// Each test owns its browser session, which keeps suite state isolated.
	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		configReader = new ConfigReader();
		WebDriver driver = createDriver();
		DRIVER.set(driver);

		driver.manage().window().maximize();
		ExtentReportManager.info("Browser launched and window maximized");
		loginToApplication();
		System.out.println("started session");
	}

	private WebDriver createDriver() {
		String browser = System.getProperty("browser", "chrome");
		if (!"chrome".equalsIgnoreCase(browser)) {
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		ChromeOptions options = new ChromeOptions();

		Map<String, Object> prefs = new HashMap<>();
		prefs.put("credentials_enable_service", false);
		prefs.put("profile.password_manager_enabled", false);
		prefs.put("profile.password_manager_leak_detection", false);

		options.setExperimentalOption("prefs", prefs);
		options.addArguments("--disable-notifications");
		options.addArguments("--disable-save-password-bubble");
		options.addArguments("--disable-infobars");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-gpu");
		options.addArguments("--no-first-run");
		options.addArguments("--no-sandbox");
		options.addArguments("--remote-debugging-port=0");
		options.addArguments("--user-data-dir=" + createTemporaryChromeProfile());

		if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
			options.addArguments("--headless=new");
			options.addArguments("--window-size=1440,900");
		}

		return new ChromeDriver(options);
	}

	private String createTemporaryChromeProfile() {
		try {
			Path profileDirectory = Files.createTempDirectory("selenium-chrome-profile-");
			return profileDirectory.toAbsolutePath().toString();
		} catch (IOException e) {
			throw new IllegalStateException("Unable to create a temporary Chrome profile", e);
		}
	}

	protected WebDriver getDriver() {
		WebDriver driver = DRIVER.get();
		if (driver == null) {
			throw new IllegalStateException("WebDriver has not been initialized for this test");
		}
		return driver;
	}

	private void loginToApplication() {
		Landingpage landingpage = new Landingpage(getDriver());
		ExtentReportManager.info("Navigating to application URL");
		landingpage.goTo(configReader.getApplicationUrl());
		ExtentReportManager.pass("Successfully navigated to: " + configReader.getApplicationUrl());
		
		ExtentReportManager.info("Attempting login with provided credentials");
		landingpage.loginpage(configReader.getUserEmail(), configReader.getUserPassword());

		if (!landingpage.verifyLoginSuccess()) {
			String error = landingpage.getLoginError();
			ScreenshotUtil.captureAndAttachScreenshot(getDriver(), "login_failure");
			ExtentReportManager.fail("Login failed: " + error);
			throw new RuntimeException("Login failed: " + error);
		}
		ExtentReportManager.pass("Login successful");
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		WebDriver driver = DRIVER.get();
		if (driver != null) {
			ExtentReportManager.info("Closing browser session");
			driver.quit();
			DRIVER.remove();
		}
	}
	
	
}
