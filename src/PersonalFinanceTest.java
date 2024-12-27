import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonalFinanceTest {

    private PersonalFinanceApp app;

    @BeforeEach
    public void setUp() {
        app = new PersonalFinanceApp();
    }

    @Test
    public void testAddTransactionIncome() {
        app.descriptionField.setText("Salary");
        app.typeComboBox.setSelectedItem("Income");
        app.amountField.setText("5000");
        app.addTransaction();

        // Validate table row count
        assertEquals(1, app.tableModel.getRowCount());

        // Validate total balance
        assertEquals(5000.0, app.totalBalance);

        // Validate row content
        assertEquals("Salary", app.tableModel.getValueAt(0, 0));
        assertEquals("Income", app.tableModel.getValueAt(0, 1));
        assertEquals("Rp 5000.0", app.tableModel.getValueAt(0, 2));
    }

    @Test
    public void testAddTransactionExpense() {
        app.descriptionField.setText("Groceries");
        app.typeComboBox.setSelectedItem("Expense");
        app.amountField.setText("2000");
        app.addTransaction();

        // Validate table row count
        assertEquals(1, app.tableModel.getRowCount());

        // Validate total balance
        assertEquals(-2000.0, app.totalBalance);

        // Validate row content
        assertEquals("Groceries", app.tableModel.getValueAt(0, 0));
        assertEquals("Expense", app.tableModel.getValueAt(0, 1));
        assertEquals("Rp 2000.0", app.tableModel.getValueAt(0, 2));
    }

    @Test
    public void testUpdateTransaction() {
        // Add initial transaction
        app.descriptionField.setText("Salary");
        app.typeComboBox.setSelectedItem("Income");
        app.amountField.setText("5000");
        app.addTransaction();

        // Select the row and update it
        app.financeTable.setRowSelectionInterval(0, 0);
        app.descriptionField.setText("Freelance Work");
        app.typeComboBox.setSelectedItem("Income");
        app.amountField.setText("6000");
        app.updateTransaction();

        // Validate table content
        assertEquals("Freelance Work", app.tableModel.getValueAt(0, 0));
        assertEquals("Income", app.tableModel.getValueAt(0, 1));
        assertEquals("Rp 6000.0", app.tableModel.getValueAt(0, 2));

        // Validate total balance
        assertEquals(6000.0, app.totalBalance);
    }

    @Test
    public void testDeleteTransaction() {
        // Add a transaction
        app.descriptionField.setText("Salary");
        app.typeComboBox.setSelectedItem("Income");
        app.amountField.setText("5000");
        app.addTransaction();

        // Select the row and delete it
        app.financeTable.setRowSelectionInterval(0, 0);
        app.deleteTransaction();

        // Validate table row count
        assertEquals(0, app.tableModel.getRowCount());

        // Validate total balance
        assertEquals(0.0, app.totalBalance);
    }

    @Test
    public void testAddTransactionInvalidAmount() {
        app.descriptionField.setText("Invalid Test");
        app.typeComboBox.setSelectedItem("Income");
        app.amountField.setText("invalid_number");

        // Simulate adding transaction
        app.addTransaction();

        // Validate table remains empty
        assertEquals(0, app.tableModel.getRowCount());

        // Validate total balance remains unchanged
        assertEquals(0.0, app.totalBalance);
    }

    @Test
    public void testUpdateTransactionInvalidSelection() {
        app.descriptionField.setText("Salary");
        app.typeComboBox.setSelectedItem("Income");
        app.amountField.setText("5000");
        app.addTransaction();

        // Try updating without selecting a row
        app.descriptionField.setText("Updated Salary");
        app.amountField.setText("6000");
        app.updateTransaction();

        // Validate that the original row remains unchanged
        assertEquals("Salary", app.tableModel.getValueAt(0, 0));
        assertEquals("Income", app.tableModel.getValueAt(0, 1));
        assertEquals("Rp 5000.0", app.tableModel.getValueAt(0, 2));
    }
}
