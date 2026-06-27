package view.submenu;

import constant.CommonConstant;
import model.Transaction;
import view.custom.CustomTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;

public class TransactionAmountRenderer extends DefaultTableCellRenderer {
    private final DecimalFormat formatter = new DecimalFormat(CommonConstant.NUMBER_FORMAT);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Color rowColor = isSelected ? table.getSelectionBackground()
                : ((row % 2 == 0) ? Color.WHITE : CustomTable.ALTERNATE_ROW_COLOR);
        this.setBackground(rowColor);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        if (value instanceof Transaction data) {
            String amountField = data.getCurrency().getSign() + formatter.format(data.getAmount());
            this.setValue(amountField);
        }
        return this;
    }
}
