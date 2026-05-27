package view.submenu;

import constant.TransactionType;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TransactionTypeRenderer extends DefaultTableCellRenderer {

    private static final Color RED_BACKGROUND = new Color(255, 230, 230);
    private static final Color RED_TEXT = new Color(153, 0, 0);

    private static final Color GREEN_BACKGROUND = new Color(230, 255, 230);
    private static final Color GREEN_TEXT = new Color(0, 102, 0);

    private static final Color YELLOW_BACKGROUND = new Color(255, 255, 204);
    private static final Color YELLOW_TEXT = new Color(153, 102, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        this.setHorizontalAlignment(SwingConstants.CENTER);

        if (value instanceof TransactionType type) {
            this.setText(type.getPrettyName());
            switch (type) {
                case DEBIT:
                    this.setBackground(RED_BACKGROUND);
                    this.setForeground(RED_TEXT);
                    break;

                case CHARGE:
                    this.setBackground(GREEN_BACKGROUND);
                    this.setForeground(GREEN_TEXT);
                    break;

                case TO_PAY:
                    this.setBackground(YELLOW_BACKGROUND);
                    this.setForeground(YELLOW_TEXT);
                    break;

                default:
                    this.setBackground(table.getBackground());
                    this.setForeground(table.getForeground());
                    break;
            }
        }

        if (isSelected) {
            this.setBackground(table.getSelectionBackground());
        }

        return this;
    }
}