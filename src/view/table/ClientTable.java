package view.table;

import constant.TableHeaderConstant;
import model.Client;
import view.custom.CustomTable;
import view.submenu.SubMenuClientEditor;
import view.submenu.SubMenuRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;

public class ClientTable extends CustomTable<Client> {

    private static final String[] COLUMNS = {
            TableHeaderConstant.ID,
            TableHeaderConstant.NAME,
            TableHeaderConstant.LASTNAME,
            TableHeaderConstant.ACTIONS
    };
    private static final int ACTIONS_COLUMN_INDEX = 3;
    private static final int ACTIONS_COLUMN_PREFERRED_WIDTH = 200;

    // Actions
    private final Runnable onUpdate;
    private final Runnable onDelete;

    public ClientTable(Runnable onUpdate, Runnable onDelete) {
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
        super(COLUMNS, (_, column) -> column == ACTIONS_COLUMN_INDEX);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        TableColumn actionColumn = this.getTable().getColumnModel().getColumn(ACTIONS_COLUMN_INDEX);
        actionColumn.setPreferredWidth(ACTIONS_COLUMN_PREFERRED_WIDTH);
        actionColumn.setCellRenderer(new SubMenuRenderer());
        actionColumn.setCellEditor(new SubMenuClientEditor(this.onUpdate, this.onDelete));
    }

    @Override
    protected Object[] mapToRow(Client data) {
        return new Object[]{
                data.getId(),
                data.getName(),
                data.getLastName(),
                data
        };
    }
}