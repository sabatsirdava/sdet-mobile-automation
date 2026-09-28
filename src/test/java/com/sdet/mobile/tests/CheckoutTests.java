package com.sdet.mobile.tests;

import com.sdet.mobile.model.Product;
import com.sdet.mobile.pages.CartPage;
import com.sdet.mobile.pages.CheckoutAddressPage;
import com.sdet.mobile.pages.CheckoutCompletePage;
import com.sdet.mobile.pages.LoginPage;
import com.sdet.mobile.testdata.CheckoutData;
import com.sdet.mobile.testdata.ExpectedMessages;
import com.sdet.mobile.testdata.Users;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class CheckoutTests extends BaseTest {

    @Test(groups = {"regression"}, description = "Guests are asked to log in when they proceed to checkout")
    public void guestCheckoutRequiresLogin() {
        CartPage cart = openApp().openProductDetails(Product.BACKPACK).addToCart().openCart();

        LoginPage login = cart.proceedToCheckoutAsGuest();

        assertTrue(login.isLoaded(), "Login screen should be displayed for a guest who proceeds to checkout");
    }

    @Test(groups = {"regression"}, description = "Submitting an empty address form shows validation errors and blocks checkout")
    public void emptyAddressFormShowsValidationErrors() {
        CheckoutAddressPage address = openApp()
                .openLoginScreen()
                .loginToCatalog(Users.VALID)
                .openProductDetails(Product.BACKPACK)
                .addToCart()
                .openCart()
                .proceedToCheckout();

        address.submitEmptyForm();

        assertFalse(address.getValidationErrors().isEmpty(), "Validation messages should be displayed for required fields");
        assertTrue(address.isLoaded(), "User should stay on the address form until it is valid");
    }

    @Test(groups = {"smoke", "regression"}, description = "A logged-in user can complete checkout with valid address and payment data")
    public void loggedInUserCanCompleteCheckout() {
        CheckoutCompletePage complete = openApp()
                .openLoginScreen()
                .loginToCatalog(Users.VALID)
                .openProductDetails(Product.BACKPACK)
                .addToCart()
                .openCart()
                .proceedToCheckout()
                .submitAddress(CheckoutData.ADDRESS)
                .submitPayment(CheckoutData.CARD)
                .placeOrder();

        assertTrue(complete.getTitle().contains(ExpectedMessages.CHECKOUT_COMPLETE_TITLE),
                "Confirmation title was '" + complete.getTitle() + "'");
        assertFalse(complete.getThankYouMessage().isBlank(), "A thank-you message should be displayed");
    }
}
