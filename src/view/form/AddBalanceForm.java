package view.form;

import constant.*;
import enums.ButtonVariant;
import model.Product;
import service.ProductService;
import util.Dialog;
import view.custom.CustomButton;
import view.custom.CustomFormattedTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class AddBalanceForm extends JFrame {

    // Services
    private final ProductService productService = ProductService.getInstance();

    // UI Components
    private CustomFormattedTextField balanceField;

    // State
    private final Product product;

    // Actions
    private final Runnable onSuccessSubmit;

    public AddBalanceForm(Product product, Runnable onSuccessSubmit) {
        this.product = product;
        this.onSuccessSubmit = onSuccessSubmit;
        this.setupFrame();
        this.initComponents();
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(TitleConstant.ADD_BALANCE_FORM);
    }

    private void initComponents() {
        this.balanceField = new CustomFormattedTextField();

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel(TitleConstant.ADD_BALANCE_FORM, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(this.buildForm(), BorderLayout.CENTER);
        panel.add(this.buildFooter(), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(1, 2, 5, 0));

        form.add(new JLabel(FormFieldConstant.AMOUNT));
        form.add(this.balanceField);

        return form;
    }

    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        CustomButton cancelButton = new CustomButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener(_ -> this.dispose());

        CustomButton submitButton = new CustomButton(ButtonConstant.SUBMIT_BUTTON, ButtonVariant.CREATE);
        submitButton.addActionListener(_ -> this.onSubmit());

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }

    private void onSubmit() {
        if (!this.isBalanceValid()) {
            return;
        }

        double parsedBalance = Double.parseDouble(this.balanceField.getText());

        try {
            this.productService.deposit(this.product.getId(), parsedBalance);
            this.onSuccessSubmit.run();
            this.dispose();
        } catch (Exception exception) {
            Dialog.showError(this, FeedbackConstant.ERROR_ADDING_PRODUCT_BALANCE_MESSAGE);
        }
    }

    private boolean isBalanceValid() {
        String balance = this.balanceField.getText();

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
                Dialog.showWarning(this, FeedbackConstant.MAX_BALANCE_TO_ADD_FIELD);
                return false;
            }

            double newProductBalance = this.product.getBalance() + parsedBalance;
            if (newProductBalance >= CommonConstant.MAX_BALANCE) {
                Dialog.showWarning(this, FeedbackConstant.MAX_BALANCE_REACHED_FIELD);
                return false;
            }

            return true;
        } catch (NumberFormatException exception) {
            Dialog.showWarning(this, FeedbackConstant.NOT_NUMERIC_BALANCE_FIELD);
            return false;
        }
    }
}