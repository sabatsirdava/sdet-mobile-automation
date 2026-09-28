package com.sdet.mobile.pages;

import com.sdet.mobile.config.FrameworkConfig;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Common waits and interactions shared by page objects.
 */
public abstract class BasePage {

    protected final AndroidDriver driver;
    private final Duration timeout;

    protected BasePage(AndroidDriver driver) {
        this.driver = driver;
        this.timeout = FrameworkConfig.get().explicitWait();
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    protected WebElement waitVisible(WebElement element) {
        return newWait().until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitVisible(By by) {
        return newWait().until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /** Waits for an arbitrary condition; {@code description} becomes the timeout message. */
    protected void waitUntil(BooleanSupplier condition, String description) {
        // Typed Function avoids the until(Function)/until(Predicate) overload ambiguity.
        Function<WebDriver, Boolean> check = driver -> condition.getAsBoolean();
        newWait().withMessage("Timed out waiting for: " + description).until(check);
    }

    protected void click(WebElement element) {
        newWait().until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    protected void type(WebElement element, String text) {
        WebElement visible = waitVisible(element);
        visible.clear();
        visible.sendKeys(text);
    }

    protected String textOf(WebElement element) {
        return waitVisible(element).getText();
    }

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected void hideKeyboardIfShown() {
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
    }

    protected void scrollToText(String text) {
        scrollIntoView("new UiSelector().text(\"" + escape(text) + "\")");
    }

    protected void scrollToDescription(String description) {
        scrollIntoView("new UiSelector().description(\"" + escape(description) + "\")");
    }

    protected void scrollToResourceId(String resourceId) {
        var matches = driver.findElements(AppiumBy.id(resourceId));
        if (!matches.isEmpty() && isDisplayed(matches.get(0))) {
            return;
        }
        scrollIntoView("new UiSelector().resourceId(\"" + resourceId + "\")");
    }

    private void scrollIntoView(String uiSelector) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" + uiSelector + ")"));
    }

    private WebDriverWait newWait() {
        return new WebDriverWait(driver, timeout);
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
