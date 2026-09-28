package com.sdet.mobile.model;

public record ShippingAddress(String fullName, String addressLine1, String city,
                              String state, String zipCode, String country) {
}
