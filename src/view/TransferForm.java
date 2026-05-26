package view;

import constant.ButtonConstant;
import constant.CommonConstant;
import constant.FeedbackConstant;
import constant.TitleConstant;
import exception.JDBCException;
import model.Client;
import model.Product;
import service.ProductService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class TransferForm extends JFrame {

    private final JTextField destinationAccountField = new JTextField();
    private final JLabel destinationNameLabel = new JLabel();
    private final JLabel currentBalanceLabel = new JLabel();
    private final JLabel transferCurrencyLabel = new JLabel();
    private final List<Client> clients;
    private final ProductService productService = new ProductService();
    private JComboBox<Client> clientJComboBox;
    private JComboBox<Product> productJComboBox;
    private DefaultComboBoxModel<Product> productValuesJComboBox;
    private JTextField transferAmountField;
    private JButton seachButton = new JButton(ButtonConstant.SEARCH);
    private JButton submitButton = new JButton(ButtonConstant.TRANSFER_BUTTON);
    private Product originProduct;
    private Product destinationProduct;

    public TransferForm(List<Client> clients) {
        this.clients = clients;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(TitleConstant.TRANSFER_FORM), BorderLayout.NORTH);
        panel.add(this.buildForm(), BorderLayout.CENTER);
        panel.add(this.buildFooter(), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private JPanel buildForm() {
        this.buildClientComboBox();
        this.buildProductComboBox();
        this.buildBalanceTextField();
        this.buildSearchAccountButton();

        JPanel form = new JPanel(new GridLayout(6, 2));
        form.add(new JLabel(CommonConstant.CLIENT_FIELD));
        form.add(this.clientJComboBox);
        form.add(new JLabel(CommonConstant.PRODUCT_FORM_FIELD));
        form.add(this.productJComboBox);

        form.add(new JLabel(CommonConstant.DESTINATION_ACCOUNT_FORM_FIELD));
        JPanel destinationAccountPanel = new JPanel(new GridLayout(1, 2));
        this.destinationAccountField.setEnabled(false);
        destinationAccountPanel.add(this.destinationAccountField);
        destinationAccountPanel.add(this.seachButton);
        form.add(destinationAccountPanel);
        form.add(new JLabel(CommonConstant.DESTINATION_ACCOUNT_FORM_LABEL));
        form.add(this.destinationNameLabel);

        form.add(new JLabel(CommonConstant.CURRENT_BALANCE_FORM_FIELD));
        form.add(this.currentBalanceLabel);

        form.add(new JLabel(CommonConstant.TRANSFER_BALANCE_FORM_FIELD));
        JPanel transferAmountPanel = new JPanel(new GridLayout(1, 2));
        transferAmountPanel.add(this.transferCurrencyLabel);
        transferAmountPanel.add(this.transferAmountField);
        form.add(transferAmountPanel);

        return form;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        BoxLayout boxLayout = new BoxLayout(footer, BoxLayout.X_AXIS);
        footer.setLayout(boxLayout);

        JButton cancelButton = new JButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener((event) -> this.dispose());

        this.submitButton = new JButton(ButtonConstant.TRANSFER_BUTTON);
        this.submitButton.setEnabled(false);
        this.submitButton.addActionListener((event) -> onSubmit());

        footer.add(cancelButton);
        footer.add(this.submitButton);

        return footer;
    }

    private void buildClientComboBox() {
        DefaultComboBoxModel<Client> clientValues = new DefaultComboBoxModel<>();
        clientValues.addAll(this.clients);

        JComboBox<Client> comboBox = new JComboBox<>(clientValues);
        comboBox.addItemListener((event) -> {
            if (event.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                String clientId = ((Client) event.getItem()).getId();
                this.updateProductsOnClientChange(clientId);
            }
        });

        this.clientJComboBox = comboBox;
    }

    private void buildProductComboBox() {
        this.productValuesJComboBox = new DefaultComboBoxModel<>();

        JComboBox<Product> comboBox = new JComboBox<>(this.productValuesJComboBox);
        comboBox.addItemListener((event) -> {
            if (event.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                Product product = ((Product) event.getItem());
                this.updateOnChangeProduct(product);
            }
        });
        comboBox.setEnabled(false);
        this.productJComboBox = comboBox;
    }

    private void buildBalanceTextField() {
        JTextField textField = new JTextField();
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                submitButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                submitButton.setEnabled(!textField.getText().isBlank());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                submitButton.setEnabled(true);
            }
        });
        textField.setEnabled(false);
        this.transferAmountField = textField;
    }

    private void buildSearchAccountButton() {
        this.seachButton.setEnabled(false);
        this.seachButton.addActionListener((event) -> {
            String destinationAccount = this.destinationAccountField.getText();
            if (destinationAccount == null || destinationAccount.isBlank()) {
                JOptionPane.showMessageDialog(this, FeedbackConstant.EMPTY_DESTINATION_ACCOUNT_FIELD,
                        FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
                this.transferAmountField.setEnabled(false);
                return;
            }
            Optional<Product> productOptional = productService.findByIdOrAliasOrCbu(destinationAccount);
            if (productOptional.isEmpty()) {
                JOptionPane.showMessageDialog(this, FeedbackConstant.DESTINATION_ACCOUNT_NOT_FOUND,
                        FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
                this.destinationAccountField.setText("");
                this.transferAmountField.setEnabled(false);
                return;
            }
            this.destinationProduct = productOptional.get();
            if (this.destinationProduct.getId().equals(this.originProduct.getId())) {
                JOptionPane.showMessageDialog(this, FeedbackConstant.CANNOT_TRANSFER_TO_SAME_PRODUCT,
                        FeedbackConstant.INVALID_FIELD, JOptionPane.ERROR_MESSAGE);
                this.destinationAccountField.setText("");
                this.transferAmountField.setEnabled(false);
                return;
            }
            if (!this.destinationProduct.getType().getCurrency().equals(this.originProduct.getType().getCurrency())) {
                JOptionPane.showMessageDialog(this, FeedbackConstant.CURRENCY_ARE_NOT_SAME,
                        FeedbackConstant.INVALID_FIELD, JOptionPane.ERROR_MESSAGE);
                this.destinationAccountField.setText("");
                this.transferAmountField.setEnabled(false);
                return;
            }
            this.clients.stream()
                    .filter(client -> client.getId().equals(this.destinationProduct.getClientId()))
                    .findFirst()
                    .ifPresent((productClient) -> {
                        this.destinationNameLabel.setText(productClient.getName() + " " + productClient.getLastName());
                        this.transferAmountField.setEnabled(true);
                    });
        });
    }

    private boolean isBalanceValid() {
        if (this.originProduct.getBalance() == 0d) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean isFieldTextValid = this.isBalanceTextFieldValid();
        if (!isFieldTextValid) {
            return false;
        }
        double parsedBalance = Double.parseDouble(this.transferAmountField.getText());
        double newOriginBalance = this.originProduct.getBalance() - parsedBalance;
        if (newOriginBalance < 0) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isBalanceTextFieldValid() {
        String balance = this.transferAmountField.getText();
        if (balance == null || balance.isBlank()) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.INVALID_BALANCE_FIELD,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            double parsedBalance = Double.parseDouble(balance);
            if (parsedBalance < 0) {
                JOptionPane.showMessageDialog(this, FeedbackConstant.NEGATIVE_BALANCE_FIELD,
                        FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
                return false;
            }
            if (parsedBalance > CommonConstant.MAX_BALANCE_TO_ADD) {
                JOptionPane.showMessageDialog(this, FeedbackConstant.MAX_BALANCE_TO_TRANSFER_FIELD,
                        FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.NOT_NUMERIC_BALANCE_FIELD,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void updateProductsOnClientChange(String clientId) {
        List<Product> clientProducts = productService.findByClientId(clientId);
        this.productValuesJComboBox.removeAllElements();
        clientProducts.forEach(this.productValuesJComboBox::addElement);
        productJComboBox.setEnabled(true);
        this.destinationAccountField.setText("");
        this.destinationAccountField.setEnabled(false);
        this.seachButton.setEnabled(false);
        this.destinationNameLabel.setText("");
        this.currentBalanceLabel.setText("");
        this.transferCurrencyLabel.setText("");
    }

    private void updateOnChangeProduct(Product product) {
        this.originProduct = product;
        String currency = product.getType().getCurrency();
        currentBalanceLabel.setText(currency + product.getBalance());
        transferCurrencyLabel.setText(currency);
        this.destinationAccountField.setEnabled(true);
        this.seachButton.setEnabled(true);
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
            JOptionPane.showMessageDialog(this, FeedbackConstant.ERROR_TRANSFERING_MESSAGE,
                    FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
