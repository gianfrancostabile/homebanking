package view;

import constant.*;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ClientForm extends JFrame {

    private String clientId = null;
    private final JTextField nameField;
    private final JTextField lastnameField;

    public ClientForm(Client clientToUpdate, Consumer<Client> onSuccessSubmit) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        String nameFieldValue = "";
        String lastnameFieldValue = "";
        if (clientToUpdate != null) {
            clientId = clientToUpdate.getId();
            nameFieldValue = clientToUpdate.getName();
            lastnameFieldValue = clientToUpdate.getLastName();
        }
        nameField = new JTextField(nameFieldValue);
        nameField.grabFocus();
        lastnameField = new JTextField(lastnameFieldValue);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(TitleConstant.ADD_CLIENT_FORM), BorderLayout.NORTH);
        panel.add(this.buildForm(), BorderLayout.CENTER);
        panel.add(this.buildFooter(onSuccessSubmit), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private JPanel buildForm() {
        JPanel form = new JPanel(new GridLayout(3, 2));
        form.add(new JLabel(CommonConstant.NAME_FORM_FIELD));
        form.add(nameField);
        form.add(new JLabel(CommonConstant.LASTNAME_FORM_FIELD));
        form.add(lastnameField);
        return form;
    }

    private JPanel buildFooter(Consumer<Client> onSuccessSubmit) {
        JPanel footer = new JPanel();
        BoxLayout boxLayout = new BoxLayout(footer, BoxLayout.X_AXIS);
        footer.setLayout(boxLayout);

        JButton cancelButton = new JButton(ButtonConstant.CANCEL_BUTTON);
        cancelButton.addActionListener((event) -> this.dispose());

        JButton submitButton = new JButton(clientId == null ? ButtonConstant.SUBMIT_BUTTON : ButtonConstant.UPDATE_SUBMIT_BUTTON);
        submitButton.addActionListener((event) -> {
            if (this.isNameValid() && this.isLastnameValid()) {
                Client client = new Client(clientId, nameField.getText(), lastnameField.getText());
                onSuccessSubmit.accept(client);
                dispose();
            }
        });

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }

    private boolean isNameValid() {
        String name = nameField.getText();
        if (name == null || name.isBlank() || name.length() > 30) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.INVALID_NAME_FIELD,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isLastnameValid() {
        String lastname = lastnameField.getText();
        if (lastname == null || lastname.isBlank() || lastname.length() > 30) {
            JOptionPane.showMessageDialog(this, FeedbackConstant.INVALID_LASTNAME_FIELD,
                    FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
