public class DrinksCategory extends FoodCategory 
{
    public DrinksCategory() 
    {
        super("Drinks"); //Category name
    }

    @Override
    protected void loadItems() 
    {
        //Drinks specified
        itemList.add(new FoodItem("Iced Lemon Tea", 4.00,"IcedLemonTea.jpg"));
        itemList.add(new FoodItem("Milo Ais", 3.50,"MiloAis.jpeg"));
        itemList.add(new FoodItem("Coca-Cola", 2.50,"CocaCola.png"));
        itemList.add(new FoodItem("Orange Juice", 4.50,"OrangeJuice.jpg"));
        itemList.add(new FoodItem("Mineral Water", 0.50,"MineralWater.jpg"));
    }
}