import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionLogger {

    private static final String FILE_NAME = "transactions.txt";

    public static void logTransaction(Session session, boolean isDineIn, String paymentMethod) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) 
        {
            writer.write("=== New Transaction ===\n");

            writer.write("Time: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n");

            writer.write("Order Type: " + session.getOrderType() + "\n");

            if (session.getOrderType().equalsIgnoreCase("Dine-in")) 
            {
                writer.write("Table Number: " + session.getIdentifier() + "\n");
            } 
            else if ((session.getOrderType().equalsIgnoreCase("Takeaway")))
            {
                writer.write("Phone Number: " + session.getIdentifier() + "\n");
            }


            writer.write("Payment Method: " + paymentMethod + "\n");

            writer.write("Items:\n");

            // Count quantities without changing cart structure
            Map<String, Integer> itemCounts = new HashMap<>();
            Map<String, Double> itemPrices = new HashMap<>();
            List<FoodItem> items = session.getCart().getItems();  // assuming cart stores List<FoodItem>

            for (FoodItem item : items) {
                itemCounts.put(item.getName(), itemCounts.getOrDefault(item.getName(), 0) + 1);
                itemPrices.put(item.getName(), item.getPrice()); // just remember latest price
            }

            for (String name : itemCounts.keySet()) {
                int qty = itemCounts.get(name);
                double price = itemPrices.get(name);
                writer.write(" - " + name + " x" + qty + " @ RM" + price + "\n");
            }

            writer.write("Total: RM" + session.getCart().getTotal() + "\n");
            writer.write("=======================\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
