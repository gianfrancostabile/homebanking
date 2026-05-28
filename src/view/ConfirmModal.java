package view;

import constant.ButtonConstant;
import enums.ButtonVariant;
import view.custom.CustomButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfirmModal extends JDialog {

    public ConfirmModal(String message, Runnable onYes) {
        super((Frame) null, true);
        this.setupDialog();
        this.initComponents(message, onYes);
    }

    private void setupDialog() {
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }

    private void initComponents(String message, Runnable onYes) {
        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);

        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(this.buildFooter(onYes), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel buildFooter(Runnable onYes) {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        CustomButton cancelButton = new CustomButton(ButtonConstant.NO_BUTTON, ButtonVariant.DELETE);
        cancelButton.addActionListener(event -> this.dispose());

        CustomButton submitButton = new CustomButton(ButtonConstant.YES_BUTTON);
        submitButton.addActionListener(event -> {
            onYes.run();
            this.dispose();
        });

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }
}