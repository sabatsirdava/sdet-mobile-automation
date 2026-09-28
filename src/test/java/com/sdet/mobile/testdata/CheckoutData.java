package com.sdet.mobile.testdata;

import com.sdet.mobile.model.PaymentCard;
import com.sdet.mobile.model.ShippingAddress;

public final class CheckoutData {

    public static final ShippingAddress ADDRESS = new ShippingAddress(
            "Test User",
            "123 Test Street",
            "Tbilisi",
            "Tbilisi",
            "0108",
            "Georgia");

    public static final PaymentCard CARD = new PaymentCard(
            "Test User",
            "3258128178923358",
            "03/30",
            "123");

    private CheckoutData() {
    }
}
