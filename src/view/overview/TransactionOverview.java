package view.overview;

import constant.TitleConstant;
import dto.TransactionFilter;
import model.Client;
import model.Transaction;
import service.TransactionService;
import view.form.TransactionFilterForm;
import view.table.TransactionTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class TransactionOverview extends JFrame {

    // UI Configuration
    private static final int PADDING = 15;
    private static final int GAP = 10;

    // Services
    private final TransactionService transactionService = TransactionService.getInstance();

    // State
    private final Client client;
    private List<Transaction> transactions;

    // Components UI
    private TransactionTable transactionTable;
    private TransactionFilterForm filterForm;

    public TransactionOverview(Client client) {
        this.client = client;

        setupFrame();
        initComponents();

        filterForm.loadAndPopulateTransactions();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(TitleConstant.TRANSACTION_OVERVIEW);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(GAP, GAP));
        mainPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        filterForm = new TransactionFilterForm(this.client, this::reloadTransactionByFilter, this::getListedTransactions);
        transactionTable = new TransactionTable();

        mainPanel.add(filterForm, BorderLayout.NORTH);
        mainPanel.add(transactionTable, BorderLayout.CENTER);

        add(mainPanel);
    }

    private List<Transaction> getListedTransactions() {
        return this.transactions;
    }

    private void reloadTransactionByFilter(TransactionFilter filter) {
        this.transactions = transactionService.findTransactionByClientIdAndDateAndType(
                filter.getClientId(), filter.getFromDate(), filter.getToDate(), filter.getTransactionType());
        transactionTable.reAppend(this.transactions);
    }
}