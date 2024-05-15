package org.example;

import jakarta.persistence.Entity;

@Entity
public class Customer extends Company{
    private double discount;
    public Customer(){};

    public Customer(String companyName, String city, String street, String zipCode, double discount){
        super(companyName, city, street, zipCode);
        this.discount = discount;
    }
}
