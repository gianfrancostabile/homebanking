package view;

import constant.ButtonConstant;

import javax.swing.*;
import java.awt.*;

public class ConfirmModal extends JFrame {

    public ConfirmModal(String message, Runnable onYes) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(message), BorderLayout.CENTER);
        panel.add(this.buildFooter(onYes), BorderLayout.SOUTH);

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private JPanel buildFooter(Runnable onYes) {
        JPanel footer = new JPanel();
        BoxLayout boxLayout = new BoxLayout(footer, BoxLayout.X_AXIS);
        footer.setLayout(boxLayout);

        JButton cancelButton = new JButton(ButtonConstant.NO_BUTTON);
        cancelButton.addActionListener((event) -> this.dispose());

        JButton submitButton = new JButton(ButtonConstant.YES_BUTTON);
        submitButton.addActionListener((event) -> {
            onYes.run();
            this.dispose();
        });

        footer.add(cancelButton);
        footer.add(submitButton);

        return footer;
    }
}

