package view.custom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomTextField extends JTextField {

    private static final Color DEFAULT_BORDER_COLOR = new Color(204, 204, 204);
    private static final Color FOCUS_BORDER_COLOR = new Color(0, 123, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private Color currentBorderColor = DEFAULT_BORDER_COLOR;

    public CustomTextField(String value) {
        super(value);
        this.initComponents();
    }

    public CustomTextField() {
        super();
        this.initComponents();
    }

    private void initComponents() {
        this.setOpaque(false);
        this.setBackground(BACKGROUND_COLOR);
        this.setForeground(new Color(33, 37, 41));
        this.setFont(this.getFont().deriveFont(Font.PLAIN, 13f));

        this.setBorder(new EmptyBorder(8, 12, 8, 12));
        this.setCaretColor(FOCUS_BORDER_COLOR);

        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                currentBorderColor = FOCUS_BORDER_COLOR;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                currentBorderColor = DEFAULT_BORDER_COLOR;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(this.getBackground());
        g2.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);

        g2.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(currentBorderColor);
        g2.setStroke(new BasicStroke(1.5f)); // Grosor del borde
        g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);

        g2.dispose();
    }
}