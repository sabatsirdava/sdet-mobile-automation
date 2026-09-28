package com.sdet.mobile.tests;

import com.sdet.mobile.model.Product;
import com.sdet.mobile.pages.CartPage;
import com.sdet.mobile.pages.LoginPage;
import com.sdet.mobile.pages.ProductDetailsPage;
import com.sdet.mobile.pages.ProductsPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class NavigationTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "The drawer menu navigates between Catalog and Login screens")
    public void menuNavigatesBetweenCatalogAndLogin() {
        ProductsPage products = openApp();

        LoginPage login = products.openLoginScreen();
        assertTrue(login.isLoaded(), "Login screen should be displayed after choosing 'Log In'");

        ProductsPage backToCatalog = login.openCatalogViaMenu();
        assertTrue(backToCatalog.isLoaded(), "Products screen should be displayed after choosing 'Catalog'");
    }

    @Test(groups = {"regression"}, description = "Product details show the selected product and back returns to the catalogue")
    public void productDetailsMatchSelectedProduct() {
        ProductsPage products = openApp();

        ProductDetailsPage details = products.openProductDetails(Product.BACKPACK);
        assertEquals(details.getProductName(), Product.BACKPACK.displayName(), "Product name on details screen");
        assertTrue(details.getProductPrice().contains(Product.BACKPACK.price()),
                "Product price should match the selected product");

        assertTrue(details.goBackToProducts().isLoaded(), "Back should return to the Products screen");
    }

    @Test(groups = {"regression"}, description = "A fresh session shows the empty-cart state")
    public void emptyCartShowsEmptyState() {
        CartPage cart = openApp().openCart();

        assertTrue(cart.isEmpty(), "Cart should show its empty state before anything is added");
        assertEquals(cart.getItemCount(), 0, "Empty cart should list no items");
    }
}
