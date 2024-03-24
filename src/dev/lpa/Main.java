package dev.lpa;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

    }
}

class Store{

    public static void main(String[] args) {

    }

    private Set<InventoryItem> inventory;

    private HashMap<LocalDate, Cart> carts;

    // Keyed by product category
    // Not sure I did this right
    private HashMap<String, InventoryItem> aisleInventory;




    public void listProductsByCategory(){

    }

}

enum ProductCategory{

    PRODUCE, DAIRY, CEREAL, MEAT, BEVERAGE
}

record Product (String sku, String name, String mfgr, ProductCategory category){ }

enum CartType{
    PHYSICAL, VIRTUAL
}

class Cart {
    public static int ID = 1;

    private int id;

    private Set<Product> products;

    private LocalDate date;

    private CartType type;

    // Add item to cart
    public void addItem (Product product){
        if (products.contains(product)){
            System.out.println("Product already in cart");
            return;
        }
        else{
            products.add(product);
        }
    }

    public void removeItem(Product product){
        products.remove(product);
    }

    // Flesh this out later
    public void printSalesSlip(){

    }

    public Cart(Set<Product> products, LocalDate date) {
        this.products = products;
        this.date = LocalDate.now();
        // Not sure I did this right
        this.id = ID;
        ID++;
    }
}



class InventoryItem{

    private Product product;
    private int qtyTotal;
    private int qtyReserved;
    private int qtyReorder;
    private int qtyLow;
    private double salesPrice;

    public InventoryItem(Product product, int qtyTotal, int qtyReorder, int qtyLow, double salesPrice) {
        this.product = product;
        this.qtyTotal = qtyTotal;
        this.qtyReorder = qtyReorder;
        this.qtyLow = qtyLow;
        this.salesPrice = salesPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQtyTotal() {
        return qtyTotal;
    }

    public int getQtyReserved() {
        return qtyReserved;
    }

    public double getSalesPrice() {
        return salesPrice;
    }


    public void reserveItem(int numItems){
        qtyReserved += numItems;
    }

    // Not sure what this is supposed to be
    // Maybe release item from reservation?
    public void releaseItem(int numItems) {
        qtyReserved-= numItems;
    }

    public void sellItem(int numItems){

        if (qtyTotal <= numItems){
            System.out.println("Insufficient inventory");
            return;
        }

        qtyTotal -= numItems;

        if(qtyTotal <= qtyLow){
            placeInventoryOrder();
        }
    }

    public void placeInventoryOrder(){
        qtyTotal += qtyReorder;
    }

}