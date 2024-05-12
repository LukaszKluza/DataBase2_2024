package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.hibernate.query.Query;

public class Main {

    private static SessionFactory sessionFactory = null;

    public static void main(String[] args) {
        sessionFactory = getSessionFactory();
        Session session = sessionFactory.openSession();
        Supplier ikea = new Supplier("Ikea", "Main Street", "New York");
        Supplier lidl = new Supplier("Lidl", "Second Street", "Miami");
        Supplier aldi = new Supplier("Aldi", "Kaufingerstraße", "Berlin");

        Product tv = new Product("TV", 5);
        Product phone = new Product("Phone", 5);
        Product ball = new Product("Ball", 5);
        Product teddy = new Product("Teddy", 5);

        Category electronics = new Category("Electronics");
        Category toys = new Category("Toys");

        tv.setSupplier(ikea);
        tv.setCategory(electronics);

        phone.setSupplier(ikea);
        phone.setCategory(electronics);

        ball.setSupplier(lidl);
        ball.setCategory(toys);

        teddy.setSupplier(aldi);
        teddy.setCategory(toys);


        ikea.add(tv);
        ikea.add(phone);
        lidl.add(ball);
        aldi.add(teddy);

        electronics.add(tv);
        electronics.add(phone);
        toys.add(ball);
        toys.add(teddy);

        Transaction tx = session.beginTransaction();

        session.persist(tv);
        session.persist(phone);
        session.persist(ball);
        session.persist(teddy);

        session.persist(ikea);
        session.persist(lidl);
        session.persist(aldi);

        session.persist(electronics);
        session.persist(toys);

        tx.commit();
        //SELECT * FROM Product
        Query<Product> products =  session.createQuery("from Product ", Product.class);
        products.list().stream()
                .map(Product::toString)
                .forEach(System.out::println);

        //SELECT * FROM Supplier
        Query<Supplier> suppliers =  session.createQuery("from Supplier ", Supplier.class);
        suppliers.list().stream()
                .map(Supplier::toString)
                .forEach(System.out::println);

        //SELECT * FROM Category
        Query<Category> categories =  session.createQuery("from Category", Category.class);
        categories.list().stream()
                .map(Category::toString)
                .forEach(System.out::println);

        session.close();
    }

    private static SessionFactory getSessionFactory(){
        if (sessionFactory == null){
            Configuration configuration = new Configuration();
            sessionFactory = configuration.configure().buildSessionFactory();
        }
        return sessionFactory;
    }
}