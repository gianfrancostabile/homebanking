package view.submenu;

import constant.ButtonConstant;
import constant.FeedbackConstant;
import model.Card;
import model.Client;
import model.Product;
import service.CardService;
import service.ClientService;
import service.ProductService;
import view.ClientForm;
import view.ConfirmModal;
import view.ProductForm;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class SubMenuClientTable extends JPanel {

    private final ClientService clientService = new ClientService();
    private final ProductService productService = new ProductService();
    private final CardService cardService = new CardService();
    private final Client client;

    public SubMenuClientTable(Client client, Consumer<Client> onUpdate, Consumer<String> onDelete) {
        this.client = client;
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        JButton simulateButton = new JButton(ButtonConstant.SIMULATE_BUTTON);
        simulateButton.addActionListener((event) -> {
        });
        JButton handleProductsButton = new JButton(ButtonConstant.ADD_ITEM);
        handleProductsButton.addActionListener((event) -> {
            new ProductForm(client, (products) -> {
                try {
                    for (Product product : products) {
                        productService.insert(product);
                        String productId = product.getId();
                        for (Card card : product.getCards()) {
                            card.setProductId(productId);
                            cardService.insert(card);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, FeedbackConstant.ERROR_UPDATING_CLIENT_PRODUCTS_MESSAGE,
                            FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                }
            });
        });
        JButton updateButton = new JButton(ButtonConstant.UPDATE_BUTTON);
        updateButton.addActionListener((event) ->
                new ClientForm(client, (updatedClient -> {
                    try {
                        this.clientService.update(updatedClient);
                        onUpdate.accept(updatedClient);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, FeedbackConstant.ERROR_UPDATING_CLIENT_MESSAGE,
                                FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                }))
        );
        JButton deleteButton = new JButton(ButtonConstant.DELETE_CLIENT);
        deleteButton.addActionListener((event) ->
                new ConfirmModal(FeedbackConstant.INFO_DELETE_CLIENT, () -> deleteClient(onDelete))
        );

        this.add(simulateButton);
        this.add(handleProductsButton);
        this.add(updateButton);
        this.add(deleteButton);
    }

    public Client getClient() {
        return client;
    }

    public void deleteClient(Consumer<String> onDelete) {
        try {
            String clientId = client.getId();
            this.productService.deleteByClientId(clientId);
            this.clientService.deleteById(clientId);
            onDelete.accept(clientId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.ERROR_DELETING_CLIENT_MESSAGE,
                    FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
