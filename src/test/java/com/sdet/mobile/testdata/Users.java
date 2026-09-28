package com.sdet.mobile.testdata;

import com.sdet.mobile.model.Credentials;

/** Public demo accounts of Sauce Labs My Demo App (documented in the app's own repository; not secrets). */
public final class Users {

    public static final Credentials VALID = new Credentials("bob@example.com", "10203040");
    public static final Credentials LOCKED_OUT = new Credentials("alice@example.com", "10203040");
    public static final Credentials MISSING_USERNAME = new Credentials("", "10203040");
    public static final Credentials MISSING_PASSWORD = new Credentials("bob@example.com", "");

    private Users() {
    }
}
