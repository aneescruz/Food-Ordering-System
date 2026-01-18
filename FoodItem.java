public class FoodItem 
{
    private String name;
    private double price;
    private double averageRating; 
    private int totalRatings;
    private int ratingSum;
    private String imagePath;  

    public FoodItem(String name, double price, String imagePath) 
    {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.averageRating = 0.0;
        this.totalRatings = 0;
        this.ratingSum = 0;
    }

    public void addRating(int rating) 
    {
        ratingSum += rating;
        totalRatings++;
        averageRating = (double) ratingSum / totalRatings;
    }

    // Getters
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getAverageRating() { return averageRating; }
    public String getImagePath() { return imagePath; }  

    public Object getTotalRatings() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalRatings'");
    }
}