package view.submenu;

import constant.ButtonVariant;
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
import java.util.function.Consumer;

public class SubMenuClientTable extends JPanel {

    // Singletons / Services
    private final ClientService clientService = ClientService.getInstance();
    private final ProductService productService = ProductService.getInstance();
    private final CardService cardService = CardService.getInstance();

    // State
    private final Client client;

    public SubMenuClientTable(Client client, Consumer<Client> onUpdate, Consumer<String> onDelete) {
        this.client = client;
        this.initComponents(onUpdate, onDelete);
    }

    private void initComponents(Consumer<Client> onUpdate, Consumer<String> onDelete) {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 0));
        this.setOpaque(false);

        CustomButton simulateButton = new CustomButton(ButtonVariant.SEARCH);
        simulateButton.addActionListener(_ -> this.onSimulateClick());

        CustomButton handleProductsButton = new CustomButton(ButtonVariant.CREATE);
        handleProductsButton.addActionListener(_ -> this.onHandleProductsClick());

        CustomButton updateButton = new CustomButton(ButtonVariant.UPDATE);
        updateButton.addActionListener(_ -> this.onUpdateClick(onUpdate));

        CustomButton deleteButton = new CustomButton(ButtonVariant.DELETE);
        deleteButton.addActionListener(_ ->
                new ConfirmModal(FeedbackConstant.INFO_DELETE_CLIENT, () -> this.deleteClient(onDelete))
        );

        this.add(simulateButton);
        this.add(handleProductsButton);
        this.add(updateButton);
        this.add(deleteButton);
    }

    private void onSimulateClick() {
        new TransactionOverview(this.client);
    }

    private void onHandleProductsClick() {
        new ProductForm(this.client, this::processProductsSubmission);
    }

    private void processProductsSubmission(List<Product> products) {
        try {
            for (Product product : products) {
                this.productService.insert(product);
                String productId = product.getId();

                for (Card card : product.getCards()) {
                    card.setProductId(productId);
                    this.cardService.insert(card);
                }
            }
        } catch (Exception e) {
            Dialog.showError(this, FeedbackConstant.ERROR_UPDATING_CLIENT_PRODUCTS_MESSAGE);
        }
    }

    private void onUpdateClick(Consumer<Client> onUpdate) {
        new ClientForm(this.client, updatedClient -> {
            try {
                this.clientService.update(updatedClient);
                onUpdate.accept(updatedClient);
            } catch (Exception e) {
                Dialog.showError(this, FeedbackConstant.ERROR_UPDATING_CLIENT_MESSAGE);
            }
        });
    }

    public void deleteClient(Consumer<String> onDelete) {
        try {
            String clientId = this.client.getId();
            this.clientService.deleteById(clientId);
            onDelete.accept(clientId);
        } catch (Exception e) {
            Dialog.showError(this, FeedbackConstant.ERROR_DELETING_CLIENT_MESSAGE);
        }
    }

    public Client getClient() {
        return this.client;
    }
}