package view.submenu;

import enums.ButtonVariant;
import model.Product;
import view.custom.CustomButton;
import view.form.AddBalanceForm;
import view.overview.CardsOverview;

import javax.swing.*;
import java.awt.*;

public class SubMenuProductTable extends JPanel {

    private final Runnable onSuccess;
    private final Product product;

    public SubMenuProductTable(Product product, Runnable onSuccess) {
        this.product = product;
        this.onSuccess = onSuccess;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));
        this.setOpaque(false);

        CustomButton addBalanceButton = new CustomButton(ButtonVariant.CREATE);
        addBalanceButton.addActionListener(_ -> onAddBalanceClick());

        CustomButton simulateButton = new CustomButton(ButtonVariant.SEARCH);
        simulateButton.addActionListener(_ -> onSimulateClick());

        this.add(addBalanceButton);
        this.add(simulateButton);
    }

    private void onAddBalanceClick() {
        new AddBalanceForm(this.product, onSuccess);
    }

    private void onSimulateClick() {
        new CardsOverview(this.product.getCards());
    }

    public Product getProduct() {
        return this.product;
    }
}