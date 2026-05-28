package view.submenu;

import enums.ButtonVariant;
import model.Product;
import view.custom.CustomButton;
import view.form.AddBalanceForm;
import view.overview.CardsOverview;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SubMenuProductTable extends JPanel {

    private final Product product;

    public SubMenuProductTable(Product product, Consumer<Product> onSuccess) {
        this.product = product;
        this.initComponents(onSuccess);
    }

    private void initComponents(Consumer<Product> onSuccess) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 6, 0));
        this.setOpaque(false);

        CustomButton addBalanceButton = new CustomButton(ButtonVariant.CREATE);
        addBalanceButton.addActionListener(event -> this.onAddBalanceClick(onSuccess));

        CustomButton simulateButton = new CustomButton(ButtonVariant.SEARCH);
        simulateButton.addActionListener(event -> this.onSimulateClick());

        this.add(addBalanceButton);
        this.add(simulateButton);
    }

    private void onAddBalanceClick(Consumer<Product> onSuccess) {
        new AddBalanceForm(this.product, onSuccess);
    }

    private void onSimulateClick() {
        new CardsOverview(this.product.getCards());
    }

    public Product getProduct() {
        return this.product;
    }
}