package com.sdet.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/** Order confirmation screen. */
public class CheckoutCompletePage extends BasePage {

    @AndroidFindBy(id = AppIds.ID + "completeTV")
    private WebElement completeTitle;

    @AndroidFindBy(id = AppIds.ID + "thankYouTV")
    private WebElement thankYouMessage;

    @AndroidFindBy(accessibility = "Tap to open catalog")
    private WebElement continueShoppingButton;

    public CheckoutCompletePage(AndroidDriver driver) {
        super(driver);
    }

    public CheckoutCompletePage waitUntilLoaded() {
        waitVisible(completeTitle);
        return this;
    }

    public String getTitle() {
        return textOf(completeTitle);
    }

    public String getThankYouMessage() {
        return textOf(thankYouMessage);
    }

    public ProductsPage continueShopping() {
        click(continueShoppingButton);
        return new ProductsPage(driver).waitUntilLoaded();
    }
}
