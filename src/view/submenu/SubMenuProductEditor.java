package view.submenu;

import model.Client;
import model.Product;
import view.custom.SubMenuCellEditor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class SubMenuProductEditor extends SubMenuCellEditor<Product> {

    private final Runnable onSuccess;

    public SubMenuProductEditor(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        super();
    }

    @Override
    protected JPanel buildPanel(Product data) {
        return new SubMenuProductTable(data, () -> {
            stopCellEditing();
            onSuccess.run();
        });
    }
}