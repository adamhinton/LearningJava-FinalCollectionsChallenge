package dev.lpa;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
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


class Cart {
    enum CartType{
        PHYSICAL, VIRTUAL
    }


    private static int lastId = 1;

    private int id;

    private LocalDate cartDate;

    private CartType type;

    // keyed by sku
    private Map<String, Integer> products;

//     Add item to cart
    public void addItem (InventoryItem item, int qty){
        products.merge(item.getProduct().sku(), qty, Integer::sum);
    }

    public void removeItem(Product product){
        products.remove(product);
    }

    // Flesh this out later
    public void printSalesSlip(){

    }

    public Cart(CartType cartType, int days) {
        this.type = type;
        this.cartDate = LocalDate.now().minusDays(days);
        id = lastId++;

        products = new HashMap<>();
    }

    public Cart(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public LocalDate getCartDate() {
        return cartDate;
    }



}



class InventoryItem{

    private Product product;
    private int qtyTotal;
    private int qtyReserved;
    private int qtyReorder;
    private int qtyLow;
    private double price;

    public InventoryItem(Product product, int qtyTotal, int qtyLow, double price) {
        this.product = product;
        this.qtyTotal = qtyTotal;
        this.qtyLow = qtyLow;
        this.price = price;
        this.qtyReorder = qtyTotal;
    }

    public Product getProduct() {
        return product;
    }

    public double getPrice() {
        return price;
    }


    // Adding items to shopping carts
    public boolean reserveItem(int numItems){
        if ((qtyTotal - qtyReserved)  >= numItems) {
            qtyReserved+= numItems;
            return true;
        }
        return false;
    }

    // Shopper removes item from cart
    public void releaseItem(int numItems) {
        qtyReserved-= numItems;
    }

    public boolean sellItem(int numItems){

        if (qtyTotal >= numItems){
            qtyTotal -= numItems;
            qtyReserved -= numItems;
            if(qtyTotal <= qtyLow){
                placeInventoryOrder();
            }
            return true;
        }
        return false;

    }

    private void placeInventoryOrder(){
        System.out.printf("Ordering qty %d : %s%n", qtyReorder, product);
    }

    @Override
    public String toString() {
        return "%s, $%.2f : [%04d,% 2d]".formatted(product, price, qtyTotal, qtyReserved);
    }


}