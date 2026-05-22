package seleniumcode.com.learn.seleniumframework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import seleniumcode.com.learn.seleniumframework.reports.ExtentReportManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    private static final String SCREENSHOT_DIR = "test-output/screenshots";

    /**
     * Take screenshot and save to disk
     */
    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            createScreenshotDirectory();
            
            String timestamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
            String fileName = screenshotName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + File.separator + fileName;

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get(filePath));

            return filePath;
        } catch (IOException e) {
            ExtentReportManager.fail("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot and attach to extent report
     */
    public static void captureAndAttachScreenshot(WebDriver driver, String screenshotName) {
        try {
            String filePath = captureScreenshot(driver, screenshotName);
            if (filePath != null) {
                ExtentReportManager.attachScreenshot(filePath);
                ExtentReportManager.info("Screenshot captured: " + screenshotName);
            }
        } catch (Exception e) {
            ExtentReportManager.fail("Error in capturing screenshot: " + e.getMessage());
        }
    }

    /**
     * Create the screenshot directory if it doesn't exist
     */
    private static void createScreenshotDirectory() {
        File screenshotDirectory = new File(SCREENSHOT_DIR);
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdirs();
        }
    }
}
