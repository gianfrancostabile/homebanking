package view;

import constant.*;
import exception.JDBCException;
import model.Card;
import model.Client;
import model.Product;
import service.CardService;
import service.ProductService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PayForm extends JFrame {

    private final JLabel currentBalanceLabel = new JLabel(CommonConstant.CURRENT_BALANCE_FORM_FIELD);
    private final JLabel currentBalanceValueLabel = new JLabel();
    private final JLabel payCurrencyLabel = new JLabel();
    private final ProductService productService = new ProductService();
    private final CardService cardService = new CardService();
    private JComboBox<Client> clientJComboBox;
    private JComboBox<Card> cardJComboBox;
    private DefaultComboBoxModel<Card> cardValuesJComboBox;
    private JTextField payAmountField;
    private JButton submitButton = new JButton(ButtonConstant.TRANSFER_BUTTON);
    private final List<Client> clients;
    private List<Product> clientProducts;
    private Product selectedProduct;
    private Card selectedCard;

    public PayForm(List<Client> clients) {
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
        this.buildCardComboBox();
        this.buildBalanceTextField();

        JPanel form = new JPanel(new GridLayout(6, 2));
        form.add(new JLabel(CommonConstant.CLIENT_FIELD));
        form.add(this.clientJComboBox);

        form.add(new JLabel(CommonConstant.CARD_FORM_FIELD));
        form.add(this.cardJComboBox);

        form.add(this.currentBalanceLabel);
        form.add(this.currentBalanceValueLabel);

        form.add(new JLabel(CommonConstant.TRANSFER_BALANCE_FORM_FIELD));
        JPanel transferAmountPanel = new JPanel(new GridLayout(1, 2));
        transferAmountPanel.add(this.payCurrencyLabel);
        transferAmountPanel.add(this.payAmountField);
        form.add(transferAmountPanel);

        return form;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        BoxLayout boxLayout = new BoxLayout(footer, BoxLayout.X_AXIS);
        footer.setLayout(boxLayout);

        JButton cancelButton = new JButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener((event) -> this.dispose());

        this.submitButton = new JButton(ButtonConstant.PAY_SUBMIT_BUTTON);
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
                this.updateCardsOnClientChange(clientId);
            }
        });

        this.clientJComboBox = comboBox;
    }

    private void buildCardComboBox() {
        this.cardValuesJComboBox = new DefaultComboBoxModel<>();

        JComboBox<Card> comboBox = new JComboBox<>(this.cardValuesJComboBox);
        comboBox.addItemListener((event) -> {
            if (event.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                Card card = ((Card) event.getItem());
                this.updateOnChangeCard(card);
            }
        });
        comboBox.setEnabled(false);
        this.cardJComboBox = comboBox;
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
        this.payAmountField = textField;
    }

    private boolean isBalanceValid() {
        if ((CardType.DEBIT.equals(this.selectedCard.getType()) && this.selectedProduct.getBalance() <= 0d) ||
                (CardType.CREDIT.equals(this.selectedCard.getType()) && this.selectedCard.getAvailableDebtBalance() <= 0d)) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean isFieldTextValid = this.isBalanceTextFieldValid();
        if (!isFieldTextValid) {
            return false;
        }
        double parsedBalance = Double.parseDouble(this.payAmountField.getText());
        double sourceToRemove = CardType.DEBIT.equals(this.selectedCard.getType()) ? this.selectedProduct.getBalance() : this.selectedCard.getAvailableDebtBalance();
        double newOriginBalance = sourceToRemove - parsedBalance;
        if (newOriginBalance < 0) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.ORIGIN_PRODUCT_DOES_NOT_HAVE_BALANCE,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isBalanceTextFieldValid() {
        String balance = this.payAmountField.getText();
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

    private void updateCardsOnClientChange(String clientId) {
        this.clientProducts = productService.findByClientId(clientId);
        List<Card> clientCards = cardService.findByProductIdList(this.clientProducts.stream().map(Product::getId).collect(Collectors.toList()));
        this.cardValuesJComboBox.removeAllElements();
        clientCards.forEach(this.cardValuesJComboBox::addElement);
        cardJComboBox.setEnabled(true);
        this.currentBalanceValueLabel.setText("");
        this.payAmountField.setText("");
        this.payAmountField.setEnabled(false);
    }

    private void updateOnChangeCard(Card card) {
        String productId = card.getProductId();
        Optional<Product> productOptional = this.clientProducts.stream().filter(product -> product.getId().equals(productId)).findFirst();
        if (productOptional.isEmpty()) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.PRODUCT_CARD_NOT_FOUND,
                    FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }
        Product product = productOptional.get();
        String currency = product.getType().getCurrency();
        Double balance;
        if (CardType.CREDIT.equals(card.getType())) {
            currentBalanceLabel.setText(CommonConstant.CREDIT_LIMIT_FORM_FIELD);
            balance = card.getAvailableDebtBalance();
        } else {
            currentBalanceLabel.setText(CommonConstant.CURRENT_BALANCE_FORM_FIELD);
            balance = product.getBalance();
        }
        currentBalanceValueLabel.setText(currency + balance);
        payCurrencyLabel.setText(currency);
        this.payAmountField.setEnabled(true);
        this.selectedProduct = product;
        this.selectedCard = card;
    }

    private void onSubmit() {
        if (!this.isBalanceValid()) {
            return;
        }
        double parsedBalance = Double.parseDouble(this.payAmountField.getText());
        try {
            this.productService.payWithCard(this.selectedProduct.getId(), this.selectedCard, parsedBalance);
            JOptionPane.showMessageDialog(this, FeedbackConstant.PAYMENT_DONE,
                    FeedbackConstant.SUCCESS_TITLE, JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (JDBCException e) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.ERROR_PAYING_MESSAGE,
                    FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
