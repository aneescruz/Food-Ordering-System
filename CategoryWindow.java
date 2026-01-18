import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CategoryWindow extends JFrame {

    private JTextArea itemDisplayArea;

    public CategoryWindow() {
        setTitle("Food Categories");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons for each category
        JButton riceButton = new JButton("Rice");
        JButton noodleButton = new JButton("Noodle");
        JButton drinksButton = new JButton("Drinks");

        // Add buttons to the panel
        buttonPanel.add(riceButton);
        buttonPanel.add(noodleButton);
        buttonPanel.add(drinksButton);

        // Text area for displaying items
        itemDisplayArea = new JTextArea();
        itemDisplayArea.setEditable(false);
        itemDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(itemDisplayArea);

        // Add components to the frame
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Button listeners
        riceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCategoryItems(new RiceCategory());
            }
        });

        noodleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCategoryItems(new NoodleCategory());
            }
        });

        drinksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayCategoryItems(new DrinksCategory());
            }
        });
    }

    private void displayCategoryItems(FoodCategory category) {
        StringBuilder sb = new StringBuilder();
        sb.append("Category: ").append(category.getCategoryName()).append("\n\n");

        for (FoodItem item : category.getItemList()) {
            sb.append(String.format("%-25s RM %.2f\n", item.getName(), item.getPrice()));
        }

        itemDisplayArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CategoryWindow().setVisible(true);
        });
    }
}