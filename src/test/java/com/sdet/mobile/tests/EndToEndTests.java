package com.sdet.mobile.tests;

import com.sdet.mobile.model.Product;
import com.sdet.mobile.pages.CartPage;
import com.sdet.mobile.pages.CheckoutCompletePage;
import com.sdet.mobile.pages.ProductsPage;
import com.sdet.mobile.testdata.CheckoutData;
import com.sdet.mobile.testdata.ExpectedMessages;
import com.sdet.mobile.testdata.Users;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class EndToEndTests extends BaseTest {

    private static final int ONE_PRODUCT = 1;
    private static final int TWO_PRODUCTS = 2;

    @Test(groups = {"e2e", "regression"}, description = "Guest buys a product end to end, is asked to log in during checkout, then logs out")
    public void guestCheckoutPurchaseJourney() {
        ProductsPage products = openApp();
        products.openProductDetails(Product.BACKPACK).addToCart().goBackToProducts();
        CartPage cart = products.openProductDetails(Product.BIKE_LIGHT).addToCart().openCart();
        assertEquals(cart.getItemCount(), TWO_PRODUCTS, "Both products should be in the cart");

        cart.removeFirstProduct();
        assertEquals(cart.getItemCount(), ONE_PRODUCT, "One product should remain after removing one");

        CheckoutCompletePage complete = cart.proceedToCheckoutAsGuest()
                .loginToCheckout(Users.VALID)
                .submitAddress(CheckoutData.ADDRESS)
                .submitPayment(CheckoutData.CARD)
                .placeOrder();
        assertTrue(complete.getTitle().contains(ExpectedMessages.CHECKOUT_COMPLETE_TITLE),
                "Confirmation title was '" + complete.getTitle() + "'");

        ProductsPage afterOrder = complete.continueShopping();
        assertEquals(afterOrder.getCartBadgeCount(), 0, "Cart should be empty after the order is placed");

        afterOrder.logout();
        assertFalse(afterOrder.isUserLoggedIn(), "User should be logged out at the end of the journey");
    }
}
