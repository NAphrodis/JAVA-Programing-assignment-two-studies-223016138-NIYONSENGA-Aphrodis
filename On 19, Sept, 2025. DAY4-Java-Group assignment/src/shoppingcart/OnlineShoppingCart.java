package shoppingcart;


class Product {
 private String id;
 private String name;
 private double price;
 private int quantity;

 public Product(String productId, String name, double price, int quantity) {
     this.id = productId;
     this.name = name;
     this.price = price;
     this.quantity = quantity;
 }


 public double calculateCost() {
     return price * quantity;
 }

 public String getName() {
     return name;
 }
}


public class OnlineShoppingCart {
 public static void main(String[] args) {

     Product[] products = new Product[3];
     products[0] = new Product("P001", "Laptop", 40000, 1);
     products[1] = new Product("P002", "Mouse", 1000, 2);
     products[2] = new Product("P003", "Keyboard", 3000, 1);

     double totalBill = 0;


     for (Product product : products) {
         double cost = product.calculateCost();
         System.out.println(product.getName() + " cost: " + cost);
         totalBill += cost;
     }

     boolean discountApplied = false;

     
     if (totalBill > 10000) {
         totalBill = totalBill * 0.9; 
         discountApplied = true;
     }

     System.out.println("\nTotal bill after discount (if any): " + totalBill);

     if (discountApplied) {
         System.out.println("A 10% discount was applied.");
     } else {
         System.out.println("No discount was applied.");
     }
 }
}
