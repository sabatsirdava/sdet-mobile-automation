package com.sdet.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    @AndroidFindBy(id = AppIds.ID + "noItemTitleTV")
    private WebElement emptyCartTitle;

    @AndroidFindBy(id = AppIds.ID + "titleTV")
    private List<WebElement> itemNames;

    @AndroidFindBy(id = AppIds.ID + "priceTV")
    private List<WebElement> itemPrices;

    @AndroidFindBy(accessibility = "Removes product from cart")
    private List<WebElement> removeButtons;

    @AndroidFindBy(accessibility = "Confirms products for checkout")
    private WebElement proceedToCheckoutButton;

    private final AppHeader header;

    public CartPage(AndroidDriver driver) {
        super(driver);
        this.header = new AppHeader(driver);
    }

    public CartPage waitUntilLoaded() {
        waitUntil(() -> !itemNames.isEmpty() || isEmpty(),
                "the cart to render its items or its empty state");
        return this;
    }

    public boolean isEmpty() {
        return isDisplayed(emptyCartTitle);
    }

    public List<String> getProductNames() {
        return itemNames.stream().map(WebElement::getText).toList();
    }

    public List<String> getProductPrices() {
        return itemPrices.stream().map(WebElement::getText).toList();
    }

    public int getItemCount() {
        return itemNames.size();
    }

    public CartPage removeFirstProduct() {
        int before = itemNames.size();
        click(removeButtons.get(0));
        waitUntil(() -> itemNames.size() < before || isEmpty(), "the removed product to disappear from the cart");
        return this;
    }

    /** Logged-in checkout: goes straight to the shipping address form. */
    public CheckoutAddressPage proceedToCheckout() {
        click(proceedToCheckoutButton);
        return new CheckoutAddressPage(driver).waitUntilLoaded();
    }

    /** Guest checkout: the app redirects to the login screen first. */
    public LoginPage proceedToCheckoutAsGuest() {
        click(proceedToCheckoutButton);
        return new LoginPage(driver).waitUntilLoaded();
    }

    public int getCartBadgeCount() {
        return header.getCartBadgeCount();
    }
}
