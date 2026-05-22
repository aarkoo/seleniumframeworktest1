package seleniumcode.com.learn.seleniumframework.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import seleniumcode.com.learn.seleniumframework.reports.ExtentReportManager;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite Started: " + context.getName());
        ExtentReportManager.initializeReport();
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Completed: " + context.getName());
        ExtentReportManager.flushReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Test Started: " + result.getMethod().getMethodName());
        ExtentReportManager.createTest(
            result.getMethod().getMethodName(),
            result.getMethod().getDescription() != null ? result.getMethod().getDescription() : ""
        );
        ExtentReportManager.info("Test: " + result.getMethod().getMethodName() + " started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getMethod().getMethodName());
        ExtentReportManager.pass("Test: " + result.getMethod().getMethodName() + " PASSED");
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("Test Failed: " + result.getMethod().getMethodName());
        ExtentReportManager.fail("Test: " + result.getMethod().getMethodName() + " FAILED");
        ExtentReportManager.fail("Exception thrown:", result.getThrowable());
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getMethod().getMethodName());
        ExtentReportManager.warning("Test: " + result.getMethod().getMethodName() + " SKIPPED");
        ExtentReportManager.warning("Reason: " + result.getSkipCausedBy());
        ExtentReportManager.removeTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Not commonly used
    }
}
