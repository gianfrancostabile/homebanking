package view.form;

import builder.ProductBuilder;
import constant.*;
import enums.ButtonVariant;
import enums.ProductType;
import model.Client;
import model.Product;
import service.ProductService;
import view.custom.CustomButton;
import view.custom.CustomComboBox;
import view.table.ProductTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProductForm extends JFrame {

    // Services
    private final ProductService productService = ProductService.getInstance();

    // UI Components
    private CustomComboBox<ProductType> productTypeField;
    private final ProductTable productTable;

    // State
    private final List<Product> newProducts = new ArrayList<>();
    private final Client client;

    public ProductForm(Client client, Consumer<List<Product>> onSuccessSubmit) {
        this.client = client;
        this.productTable = new ProductTable(this::refillTable);
        this.refillTable();

        this.setupFrame();
        this.initComponents(onSuccessSubmit);
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(TitleConstant.CLIENT_PRODUCT_FORM);
    }

    private void initComponents(Consumer<List<Product>> onSuccessSubmit) {
        this.productTypeField = new CustomComboBox<>(ProductType.values());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel(TitleConstant.CLIENT_PRODUCT_FORM, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(this.buildCenter(), BorderLayout.CENTER);
        panel.add(this.buildFooter(onSuccessSubmit), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel buildCenter() {
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));

        centerPanel.add(this.buildForm(), BorderLayout.NORTH);
        centerPanel.add(this.productTable, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        CustomButton addProductButton = new CustomButton(ButtonVariant.CREATE);
        addProductButton.addActionListener(_ -> this.onAddProductClick());

        form.add(new JLabel(FormFieldConstant.PRODUCT));
        form.add(this.productTypeField);
        form.add(addProductButton);

        return form;
    }

    private JPanel buildFooter(Consumer<List<Product>> onSuccessSubmit) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        CustomButton cancelButton = new CustomButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener(_ -> this.dispose());

        CustomButton submitButton = new CustomButton(ButtonConstant.UPDATE_SUBMIT_BUTTON, ButtonVariant.CREATE);
        submitButton.addActionListener(_ -> {
            onSuccessSubmit.accept(this.newProducts);
            this.dispose();
        });

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }

    private void onAddProductClick() {
        ProductType selectedType = (ProductType) this.productTypeField.getSelectedItem();
        if (selectedType == null) {
            return;
        }

        Product product = ProductBuilder.build(selectedType, this.client);

        this.newProducts.add(product);
        this.productTable.append(product);
    }

    private void refillTable() {
        this.productTable.reAppend(this.productService.findByClientId(client.getId()));
    }
}