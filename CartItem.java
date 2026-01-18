public class CartItem 
{
    private FoodItem fooditem;
    private int quantity;

    public CartItem(FoodItem item, int quantity) 
    {
        this.fooditem = item;
        this.quantity = quantity;
    }

    public FoodItem getFoodItem() 
    {
        return fooditem;
    }

    public int getQuantity() 
    {
        return quantity;
    }

    public double getTotalPrice() 
    {
        return fooditem.getPrice() * quantity;
    }
}