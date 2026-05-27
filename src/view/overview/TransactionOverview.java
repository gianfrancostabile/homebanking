package view.overview;

import constant.*;
import model.Client;
import model.Transaction;
import service.TransactionService;
import util.Dialog;
import view.custom.CustomButton;
import view.custom.CustomComboBox;
import view.custom.CustomTextField;
import view.table.TransactionTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionOverview extends JFrame {

    // Configuración Visual
    private static final int PADDING = 15;
    private static final int GAP = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(CommonConstant.DAY_FORMAT);

    // Servicios
    private final TransactionService transactionService = TransactionService.getInstance();
    // Estado
    private final Client client;
    // Componentes UI
    private CustomComboBox<TransactionType> typeField;
    private CustomTextField fromDateField;
    private CustomTextField toDateField;
    private TransactionTable transactionTable;

    public TransactionOverview(Client client) {
        this.client = client;

        setupFrame();
        initComponents();

        loadAndPopulateTransactions();

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

        transactionTable = new TransactionTable();

        mainPanel.add(buildFilterPanel(), BorderLayout.NORTH);
        mainPanel.add(transactionTable, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel buildFilterPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);

        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        fromDateField = new CustomTextField(firstDayOfMonth.format(DATE_FORMATTER));
        fromDateField.setPreferredSize(new Dimension(110, 35));

        toDateField = new CustomTextField(today.format(DATE_FORMATTER));
        toDateField.setPreferredSize(new Dimension(110, 35));

        typeField = new CustomComboBox<>(TransactionType.values());
        typeField.setPreferredSize(new Dimension(140, 35));

        CustomButton searchButton = new CustomButton(ButtonVariant.SEARCH);
        searchButton.setPreferredSize(new Dimension(100, 35));
        searchButton.addActionListener(_ -> onSearch());

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel(CommonConstant.FROM_LABEL), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        form.add(fromDateField, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 5);
        form.add(new JLabel(CommonConstant.TO_LABEL), gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 20);
        form.add(toDateField, gbc);

        gbc.gridx = 4;
        gbc.insets = new Insets(0, 0, 0, 5);
        form.add(new JLabel(CommonConstant.TYPE_LABEL), gbc);

        gbc.gridx = 5;
        gbc.insets = new Insets(0, 0, 0, 25);
        form.add(typeField, gbc);

        gbc.gridx = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(searchButton, gbc);

        return form;
    }

    private void onSearch() {
        boolean isFromValid = fromDateField.validateDateFormat(DATE_FORMATTER);
        boolean isToValid = toDateField.validateDateFormat(DATE_FORMATTER);

        if (!isFromValid || !isToValid) {
            Dialog.showWarning(this, FeedbackConstant.INVALID_DATE_FORMAT);
            return;
        }

        LocalDate fromDate = LocalDate.parse(fromDateField.getText().trim(), DATE_FORMATTER);
        LocalDate toDate = LocalDate.parse(toDateField.getText().trim(), DATE_FORMATTER);

        if (fromDate.isAfter(toDate)) {
            fromDateField.setError(true);
            toDateField.setError(true);
            Dialog.showWarning(this, FeedbackConstant.TO_GREATER_THAN_FROM_DATE);
            return;
        }

        loadAndPopulateTransactions();
    }

    private void loadAndPopulateTransactions() {
        String from = fromDateField.getText().trim();
        String to = toDateField.getText().trim();
        TransactionType selectedType = (TransactionType) typeField.getSelectedItem();

        TransactionType typeFilter = TransactionType.NONE.equals(selectedType) ? null : selectedType;

        List<Transaction> transactions = transactionService.findTransactionByClientIdAndDateAndType(client.getId(), from, to, typeFilter);

        transactionTable.clearTable();
        transactionTable.appendTransactions(transactions);
    }
}