package view;

import constant.CommonConstant;
import constant.ProductType;
import model.Product;
import view.submenu.SubMenuProductEditor;
import view.submenu.SubMenuProductRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class ProductTable extends JPanel {

    private static final String[] COLUMNS = {
        CommonConstant.PRODUCT_TYPE_HEADER,
        CommonConstant.ALIAS_HEADER,
        CommonConstant.CBU_HEADER,
        CommonConstant.BALANCE_HEADER,
        CommonConstant.ACTIONS_TABLE_HEADER
    };

    private final DefaultTableModel tableModel;

    public ProductTable() {
        this.tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(27);
        TableColumn actionColumn = table.getColumnModel().getColumn(4);
        actionColumn.setPreferredWidth(125);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        actionColumn.setCellRenderer(new SubMenuProductRenderer());
        actionColumn.setCellEditor(new SubMenuProductEditor(this::updateProduct));

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void appendProduct(Product product) {
        String cbu = product.getCbu();
        if (cbu == null || cbu.isBlank()) {
            cbu = "";
        }
        String alias = product.getAlias();
        if (alias == null || alias.isBlank()) {
            alias = "";
        }
        ProductType type = product.getType();
        String balance = type.getCurrency() + product.getBalance().toString();
        tableModel.addRow(new Object[]{type, alias, cbu, balance, product});
    }

    public void appendProducts(List<Product> products) {
        products.forEach(this::appendProduct);
    }

    private void updateProduct(Product product) {
        int row = findRowById(product.getAlias());
        if (row == -1) {
            return;
        }
        ProductType type = product.getType();
        String balance = type.getCurrency() + product.getBalance().toString();
        tableModel.setValueAt(type, row, 0);
        tableModel.setValueAt(product.getAlias(), row, 1);
        tableModel.setValueAt(product.getCbu(), row, 2);
        tableModel.setValueAt(balance, row, 3);
        tableModel.setValueAt(product, row, 4);
    }

    private int findRowById(String alias) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).equals(alias)) {
                return i;
            }
        }
        return -1;
    }
}
