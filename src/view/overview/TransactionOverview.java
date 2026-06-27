package view.overview;

import constant.*;
import enums.TransactionType;
import model.Client;
import model.Transaction;
import service.ClientService;
import service.TransactionService;
import service.impl.ReportTransactionService;
import util.Dialog;
import view.custom.CustomButton;
import view.custom.CustomComboBox;
import view.custom.CustomTextField;
import view.table.TransactionTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionOverview extends JFrame {

    // UI Configuration
    private static final int PADDING = 15;
    private static final int GAP = 10;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(CommonConstant.DAY_FORMAT);

    // Services
    private final ClientService clientService = ClientService.getInstance();
    private final TransactionService transactionService = TransactionService.getInstance();
    private final ReportTransactionService reportTransactionService = ReportTransactionService.getInstance();

    // State
    private final Client client;
    private List<Transaction> transactions;

    // Components UI
    private CustomComboBox<Client> clientField;
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

        DefaultComboBoxModel<Client> clientsBoxModel = new DefaultComboBoxModel<>();
        this.clientService.findAll().forEach(clientsBoxModel::addElement);
        clientField = new CustomComboBox<>(clientsBoxModel);
        clientField.setPreferredSize(new Dimension(220, 35));
        clientField.setSelectedItem(this.client);
        clientField.addItemListener((event) -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                onSearch();
            }
        });

        fromDateField = new CustomTextField(firstDayOfMonth.format(DATE_FORMATTER));
        fromDateField.setPreferredSize(new Dimension(110, 35));
        fromDateField.actionAfterInactiveTimer(1500, this::onSearch);

        toDateField = new CustomTextField(today.format(DATE_FORMATTER));
        toDateField.setPreferredSize(new Dimension(110, 35));
        toDateField.actionAfterInactiveTimer(1500, this::onSearch);

        typeField = new CustomComboBox<>(TransactionType.values());
        typeField.setPreferredSize(new Dimension(140, 35));
        typeField.addItemListener((event) -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                onSearch();
            }
        });

        CustomButton printButton = new CustomButton(ButtonConstant.PRINT_BUTTON);
        printButton.addActionListener((_) -> this.onPrint());

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel(FormFieldConstant.CLIENT), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        form.add(clientField, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 5);
        form.add(new JLabel(FormFieldConstant.FROM), gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 20);
        form.add(fromDateField, gbc);

        gbc.gridx = 4;
        gbc.insets = new Insets(0, 0, 0, 5);
        form.add(new JLabel(FormFieldConstant.TO), gbc);

        gbc.gridx = 5;
        gbc.insets = new Insets(0, 0, 0, 20);
        form.add(toDateField, gbc);

        gbc.gridx = 6;
        gbc.insets = new Insets(0, 0, 0, 5);
        form.add(new JLabel(FormFieldConstant.TYPE), gbc);

        gbc.gridx = 7;
        gbc.insets = new Insets(0, 0, 0, 25);
        form.add(typeField, gbc);

        gbc.gridx = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(printButton, gbc);

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

    private void onPrint() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(TitleConstant.SELECT_DESTINATION_FOLDER);
        chooser.setSelectedFile(new File(CommonConstant.REPORT_FILE_NAME));

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            reportTransactionService.print(this.transactions, chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void loadAndPopulateTransactions() {
        Client selectedClient = (Client) clientField.getSelectedItem();
        LocalDate from = LocalDate.parse(fromDateField.getText().trim(), DATE_FORMATTER);
        LocalDate to = LocalDate.parse(toDateField.getText().trim(), DATE_FORMATTER);
        TransactionType selectedType = (TransactionType) typeField.getSelectedItem();

        TransactionType typeFilter = TransactionType.NONE.equals(selectedType) ? null : selectedType;

        this.transactions = transactionService.findTransactionByClientIdAndDateAndType(
                selectedClient.getId(), from.atStartOfDay(), to.atTime(LocalTime.MAX), typeFilter);

        transactionTable.reAppend(this.transactions);
    }
}