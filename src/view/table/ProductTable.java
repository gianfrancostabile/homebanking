package view.table;

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

    private static final int ALIAS_COLUMN_INDEX = 1;
    private static final int ACTIONS_COLUMN_INDEX = 4;
    private static final int ACTIONS_COLUMN_PREFERRED_WIDTH = 125;
    private static final int NOT_FOUND_ROW = -1;

    private final DefaultTableModel tableModel;

    public ProductTable() {
        this.tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == ACTIONS_COLUMN_INDEX;
            }
        };

        this.initComponents();
    }

    private void initComponents() {
        JTable table = new JTable(this.tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(CommonConstant.ROW_HEIGHT);

        TableColumn actionColumn = table.getColumnModel().getColumn(ACTIONS_COLUMN_INDEX);
        actionColumn.setPreferredWidth(ACTIONS_COLUMN_PREFERRED_WIDTH);
        actionColumn.setCellRenderer(new SubMenuProductRenderer());
        actionColumn.setCellEditor(new SubMenuProductEditor(this::updateProduct));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 200));

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void appendProduct(Product product) {
        if (product == null) return;

        String cbu = (product.getCbu() == null) ? "" : product.getCbu().trim();
        String alias = (product.getAlias() == null) ? "" : product.getAlias().trim();
        ProductType type = product.getType();
        String balanceText = this.formatBalance(type, product.getBalance());

        this.tableModel.addRow(new Object[]{type, alias, cbu, balanceText, product});
    }

    public void appendProducts(List<Product> products) {
        if (products != null) {
            products.forEach(this::appendProduct);
        }
    }

    private void updateProduct(Product product) {
        if (product == null) return;

        int row = this.findRowById(product.getAlias());
        if (row == NOT_FOUND_ROW) {
            return;
        }

        ProductType type = product.getType();
        String balanceText = this.formatBalance(type, product.getBalance());

        this.tableModel.setValueAt(type, row, 0);
        this.tableModel.setValueAt(product.getAlias(), row, ALIAS_COLUMN_INDEX);
        this.tableModel.setValueAt(product.getCbu(), row, 2);
        this.tableModel.setValueAt(balanceText, row, 3);
        this.tableModel.setValueAt(product, row, ACTIONS_COLUMN_INDEX);
    }

    private int findRowById(String alias) {
        if (alias == null) return NOT_FOUND_ROW;

        for (int i = 0; i < this.tableModel.getRowCount(); i++) {
            Object value = this.tableModel.getValueAt(i, ALIAS_COLUMN_INDEX);
            if (value != null && value.equals(alias)) {
                return i;
            }
        }
        return NOT_FOUND_ROW;
    }

    private String formatBalance(ProductType type, Double balance) {
        if (type == null || balance == null) {
            return "";
        }
        return type.getCurrency().getSign() + balance;
    }
}