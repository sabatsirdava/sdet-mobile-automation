package com.sdet.mobile.tests;

import com.sdet.mobile.model.Product;
import com.sdet.mobile.pages.CartPage;
import com.sdet.mobile.pages.ProductDetailsPage;
import com.sdet.mobile.pages.ProductsPage;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CartTests extends BaseTest {

    private static final int ONE_PRODUCT = 1;
    private static final int TWO_PRODUCTS = 2;

    @Test(groups = {"smoke", "regression"}, description = "A product added from its details screen appears in the cart")
    public void addedProductAppearsInCart() {
        ProductDetailsPage details = openApp().openProductDetails(Product.BACKPACK).addToCart();
        assertEquals(details.getCartBadgeCount(), ONE_PRODUCT, "Cart badge after adding one product");

        CartPage cart = details.openCart();

        assertEquals(cart.getProductNames(), List.of(Product.BACKPACK.displayName()), "Products listed in the cart");
        assertTrue(cart.getProductPrices().get(0).contains(Product.BACKPACK.price()),
                "Cart price should contain " + Product.BACKPACK.price() + " but was " + cart.getProductPrices());
    }

    @Test(groups = {"regression"}, description = "Several different products are counted and listed in the cart")
    public void multipleProductsAreCountedAndListed() {
        ProductsPage products = openApp();
        products.openProductDetails(Product.BACKPACK).addToCart().goBackToProducts();
        ProductDetailsPage second = products.openProductDetails(Product.BIKE_LIGHT).addToCart();
        assertEquals(second.getCartBadgeCount(), TWO_PRODUCTS, "Cart badge after adding two products");

        CartPage cart = second.openCart();

        assertEquals(cart.getItemCount(), TWO_PRODUCTS, "Number of rows in the cart");
        assertTrue(cart.getProductNames().containsAll(
                        List.of(Product.BACKPACK.displayName(), Product.BIKE_LIGHT.displayName())),
                "Cart should contain both products but contained " + cart.getProductNames());
    }

    @Test(groups = {"regression"}, description = "Removing the only product empties the cart")
    public void removingOnlyProductEmptiesCart() {
        CartPage cart = openApp().openProductDetails(Product.BACKPACK).addToCart().openCart();
        cart.removeFirstProduct();

        assertTrue(cart.isEmpty(), "Cart should show its empty state after removing the only product");
        assertEquals(cart.getCartBadgeCount(), 0, "Cart badge should disappear for an empty cart");
    }
}
