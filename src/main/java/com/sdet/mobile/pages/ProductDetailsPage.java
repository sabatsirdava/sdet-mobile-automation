package com.sdet.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ProductDetailsPage extends BasePage {

    @AndroidFindBy(id = AppIds.ID + "productTV")
    private WebElement productName;

    @AndroidFindBy(id = AppIds.ID + "priceTV")
    private WebElement productPrice;

    @AndroidFindBy(accessibility = "Tap to add product to cart")
    private WebElement addToCartButton;

    private final AppHeader header;

    public ProductDetailsPage(AndroidDriver driver) {
        super(driver);
        this.header = new AppHeader(driver);
    }

    public ProductDetailsPage waitUntilLoaded() {
        waitVisible(addToCartButton);
        return this;
    }

    public String getProductName() {
        return textOf(productName);
    }

    public String getProductPrice() {
        return textOf(productPrice);
    }

    public ProductDetailsPage addToCart() {
        int before = header.getCartBadgeCount();
        click(addToCartButton);
        header.waitForCartCount(before + 1);
        return this;
    }

    public int getCartBadgeCount() {
        return header.getCartBadgeCount();
    }

    public CartPage openCart() {
        header.openCart();
        return new CartPage(driver).waitUntilLoaded();
    }

    public ProductsPage goBackToProducts() {
        driver.navigate().back();
        return new ProductsPage(driver).waitUntilLoaded();
    }
}
