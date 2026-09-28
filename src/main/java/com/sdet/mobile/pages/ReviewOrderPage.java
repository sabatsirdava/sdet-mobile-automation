package com.sdet.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ReviewOrderPage extends BasePage {

    private static final String PLACE_ORDER_DESC = "Completes the process of checkout";

    @AndroidFindBy(accessibility = PLACE_ORDER_DESC)
    private WebElement placeOrderButton;

    public ReviewOrderPage(AndroidDriver driver) {
        super(driver);
    }

    public ReviewOrderPage waitUntilLoaded() {
        scrollToDescription(PLACE_ORDER_DESC);
        waitVisible(placeOrderButton);
        return this;
    }

    public CheckoutCompletePage placeOrder() {
        click(placeOrderButton);
        return new CheckoutCompletePage(driver).waitUntilLoaded();
    }
}
