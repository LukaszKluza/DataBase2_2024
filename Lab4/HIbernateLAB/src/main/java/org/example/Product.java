package org.example;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String productName;
    private int unitInStock;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "SupplierID")
    private Supplier supplier;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "CategoryID")
    private Category category;

    @ManyToMany(mappedBy = "products")
    private final Set<Invoice> invoices = new HashSet<>();

    public Product(){}
    public Product(String productName, int unitsInStock){
        this.productName = productName;
        this.unitInStock = unitsInStock;
    }

    public void setSupplier(Supplier supplier){
        this.supplier = supplier;
    }

    public Supplier getSupplier(){
        return supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName(){
        return productName;
    }

    public void addInvoice(Invoice invoice){
        invoices.add(invoice);
    }
    public int getUnitInStock(){
        return unitInStock;
    }
    public void updateUnitInStock(int unitInStock){
        this.unitInStock = unitInStock;
    }
    @Override
    public String toString() {
        return "Product{" +
                "productName='" + productName + '\'' +
                ", unitInStock=" + unitInStock +
                ", supplier=" + supplier +
                ", category=" + category.getName() +
                ", invoices=" + invoices.stream()
                    .map(Invoice::getInvoiceNumber).toList()+
                '}';
    }
}
