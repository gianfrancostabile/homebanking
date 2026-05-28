package view.custom;

import enums.ButtonVariant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class CustomButton extends JButton {

    private final ButtonVariant variant;
    private final boolean isIconOnly;

    public CustomButton(ButtonVariant variant) {
        super("");
        this.variant = variant;
        this.isIconOnly = true;
        this.initComponents();
    }

    public CustomButton(String text, ButtonVariant variant) {
        super(text);
        this.variant = variant;
        this.isIconOnly = false;
        this.initComponents();
    }

    public CustomButton(String text) {
        this(text, ButtonVariant.DEFAULT);
    }

    private void initComponents() {
        this.setFont(this.getFont().deriveFont(Font.BOLD, 12f));
        this.setForeground(variant.getForegroundColor());
        this.setBackground(variant.getBackgroundColor());

        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (this.isIconOnly) {
            this.setBorder(new EmptyBorder(6, 8, 6, 8));
            this.loadIcon();
        } else {
            this.setBorder(new EmptyBorder(8, 15, 8, 15));
        }

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(variant.getBackgroundColor().brighter());
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(variant.getBackgroundColor());
                repaint();
            }
        });
    }

    private void loadIcon() {
        if (ButtonVariant.DEFAULT.equals(this.variant)) {
            return;
        }
        try {
            URL iconURL = getClass().getResource("/icons/" + variant.getIconName());
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                this.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception _) {
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(this.getBackground());
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 8, 8);

        g2.dispose();
        super.paintComponent(g);
    }
}
