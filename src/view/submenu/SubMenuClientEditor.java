package view.submenu;

import model.Client;
import view.custom.SubMenuCellEditor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class SubMenuClientEditor extends SubMenuCellEditor<Client> {

    private final Runnable onUpdate;
    private final Runnable onDelete;

    public SubMenuClientEditor(Runnable onUpdate, Runnable onDelete) {
        this.onUpdate = onUpdate;
        this.onDelete = onDelete;
        super();
    }

    @Override
    protected JPanel buildPanel(Client data) {
        return new SubMenuClientTable(data, onUpdate, onDelete);
    }
}