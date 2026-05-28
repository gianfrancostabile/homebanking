package view.overview;

import constant.ButtonConstant;
import constant.FeedbackConstant;
import service.ClientService;
import util.Dialog;
import view.custom.CustomButton;
import view.form.ClientForm;
import view.form.PayForm;
import view.form.TransferForm;
import view.table.ClientTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ClientOverview extends JPanel {

    // Services
    private final ClientService clientService = ClientService.getInstance();

    // UI Components
    private ClientTable clientTable;

    public ClientOverview() {
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(15, 15, 15, 15));

        this.clientTable = new ClientTable(this::refillTable, this::refillTable);
        this.refillTable();

        this.add(this.buildActionPanel(), BorderLayout.NORTH);
        this.add(this.clientTable, BorderLayout.CENTER);
    }

    private JPanel buildActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        CustomButton addClientButton = new CustomButton(ButtonConstant.ADD_CLIENT_BUTTON);
        addClientButton.addActionListener(_ -> this.onAddClientClick());

        CustomButton transferButton = new CustomButton(ButtonConstant.TRANSFER_BUTTON);
        transferButton.addActionListener(_ -> new TransferForm(this.clientService.findAll()));

        CustomButton payButton = new CustomButton(ButtonConstant.PAY_BUTTON);
        payButton.addActionListener(_ -> new PayForm(this.clientService.findAll()));

        actionPanel.add(addClientButton);
        actionPanel.add(transferButton);
        actionPanel.add(payButton);

        return actionPanel;
    }

    private void onAddClientClick() {
        new ClientForm(null, newClient -> {
            try {
                this.clientService.insert(newClient);
                this.refillTable();
            } catch (Exception e) {
                Dialog.showError(this, FeedbackConstant.ERROR_ADDING_CLIENT_MESSAGE);
            }
        });
    }

    private void refillTable() {
        this.clientTable.reAppend(this.clientService.findAll());
    }
}