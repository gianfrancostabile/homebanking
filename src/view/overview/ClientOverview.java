package view.overview;

import constant.ButtonConstant;
import constant.FeedbackConstant;
import model.Client;
import service.ClientService;
import view.form.ClientForm;
import view.custom.CustomButton;
import view.table.ClientTable;
import view.form.PayForm;
import view.form.TransferForm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ClientOverview extends JPanel {

    // Services
    private final ClientService clientService = ClientService.getInstance();

    // UI Components
    private ClientTable clientTable;

    // State
    private List<Client> clients;

    public ClientOverview() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));

        this.clients = this.clientService.findAll();

        this.clientTable = new ClientTable();
        this.clientTable.appendPeople(this.clients);

        this.add(this.buildActionPanel(), BorderLayout.NORTH);
        this.add(this.clientTable, BorderLayout.CENTER);
    }

    private JPanel buildActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        CustomButton addClientButton = new CustomButton(ButtonConstant.ADD_CLIENT_BUTTON);
        addClientButton.addActionListener(event -> this.onAddClientClick());

        CustomButton transferButton = new CustomButton(ButtonConstant.TRANSFER_BUTTON);
        transferButton.addActionListener(event -> new TransferForm(this.clientService.findAll()));

        CustomButton payButton = new CustomButton(ButtonConstant.PAY_BUTTON);
        payButton.addActionListener(event -> new PayForm(this.clientService.findAll()));

        actionPanel.add(addClientButton);
        actionPanel.add(transferButton);
        actionPanel.add(payButton);

        return actionPanel;
    }

    private void onAddClientClick() {
        new ClientForm(null, newClient -> {
            try {
                this.clientService.insert(newClient);
                this.clientTable.appendPerson(newClient);

                this.clients.add(newClient);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        this,
                        FeedbackConstant.ERROR_ADDING_CLIENT_MESSAGE,
                        FeedbackConstant.ERROR_TITLE,
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}