package com.sdet.mobile.pages;

import com.sdet.mobile.model.Product;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/** Product catalogue - the landing screen after the splash screen and after login/logout. */
public class ProductsPage extends BasePage {

    private static final String PAGE_TITLE = "Products";

    @AndroidFindBy(id = AppIds.ID + "productTV")
    private WebElement title;

    private final AppHeader header;

    public ProductsPage(AndroidDriver driver) {
        super(driver);
        this.header = new AppHeader(driver);
    }

    public ProductsPage waitUntilLoaded() {
        waitUntil(this::isLoaded, "the Products screen to be displayed");
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(title) && PAGE_TITLE.equals(title.getText().trim());
    }

    public ProductDetailsPage openProductDetails(Product product) {
        scrollToText(product.displayName());
        String imageXpath = "//android.widget.TextView[@text=\"" + product.displayName() + "\"]"
                + "/parent::android.view.ViewGroup/android.widget.ImageView";
        waitVisible(AppiumBy.xpath(imageXpath)).click();
        return new ProductDetailsPage(driver).waitUntilLoaded();
    }

    public CartPage openCart() {
        header.openCart();
        return new CartPage(driver).waitUntilLoaded();
    }

    public LoginPage openLoginScreen() {
        header.selectLogin();
        return new LoginPage(driver).waitUntilLoaded();
    }

    public LoginPage logout() {
        header.logout();
        return new LoginPage(driver).waitUntilLoaded();
    }

    public boolean isUserLoggedIn() {
        return header.isUserLoggedIn();
    }

    public int getCartBadgeCount() {
        return header.getCartBadgeCount();
    }
}
