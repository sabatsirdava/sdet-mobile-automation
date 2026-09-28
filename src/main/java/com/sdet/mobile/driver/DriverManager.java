package com.sdet.mobile.driver;

import com.sdet.mobile.config.FrameworkConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public final class DriverManager {

    private static final Logger LOG = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static AndroidDriver initDriver() {
        if (DRIVER.get() != null) {
            throw new IllegalStateException("Driver already exists for this test thread.");
        }

        FrameworkConfig config = FrameworkConfig.get();

        LOG.info("Starting Appium session on device '{}'", config.deviceName());

        try {
            AndroidDriver driver =
                    new AndroidDriver(config.serverUrl(), buildOptions());

            DRIVER.set(driver);
            LOG.info("Appium session started: {}", driver.getSessionId());

            return driver;
        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "Could not create Appium session. "
                            + "Check that Appium is running and the device is available.",
                    e);
        }
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = DRIVER.get();

        if (driver == null) {
            throw new IllegalStateException(
                    "No Appium driver exists for the current test thread.");
        }

        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        AndroidDriver driver = DRIVER.get();

        if (driver == null) {
            return;
        }

        try {
            LOG.info("Quitting Appium session {}", driver.getSessionId());
            driver.quit();
        } finally {
            DRIVER.remove();
        }
    }

    private static UiAutomator2Options buildOptions() {
        FrameworkConfig config = FrameworkConfig.get();

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName(config.platformName())
                .setAutomationName(config.automationName())
                .setDeviceName(config.deviceName())
                .setAppPackage(config.appPackage())
                .setAppActivity(config.appActivity())
                // The splash screen hands off to the main activity.
                .setAppWaitActivity("*")
                // Start each test with a clean application state.
                .setNoReset(false)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(120));

        config.appPath().ifPresent(options::setApp);

        return options;
    }
}