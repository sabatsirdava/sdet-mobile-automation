package com.sdet.mobile.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

/**
 * Common toolbar and navigation drawer actions.
 */
public class AppHeader extends BasePage {

    @AndroidFindBy(accessibility = "View menu")
    private WebElement menuButton;

    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"com.saucelabs.mydemoapp.android:id/itemTV\").text(\"Catalog\")")
    private WebElement catalogItem;

    @AndroidFindBy(accessibility = "Login Menu Item")
    private WebElement loginItem;

    @AndroidFindBy(accessibility = "Logout Menu Item")
    private WebElement logoutItem;

    @AndroidFindBy(id = AppIds.ID + "cartIV")
    private WebElement cartIcon;

    @AndroidFindBy(id = AppIds.ID + "cartTV")
    private WebElement cartBadge;

    // Standard Android AlertDialog buttons (framework ids, not app ids).
    @AndroidFindBy(uiAutomator = "new UiSelector().resourceId(\"android:id/button1\").textMatches(\"(?i)log ?out\")")
    private WebElement confirmLogoutButton;

    public AppHeader(AndroidDriver driver) {
        super(driver);
    }

    public void openMenu() {
        click(menuButton);
        waitVisible(catalogItem); // the drawer is open once its entries render
    }

    public void closeMenu() {
        driver.navigate().back();
        waitUntil(() -> !isDisplayed(catalogItem), "navigation drawer to close");
    }

    public void selectCatalog() {
        openMenu();
        click(catalogItem);
    }

    public void selectLogin() {
        openMenu();
        click(loginItem);
    }

    /** Full log-out flow: menu entry, confirmation dialog, success dialog. */
    public void logout() {
        openMenu();
        click(logoutItem);
        click(confirmLogoutButton);
    }

    /** Returns true when the logout option is available. */
    public boolean isUserLoggedIn() {
        openMenu();
        boolean loggedIn = isDisplayed(logoutItem);
        closeMenu();
        return loggedIn;
    }

    /** Returns true when the login option is available. */
    public boolean isLoginOptionAvailable() {
        openMenu();
        boolean available = isDisplayed(loginItem);
        closeMenu();
        return available;
    }

    public void openCart() {
        click(cartIcon);
    }

    /** Number on the cart badge; 0 when the badge is not shown (empty cart). */
    public int getCartBadgeCount() {
        if (!isDisplayed(cartBadge)) {
            return 0;
        }
        return Integer.parseInt(cartBadge.getText().trim());
    }

    public void waitForCartCount(int expected) {
        waitUntil(() -> getCartBadgeCount() == expected, "cart badge to show " + expected);
    }
}
