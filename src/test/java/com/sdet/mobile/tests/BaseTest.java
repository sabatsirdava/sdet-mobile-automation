package com.sdet.mobile.tests;

import com.sdet.mobile.driver.DriverManager;
import com.sdet.mobile.pages.ProductsPage;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void startSession() {
        DriverManager.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void endSession() {
        DriverManager.quitDriver();
    }

    protected AndroidDriver driver() {
        return DriverManager.getDriver();
    }

    /** Opens the app and waits for the Products screen. */
    protected ProductsPage openApp() {
        return new ProductsPage(driver()).waitUntilLoaded();
    }
}
