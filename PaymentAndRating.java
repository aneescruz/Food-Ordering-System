import java.util.*;

// Main class to handle the entire ordering, payment, and rating process
public class PaymentAndRating {
    // Scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    // Flag to determine if the order is dine-in or takeaway
    private static boolean isDineIn;

    // List to store all food categories (e.g., Rice, Noodle, Drinks)
    private static List<FoodCategory> categories = new ArrayList<>();

    // Shopping cart to hold selected food items
    private static Cart cart = new Cart();

    // Entry point of the program
    public static void main(String[] args) {
        // Display welcome message and order type selection
        System.out.println("Welcome to the Restaurant!");
        System.out.println("Please select your order type:");
        System.out.println("1. Dine-in");
        System.out.println("2. Takeaway");
        System.out.print("Enter your choice: ");
        int orderType = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Determine if it's dine-in based on user input
        isDineIn = (orderType == 1);

        // Load menu categories into the system
        loadMenuCategories();

        // Let the user select food items to order
        takeOrder();

        // If no items were selected, exit the program
        if (cart.isEmpty()) {
            System.out.println("You have not selected any food. Exiting.");
            return;
        }

        // Display the total price of the order
        double orderAmount = cart.getTotal();
        System.out.printf("\nThe total amount of your order is: RM%.2f\n", orderAmount);

        // Ask the user to choose a payment method
        System.out.println("Please select the payment method:");
        System.out.println("1. Cash");
        System.out.println("2. Card");
        System.out.print("Please select an option (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Process payment based on the user's choice
        boolean paymentSuccess = false;
        switch (choice) {
            case 1:
                paymentSuccess = processCashPayment(orderAmount);
                break;
            case 2:
                paymentSuccess = processCardPayment(orderAmount);
                break;
            default:
                System.out.println("Invalid payment method.");
        }

        // After successful payment:If dine-in, prompt user to rate food，Otherwise, show thank-you message
        if (paymentSuccess && isDineIn) {
            System.out.println("\nAs a dine-in customer, please rate your food experience.");
            rateFood();
            displayRatings();
        } else if (paymentSuccess) {
            System.out.println("Thank you for your order!");
        }
    }

    // Load the predefined food categories into the system
    private static void loadMenuCategories() {
        categories.add(new RiceCategory());
        categories.add(new NoodleCategory());
        categories.add(new DrinksCategory());
    }

    // Display the menu and allow the user to select food items
    private static void takeOrder() {
        System.out.println("\n--- Menu ---");
        int count = 1; // Item numbering
        Map<Integer, FoodItem> itemMap = new HashMap<>(); // Maps number to food item

        // Loop through each category and display items
        for (FoodCategory category : categories) {
            System.out.println("[" + category.getCategoryName() + "]");
            for (FoodItem item : category.getItemList()) {
                System.out.printf("%d. %s - RM%.2f\n", count, item.getName(), item.getPrice());
                itemMap.put(count, item); // Store mapping for selection
                count++;
            }
        }

        // Let user add items to cart
        while (true) {
            System.out.print("Enter item number to add to cart (0 to finish): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (choice == 0) break; // End ordering

            FoodItem selected = itemMap.get(choice);
            if (selected != null) {
                cart.addItem(selected);
                System.out.println(selected.getName() + " added to cart.");
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    // Handles cash payment logic
    private static boolean processCashPayment(double amount) {
        System.out.print("Please enter the amount of cash paid by the customer: ");
        double cash = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        // Check if the cash is sufficient
        if (cash < amount) {
            System.out.println("The amount is insufficient. Payment failed.");
            return false;
        } else {
            double change = cash - amount;
            System.out.printf("Payment successful! Change: %.2f MYR\n", change);
            return true;
        }
    }

    // Handles card payment logic
    private static boolean processCardPayment(double amount) {
        System.out.print("Please enter the card number: ");
        String cardNumber = scanner.nextLine();

        // Validate card number length
        if (cardNumber.length() < 8) {
            System.out.println("Invalid card number. Payment failed.");
            return false;
        } else {
            // Display success and mask all but last 4 digits
            System.out.println("Payment successful! Card ending with " +
                cardNumber.substring(cardNumber.length() - 4) +
                ". Amount deducted: " + amount + " MYR");
            return true;
        }
    }

    // Allows the dine-in user to rate items they ordered
    private static void rateFood() {
        List<FoodItem> orderedItems = cart.getItems();

        if (orderedItems.isEmpty()) {
            System.out.println("No items in the cart to rate.");
            return;
        }

        // Display ordered items
        System.out.println("\n--- Rate Your Ordered Items ---");
        for (int i = 0; i < orderedItems.size(); i++) {
            System.out.println((i + 1) + ". " + orderedItems.get(i).getName());
        }

        // Ask user which item to rate
        System.out.print("Enter the number of the item you want to rate: ");
        int index = scanner.nextInt();
        scanner.nextLine();

        // Validate selection
        if (index < 1 || index > orderedItems.size()) {
            System.out.println("Invalid item number.");
            return;
        }

        FoodItem selected = orderedItems.get(index - 1);

        // Ask for rating score
        System.out.print("Please enter your rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();

        // Validate rating
        if (rating < 1 || rating > 5) {
            System.out.println("Invalid rating.");
            return;
        }

        // Save the rating
        selected.addRating(rating);
        System.out.println("Thank you! You rated " + selected.getName() + " with " + rating + " stars.");
    }

    // Display average rating and rating count for each ordered item
    private static void displayRatings() {
        System.out.println("\n--- Your Ordered Items' Ratings ---");
        Set<String> printed = new HashSet<>();

        for (FoodItem item : cart.getItems()) {
            if (!printed.contains(item.getName())) {
                System.out.printf("%s - Average Rating: %.2f (%d ratings)\n",
                    item.getName(), item.getAverageRating(), item.getTotalRatings());
                printed.add(item.getName()); // Avoid duplicate display
            }
        }
    }

    // Unused placeholder method for future payment processing
    public static boolean process(double total) {
        throw new UnsupportedOperationException("Unimplemented method 'process'");
    }

    // Unused placeholder method for future rating implementation
    public static void rateItems(List<FoodItem> items) {
        throw new UnsupportedOperationException("Unimplemented method 'rateItems'");
    }
}