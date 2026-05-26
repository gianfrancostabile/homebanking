package view.submenu;

import model.Client;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.function.Consumer;

public class SubMenuClientEditor extends AbstractCellEditor implements TableCellEditor {
    private SubMenuClientTable editPanel;
    private final Consumer<Client> onUpdate;
    private final Consumer<String> onDelete;

    public SubMenuClientEditor(Consumer<Client> onUpdate, Consumer<String> onDelete) {
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (value instanceof Client client) {
            editPanel = new SubMenuClientTable(client,
                    updatedClient -> {
                        stopCellEditing();
                        onUpdate.accept(updatedClient);
                    },
                    clientId -> {
                        stopCellEditing();
                        onDelete.accept(clientId);
                    });
            editPanel.setBackground(table.getSelectionBackground());
            return editPanel;
        }
        return new JLabel();
    }

    @Override
    public Object getCellEditorValue() {
        return editPanel != null ? editPanel.getClient() : null;
    }
}