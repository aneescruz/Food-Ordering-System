import java.util.ArrayList;
import java.util.List;

public abstract class FoodCategory 
{

    protected String categoryName; //Name of category
    protected List<FoodItem> itemList; //List of items under selected category

    public FoodCategory(String categoryName) {
        this.categoryName = categoryName;
        this.itemList = new ArrayList<>();
        loadItems();
    }

    protected abstract void loadItems();

    public List<FoodItem> getItemList() {return itemList;} //Return list of items in category

    public String getCategoryName() {return categoryName;}
}