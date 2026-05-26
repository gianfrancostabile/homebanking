package view.submenu;

import constant.ButtonConstant;
import model.Card;
import model.Product;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SubMenuProductTable extends JPanel {
    private final Product product;

    public SubMenuProductTable(Product product, Consumer<Product> onSuccess) {
        this.product = product;
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        JButton simulateButton = new JButton(ButtonConstant.SIMULATE_BUTTON);
        simulateButton.addActionListener((event) -> new CardsOverview(product.getCards()));

        JButton addBalanceButton = new JButton(ButtonConstant.ADD_ITEM);
        addBalanceButton.addActionListener((event) -> new AddBalanceForm(product, onSuccess));

        this.add(addBalanceButton);
        this.add(simulateButton);
    }

    public Product getProduct() {
        return product;
    }
}
