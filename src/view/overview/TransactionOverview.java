package view.overview;

import constant.CommonConstant;
import constant.TitleConstant;
import model.Client;
import model.Transaction;
import service.TransactionService;
import view.table.TransactionTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TransactionOverview extends JFrame {

    // Services
    private final TransactionService transactionService = TransactionService.getInstance();

    // UI Components
    private TransactionTable transactionTable;

    // State
    private final Client client;
    private List<Transaction> transactions;

    public TransactionOverview(Client client) {
        this.client = client;
        this.initComponents();
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(TitleConstant.TRANSACTION_OVERVIEW);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(TitleConstant.TRANSACTION_OVERVIEW + " - " + client.getName() + " " + client.getLastName(), SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14f));
        panel.add(titleLabel, BorderLayout.NORTH);

        this.transactions = this.transactionService.findTransactionByClientId(this.client.getId());

        this.transactionTable = new TransactionTable();
        this.transactionTable.appendTransactions(this.transactions);

        panel.add(this.transactionTable, BorderLayout.CENTER);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
