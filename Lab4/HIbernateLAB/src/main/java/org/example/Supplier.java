package org.example;

import jakarta.persistence.*;


@Entity
public class Supplier extends Company{

    private String bankAccountNumber;
    public Supplier(String companyName, String city, String street, String zipCode, String bankAccountNumber){
        super(companyName, city, street, zipCode);
        this.bankAccountNumber = bankAccountNumber;
    }
    public Supplier() {}

}
