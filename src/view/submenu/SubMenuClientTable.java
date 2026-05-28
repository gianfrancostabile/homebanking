package view.submenu;

import enums.ButtonVariant;
import constant.FeedbackConstant;
import model.Card;
import model.Client;
import model.Product;
import service.CardService;
import service.ClientService;
import service.ProductService;
import util.Dialog;
import view.form.ClientForm;
import view.ConfirmModal;
import view.form.ProductForm;
import view.custom.CustomButton;
import view.overview.TransactionOverview;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SubMenuClientTable extends JPanel {

    // Services
    private final ClientService clientService = ClientService.getInstance();
    private final ProductService productService = ProductService.getInstance();
    private final CardService cardService = CardService.getInstance();

    // State
    private final Client client;

    // Actions
    private final Runnable onUpdate;
    private final Runnable onDelete;

    public SubMenuClientTable(Client client, Runnable onUpdate, Runnable onDelete) {
        this.client = client;
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        this.setOpaque(false);

        CustomButton simulateButton = new CustomButton(ButtonVariant.SEARCH);
        simulateButton.addActionListener(_ -> onSimulateClick());

        CustomButton handleProductsButton = new CustomButton(ButtonVariant.CREATE);
        handleProductsButton.addActionListener(_ -> onHandleProductsClick());

        CustomButton updateButton = new CustomButton(ButtonVariant.UPDATE);
        updateButton.addActionListener(_ -> onUpdateClick());

        CustomButton deleteButton = new CustomButton(ButtonVariant.DELETE);
        deleteButton.addActionListener(_ ->
                new ConfirmModal(FeedbackConstant.INFO_DELETE_CLIENT, this::deleteClient)
        );

        this.add(simulateButton);
        this.add(handleProductsButton);
        this.add(updateButton);
        this.add(deleteButton);
    }

    private void onSimulateClick() {
        new TransactionOverview(client);
    }

    private void onHandleProductsClick() {
        new ProductForm(client, this::processProductsSubmission);
    }

    private void processProductsSubmission(List<Product> products) {
        try {
            for (Product product : products) {
                productService.insert(product);
                String productId = product.getId();

                for (Card card : product.getCards()) {
                    card.setProductId(productId);
                    cardService.insert(card);
                }
            }
            onUpdate.run();

        } catch (Exception e) {
            Dialog.showError(this, FeedbackConstant.ERROR_UPDATING_CLIENT_PRODUCTS_MESSAGE);
        }
    }

    private void onUpdateClick() {
        new ClientForm(client, updatedClient -> {
            try {
                clientService.update(updatedClient);
                onUpdate.run();
            } catch (Exception e) {
                Dialog.showError(this, FeedbackConstant.ERROR_UPDATING_CLIENT_MESSAGE);
            }
        });
    }

    public void deleteClient() {
        try {
            clientService.deleteById(client.getId());
            onDelete.run();
        } catch (Exception e) {
            Dialog.showError(this, FeedbackConstant.ERROR_DELETING_CLIENT_MESSAGE);
        }
    }

    public Client getClient() {
        return client;
    }
}