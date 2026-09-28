package com.sdet.mobile.pages;

import com.sdet.mobile.model.Credentials;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

public class LoginPage extends BasePage {

    @AndroidFindBy(id = AppIds.ID + "nameET")
    private WebElement usernameField;

    @AndroidFindBy(id = AppIds.ID + "passwordET")
    private WebElement passwordField;

    @AndroidFindBy(accessibility = "Tap to login with given credentials")
    private WebElement loginButton;

    @AndroidFindBy(id = AppIds.ID + "nameErrorTV")
    private WebElement usernameError;

    @AndroidFindBy(id = AppIds.ID + "passwordErrorTV")
    private WebElement passwordError;

    private final AppHeader header;

    public LoginPage(AndroidDriver driver) {
        super(driver);
        this.header = new AppHeader(driver);
    }

    public LoginPage waitUntilLoaded() {
        waitVisible(usernameField);
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(usernameField) && isDisplayed(passwordField);
    }

    public ProductsPage loginToCatalog(Credentials credentials) {
        submit(credentials);
        return new ProductsPage(driver).waitUntilLoaded();
    }

    public CheckoutAddressPage loginToCheckout(Credentials credentials) {
        submit(credentials);
        return new CheckoutAddressPage(driver).waitUntilLoaded();
    }

    public LoginPage loginExpectingError(Credentials credentials) {
        submit(credentials);
        waitUntil(() -> !getErrorMessages().isEmpty(), "a login validation message to appear");
        return this;
    }

    public List<String> getErrorMessages() {
        return Stream.of(usernameError, passwordError)
                .filter(this::isDisplayed)
                .map(WebElement::getText)
                .filter(text -> !text.isBlank())
                .toList();
    }

    public ProductsPage openCatalogViaMenu() {
        header.selectCatalog();
        return new ProductsPage(driver).waitUntilLoaded();
    }

    private void submit(Credentials credentials) {
        waitUntilLoaded();
        if (!credentials.username().isEmpty()) {
            type(usernameField, credentials.username());
        }
        if (!credentials.password().isEmpty()) {
            type(passwordField, credentials.password());
        }
        hideKeyboardIfShown();
        click(loginButton);
    }
}
