package view.form;

import constant.*;
import dto.TransactionFilter;
import enums.TransactionType;
import model.Client;
import model.Transaction;
import service.ClientService;
import service.impl.ReportTransactionService;
import util.Dialog;
import view.custom.CustomButton;
import view.custom.CustomComboBox;
import view.custom.CustomTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TransactionFilterForm extends JPanel {

    // UI Configuration
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(CommonConstant.DAY_FORMAT);

    // Services
    private final ClientService clientService = ClientService.getInstance();
    private final ReportTransactionService reportTransactionService = ReportTransactionService.getInstance();
    // Actions
    private final Consumer<TransactionFilter> onSubmitFilter;
    private final Supplier<List<Transaction>> getListedTransactions;
    // Components UI
    private CustomComboBox<Client> clientComboBox;
    private CustomComboBox<TransactionType> typeComboBox;
    private CustomTextField fromDateField;
    private CustomTextField toDateField;

    public TransactionFilterForm(Client defaultClient, Consumer<TransactionFilter> onSubmitFilter, Supplier<List<Transaction>> getListedTransactions) {
        super(new GridBagLayout());
        this.onSubmitFilter = onSubmitFilter;
        this.getListedTransactions = getListedTransactions;

        this.initComponents(defaultClient);
        this.buildForm();
    }

    private void buildForm() {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 0, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel(FormFieldConstant.CLIENT), gbc);

        gbc.gridx = 1;
        gbc.insets = new Insets(0, 0, 0, 20);
        this.add(clientComboBox, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(0, 0, 0, 5);
        this.add(new JLabel(FormFieldConstant.FROM), gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(0, 0, 0, 20);
        this.add(fromDateField, gbc);

        gbc.gridx = 4;
        gbc.insets = new Insets(0, 0, 0, 5);
        this.add(new JLabel(FormFieldConstant.TO), gbc);

        gbc.gridx = 5;
        gbc.insets = new Insets(0, 0, 0, 20);
        this.add(toDateField, gbc);

        gbc.gridx = 6;
        gbc.insets = new Insets(0, 0, 0, 5);
        this.add(new JLabel(FormFieldConstant.TYPE), gbc);

        gbc.gridx = 7;
        gbc.insets = new Insets(0, 0, 0, 25);
        this.add(typeComboBox, gbc);

        gbc.gridx = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(this.buildPrintButton(), gbc);
    }

    private void initComponents(Client defaultClient) {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);

        this.buildClientComboBox(defaultClient);
        this.buildFromDateField(firstDayOfMonth);
        this.buildToDateField(today);
        this.buildTypeComboBox();
    }

    private void buildClientComboBox(Client defaultClient) {
        DefaultComboBoxModel<Client> clientsBoxModel = new DefaultComboBoxModel<>();
        this.clientService.findAll().forEach(clientsBoxModel::addElement);
        clientComboBox = new CustomComboBox<>(clientsBoxModel);
        clientComboBox.setPreferredSize(new Dimension(220, 35));
        if (defaultClient != null) {
            clientComboBox.setSelectedItem(defaultClient);
        }
        clientComboBox.addItemListener((event) -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                onSearch();
            }
        });
    }

    private void buildFromDateField(LocalDate firstDayOfMonth) {
        fromDateField = new CustomTextField(firstDayOfMonth.format(DATE_FORMATTER));
        fromDateField.setPreferredSize(new Dimension(110, 35));
        fromDateField.actionAfterInactiveTimer(1500, this::onSearch);
    }

    private void buildToDateField(LocalDate today) {
        toDateField = new CustomTextField(today.format(DATE_FORMATTER));
        toDateField.setPreferredSize(new Dimension(110, 35));
        toDateField.actionAfterInactiveTimer(1500, this::onSearch);
    }

    private void buildTypeComboBox() {
        typeComboBox = new CustomComboBox<>(TransactionType.values());
        typeComboBox.setPreferredSize(new Dimension(140, 35));
        typeComboBox.addItemListener((event) -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                onSearch();
            }
        });
    }

    private JButton buildPrintButton() {
        CustomButton printButton = new CustomButton(ButtonConstant.PRINT_BUTTON);
        printButton.addActionListener((_) -> this.onPrint());
        return printButton;
    }

    private void onSearch() {
        boolean isFromValid = fromDateField.validateDateFormat(DATE_FORMATTER);
        boolean isToValid = toDateField.validateDateFormat(DATE_FORMATTER);

        if (!isFromValid || !isToValid) {
            util.Dialog.showWarning(this, FeedbackConstant.INVALID_DATE_FORMAT);
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
            reportTransactionService.print(this.getListedTransactions.get(), chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void loadAndPopulateTransactions() {
        Client selectedClient = (Client) clientComboBox.getSelectedItem();
        LocalDate from = LocalDate.parse(fromDateField.getText().trim(), DATE_FORMATTER);
        LocalDate to = LocalDate.parse(toDateField.getText().trim(), DATE_FORMATTER);
        TransactionType selectedType = (TransactionType) typeComboBox.getSelectedItem();

        TransactionType typeFilter = TransactionType.NONE.equals(selectedType) ? null : selectedType;

        TransactionFilter filter = new TransactionFilter(selectedClient.getId(), from.atStartOfDay(), to.atTime(LocalTime.MAX), typeFilter);
        this.onSubmitFilter.accept(filter);
    }
}
