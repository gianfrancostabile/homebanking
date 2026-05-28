package view.custom;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public abstract class SubMenuCellEditor<T> extends AbstractCellEditor implements TableCellEditor {

    private final JPanel container;
    private T currentValue;

    protected SubMenuCellEditor() {
        this.container = new JPanel(new GridBagLayout());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        try {
            @SuppressWarnings("unchecked")
            T data = (T) value;

            if (data != null) {
                this.currentValue = data;
                this.container.removeAll();

                JPanel editPanel = this.buildPanel(data);

                Color rowColor = (row % 2 == 0) ? Color.WHITE : CustomTable.ALTERNATE_ROW_COLOR;

                this.container.setBackground(rowColor);
                this.container.setOpaque(true);
                editPanel.setOpaque(false);
                editPanel.setBackground(rowColor);

                this.container.add(editPanel);
                return this.container;
            }
        } catch (ClassCastException _) {
        }

        return new JLabel();
    }

    @Override
    public Object getCellEditorValue() {
        return this.currentValue;
    }

    protected abstract JPanel buildPanel(T data);
}