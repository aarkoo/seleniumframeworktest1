package seleniumcode.com.learn.seleniumframework.utils;

import seleniumcode.com.learn.seleniumframework.reports.ExtentReportManager;

public class LogUtil {

    /**
     * Log test step with automatic formatting
     */
    public static void step(String stepName) {
        ExtentReportManager.info("Step: " + stepName);
    }

    /**
     * Log successful action
     */
    public static void success(String message) {
        ExtentReportManager.pass("✓ " + message);
    }

    /**
     * Log failed action
     */
    public static void error(String message) {
        ExtentReportManager.fail("✗ " + message);
    }

    /**
     * Log warning message
     */
    public static void warn(String message) {
        ExtentReportManager.warning("⚠ " + message);
    }

    /**
     * Log information message
     */
    public static void info(String message) {
        ExtentReportManager.info("ℹ " + message);
    }

    /**
     * Log with formatted parameters
     */
    public static void stepWithParams(String stepName, String... params) {
        StringBuilder sb = new StringBuilder("Step: " + stepName);
        if (params.length > 0) {
            sb.append(" | Parameters: ");
            for (int i = 0; i < params.length; i++) {
                sb.append("[").append(params[i]).append("]");
                if (i < params.length - 1) {
                    sb.append(", ");
                }
            }
        }
        ExtentReportManager.info(sb.toString());
    }

    /**
     * Log assertion verification
     */
    public static void verify(String assertion, boolean isTrue) {
        if (isTrue) {
            success("Assertion verified: " + assertion);
        } else {
            error("Assertion failed: " + assertion);
        }
    }

    /**
     * Log with exception details
     */
    public static void exception(String message, Exception e) {
        ExtentReportManager.fail(message, e);
    }

    /**
     * Create a section header in the report
     */
    public static void section(String sectionName) {
        ExtentReportManager.info("========== " + sectionName.toUpperCase() + " ==========");
    }
}
