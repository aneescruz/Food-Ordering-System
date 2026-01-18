import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MainMenu extends JFrame {
    private JComboBox<String> orderTypeCombo;
    private JTextField identifierField;
    private JButton startOrderButton;
    private Session session;
    private JPanel contentPanel;
    private List<FoodCategory> categories;
    private List<FoodItem> paidItems = new ArrayList<>();
    private boolean isDineIn;

    public MainMenu() {
        setTitle("🍽️ Restaurant Ordering System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        categories = Arrays.asList(
                new RiceCategory(),
                new NoodleCategory(),
                new DrinksCategory()
        );

        initStartPanel();
    }

    private void initStartPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Start Order"));

        orderTypeCombo = new JComboBox<>(new String[]{"Dine-In", "Takeaway"});
        identifierField = new JTextField();
        startOrderButton = new JButton("Start Ordering");
        startOrderButton.addActionListener(e -> handleStartOrder());

        panel.add(new JLabel("Select Order Type:"));
        panel.add(orderTypeCombo);
        panel.add(new JLabel("Enter Table Number (1–20) or Phone (10–12 digits):"));
        panel.add(identifierField);
        panel.add(startOrderButton);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(panel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void handleStartOrder() {
        String orderType = (String) orderTypeCombo.getSelectedItem();
        String identifier = identifierField.getText().trim();

        if ("Dine-In".equals(orderType)) {
            if (!identifier.matches("\\d+") || Integer.parseInt(identifier) < 1 || Integer.parseInt(identifier) > 20) {
                JOptionPane.showMessageDialog(this, "Invalid table number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            isDineIn = true;
        } else {
            if (!identifier.matches("\\d{10,12}")) {
                JOptionPane.showMessageDialog(this, "Invalid phone number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            isDineIn = false;
        }

        session = new Session(orderType, identifier);
        showCategoryPanel();
    }

    private void showCategoryPanel() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (FoodCategory category : categories) {
            listModel.addElement(category.getCategoryName());
        }

        JList<String> categoryList = new JList<>(listModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(categoryList), BorderLayout.CENTER);

        JButton selectButton = new JButton("Select Category");
        JButton checkoutButton = new JButton("Proceed to Checkout");

        selectButton.addActionListener(e -> {
            int index = categoryList.getSelectedIndex();
            if (index >= 0) {
                showItemPanel(categories.get(index));
            }
        });

        checkoutButton.addActionListener(e -> showCheckoutPanel());

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(selectButton);
        bottomPanel.add(checkoutButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showItemPanel(FoodCategory category) {
        contentPanel.removeAll();
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<FoodItem> items = category.getItemList();
        for (FoodItem item : items) {
            listModel.addElement(item.getName() + " - RM " + item.getPrice() + " (⭐ " + item.getAverageRating() + ")");
        }

        JList<String> itemList = new JList<>(listModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTextField quantityField = new JTextField(5);

        JButton addButton = new JButton("Add to Cart");
        addButton.addActionListener(e -> {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    int quantity = Integer.parseInt(quantityField.getText());
                    if (quantity < 1 || quantity > 20) throw new NumberFormatException();

                    FoodItem selectedItem = items.get(selectedIndex);
                    for (int i = 0; i < quantity; i++) session.getCart().addItem(selectedItem);
                    JOptionPane.showMessageDialog(this, quantity + " x " + selectedItem.getName() + " added to cart.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid quantity (1–20).", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(addButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showCategoryPanel());

        panel.add(new JScrollPane(itemList), BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);
        panel.add(backButton, BorderLayout.NORTH);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // 结账界面 + 删除功能 + 继续加菜
    private void showCheckoutPanel() {
        contentPanel.removeAll();
        JPanel panel = new JPanel(new BorderLayout());

        DefaultListModel<String> cartModel = new DefaultListModel<>();
        for (FoodItem item : session.getCart().getItems()) {
            cartModel.addElement(item.getName() + " - RM " + String.format("%.2f", item.getPrice()));
        }
        JList<String> cartList = new JList<>(cartModel);
        cartList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel totalLabel = new JLabel("Total: MYR " + String.format("%.2f", session.getCart().getTotal()));

        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Remove Selected Item");
        JButton continueButton = new JButton("Continue Ordering"); // 新增按钮
        JButton payButton = new JButton("Proceed to Payment");

        removeButton.addActionListener(e -> {
            int index = cartList.getSelectedIndex();
            if (index >= 0) {
                FoodItem removedItem = session.getCart().getItems().get(index);
                session.getCart().removeItem(removedItem);
                cartModel.remove(index);
                totalLabel.setText("Total: MYR " + String.format("%.2f", session.getCart().getTotal()));
                JOptionPane.showMessageDialog(this, removedItem.getName() + " removed from cart.");
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item to remove.");
            }
        });

        // 点击后回到分类页面继续加菜
        continueButton.addActionListener(e -> showCategoryPanel());

        payButton.addActionListener(e -> handlePayment());

        buttonPanel.add(removeButton);
        buttonPanel.add(continueButton); // 添加到按钮面板
        buttonPanel.add(payButton);

        panel.add(new JScrollPane(cartList), BorderLayout.CENTER);
        panel.add(totalLabel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void handlePayment() {
        if (session.getCart().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty.");
            return;
        }

        double total = session.getCart().getTotal();
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
        String paymentMethod = null;

        if (method == 0) 
        {
            paymentMethod = "Cash";
            String cashInput = JOptionPane.showInputDialog(this, "Enter cash amount:");
            try {
                double cash = Double.parseDouble(cashInput);
                if (cash < total) {
                    JOptionPane.showMessageDialog(this, "Insufficient cash. Payment failed.");
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Payment successful!\nChange: RM" +
                            String.format("%.2f", (cash - total)));
                    success = true;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.");
            }
        } 
        else if (method == 1) 
        {
            paymentMethod = "Card";
            String cardNumber = JOptionPane.showInputDialog(this, "Enter card number:");
            if (cardNumber != null && cardNumber.length() >= 8) {
                JOptionPane.showMessageDialog(this, "Payment successful!\nCard ending with " +
                        cardNumber.substring(cardNumber.length() - 4));
                success = true;
                
            } else {
                JOptionPane.showMessageDialog(this, "Invalid card number. Payment failed.");
            }
        }

        if (success) {
            paidItems = new ArrayList<>(session.getCart().getItems());
            TransactionLogger.logTransaction(session, isDineIn, paymentMethod);

            if (isDineIn) {
                handleRating();
            } else {
                JOptionPane.showMessageDialog(this, "Thank you for your order!");
                System.exit(0);
            }
        }
    }

    private void handleRating() {
        if (paidItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items to rate.");
            return;
        }

        String[] names = paidItems.stream().map(FoodItem::getName).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(this,
                "Select an item to rate:", "Rating",
                JOptionPane.QUESTION_MESSAGE, null,
                names, names[0]);

        if (selected != null) {
            for (FoodItem item : paidItems) 
            {
                if (item.getName().equals(selected)) 
                {
                    String rateInput = JOptionPane.showInputDialog(this, "Enter rating (1-5):");
                    try {
                        int rating = Integer.parseInt(rateInput);
                        if (rating < 1 || rating > 5) 
                        {
                            JOptionPane.showMessageDialog(this, "Rating must be between 1 and 5.");
                            return;
                        }
                        item.addRating(rating);

                        JOptionPane.showMessageDialog(this, "Thanks! You rated " + item.getName() +
                        " with " + rating + " stars.");

                        JOptionPane.showMessageDialog(this, "Thank you for your order!");
                        System.exit(0);

                        displayAllRatings();
                        
                    } 
                    catch (NumberFormatException ex) 
                    {
                        JOptionPane.showMessageDialog(this, "Invalid rating.");
                    }
                }
            }
        }
    }

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

        JTextArea ratingsArea = new JTextArea(sb.toString(), 20, 40);
        ratingsArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ratingsArea), "All Ratings", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}