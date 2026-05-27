package view.custom;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomComboBox<E> extends JComboBox<E> {

    private static final Color DEFAULT_BORDER_COLOR = new Color(204, 204, 204);
    private static final Color FOCUS_BORDER_COLOR = new Color(0, 123, 255);
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private Color currentBorderColor = DEFAULT_BORDER_COLOR;

    public CustomComboBox() {
        super();
        this.initComponents();
    }

    public CustomComboBox(DefaultComboBoxModel<E> model) {
        super(model);
        this.initComponents();
    }

    public CustomComboBox(E[] items) {
        super(items);
        this.initComponents();
    }

    private void initComponents() {
        this.setOpaque(false);
        this.setBackground(BACKGROUND_COLOR);
        this.setForeground(new Color(33, 37, 41));
        this.setFont(this.getFont().deriveFont(Font.PLAIN, 13f));

        this.setBorder(new EmptyBorder(4, 4, 4, 4));

        this.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, false);

                this.setBorder(new EmptyBorder(6, 10, 6, 10));
                this.setOpaque(true);

                if (index == -1) {
                    this.setBackground(BACKGROUND_COLOR);
                    this.setForeground(new Color(33, 37, 41));
                } else {
                    if (isSelected) {
                        this.setBackground(new Color(0, 123, 255));
                        this.setForeground(Color.WHITE);
                    } else {
                        this.setBackground(BACKGROUND_COLOR);
                        this.setForeground(new Color(33, 37, 41));
                    }
                }
                return this;
            }
        });

        this.cleanupChildComponents();

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

    private void cleanupChildComponents() {
        for (int i = 0; i < this.getComponentCount(); i++) {
            Component child = this.getComponent(i);
            if (child instanceof JComponent jChild) {
                jChild.setBorder(new EmptyBorder(0, 0, 0, 0));
                jChild.setOpaque(false);
            }
        }
        if (this.getEditor() != null && this.getEditor().getEditorComponent() instanceof JComponent jEditor) {
            jEditor.setBorder(new EmptyBorder(0, 0, 0, 0));
            jEditor.setOpaque(false);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        this.cleanupChildComponents();
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
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 8, 8);

        g2.dispose();
    }
}
