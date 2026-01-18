import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/*A GUI window that allows users to browse a food menu by category,
add selected items to a shopping cart, and view the contents and total cost of the cart.*/
public class CartWindow extends JFrame {

    // GUI components for displaying the menu and cart
    private JTextArea menuArea;
    private JTextArea cartArea;

    // Button for adding selected items to the cart
    private JButton addButton;

    // Dropdowns for selecting food categories and items
    private JComboBox<String> categoryCombo;
    private JComboBox<String> itemCombo;

    // Object to store cart contents
    private Cart cart;

    // List of all available food categories
    private List<FoodCategory> categories;

    //Constructor: initializes the main window and loads food data//
    public CartWindow() {
        setTitle("Cart Viewer"); // Window title
        setSize(700, 500); // Window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app when window is closed
        setLocationRelativeTo(null); // Center the window

        // Initialize the cart and load food categories
        cart = new Cart();
        categories = new ArrayList<>();
        categories.add(new RiceCategory());
        categories.add(new NoodleCategory());
        categories.add(new DrinksCategory());

        // Initialize the UI components and layout
        initComponents();

        // Display the menu and update available item options
        updateMenuDisplay();
        updateItemCombo();
    }

    // Sets up the GUI layout and components (buttons, text areas, combo boxes)
    private void initComponents() {
        //Top panel for selecting food category
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Category:"));
        categoryCombo = new JComboBox<>();

        // Populate the category dropdown with names of food categories
        for (FoodCategory cat : categories) {
            categoryCombo.addItem(cat.getCategoryName());
        }

        // Update the item dropdown whenever a category is selected
        categoryCombo.addActionListener(e -> updateItemCombo());
        topPanel.add(categoryCombo);

        //Center panel for item selection and add button
        JPanel centerPanel = new JPanel();
        itemCombo = new JComboBox<>(); // Dropdown for food items

        centerPanel.add(new JLabel("Select Item:"));
        centerPanel.add(itemCombo);

        addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> handleAddToCart()); // Add item on click
        centerPanel.add(addButton);

        //Menu display area on the left
        menuArea = new JTextArea(15, 30);
        menuArea.setEditable(false);
        JScrollPane menuScroll = new JScrollPane(menuArea);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Menu"));

        //Cart display area on the right
        cartArea = new JTextArea(15, 30);
        cartArea.setEditable(false);
        JScrollPane cartScroll = new JScrollPane(cartArea);
        cartScroll.setBorder(BorderFactory.createTitledBorder("Cart"));

        //Bottom layout: side-by-side menu and cart display
        JPanel centerContent = new JPanel(new GridLayout(1, 2));
        centerContent.add(menuScroll);
        centerContent.add(cartScroll);

        // Add all main panels to the window
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(centerContent, BorderLayout.SOUTH);
    }

    //Displays all food categories and their items in the menu area.
    private void updateMenuDisplay() {
        StringBuilder sb = new StringBuilder();
        for (FoodCategory category : categories) {
            sb.append("[").append(category.getCategoryName()).append("]\n");
            for (FoodItem item : category.getItemList()) {
                sb.append(" - ").append(item.getName())
                  .append(" (RM ").append(item.getPrice()).append(")\n");
            }
            sb.append("\n");
        }
        menuArea.setText(sb.toString()); // Show menu in the left text area
    }

    //Updates the itemCombo dropdown to show items from the currently selected category.
    private void updateItemCombo() {
        itemCombo.removeAllItems(); // Clear previous items

        int selectedIndex = categoryCombo.getSelectedIndex();
        if (selectedIndex >= 0) {
            FoodCategory selectedCategory = categories.get(selectedIndex);
            for (FoodItem item : selectedCategory.getItemList()) {
                itemCombo.addItem(item.getName());
            }
        }
    }

    //Handles the action of adding the selected item from the dropdown to the cart.
    private void handleAddToCart() {
        int categoryIndex = categoryCombo.getSelectedIndex();
        String selectedItemName = (String) itemCombo.getSelectedItem();

        if (categoryIndex >= 0 && selectedItemName != null) {
            FoodCategory category = categories.get(categoryIndex);

            // Find the selected item and add it to the cart
            for (FoodItem item : category.getItemList()) {
                if (item.getName().equals(selectedItemName)) {
                    cart.addItem(item);
                    break;
                }
            }

            updateCartDisplay(); // Refresh the cart display after adding item
        }
    }

    //Updates the cart display with the list of added items and total cost.
    private void updateCartDisplay() {
        StringBuilder sb = new StringBuilder();
        double total = 0;

        for (FoodItem item : cart.getItems()) {
            sb.append(item.getName())
              .append(" - RM ")
              .append(String.format("%.2f", item.getPrice()))
              .append("\n");
            total += item.getPrice();
        }

        sb.append("\nTotal: RM ").append(String.format("%.2f", total));
        cartArea.setText(sb.toString()); // Display cart contents in the right text area
    }

    //Main method to launch the GUI application.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CartWindow().setVisible(true); // Start the GUI
        });
    }
}