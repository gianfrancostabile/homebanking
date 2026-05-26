package view.submenu;

import model.Client;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SubMenuClientRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Client client) {
            SubMenuClientTable renderPanel = new SubMenuClientTable(client, null, null);
            renderPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return renderPanel;
        }
        return new JLabel();
    }
}
