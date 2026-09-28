package com.sdet.mobile.pages;

import com.sdet.mobile.model.ShippingAddress;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Stream;

public class CheckoutAddressPage extends BasePage {

    private static final String CITY_ID = AppIds.ID + "cityET";
    private static final String STATE_ID = AppIds.ID + "stateET";
    private static final String ZIP_ID = AppIds.ID + "zipET";
    private static final String COUNTRY_ID = AppIds.ID + "countryET";
    private static final String SAVE_BUTTON_DESC = "Saves user info for checkout";

    @AndroidFindBy(id = AppIds.ID + "fullNameET")
    private WebElement fullNameField;

    @AndroidFindBy(id = AppIds.ID + "address1ET")
    private WebElement address1Field;

    @AndroidFindBy(id = CITY_ID)
    private WebElement cityField;

    @AndroidFindBy(id = STATE_ID)
    private WebElement stateField;

    @AndroidFindBy(id = ZIP_ID)
    private WebElement zipField;

    @AndroidFindBy(id = COUNTRY_ID)
    private WebElement countryField;

    @AndroidFindBy(accessibility = SAVE_BUTTON_DESC)
    private WebElement toPaymentButton;

    @AndroidFindBy(id = AppIds.ID + "fullNameErrorTV")
    private WebElement fullNameError;

    @AndroidFindBy(id = AppIds.ID + "address1ErrorTV")
    private WebElement address1Error;

    @AndroidFindBy(id = AppIds.ID + "cityErrorTV")
    private WebElement cityError;

    @AndroidFindBy(id = AppIds.ID + "zipErrorTV")
    private WebElement zipError;

    @AndroidFindBy(id = AppIds.ID + "countryErrorTV")
    private WebElement countryError;

    public CheckoutAddressPage(AndroidDriver driver) {
        super(driver);
    }

    public CheckoutAddressPage waitUntilLoaded() {
        waitVisible(fullNameField);
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(fullNameField);
    }

    public CheckoutPaymentPage submitAddress(ShippingAddress address) {
        fill(address);
        submit();
        return new CheckoutPaymentPage(driver).waitUntilLoaded();
    }

    /** Negative path: submit the untouched form and stay on this screen. */
    public CheckoutAddressPage submitEmptyForm() {
        submit();
        waitUntil(() -> !getValidationErrors().isEmpty(), "address validation messages to appear");
        return this;
    }

    public List<String> getValidationErrors() {
        return Stream.of(fullNameError, address1Error, cityError, zipError, countryError)
                .filter(this::isDisplayed)
                .map(WebElement::getText)
                .filter(text -> !text.isBlank())
                .toList();
    }

    private void fill(ShippingAddress address) {
        type(fullNameField, address.fullName());
        type(address1Field, address.addressLine1());
        scrollToResourceId(CITY_ID);
        type(cityField, address.city());
        scrollToResourceId(STATE_ID);
        type(stateField, address.state());
        scrollToResourceId(ZIP_ID);
        type(zipField, address.zipCode());
        scrollToResourceId(COUNTRY_ID);
        type(countryField, address.country());
    }

    private void submit() {
        hideKeyboardIfShown();
        scrollToDescription(SAVE_BUTTON_DESC);
        click(toPaymentButton);
    }
}
