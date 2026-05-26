package view.submenu;

import model.Product;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SubMenuProductRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Product product && product.getId() != null) {
            SubMenuProductTable renderPanel = new SubMenuProductTable(product, null);
            renderPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return renderPanel;
        }
        return new JLabel();
    }
}
