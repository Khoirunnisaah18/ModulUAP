import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PersonalFinanceApp extends JFrame {
    public JTextField descriptionField;
    public JTextField amountField;
    public JComboBox<String> typeComboBox;
    private JLabel totalBalanceLabel;
    public JTable financeTable;
    public DefaultTableModel tableModel;
    public double totalBalance = 0.0;

    public PersonalFinanceApp() {
        // Set up the frame
        setTitle("Personal Finance Tracker");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 123, 255));

        // Add image to header
        JLabel iconLabel = new JLabel(new ImageIcon(new ImageIcon("src/images/icon_finance.jpg").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH))); // Scaled image
        headerPanel.add(iconLabel, BorderLayout.WEST);


        JLabel headerLabel = new JLabel("\uD83D\uDCB0 Personal Finance Tracker", JLabel.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Transaction"));
        inputPanel.setBackground(new Color(255, 255, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Input Components
        JLabel descriptionLabel = new JLabel("\uD83D\uDCDD Description:");
        descriptionField = new JTextField();

        JLabel typeLabel = new JLabel("\uD83D\uDCB3 Type:");
        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});

        JLabel amountLabel = new JLabel("\uD83D\uDCB5 Amount (Rp):");
        amountField = new JTextField();

        JButton addButton = new JButton("Add Transaction");
        addButton.setBackground(new Color(40, 167, 69));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        JButton updateButton = new JButton("Update Transaction");
        updateButton.setBackground(new Color(255, 193, 7));
        updateButton.setForeground(Color.WHITE);
        updateButton.setFocusPainted(false);

        JButton deleteButton = new JButton("Delete Transaction");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);

        // Arrange Input Components
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(typeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        inputPanel.add(amountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        inputPanel.add(addButton, gbc);

        gbc.gridy = 4;
        inputPanel.add(updateButton, gbc);

        gbc.gridy = 5;
        inputPanel.add(deleteButton, gbc);

        // Table for Transactions
        String[] columnNames = {"Description", "Type", "Amount (Rp)"};
        tableModel = new DefaultTableModel(columnNames, 0);
        financeTable = new JTable(tableModel);
        financeTable.setFillsViewportHeight(true);
        financeTable.setRowHeight(25);
        financeTable.setShowGrid(true);
        financeTable.setGridColor(new Color(230, 230, 230));
        JScrollPane tableScrollPane = new JScrollPane(financeTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Transaction History"));

        // Total Balance Panel
        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(new Color(245, 245, 245));
        totalBalanceLabel = new JLabel("Total Balance: Rp 0.0", JLabel.CENTER);
        totalBalanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        balancePanel.add(totalBalanceLabel);

        // Button Actions
        addButton.addActionListener(e -> addTransaction());
        updateButton.addActionListener(e -> updateTransaction());
        deleteButton.addActionListener(e -> deleteTransaction());

        // Table Mouse Listener for Selecting Rows
        financeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = financeTable.getSelectedRow();
                if (selectedRow != -1) {
                    descriptionField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    typeComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString());
                    amountField.setText(tableModel.getValueAt(selectedRow, 2).toString().replace("Rp ", ""));
                }
            }
        });

        // Add Panels to Frame
        add(headerPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.WEST);
        add(tableScrollPane, BorderLayout.CENTER);
        add(balancePanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void addTransaction() {
        try {
            String description = descriptionField.getText();
            String type = (String) typeComboBox.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());

            if (description.isEmpty() || amount <= 0) {
                throw new IllegalArgumentException("Invalid input. Please provide valid description and amount.");
            }

            // Update balance
            if (type.equals("Income")) {
                totalBalance += amount;
            } else {
                totalBalance -= amount;
            }

            totalBalanceLabel.setText("Total Balance: Rp " + totalBalance);

            // Add to table
            tableModel.addRow(new Object[]{description, type, "Rp " + amount});

            // Clear inputs
            descriptionField.setText("");
            amountField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateTransaction() {
        try {
            int selectedRow = financeTable.getSelectedRow();
            if (selectedRow == -1) {
                throw new IllegalArgumentException("Please select a transaction to update.");
            }

            String description = descriptionField.getText();
            String type = (String) typeComboBox.getSelectedItem();
            double amount = Double.parseDouble(amountField.getText());

            if (description.isEmpty() || amount <= 0) {
                throw new IllegalArgumentException("Invalid input. Please provide valid description and amount.");
            }

            // Adjust balance
            String oldType = tableModel.getValueAt(selectedRow, 1).toString();
            double oldAmount = Double.parseDouble(tableModel.getValueAt(selectedRow, 2).toString().replace("Rp ", ""));
            if (oldType.equals("Income")) {
                totalBalance -= oldAmount;
            } else {
                totalBalance += oldAmount;
            }

            if (type.equals("Income")) {
                totalBalance += amount;
            } else {
                totalBalance -= amount;
            }

            totalBalanceLabel.setText("Total Balance: Rp " + totalBalance);

            // Update table
            tableModel.setValueAt(description, selectedRow, 0);
            tableModel.setValueAt(type, selectedRow, 1);
            tableModel.setValueAt("Rp " + amount, selectedRow, 2);

            // Clear inputs
            descriptionField.setText("");
            amountField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for the amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteTransaction() {
        try {
            int selectedRow = financeTable.getSelectedRow();
            if (selectedRow == -1) {
                throw new IllegalArgumentException("Please select a transaction to delete.");
            }

            // Adjust balance
            String type = tableModel.getValueAt(selectedRow, 1).toString();
            double amount = Double.parseDouble(tableModel.getValueAt(selectedRow, 2).toString().replace("Rp ", ""));
            if (type.equals("Income")) {
                totalBalance -= amount;
            } else {
                totalBalance += amount;
            }

            totalBalanceLabel.setText("Total Balance: Rp " + totalBalance);

            // Remove from table
            tableModel.removeRow(selectedRow);

            // Clear inputs
            descriptionField.setText("");
            amountField.setText("");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new PersonalFinanceApp();
    }
}
