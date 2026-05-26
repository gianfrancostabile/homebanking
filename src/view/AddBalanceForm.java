package view;

import constant.ButtonConstant;
import constant.CommonConstant;
import constant.FeedbackConstant;
import constant.TitleConstant;
import model.Product;
import service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class AddBalanceForm extends JFrame {
    private final ProductService productService = new ProductService();
    private final JFormattedTextField balanceField;
    private final Product product;

    public AddBalanceForm(Product product, Consumer<Product> onSuccessSubmit) {
        this.product = product;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.balanceField = new JFormattedTextField();

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(TitleConstant.ADD_BALANCE_FORM), BorderLayout.NORTH);
        panel.add(this.buildForm(), BorderLayout.CENTER);
        panel.add(this.buildFooter(onSuccessSubmit), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(1, 2));

        form.add(new JLabel(CommonConstant.AMOUNT_FIELD));
        form.add(this.balanceField);
        return form;
    }

    private JPanel buildFooter(Consumer<Product> onSuccessSubmit) {
        JPanel footer = new JPanel();
        BoxLayout boxLayout = new BoxLayout(footer, BoxLayout.X_AXIS);
        footer.setLayout(boxLayout);

        JButton cancelButton = new JButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener((event) -> this.dispose());

        JButton submitButton = new JButton(ButtonConstant.SUBMIT_BUTTON);
        submitButton.addActionListener((event) -> {
            if (this.isBalanceValid()) {
                double parsedBalance = Double.parseDouble(balanceField.getText());
                try {
                    productService.transfer(null, product.getId(), parsedBalance);
                    product.addBalance(parsedBalance);
                    onSuccessSubmit.accept(product);
                    this.dispose();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(this, FeedbackConstant.ERROR_ADDING_PRODUCT_BALANCE_MESSAGE,
                            FeedbackConstant.INVALID_FIELD, JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }

    private boolean isBalanceValid() {
        String balance = balanceField.getText();
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
                JOptionPane.showMessageDialog(this, FeedbackConstant.MAX_BALANCE_TO_ADD_FIELD,
                        FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
                return false;
            }
            double newProductBalance = this.product.getBalance() + parsedBalance;
            if (newProductBalance >= CommonConstant.MAX_BALANCE) {
                JOptionPane.showMessageDialog(this, FeedbackConstant.MAX_BALANCE_REACHED_FIELD,
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
}

