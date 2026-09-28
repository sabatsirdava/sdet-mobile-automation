package com.sdet.mobile.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.sdet.mobile.config.FrameworkConfig;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Manages the Extent report and the current test for each thread. */
final class ExtentReportManager {

    private static final ThreadLocal<ExtentTest> CURRENT_TEST = new ThreadLocal<>();
    private static ExtentReports extent;
    private static Path reportFile;

    private ExtentReportManager() {
    }

    static synchronized void init() {
        if (extent != null) {
            return;
        }
        FrameworkConfig config = FrameworkConfig.get();
        Path dir = config.reportsDir().resolve("extent");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot create report directory " + dir.toAbsolutePath(), e);
        }
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        reportFile = dir.resolve("report-" + stamp + ".html");

        ExtentSparkReporter spark = new ExtentSparkReporter(reportFile.toString());
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle("My Demo App - Android Automation");
        spark.config().setReportName("Sauce Labs My Demo App - Android Test Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Appium server", config.serverUrl().toString());
        extent.setSystemInfo("Device", config.deviceName());
        extent.setSystemInfo("Automation", config.automationName());
        extent.setSystemInfo("App package", config.appPackage());
        extent.setSystemInfo("Java", System.getProperty("java.version"));
    }

    static synchronized ExtentTest startTest(String name, String description) {
        ExtentTest test = extent.createTest(name, description);
        CURRENT_TEST.set(test);
        return test;
    }

    static ExtentTest currentTest() {
        return CURRENT_TEST.get();
    }

    static void endTest() {
        CURRENT_TEST.remove();
    }

    static synchronized void flush() {
        if (extent == null) {
            return;
        }
        extent.flush();
        try {
            Files.copy(reportFile, reportFile.resolveSibling("latest-report.html"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not write latest-report.html", e);
        }
        System.out.println("Extent report: " + reportFile.toAbsolutePath());
    }
}
