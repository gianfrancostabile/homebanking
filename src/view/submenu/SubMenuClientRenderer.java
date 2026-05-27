package view.submenu;

import model.Client;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SubMenuClientRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Client client) {
            SubMenuClientTable buttonPanel = new SubMenuClientTable(client, null, null);
            JPanel container = new JPanel(new GridBagLayout());
            container.setOpaque(true);
            container.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            container.add(buttonPanel);
            return container;
        }
        return new JLabel();
    }
}