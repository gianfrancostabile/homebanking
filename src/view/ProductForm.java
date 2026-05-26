package view;

import constant.*;
import model.Client;
import model.Product;
import builder.ProductBuilder;
import service.ProductService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductForm extends JFrame {

    private final JComboBox<ProductType> productTypeField;
    private final ProductService productService = new ProductService();
    private final List<Product> clientProducts;
    private final List<Product> newProducts = new ArrayList<>();
    private final ProductTable productTable = new ProductTable();

    public ProductForm(Client client, Consumer<List<Product>> onSuccessSubmit) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        clientProducts = productService.findByClientId(client.getId());
        productTypeField = new JComboBox<>(ProductType.values());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(TitleConstant.CLIENT_PRODUCT_FORM), BorderLayout.NORTH);
        panel.add(this.buildCenter(client), BorderLayout.CENTER);
        panel.add(this.buildFooter(onSuccessSubmit), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private JPanel buildForm(Client client) {
        JPanel form = new JPanel(new GridLayout(1, 3));

        JButton addProductButton = new JButton(ButtonConstant.ADD_ITEM);
        addProductButton.addActionListener((event) -> {
            Product product = ProductBuilder.build((ProductType) productTypeField.getSelectedItem(), client);
            clientProducts.add(product);
            newProducts.add(product);
            productTable.appendProduct(product);
        });

        form.add(new JLabel(CommonConstant.PRODUCT_FORM_FIELD));
        form.add(productTypeField);
        form.add(addProductButton);
        return form;
    }

    private JPanel buildCenter(Client client) {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        panel.add(buildForm(client));
        panel.add(buildProductList());
        return panel;
    }

    private JPanel buildProductList() {
        productTable.appendProducts(clientProducts);
        return productTable;
    }

    private JPanel buildFooter(Consumer<List<Product>> onSuccessSubmit) {
        JPanel footer = new JPanel();
        BoxLayout boxLayout = new BoxLayout(footer, BoxLayout.X_AXIS);
        footer.setLayout(boxLayout);

        JButton cancelButton = new JButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener((event) -> this.dispose());

        JButton submitButton = new JButton(ButtonConstant.UPDATE_SUBMIT_BUTTON);
        submitButton.addActionListener((event) -> {
            onSuccessSubmit.accept(newProducts);
            this.dispose();
        });

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }
}
