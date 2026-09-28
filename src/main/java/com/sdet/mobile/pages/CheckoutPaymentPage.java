package com.sdet.mobile.pages;

import com.sdet.mobile.model.PaymentCard;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/** Checkout step 2: payment details. */
public class CheckoutPaymentPage extends BasePage {

    private static final String REVIEW_BUTTON_DESC = "Saves payment info and launches screen to review checkout data";

    @AndroidFindBy(id = AppIds.ID + "nameET")
    private WebElement cardHolderField;

    @AndroidFindBy(id = AppIds.ID + "cardNumberET")
    private WebElement cardNumberField;

    @AndroidFindBy(id = AppIds.ID + "expirationDateET")
    private WebElement expirationDateField;

    @AndroidFindBy(id = AppIds.ID + "securityCodeET")
    private WebElement securityCodeField;

    @AndroidFindBy(accessibility = REVIEW_BUTTON_DESC)
    private WebElement reviewOrderButton;

    public CheckoutPaymentPage(AndroidDriver driver) {
        super(driver);
    }

    public CheckoutPaymentPage waitUntilLoaded() {
        waitVisible(cardHolderField);
        return this;
    }

    public ReviewOrderPage submitPayment(PaymentCard card) {
        type(cardHolderField, card.cardHolder());
        type(cardNumberField, card.number());
        type(expirationDateField, card.expirationDate());
        type(securityCodeField, card.securityCode());
        hideKeyboardIfShown();
        scrollToDescription(REVIEW_BUTTON_DESC);
        click(reviewOrderButton);
        return new ReviewOrderPage(driver).waitUntilLoaded();
    }
}
