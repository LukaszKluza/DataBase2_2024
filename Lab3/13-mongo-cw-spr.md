**Imiona i nazwiska autorów:**
Łukasz Kluza, Mateusz Sacha
--- 

## Zadanie 2  - rozwiązanie

### a)

#### Product.cs
```cs
public class Product {
    public int ProductID { get; set; }
    public Supplier? Supplier { get; set; }
    public String? ProductName { get; set; } 
    public int UnitsInStock { get; set; }
}
```

#### Suppliers.cs
```cs
public class Supplier {
    public int SupplierID { get; set; }
    public String? CompanyName { get; set; } 
    public String? Street { get; set; }
    public String? City { get; set; }
}
```

#### ProdContext.cs
```cs
using Microsoft.EntityFrameworkCore;
public class ProdContext : DbContext
{
    public DbSet<Product> Products { get; set; }
    public DbSet<Supplier> Suppliers { get; set; }
    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        base.OnConfiguring(optionsBuilder);
        optionsBuilder.UseSqlite("Datasource=MyProductDatabase");
    }
}
```

#### Program.cs
```cs
using System;
using System.Linq;

class Program {
    static void Main() { 
        ProdContext prodContext = new ProdContext();
        Console.WriteLine("Do you want to add new product (yes/no): ");
        String? response = Console.ReadLine();
        if(response == "yes") {
            Product product = CreateProduct();
            prodContext.Products.Add(product);
            prodContext.SaveChanges();
        }
       
        Supplier supplier = CreateSupplier();
        prodContext.Add(supplier);

        var lastProduct = prodContext.Products
            .OrderByDescending(prod => prod.ProductID)
            .FirstOrDefault();

        if(lastProduct != null) lastProduct.Supplier = supplier;

        prodContext.SaveChanges();

        var query = from prod in prodContext.Products
            select new
            {
                prod.ProductID,
                prod.ProductName,
                SupplierName = prod.Supplier != null ? prod.Supplier.CompanyName : "Unknown"
            };

        foreach (var prod in query) {
            Console.WriteLine($"[{prod.ProductID}] | {prod.ProductName} | {prod.SupplierName}");
        }
    }

    private static Product CreateProduct(){
        Console.WriteLine("Write new product name: ");
        String? prodName = Console.ReadLine();
        Product product = new Product { ProductName = prodName };
        Console.WriteLine("Write new product units in stock: ");
        String? units = Console.ReadLine();
        if(units != null) {
            int prodUnits = Int32.Parse(units);
            product.UnitsInStock = prodUnits;
        }
        return product;
    }

    private static Supplier CreateSupplier(){
        Console.WriteLine("Write new supplier name: ");
        String? suppName = Console.ReadLine();
        Supplier supplier = new Supplier { CompanyName = suppName };
        Console.WriteLine("Write new supplier street: ");
        String? suppStreet = Console.ReadLine();
        supplier.Street = suppStreet;
        Console.WriteLine("Write new supplier city: ");
        String? suppCity = Console.ReadLine();
        supplier.City = suppCity;
        return supplier;
    }
}
```
Przykład działania z dodawaniem nowego produktu:
![alt text](image-3.png)

Przykład działania bez dodawania nowego produktu:
![alt text](image-4.png)

Diagram bazy danych:
![alt text](image-2.png)

Tabela Products w bazie danych:
![alt text](image-5.png)

Tabela Suppliers w bazie danych:
![alt text](image-6.png)

---

### b)

#### Product.cs
```cs
public class Product {
    public int ProductID { get; set; }
    public String? ProductName { get; set; } 
    public int UnitsInStock { get; set; }
}
```

#### Supplier.cs
```cs
public class Supplier {
    public int SupplierID { get; set; }
    public String? CompanyName { get; set; } 
    public String? Street { get; set; }
    public String? City { get; set; }
    public ICollection<Product> Products { get; set;} = new List<Product>();

    public override string ToString()
    {
        if (CompanyName != null)return CompanyName;
        else return "Unknow";
    }
}
```

#### Program.cs

Funkcje CreateProduct oraz CreateSupplier nie uległy zmianie
```cs
static void Main() { 
    ProdContext prodContext = new ProdContext();

    Supplier supplier = CreateSupplier();
    prodContext.Add(supplier);
    prodContext.SaveChanges();

    Console.WriteLine("How many products do you want to add: ");
    String? response = Console.ReadLine();
    int num = 0;
    if(response != null) num = Int32.Parse(response);

    while(num > 0) {
        Product product = CreateProduct();
        prodContext.Products.Add(product);
        supplier.Products.Add(product);
        prodContext.SaveChanges();
        num--;
    }
    
    var query = from prod in prodContext.Products
        select new
        {
            prod.ProductID,
            prod.ProductName,
        };

    foreach (var prod in query) {
        Console.WriteLine($"[{prod.ProductID}] | {prod.ProductName}");
    }
}
```
Przykład działania:
![alt text](image-7.png)

Diagram bazy danych:
![alt text](image-8.png)
Jak widać diagram nie uległ zmianie.

Tabela Products w bazie danych:
![alt text](image-9.png)

Tabela Suppliers w bazie danych:
![alt text](image-10.png)

---

### c)

#### Product.cs
```cs
public class Product {
    public int ProductID { get; set; }
    public Supplier? Supplier { get; set; }
    public String? ProductName { get; set; } 
    public int UnitsInStock { get; set; }
}
```

#### Suppleir.cs
Klasa ta wygląda tak samo jak w punckie b.

#### Program.cs
Klasa ta wygląda tak samo jak w punkcie b.

Przykład działania:
![alt text](image-11.png)

Diagram bazy danych:
![alt text](image-12.png)
Jak widać diagram znów nie uległ zmianie.

Tabela Products w bazie danych:
![alt text](image-13.png)

Tabela Suppliers w bazie danych:
![alt text](image-14.png)

---

### d)

#### Product.cs
```cs
public class Product {
    public int ProductID { get; set; }
    public virtual ICollection<InvoiceProduct>? InvoiceProducts { get; set; }
    public String? ProductName { get; set; } 
    public int UnitsInStock { get; set; }
}
```

#### Invoice.cs
```cs
public class Invoice {
    [Key]
    public int InvoiceNumber { get; set; }
    public virtual ICollection<InvoiceProduct>? InvoiceProducts { get; set; }
```

#### InvoiceProduct.cs
```cs
public class InvoiceProduct {
    [Key, Column(Order = 0)]
    public int InvoiceID { get; set; }
    public virtual Invoice Invoice { get; set; }

    [Key, Column(Order = 1)]
    public int ProductID { get; set; }
    public virtual Product Product { get; set; }

    public int Quantity { get; set; }
}
}
```

#### ProdContext.cs
```cs
public class ProdContext : DbContext
{
    public DbSet<Product> Products { get; set; }
    public DbSet<Invoice> Invoices { get; set; }
    public DbSet<InvoiceProduct> InvoiceProducts { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<InvoiceProduct>()
            .HasKey(ip => new { ip.InvoiceID, ip.ProductID });
    }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        base.OnConfiguring(optionsBuilder);
        optionsBuilder.UseSqlite("Datasource=MyProductDatabase");
    }
}
```

#### Program.cs
```cs
class Program
{
    static void Main()
    {
        ProdContext prodContext = new ProdContext();

        Console.WriteLine("How many products do you want to add: ");
        string? response = Console.ReadLine();
        int num = 0;
        if (response != null) num = Int32.Parse(response);

        while (num > 0)
        {
            Product product = CreateProduct();
            prodContext.Products.Add(product);
            prodContext.SaveChanges();
            num--;
        }

        Console.WriteLine("How many transactions do you want to add: ");
        response = Console.ReadLine();
        num = 0;
        if (response != null) num = Int32.Parse(response);

        while (num > 0)
        {
            Console.WriteLine("Listing all products with their units in stock");
            var query = from prod in prodContext.Products
                        select new
                        {
                            prod.ProductID,
                            prod.ProductName,
                            prod.UnitsInStock,
                        };

            foreach (var prod in query)
            {
                Console.WriteLine($"[{prod.ProductID}] | {prod.ProductName} | {prod.UnitsInStock}");
            }

            Invoice invoice = CreateInvoice(prodContext);
            prodContext.Invoices.Add(invoice);
            prodContext.SaveChanges();
            num--;
        }

        Console.WriteLine("Enter the invoice number: ");
        int invoiceNumber = int.Parse(Console.ReadLine());
        ShowProductsSoldInInvoice(prodContext, invoiceNumber);

        Console.WriteLine("Enter the product ID: ");
        int productID = int.Parse(Console.ReadLine());
        ShowInvoicesForProduct(prodContext, productID);
    }

    private static Product CreateProduct()
    {
        Console.WriteLine("Write new product name: ");
        string? prodName = Console.ReadLine();
        Product product = new Product { ProductName = prodName };
        Console.WriteLine("Write new product units in stock: ");
        string? units = Console.ReadLine();
        if (units != null)
        {
            int prodUnits = Int32.Parse(units);
            product.UnitsInStock = prodUnits;
        }
        return product;
    }

    private static Invoice CreateInvoice(ProdContext prodContext)
    {
        Invoice invoice = new Invoice();
        invoice.InvoiceProducts = new List<InvoiceProduct>();

        Console.WriteLine("How many products do you want to buy: ");
        string? response = Console.ReadLine();
        int num = 0;
        if (response != null) num = Int32.Parse(response);

        while (num > 0)
        {
            Console.WriteLine("Write index of product you want to buy: ");
            string? index = Console.ReadLine();
            if (index != null)
            {
                int i = Int32.Parse(index);
                Console.WriteLine("Write how much of product you want to buy: ");
                string? count = Console.ReadLine();
                if (count != null)
                {
                    int c = Int32.Parse(count);
                    var product = prodContext.Products.FirstOrDefault(prod => prod.ProductID == i);
                    if (product != null)
                    {
                        if (product.UnitsInStock >= c)
                        {
                            InvoiceProduct invoiceProduct = new InvoiceProduct
                            {
                                Product = product,
                                Quantity = c
                            };
                            invoice.InvoiceProducts.Add(invoiceProduct);

                            product.UnitsInStock -= c;
                            Console.WriteLine("Product added to transaction.");
                            num--;
                        }
                        else
                        {
                            Console.WriteLine("There is not enough product units in stock.");
                        }
                    }
                    else
                    {
                        Console.WriteLine("Product with this index doesn't exist.");
                    }
                }
                else
                {
                    Console.WriteLine("That is not a number.");
                }
            }
        }

        return invoice;
    }

    static void ShowProductsSoldInInvoice(ProdContext prodContext, int invoiceNumber)
    {
        var productsInInvoice = prodContext.InvoiceProducts
            .Include(ip => ip.Product)
            .Where(ip => ip.InvoiceNumber == invoiceNumber)
            .ToList();

        Console.WriteLine($"\nProducts sold within the invoice {invoiceNumber}:");
        foreach (var item in productsInInvoice)
        {
            Console.WriteLine($"- Product ID: {item.ProductID}, Name: {item.Product.ProductName}, Quantity: {item.Quantity}");
        }
    }

    static void ShowInvoicesForProduct(ProdContext prodContext, int productID)
    {
        var invoicesForProduct = prodContext.InvoiceProducts
            .Include(ip => ip.Invoice)
            .Where(ip => ip.ProductID == productID)
            .Select(ip => ip.InvoiceNumber)
            .Distinct()
            .ToList();

        Console.WriteLine($"\nInvoices in which the product with ID {productID} was sold:");
        foreach (var invoiceNumber in invoicesForProduct)
        {
            Console.WriteLine($"- Invoice Number: {invoiceNumber}");
        }
    }
}
```

Przykład działania:
![alt text](image-15.png)
![alt text](image-16.png)
![alt text](image-17.png)
![alt text](image-18.png)

Diagram bazy danych:
![alt text](image-19.png)

Tabela Products w bazie danych:
![alt text](image-20.png)

Tabela Invoices w bazie danych:
![alt text](image-21.png)

Tabela InvoiceProducts w bazie danych:
![alt text](image-22.png)

---
