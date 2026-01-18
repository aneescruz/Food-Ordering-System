import java.util.ArrayList;
import java.util.List;

public class Cart 
{
    private List<FoodItem> items = new ArrayList<>();
    public void addItem(FoodItem item) {items.add(item);} //Adds item to cart
    
    public void removeItem(FoodItem item) {items.remove(item);} //Removes item from cart
    
    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            System.out.println("Item removed from cart.");
        } else {
            System.out.println("Invalid item number.");
        }
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("\n===== Your Cart =====");
            for (int i = 0; i < items.size(); i++) {
                FoodItem item = items.get(i);
                System.out.printf("%d. %s - RM%.2f%n", i + 1, item.getName(), item.getPrice());
            }
        }
    }

    public List<FoodItem> getItems() {return items;} //Returns list of items from cart
    public double getTotal() 
    {
        double total = 0;
        for (FoodItem item : items) total += item.getPrice();
        return total;
        //Totals price of items within cart
    }

    public void clearCart() {items.clear();}
    //Clears cart

    public boolean isEmpty() {return items.isEmpty();}
    //Checks if cart is empty
    
    public void displayItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayItems'");
    }
}