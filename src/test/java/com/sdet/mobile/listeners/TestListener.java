package com.sdet.mobile.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.sdet.mobile.config.FrameworkConfig;
import com.sdet.mobile.driver.DriverManager;
import com.sdet.mobile.utils.ScreenshotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;

/**
 * Bridges TestNG events to the Extent report and captures a screenshot on failure.
 *
 * <p>Ordering note: TestNG calls {@link #onTestFailure} BEFORE the {@code @AfterMethod} that quits
 * the driver, so the session is still alive here and the screenshot shows the real failure state.
 */
public class TestListener implements ITestListener {

    private static final Logger LOG = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.init();
        LOG.info("Starting test run '{}'", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportManager.startTest(displayName(result), result.getMethod().getDescription());
        test.assignCategory(result.getMethod().getGroups());
        LOG.info("START  {}", displayName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.currentTest().pass("Test passed");
        LOG.info("PASS   {}", displayName(result));
        ExtentReportManager.endTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("FAIL   {} - {}", displayName(result), String.valueOf(result.getThrowable()));
        ExtentTest test = ExtentReportManager.currentTest();
        String base64 = captureScreenshotAsBase64(result);
        if (base64 != null) {
            test.fail(result.getThrowable(),
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64, "Screenshot at failure").build());
        } else {
            test.fail(result.getThrowable());
        }
        ExtentReportManager.endTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.currentTest();
        if (test == null) { // skipped before it started (e.g. failed @BeforeMethod)
            test = ExtentReportManager.startTest(displayName(result), result.getMethod().getDescription());
        }
        test.skip(result.getThrowable() != null ? result.getThrowable().toString() : "Test skipped");
        LOG.warn("SKIP   {}", displayName(result));
        ExtentReportManager.endTest();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flush();
    }

    private String captureScreenshotAsBase64(ITestResult result) {
        if (!DriverManager.hasDriver()) {
            LOG.warn("No active driver - skipping screenshot for {}", displayName(result));
            return null;
        }
        try {
            Path dir = FrameworkConfig.get().reportsDir().resolve("screenshots");
            Path file = ScreenshotUtils.capture(DriverManager.getDriver(), displayName(result), dir);
            LOG.info("Screenshot saved: {}", file.toAbsolutePath());
            return Base64.getEncoder().encodeToString(Files.readAllBytes(file));
        } catch (RuntimeException | IOException e) {
            LOG.warn("Could not capture screenshot for {}: {}", displayName(result), e.toString());
            return null;
        }
    }

    private static String displayName(ITestResult result) {
        Object[] params = result.getParameters();
        String base = result.getMethod().getMethodName();
        return params.length > 0 && params[0] instanceof String label ? base + "[" + label + "]" : base
                + (params.length > 0 ? Arrays.toString(params) : "");
    }
}
