public class RiceCategory extends FoodCategory
{
    
    public RiceCategory() 
    {
        super("Rice"); //Category name
    }

    @Override
    protected void loadItems()
    {
        //Rice dishes specified
        itemList.add(new FoodItem("Mixed Fried Rice", 8.50,"MixedFriedRice.jpg"));
        itemList.add(new FoodItem("Thai Fried Rice", 9.00,"ThaiFriedRice.jpg"));
        itemList.add(new FoodItem("Braised Chicken Rice", 10.00,"BraisedChickenRice.jpg"));
    }
}