package view;

import builder.ProductBuilder;
import constant.ButtonConstant;
import constant.CommonConstant;
import constant.FeedbackConstant;
import constant.ProductType;
import model.Client;
import model.Product;
import service.ClientService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientOverview extends JPanel {
    private final List<Client> clients;
    private final ClientTable clientTable;
    private final ClientService clientService = new ClientService();

    public ClientOverview() {
        BoxLayout layoutConfiguration = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layoutConfiguration);

        this.clients = clientService.findAll();
        this.clientTable = new ClientTable();
        clientTable.appendPeople(clients);

        this.add(this.buildButtons());
        this.add(this.clientTable);
    }

    private JPanel buildButtons() {
        JPanel form = new JPanel(new GridLayout(1, 3));

        JButton addClientButton = new JButton(ButtonConstant.ADD_CLIENT_BUTTON);
        addClientButton.addActionListener((event) ->
                new ClientForm(null, (person -> {
                    try {
                        this.clientService.insert(person);
                        this.clientTable.appendPerson(person);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, FeedbackConstant.ERROR_ADDING_CLIENT_MESSAGE,
                                FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
                    }
                }))
        );

        JButton transferButton = new JButton(ButtonConstant.TRANSFER_BUTTON);
        transferButton.addActionListener((event) -> new TransferForm(this.clientService.findAll()));

        JButton payButton = new JButton(ButtonConstant.PAY_BUTTON);
        payButton.addActionListener((event) -> new PayForm(this.clientService.findAll()));

        form.add(addClientButton);
        form.add(transferButton);
        form.add(payButton);
        return form;
    }
}
