package org.example;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int invoiceNumber;
    private int quantity;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private final Set<Product> products = new HashSet<>();
    public Invoice(){}
    public void add(Product product, int quantity){
        if(product.getUnitInStock() < quantity){
            throw new IllegalArgumentException("Too small unit in stock: ");
        }
        product.updateUnitInStock(product.getUnitInStock()-quantity);
        products.add(product);
        this.quantity += quantity;
    }
    public int getInvoiceNumber(){
        return invoiceNumber;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "InvoiceNumber=" + invoiceNumber +
                ", Quantity=" + quantity +
                products.stream().map(Product::getName).toList()+
                '}';
    }
}
