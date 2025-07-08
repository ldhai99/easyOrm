package io.github.ldhai99.easyOrm.service.test.entity;

import io.github.ldhai99.easyOrm.annotation.Embeddable;

@Embeddable
public class Address {
    private String street;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String city;
}