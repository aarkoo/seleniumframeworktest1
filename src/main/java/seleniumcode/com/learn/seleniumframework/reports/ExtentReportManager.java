package seleniumcode.com.learn.seleniumframework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final String REPORT_DIR = "test-output/extent-reports";

    /**
     * Initialize ExtentReports with Spark Reporter
     */
    public static void initializeReport() {
        if (extent == null) {
            createReportDirectory();
            String reportPath = getReportPath();
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setDocumentTitle("Selenium Test Report");
            sparkReporter.config().setReportName("Selenium Automation Test Report");
            sparkReporter.config().setTimeStampFormat("dd-MMM-yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Browser", System.getProperty("browser", "Chrome"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
    }

    /**
     * Create a new test in the extent report
     */
    public static void createTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test);
    }

    /**
     * Get the current extent test
     */
    public static ExtentTest getTest() {
        return extentTest.get();
    }

    /**
     * Log a passed step
     */
    public static void pass(String message) {
        if (getTest() != null) {
            getTest().pass(message);
        }
    }

    /**
     * Log a failed step with exception
     */
    public static void fail(String message, Throwable throwable) {
        if (getTest() != null) {
            getTest().fail(message);
            getTest().fail(throwable);
        }
    }

    /**
     * Log a failed step
     */
    public static void fail(String message) {
        if (getTest() != null) {
            getTest().fail(message);
        }
    }

    /**
     * Log an info step
     */
    public static void info(String message) {
        if (getTest() != null) {
            getTest().info(message);
        }
    }

    /**
     * Log a warning step
     */
    public static void warning(String message) {
        if (getTest() != null) {
            getTest().warning(message);
        }
    }

    /**
     * Add screenshot to the report
     */
    public static void attachScreenshot(String imagePath) {
        if (getTest() != null) {
            getTest().addScreenCaptureFromPath(imagePath);
        }
    }

    /**
     * Flush the report (write to file)
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }

    /**
     * Create the report directory if it doesn't exist
     */
    private static void createReportDirectory() {
        File reportDirectory = new File(REPORT_DIR);
        if (!reportDirectory.exists()) {
            reportDirectory.mkdirs();
        }
    }

    /**
     * Generate the report file path with timestamp
     */
    private static String getReportPath() {
        String timestamp = new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date());
        return REPORT_DIR + File.separator + "ExtentReport_" + timestamp + ".html";
    }

    /**
     * Remove the thread local extent test
     */
    public static void removeTest() {
        extentTest.remove();
    }
}
