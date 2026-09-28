package com.sdet.mobile.tests;

import com.sdet.mobile.model.Credentials;
import com.sdet.mobile.pages.LoginPage;
import com.sdet.mobile.pages.ProductsPage;
import com.sdet.mobile.testdata.ExpectedMessages;
import com.sdet.mobile.testdata.Users;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class AuthenticationTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "A valid user can log in from the menu and lands on the Products screen")
    public void validLoginShowsProductsScreen() {
        ProductsPage products = openApp()
                .openLoginScreen()
                .loginToCatalog(Users.VALID);

        assertTrue(products.isLoaded(), "Products screen should be displayed after a successful login");
        assertTrue(products.isUserLoggedIn(), "Menu should offer 'Log Out' for a logged-in user");
    }

    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][]{
                {"missing username", Users.MISSING_USERNAME, ExpectedMessages.USERNAME_REQUIRED},
                {"missing password", Users.MISSING_PASSWORD, ExpectedMessages.PASSWORD_REQUIRED},
                {"locked out user", Users.LOCKED_OUT, ExpectedMessages.USER_LOCKED_OUT},
        };
    }

    @Test(groups = {"smoke", "regression"}, dataProvider = "invalidLogins",
            description = "Invalid credentials show a validation message and keep the user on the login screen")
    public void invalidLoginShowsValidationMessage(String scenario, Credentials credentials, String expectedMessage) {
        LoginPage login = openApp()
                .openLoginScreen()
                .loginExpectingError(credentials);

        List<String> messages = login.getErrorMessages();
        assertTrue(messages.stream().anyMatch(message -> message.contains(expectedMessage)),
                "Expected a message containing '" + expectedMessage + "' but found " + messages);
        assertTrue(login.isLoaded(), "User should remain on the login screen");
    }

    @Test(groups = {"regression"}, description = "Logging out returns the app to the Login screen")
    public void logoutReturnsToLoggedOutState() {
        LoginPage login = openApp().openLoginScreen().loginToCatalog(Users.VALID).logout();

        assertTrue(login.isLoaded(), "Login screen should be displayed after logout");
    }
}
