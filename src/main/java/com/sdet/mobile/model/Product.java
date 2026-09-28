package com.sdet.mobile.model;

public enum Product {
    BACKPACK("Sauce Labs Backpack", "29.99"),
    BIKE_LIGHT("Sauce Labs Bike Light", "9.99");

    private final String displayName;
    private final String price;

    Product(String displayName, String price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String displayName() {
        return displayName;
    }

    public String price() {
        return price;
    }
}
