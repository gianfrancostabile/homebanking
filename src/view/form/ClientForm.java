package view.form;

import constant.*;
import model.Client;
import util.Dialog;
import view.custom.CustomButton;
import view.custom.CustomTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class ClientForm extends JFrame {

    private static final int MAX_FIELD_LENGTH = 30;

    // UI Components
    private CustomTextField nameField;
    private CustomTextField lastnameField;

    // State
    private String clientId = null;

    public ClientForm(Client clientToUpdate, Consumer<Client> onSuccessSubmit) {
        this.setupFrame();
        this.initComponents(clientToUpdate, onSuccessSubmit);
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(TitleConstant.ADD_CLIENT_FORM);
    }

    private void initComponents(Client clientToUpdate, Consumer<Client> onSuccessSubmit) {
        this.loadClientDataIfPresent(clientToUpdate);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel(TitleConstant.ADD_CLIENT_FORM, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(this.buildForm(), BorderLayout.CENTER);
        panel.add(this.buildFooter(onSuccessSubmit), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);

        this.nameField.grabFocus();
        this.setVisible(true);
    }

    private void loadClientDataIfPresent(Client client) {
        String nameValue = "";
        String lastnameValue = "";

        if (client != null) {
            this.clientId = client.getId();
            nameValue = client.getName();
            lastnameValue = client.getLastName();
        }

        this.nameField = new CustomTextField(nameValue);
        this.lastnameField = new CustomTextField(lastnameValue);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(2, 2, 5, 10));

        form.add(new JLabel(CommonConstant.NAME_FORM_FIELD));
        form.add(this.nameField);

        form.add(new JLabel(CommonConstant.LASTNAME_FORM_FIELD));
        form.add(this.lastnameField);

        return form;
    }

    private JPanel buildFooter(Consumer<Client> onSuccessSubmit) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        CustomButton cancelButton = new CustomButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener(event -> this.dispose());

        String submitText = (this.clientId == null) ? ButtonConstant.SUBMIT_BUTTON : ButtonConstant.UPDATE_SUBMIT_BUTTON;
        CustomButton submitButton = new CustomButton(submitText, ButtonVariant.CREATE);
        submitButton.addActionListener(event -> this.onSubmit(onSuccessSubmit));

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }

    private void onSubmit(Consumer<Client> onSuccessSubmit) {
        if (!this.isNameValid() || !this.isLastnameValid()) {
            return;
        }

        Client client = new Client(this.clientId, this.nameField.getText().trim(), this.lastnameField.getText().trim());
        onSuccessSubmit.accept(client);
        this.dispose();
    }

    private boolean isNameValid() {
        String name = this.nameField.getText();
        if (name == null || name.isBlank() || name.length() > MAX_FIELD_LENGTH) {
            Dialog.showWarning(this, FeedbackConstant.INVALID_NAME_FIELD);
            return false;
        }
        return true;
    }

    private boolean isLastnameValid() {
        String lastname = this.lastnameField.getText();
        if (lastname == null || lastname.isBlank() || lastname.length() > MAX_FIELD_LENGTH) {
            Dialog.showWarning(this, FeedbackConstant.INVALID_LASTNAME_FIELD);
            return false;
        }
        return true;
    }
}