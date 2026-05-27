package view.form;

import constant.*;
import exception.JDBCException;
import model.Client;
import model.Product;
import service.ProductService;
import util.Dialog;
import view.custom.CustomButton;
import view.custom.CustomComboBox;
import view.custom.CustomTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Optional;

public class TransferForm extends JFrame {

    // Services
    private final ProductService productService = ProductService.getInstance();

    // UI Components
    private final CustomTextField destinationAccountField = new CustomTextField();
    private final JLabel destinationNameLabel = new JLabel();
    private final JLabel currentBalanceLabel = new JLabel();
    private final JLabel transferCurrencyLabel = new JLabel();
    private CustomComboBox<Client> clientJComboBox;
    private CustomComboBox<Product> productJComboBox;
    private DefaultComboBoxModel<Product> productValuesJComboBox;
    private CustomTextField transferAmountField;
    private final CustomButton searchButton = new CustomButton(ButtonVariant.SEARCH);
    private CustomButton submitButton;

    // State
    private final List<Client> clients;
    private Product originProduct;
    private Product destinationProduct;

    public TransferForm(List<Client> clients) {
        this.clients = clients;
        this.setupFrame();
        this.initComponents();
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(TitleConstant.TRANSFER_FORM);
    }

    private void initComponents() {
        this.buildClientComboBox();
        this.buildProductComboBox();
        this.buildBalanceTextField();
        this.buildSearchAccountButton();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel(TitleConstant.TRANSFER_FORM, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(this.buildForm(), BorderLayout.CENTER);
        panel.add(this.buildFooter(), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(6, 2, 5, 10));

        form.add(new JLabel(CommonConstant.CLIENT_FIELD));
        form.add(this.clientJComboBox);

        form.add(new JLabel(CommonConstant.PRODUCT_FORM_FIELD));
        form.add(this.productJComboBox);

        form.add(new JLabel(CommonConstant.DESTINATION_ACCOUNT_FORM_FIELD));
        JPanel destinationAccountPanel = new JPanel(new BorderLayout(5, 0));
        this.destinationAccountField.setEnabled(false);
        destinationAccountPanel.add(this.destinationAccountField, BorderLayout.CENTER);
        destinationAccountPanel.add(this.searchButton, BorderLayout.EAST);
        form.add(destinationAccountPanel);

        form.add(new JLabel(CommonConstant.DESTINATION_ACCOUNT_FORM_LABEL));
        form.add(this.destinationNameLabel);

        form.add(new JLabel(CommonConstant.CURRENT_BALANCE_FORM_FIELD));
        form.add(this.currentBalanceLabel);

        form.add(new JLabel(CommonConstant.TRANSFER_BALANCE_FORM_FIELD));
        JPanel transferAmountPanel = new JPanel(new BorderLayout(5, 0));
        transferAmountPanel.add(this.transferCurrencyLabel, BorderLayout.WEST);
        transferAmountPanel.add(this.transferAmountField, BorderLayout.CENTER);
        form.add(transferAmountPanel);

        return form;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        CustomButton cancelButton = new CustomButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener(_ -> this.dispose());

        this.submitButton = new CustomButton(ButtonConstant.TRANSFER_BUTTON, ButtonVariant.CREATE);
        this.submitButton.setEnabled(false);
        this.submitButton.addActionListener(_ -> onSubmit());

        footer.add(cancelButton);
        footer.add(this.submitButton);

        return footer;
    }

    private void buildClientComboBox() {
        DefaultComboBoxModel<Client> clientValues = new DefaultComboBoxModel<>();
        clientValues.addAll(this.clients);

        this.clientJComboBox = new CustomComboBox<>(clientValues);
        this.clientJComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                String clientId = ((Client) event.getItem()).getId();
                this.updateProductsOnClientChange(clientId);
            }
        });
    }

    private void buildProductComboBox() {
        this.productValuesJComboBox = new DefaultComboBoxModel<>();
        this.productJComboBox = new CustomComboBox<>(this.productValuesJComboBox);
        this.productJComboBox.setEnabled(false);

        this.productJComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Product product = (Product) event.getItem();
                if (product != null) {
                    this.updateOnChangeProduct(product);
                }
            }
        });
    }

    private void buildBalanceTextField() {
        this.transferAmountField = new CustomTextField();
        this.transferAmountField.setEnabled(false);
        this.transferAmountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { toggleSubmit(); }
            @Override public void removeUpdate(DocumentEvent e) { toggleSubmit(); }
            @Override public void changedUpdate(DocumentEvent e) { toggleSubmit(); }

            private void toggleSubmit() {
                submitButton.setEnabled(!transferAmountField.getText().isBlank());
            }
        });
    }

    private void buildSearchAccountButton() {
        this.searchButton.setEnabled(false);
        this.searchButton.addActionListener(_ -> onSearchAccountClick());
    }

    private void onSearchAccountClick() {
        String destinationAccount = this.destinationAccountField.getText();
        if (destinationAccount == null || destinationAccount.isBlank()) {
            Dialog.showWarning(this, FeedbackConstant.EMPTY_DESTINATION_ACCOUNT_FIELD);
            this.disableTransferInput();
            return;
        }

        Optional<Product> productOptional = productService.findByIdOrAliasOrCbu(destinationAccount);
        if (productOptional.isEmpty()) {
            Dialog.showWarning(this, FeedbackConstant.DESTINATION_ACCOUNT_NOT_FOUND);
            this.disableTransferInput();
            return;
        }

        Product foundProduct = productOptional.get();
        if (!validateDestinationAccount(foundProduct)) {
            this.disableTransferInput();
            return;
        }

        this.destinationProduct = foundProduct;
        this.clients.stream()
                .filter(client -> client.getId().equals(this.destinationProduct.getClientId()))
                .findFirst()
                .ifPresent(productClient -> {
                    this.destinationNameLabel.setText(productClient.getName() + " " + productClient.getLastName());
                    this.transferAmountField.setEnabled(true);
                });
    }

    private boolean validateDestinationAccount(Product destination) {
        if (destination.getId().equals(this.originProduct.getId())) {
            Dialog.showError(this, FeedbackConstant.CANNOT_TRANSFER_TO_SAME_PRODUCT);
            return false;
        }
        if (!destination.getType().getCurrency().equals(this.originProduct.getType().getCurrency())) {
            Dialog.showError(this, FeedbackConstant.CURRENCY_ARE_NOT_SAME);
            return false;
        }
        return true;
    }

    private void disableTransferInput() {
        this.destinationAccountField.setText("");
        this.transferAmountField.setEnabled(false);
    }

    private boolean isBalanceValid() {
        if (this.originProduct.getBalance() <= 0d) {
            Dialog.showError(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE);
            return false;
        }

        if (!this.isBalanceTextFieldValid()) {
            return false;
        }

        double parsedBalance = Double.parseDouble(this.transferAmountField.getText());
        if (this.originProduct.getBalance() - parsedBalance < 0) {
            Dialog.showError(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE);
            return false;
        }
        return true;
    }

    private boolean isBalanceTextFieldValid() {
        String balance = this.transferAmountField.getText();
        if (balance == null || balance.isBlank()) {
            Dialog.showWarning(this, FeedbackConstant.INVALID_BALANCE_FIELD);
            return false;
        }
        try {
            double parsedBalance = Double.parseDouble(balance);
            if (parsedBalance < 0) {
                Dialog.showWarning(this, FeedbackConstant.NEGATIVE_BALANCE_FIELD);
                return false;
            }
            if (parsedBalance > CommonConstant.MAX_BALANCE_TO_ADD) {
                Dialog.showWarning(this, FeedbackConstant.MAX_BALANCE_TO_TRANSFER_FIELD);
                return false;
            }
            return true;
        } catch (NumberFormatException exception) {
            Dialog.showError(this, FeedbackConstant.NOT_NUMERIC_BALANCE_FIELD);
            return false;
        }
    }

    private void updateProductsOnClientChange(String clientId) {
        List<Product> clientProducts = productService.findByClientId(clientId);

        this.productValuesJComboBox.removeAllElements();
        clientProducts.forEach(this.productValuesJComboBox::addElement);

        this.productJComboBox.setEnabled(true);
        this.destinationAccountField.setText("");
        this.destinationAccountField.setEnabled(false);
        this.searchButton.setEnabled(false);
        this.destinationNameLabel.setText("");
        this.currentBalanceLabel.setText("");
        this.transferCurrencyLabel.setText("");

        if (!clientProducts.isEmpty()) {
            this.productJComboBox.setSelectedIndex(0);
            this.updateOnChangeProduct((Product) this.productJComboBox.getSelectedItem());
        } else {
            this.originProduct = null;
        }
    }

    private void updateOnChangeProduct(Product product) {
        this.originProduct = product;
        String currency = product.getType().getCurrency().getSign();

        this.currentBalanceLabel.setText(currency + product.getBalance());
        this.transferCurrencyLabel.setText(currency);
        this.destinationAccountField.setEnabled(true);
        this.searchButton.setEnabled(true);
    }

    private void onSubmit() {
        if (!this.isBalanceValid()) {
            return;
        }

        double parsedBalance = Double.parseDouble(this.transferAmountField.getText());
        try {
            this.productService.transfer(this.originProduct.getId(), this.destinationProduct.getId(), parsedBalance);
            JOptionPane.showMessageDialog(this, FeedbackConstant.TRANSFER_DONE,
                    FeedbackConstant.SUCCESS_TITLE, JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (JDBCException e) {
            Dialog.showError(this, FeedbackConstant.ERROR_TRANSFERING_MESSAGE);
        }
    }
}