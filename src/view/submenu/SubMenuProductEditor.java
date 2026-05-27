package view.submenu;

import model.Product;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
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
            this.editPanel = new SubMenuProductTable(product,
                    data -> {
                        this.stopCellEditing();
                        this.onSuccess.accept(data);
                    });

            JPanel container = new JPanel(new GridBagLayout());
            container.setOpaque(true);
            container.setBackground(table.getSelectionBackground());
            container.add(this.editPanel);

            return container;
        }

        return new JLabel();
    }

    @Override
    public Object getCellEditorValue() {
        return this.editPanel != null ? this.editPanel.getProduct() : null;
    }
}