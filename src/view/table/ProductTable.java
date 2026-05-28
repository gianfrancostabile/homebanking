package view.table;

import constant.TableHeaderConstant;
import enums.ProductType;
import model.Product;
import view.custom.CustomTable;
import view.submenu.SubMenuProductEditor;
import view.submenu.SubMenuRenderer;

import javax.swing.table.TableColumn;

public class ProductTable extends CustomTable<Product> {

    private static final String[] COLUMNS = {
            TableHeaderConstant.TYPE,
            TableHeaderConstant.ALIAS,
            TableHeaderConstant.CBU,
            TableHeaderConstant.BALANCE,
            TableHeaderConstant.ACTIONS
    };
    private static final int ACTIONS_COLUMN_INDEX = 4;
    private static final int ACTIONS_COLUMN_PREFERRED_WIDTH = 125;

    // Actions
    private final Runnable onUpdate;

    public ProductTable(Runnable onUpdate) {
        this.onUpdate = onUpdate;
        super(COLUMNS, (_, column) -> column == ACTIONS_COLUMN_INDEX);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        TableColumn actionColumn = this.getTable().getColumnModel().getColumn(ACTIONS_COLUMN_INDEX);
        actionColumn.setPreferredWidth(ACTIONS_COLUMN_PREFERRED_WIDTH);
        actionColumn.setCellRenderer(new SubMenuRenderer());
        actionColumn.setCellEditor(new SubMenuProductEditor(this.onUpdate));
    }

    @Override
    protected Object[] mapToRow(Product data) {
        String cbu = (data.getCbu() == null) ? "" : data.getCbu().trim();
        String alias = (data.getAlias() == null) ? "" : data.getAlias().trim();
        ProductType type = data.getType();
        String balanceText = this.formatBalance(type, data.getBalance());
        return new Object[]{type, alias, cbu, balanceText, data};
    }

    private String formatBalance(ProductType type, Double balance) {
        if (type == null || balance == null) {
            return "";
        }
        return type.getCurrency().getSign() + balance;
    }
}