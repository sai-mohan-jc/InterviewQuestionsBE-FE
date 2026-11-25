package com.banking.notification.dto;

public class Customer {

    private String cif;
    private String name;

    public Customer(String cif, String name) {
        this.cif = cif;
        this.name = name;
    }

    public String getCif() {
        return cif;
    }

    public String getName() {
        return name;
    }
}