package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(generator = "auto")
    private int id;
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> productList = new ArrayList<>();
    public Category (){}
    public Category(String name){
        this.name = name;
    }

    public void add(Product product){
        productList.add(product);
    }
    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", productList=" + productList.stream().map(Product::getName).toList() +
                '}';
    }
}
