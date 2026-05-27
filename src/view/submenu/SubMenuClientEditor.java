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
            this.editPanel = new SubMenuClientTable(client,
                    updatedClient -> {
                        this.stopCellEditing();
                        this.onUpdate.accept(updatedClient);
                    },
                    clientId -> {
                        this.stopCellEditing();
                        this.onDelete.accept(clientId);
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
        return this.editPanel != null ? this.editPanel.getClient() : null;
    }
}