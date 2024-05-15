package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("myDatabaseConfig");
        EntityManager em = emf.createEntityManager();

        Supplier ikea = new Supplier("Ikea", "New York","Main Street", "12-123","US44123456789012345623191234511298");
        Supplier lidl = new Supplier("Lidl", "Miami","Second Street", "10-100","US442334567893902345678901234567890");
        Supplier aldi = new Supplier("Aldi", "Berlin","Kaufingerstraße", "20-100","PL441911567891912345678901234587659");

        Customer zabka = new Customer("Zabka", "Warsaw", "Marszalkowska", "20-100", 0.10);
        Customer abc = new Customer("ABC", "Cracow", "Czarnowiejska", "10-120", 0.15);

        EntityTransaction etx = em.getTransaction();
        etx.begin();
        em.persist(ikea);
        em.persist(lidl);
        em.persist(aldi);

        em.persist(zabka);
        em.persist(abc);

        etx.commit();
        em.close();
    }
}