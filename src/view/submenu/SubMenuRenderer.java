package view.submenu;

import model.Client;
import model.Product;
import view.custom.CustomTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SubMenuRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color rowColor = isSelected ? table.getSelectionBackground()
                : ((row % 2 == 0) ? Color.WHITE : CustomTable.ALTERNATE_ROW_COLOR);
        if (value instanceof Client client) {
            SubMenuClientTable buttonPanel = new SubMenuClientTable(client, null, null);
            buttonPanel.setOpaque(false);

            JPanel container = new JPanel(new GridBagLayout());
            container.setOpaque(true);
            container.setBackground(rowColor);
            container.add(buttonPanel);

            return container;
        } else if (value instanceof Product product && product.getId() != null) {
            SubMenuProductTable buttonPanel = new SubMenuProductTable(product, null);
            buttonPanel.setOpaque(false);

            JPanel container = new JPanel(new GridBagLayout());
            container.setOpaque(true);
            container.setBackground(rowColor);
            container.add(buttonPanel);

            return container;
        }
        return new JLabel();
    }
}