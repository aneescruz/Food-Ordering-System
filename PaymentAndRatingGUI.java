import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// Main GUI class for handling food ordering, payment, and rating
public class PaymentAndRatingGUI extends JFrame {
    // GUI components
    private JComboBox<String> orderTypeBox;         // Dropdown to choose between Dine-in and Takeaway
    private JTextArea menuArea, cartArea;           // Text areas for displaying the menu and cart
    private JButton payButton, rateButton;          // Buttons for payment and rating

    // Business logic variables
    private boolean isDineIn;                       // Flag to check if the customer is dining in
    private List<FoodCategory> categories = new ArrayList<>(); // List of all food categories
    private Cart cart = new Cart();                 // Shopping cart to store selected items
    private List<FoodItem> paidItems = new ArrayList<>(); // Items the user has paid for (used for rating)

    // Constructor
    public PaymentAndRatingGUI() {
        setTitle("Restaurant Ordering System");     // Set window title
        setSize(700, 500);                          // Set window size
        setDefaultCloseOperation(EXIT_ON_CLOSE);    // Exit the program when window is closed
        setLocationRelativeTo(null);                // Center the window on screen
        setLayout(new BorderLayout());              // Use border layout

        loadMenuCategories();                       // Load all categories and menu items
        initComponents();                           // Initialize GUI components
    }

    // Initialize GUI components and layout
    private void initComponents() {
        // Top panel with order type selection
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Order Type:"));
        orderTypeBox = new JComboBox<>(new String[]{"Dine-in", "Takeaway"});
        topPanel.add(orderTypeBox);
        add(topPanel, BorderLayout.NORTH);

        // Split pane to show menu and cart side by side
        JSplitPane splitPane = new JSplitPane();
        menuArea = new JTextArea(20, 30);
        menuArea.setEditable(false);
        cartArea = new JTextArea(20, 20);
        cartArea.setEditable(false);
        splitPane.setLeftComponent(new JScrollPane(menuArea));
        splitPane.setRightComponent(new JScrollPane(cartArea));
        add(splitPane, BorderLayout.CENTER);

        displayMenu(); // Populate the menuArea with menu items

        // Bottom panel with action buttons
        JPanel bottomPanel = new JPanel();
        JButton addButton = new JButton("Add to Cart");
        payButton = new JButton("Pay");
        rateButton = new JButton("Rate Food");
        rateButton.setEnabled(false); // Disable rate button until payment is complete

        bottomPanel.add(addButton);
        bottomPanel.add(payButton);
        bottomPanel.add(rateButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Button listeners
        addButton.addActionListener(e -> handleAddItem());
        payButton.addActionListener(e -> handlePayment());
        rateButton.addActionListener(e -> handleRating());
    }

    // Display the full menu from all categories in menuArea
    private void displayMenu() {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (FoodCategory category : categories) {
            sb.append("[").append(category.getCategoryName()).append("]\n");
            for (FoodItem item : category.getItemList()) {
                sb.append(index++).append(". ")
                        .append(item.getName()).append(" - RM")
                        .append(String.format("%.2f", item.getPrice())).append("\n");
            }
        }
        menuArea.setText(sb.toString());
    }

    // Handle adding a menu item to the cart based on user input
    private void handleAddItem() {
        String input = JOptionPane.showInputDialog(this, "Enter item number to add:");
        try {
            int choice = Integer.parseInt(input);
            if (choice <= 0) return;

            int index = 1;
            for (FoodCategory category : categories) {
                for (FoodItem item : category.getItemList()) {
                    if (index == choice) {
                        cart.addItem(item);
                        updateCartDisplay();
                        return;
                    }
                    index++;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid item number.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }

    // Update the cartArea with current cart contents and total
    private void updateCartDisplay() {
        StringBuilder sb = new StringBuilder();
        for (FoodItem item : cart.getItems()) {
            sb.append(item.getName()).append(" - RM")
                    .append(String.format("%.2f", item.getPrice())).append("\n");
        }
        sb.append("\nTotal: RM").append(String.format("%.2f", cart.getTotal()));
        cartArea.setText(sb.toString());
    }

    // Handle payment logic (cash or card)
    private void handlePayment() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        isDineIn = orderTypeBox.getSelectedIndex() == 0;
        double total = cart.getTotal();

        // Prompt for payment method
        String[] options = {"Cash", "Card"};
        int method = JOptionPane.showOptionDialog(this,
                "Total: RM" + String.format("%.2f", total) + "\nSelect payment method:",
                "Payment",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        boolean success = false;

        // Cash payment processing
        if (method == 0) {
            String cashInput = JOptionPane.showInputDialog(this, "Enter cash amount:");
            try {
                double cash = Double.parseDouble(cashInput);
                if (cash < total) {
                    JOptionPane.showMessageDialog(this, "Insufficient cash. Payment failed.");
                } else {
                    JOptionPane.showMessageDialog(this, "Payment successful!\nChange: RM" +
                            String.format("%.2f", (cash - total)));
                    success = true;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        }
        // Card payment processing
        else if (method == 1) {
            String cardNumber = JOptionPane.showInputDialog(this, "Enter card number:");
            if (cardNumber != null && cardNumber.length() >= 8) {
                JOptionPane.showMessageDialog(this, "Payment successful!\nCard ending with " +
                        cardNumber.substring(cardNumber.length() - 4));
                success = true;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card number. Payment failed.");
            }
        }

        // If payment successful, store paid items for rating and update UI
        if (success) {
            paidItems = new ArrayList<>(cart.getItems()); // Copy paid items for rating
            payButton.setEnabled(false); // Disable payment button
            if (isDineIn) {
                rateButton.setEnabled(true); // Enable rating only for dine-in
            } else {
                JOptionPane.showMessageDialog(this, "Thank you for your order!");
            }
        }
    }

    // Handle rating a food item from the paid items list
    private void handleRating() {
        if (paidItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items to rate.");
            return;
        }

        // Prompt user to select item from paid items
        String[] names = paidItems.stream().map(FoodItem::getName).toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(this,
                "Select an item to rate:", "Rating",
                JOptionPane.QUESTION_MESSAGE, null,
                names, names[0]);

        if (selected != null) {
            for (FoodItem item : paidItems) {
                if (item.getName().equals(selected)) {
                    // Prompt for rating input (1–5)
                    String rateInput = JOptionPane.showInputDialog(this, "Enter rating (1-5):");
                    try {
                        int rating = Integer.parseInt(rateInput);
                        if (rating < 1 || rating > 5) {
                            JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.");
                            return;
                        }
                        item.addRating(rating); // Apply rating
                        JOptionPane.showMessageDialog(this, "Thanks! You rated " + item.getName() +
                                " with " + rating + " stars.");
                        displayAllRatings();         // Show updated ratings
                        rateButton.setEnabled(false); // Disable rating after one session
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Invalid rating.");
                    }
                }
            }
        }
    }

    // Display average ratings of all food items
    private void displayAllRatings() {
        StringBuilder sb = new StringBuilder();
        for (FoodCategory category : categories) {
            sb.append("[").append(category.getCategoryName()).append("]\n");
            for (FoodItem item : category.getItemList()) {
                sb.append(item.getName())
                        .append(" - Avg Rating: ").append(String.format("%.2f", item.getAverageRating()))
                        .append(" (").append(item.getTotalRatings()).append(" ratings)\n");
            }
        }

        // Show ratings in a scrollable dialog
        JTextArea ratingsArea = new JTextArea(sb.toString(), 20, 40);
        ratingsArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ratingsArea), "All Ratings", JOptionPane.INFORMATION_MESSAGE);
    }

    // Load all food categories (Rice, Noodle, Drinks)
    private void loadMenuCategories() {
        categories.add(new RiceCategory());
        categories.add(new NoodleCategory());
        categories.add(new DrinksCategory());
    }

    // Entry point of the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaymentAndRatingGUI().setVisible(true));
    }
}