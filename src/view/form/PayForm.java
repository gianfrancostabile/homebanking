package view.form;

import constant.*;
import enums.ButtonVariant;
import enums.CardType;
import exception.JDBCException;
import model.Card;
import model.Client;
import model.Product;
import service.CardService;
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
import java.util.stream.Collectors;

public class PayForm extends JFrame {

    // Services
    private final ProductService productService = ProductService.getInstance();
    private final CardService cardService = CardService.getInstance();

    // UI Components
    private final JLabel currentBalanceLabel = new JLabel(FormFieldConstant.CURRENT_BALANCE);
    private final JLabel currentBalanceValueLabel = new JLabel();
    private final JLabel payCurrencyLabel = new JLabel();
    private CustomComboBox<Client> clientJComboBox;
    private CustomComboBox<Card> cardJComboBox;
    private DefaultComboBoxModel<Card> cardValuesJComboBox;
    private CustomTextField payAmountField;
    private CustomButton submitButton;

    // State
    private final List<Client> clients;
    private List<Product> clientProducts;
    private Product selectedProduct;
    private Card selectedCard;

    public PayForm(List<Client> clients) {
        this.clients = clients;
        this.setupFrame();
        this.initComponents();
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(TitleConstant.PAY_FORM);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel(TitleConstant.PAY_FORM, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(this.buildForm(), BorderLayout.CENTER);
        panel.add(this.buildFooter(), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel buildForm() {
        this.buildClientComboBox();
        this.buildCardComboBox();
        this.buildBalanceTextField();

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 10)); // 4 filas, 2 columnas, con espacio entre celdas

        form.add(new JLabel(FormFieldConstant.CLIENT));
        form.add(this.clientJComboBox);

        form.add(new JLabel(FormFieldConstant.CARD));
        form.add(this.cardJComboBox);

        form.add(this.currentBalanceLabel);
        form.add(this.currentBalanceValueLabel);

        form.add(new JLabel(FormFieldConstant.TRANSFER_BALANCE));

        JPanel transferAmountPanel = new JPanel(new BorderLayout(5, 0));
        transferAmountPanel.add(this.payCurrencyLabel, BorderLayout.WEST);
        transferAmountPanel.add(this.payAmountField, BorderLayout.CENTER);
        form.add(transferAmountPanel);

        return form;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        CustomButton cancelButton = new CustomButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener(event -> this.dispose());

        this.submitButton = new CustomButton(ButtonConstant.PAY_SUBMIT_BUTTON, ButtonVariant.CREATE);
        this.submitButton.setEnabled(false);
        this.submitButton.addActionListener(event -> onSubmit());

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
                this.updateCardsOnClientChange(clientId);
            }
        });
    }

    private void buildCardComboBox() {
        this.cardValuesJComboBox = new DefaultComboBoxModel<>();
        this.cardJComboBox = new CustomComboBox<>(this.cardValuesJComboBox);
        this.cardJComboBox.setEnabled(false);

        this.cardJComboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                Card card = (Card) event.getItem();
                if (card != null) {
                    this.updateOnChangeCard(card);
                }
            }
        });
    }

    private void buildBalanceTextField() {
        this.payAmountField = new CustomTextField();
        this.payAmountField.setEnabled(false);
        this.payAmountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { toggleSubmitButton(); }
            @Override public void removeUpdate(DocumentEvent e) { toggleSubmitButton(); }
            @Override public void changedUpdate(DocumentEvent e) { toggleSubmitButton(); }

            private void toggleSubmitButton() {
                submitButton.setEnabled(!payAmountField.getText().isBlank());
            }
        });
    }

    private void updateCardsOnClientChange(String clientId) {
        this.clientProducts = productService.findByClientId(clientId);

        List<String> productIds = this.clientProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<Card> clientCards = cardService.findByProductIdList(productIds);

        this.cardValuesJComboBox.removeAllElements();
        clientCards.forEach(this.cardValuesJComboBox::addElement);

        this.cardJComboBox.setEnabled(true);
        this.currentBalanceValueLabel.setText("");
        this.payAmountField.setText("");
        this.payAmountField.setEnabled(false);

        if (!clientProducts.isEmpty()) {
            this.updateOnChangeCard((Card) this.cardJComboBox.getSelectedItem());
        } else {
            this.selectedProduct = null;
            this.selectedCard = null;
        }
    }

    private void updateOnChangeCard(Card card) {
        if (card == null) {
            return;
        }
        Optional<Product> productOptional = this.clientProducts.stream()
                .filter(product -> product.getId().equals(card.getProductId()))
                .findFirst();

        if (productOptional.isEmpty()) {
            Dialog.showError(this, FeedbackConstant.PRODUCT_CARD_NOT_FOUND);
            return;
        }

        this.selectedProduct = productOptional.get();
        this.selectedCard = card;

        String currency = this.selectedProduct.getType().getCurrency().getSign();
        double balance = getAvailableBalance();

        this.currentBalanceLabel.setText(CardType.CREDIT.equals(card.getType())
                ? FormFieldConstant.CREDIT_LIMIT
                : FormFieldConstant.CURRENT_BALANCE);

        this.currentBalanceValueLabel.setText(currency + balance);
        this.payCurrencyLabel.setText(currency);
        this.payAmountField.setEnabled(true);
    }

    private double getAvailableBalance() {
        return CardType.CREDIT.equals(this.selectedCard.getType())
                ? this.selectedCard.getAvailableDebtBalance()
                : this.selectedProduct.getBalance();
    }

    private boolean isBalanceValid() {
        if (!isBalanceTextFieldValid()) {
            return false;
        }

        double availableBalance = getAvailableBalance();
        if (availableBalance <= 0) {
            Dialog.showError(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE);
            return false;
        }

        double parsedBalance = Double.parseDouble(this.payAmountField.getText());
        if (availableBalance - parsedBalance < 0) {
            Dialog.showError(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE);
            return false;
        }

        return true;
    }

    private boolean isBalanceTextFieldValid() {
        String balanceText = this.payAmountField.getText();

        if (balanceText == null || balanceText.isBlank()) {
            Dialog.showWarning(this, FeedbackConstant.INVALID_BALANCE_FIELD);
            return false;
        }

        try {
            double parsedBalance = Double.parseDouble(balanceText);
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
            Dialog.showWarning(this, FeedbackConstant.NOT_NUMERIC_BALANCE_FIELD);
            return false;
        }
    }

    private void onSubmit() {
        if (!this.isBalanceValid()) {
            return;
        }

        double parsedBalance = Double.parseDouble(this.payAmountField.getText());

        try {
            this.productService.payWithCard(this.selectedProduct.getId(), this.selectedCard, parsedBalance);
            Dialog.showSuccess(this, FeedbackConstant.PAYMENT_DONE);
            this.dispose();
        } catch (JDBCException e) {
            Dialog.showError(this, FeedbackConstant.ERROR_PAYING_MESSAGE);
        }
    }
}