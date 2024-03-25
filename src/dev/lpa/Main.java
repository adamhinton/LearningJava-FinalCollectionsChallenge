package dev.lpa;

import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {

    }
}

class Store{

    public static void main(String[] args) {
    }


    private static Random random = new Random();



    private Map<String, InventoryItem> inventory;

    private NavigableSet<Cart> carts = new TreeSet<>(Comparator.comparing(Cart::getId));

    private Map<ProductCategory, Map<String, InventoryItem>> aisleInventory;


    private void manageStoreCarts(){}

    private boolean checkOutCart(Cart cart){
        return false;
    }

    private void abandonCarts(){}

    private void listProductsByCategory(){

    }

    private void stockStore(){
        inventory = new HashMap<>();
        List<Product> products = new ArrayList<>(List.of(
                new Product("A100","apple","local",ProductCategory.PRODUCE),
                new Product("B100","banana","local",ProductCategory.PRODUCE),
                new Product("P100","pear","local",ProductCategory.PRODUCE),
                new Product("L103","lemon","local",ProductCategory.PRODUCE),
                new Product("M201","milk","farm",ProductCategory.DAIRY),
                new Product("Y001","yogurt","farm",ProductCategory.DAIRY),
                new Product("C333","cheese","farm",ProductCategory.DAIRY),
                new Product("R777","rice chex","Nabisco",ProductCategory.CEREAL),
                new Product("G111","granola","Nat Valley",ProductCategory.CEREAL),
                new Product("BB11","ground beef","butcher",ProductCategory.MEAT),
                new Product("CC11","chicken","butcher",ProductCategory.MEAT),
                new Product("BC11","bacon","butcher",ProductCategory.MEAT),
                new Product("BC77","coke","coca cola",ProductCategory.BEVERAGE),
                new Product("BC88","coffee","value",ProductCategory.BEVERAGE),
                new Product("BC99","tea","herbal",ProductCategory.BEVERAGE)
        ));

        products.forEach(p -> inventory.put(p.sku(), new InventoryItem(p, random.nextDouble(0, 1.25), 1000, 5)));


    }

    private void stockAIsles(){}

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

        if (!item.reserveItem(qty)){
            System.out.println("Could not add item. You suck!");
        }
    }

    public void removeItem(InventoryItem item, int qty){
        int current = products.get(item.getProduct().sku());

        if(current <= qty){
            qty = current;
            products.remove(item.getProduct().sku());
            System.out.printf("Item [%s] removed from basket %n", item.getProduct().name());
        }
        else{
            products.merge(item.getProduct().sku(), qty, (oldVal, newVal) -> oldVal - newVal);
            System.out.printf("%d [%s]s removed%n", qty, item.getProduct().name());
        }
        item.releaseItem(qty);
    }



    // Flesh this out later
    public void printSalesSlip(Map<String, InventoryItem> inventory){

        double total = 0;

        System.out.println("-".repeat(30));
        System.out.println("Thank you for your sale: ");

        for(var cartItem : products.entrySet()){

            var item = inventory.get(cartItem.getKey());
            int qty = cartItem.getValue();
            double itemizedPrice = (item.getPrice() * qty);
            total += itemizedPrice;
            System.out.printf("\t%s %-10s (%d)@ $%.2f = $%.2f%n", cartItem.getKey(), item.getProduct().name(), qty,
                    item.getPrice(), itemizedPrice);
        }

        System.out.printf("Total Sale: $%.2f%n", total);
        System.out.println("-".repeat(30));


    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", cartDate=" + cartDate +
                ", products=" + products +
                '}';
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

    public InventoryItem(Product product, double price, int qtyTotal, int qtyLow) {
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