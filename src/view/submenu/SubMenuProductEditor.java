package view.submenu;

import model.Product;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SubMenuProductEditor extends AbstractCellEditor implements TableCellEditor {
    private final Consumer<Product> onSuccess;
    private SubMenuProductTable editPanel;

    public SubMenuProductEditor(Consumer<Product> onSuccess) {
        this.onSuccess = onSuccess;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value instanceof Product product) {
            editPanel = new SubMenuProductTable(product,
                    data -> {
                        stopCellEditing();
                        this.onSuccess.accept(data);
                    });
            editPanel.setBackground(table.getSelectionBackground());
            return editPanel;
        }
        return new JLabel();
    }

    @Override
    public Object getCellEditorValue() {
        return editPanel != null ? editPanel.getProduct() : null;
    }
}