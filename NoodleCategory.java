public class NoodleCategory extends FoodCategory {
    public NoodleCategory() {
        super("Noodle"); // Category name
    }

    @Override
    protected void loadItems() {
        itemList.add(new FoodItem("Beef Noodles", 9.50, "BeefNoodles.jpg"));
        itemList.add(new FoodItem("Fried Chicken Noodles", 9.00, "NoodlesCategory.jpg"));
        itemList.add(new FoodItem("Braised Lamb Noodles", 10.50, "SpicyCuminLambNoodles.jpg"));
    }
}