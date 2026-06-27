package view.table;

import constant.CommonConstant;
import constant.TableHeaderConstant;
import model.Transaction;
import view.custom.CustomTable;
import view.submenu.TransactionAmountRenderer;
import view.submenu.TransactionTypeRenderer;

import javax.swing.table.TableColumn;
import java.time.format.DateTimeFormatter;

public class TransactionTable extends CustomTable<Transaction> {

    private static final String[] COLUMNS = {
            TableHeaderConstant.ID,
            TableHeaderConstant.DATE,
            TableHeaderConstant.TYPE,
            TableHeaderConstant.PAYMENT_METHOD,
            TableHeaderConstant.AMOUNT,
            TableHeaderConstant.ORIGIN,
            TableHeaderConstant.DESTINATION
    };
    private static final int DATE_INDEX = 1;
    private static final int TYPE_COLUMN_INDEX = 2;
    private static final int AMOUNT_INDEX = 4;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(CommonConstant.TRANSACTION_DATE_FORMAT);

    public TransactionTable() {
        super(COLUMNS);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        TableColumn dateColumn = this.getTable().getColumnModel().getColumn(DATE_INDEX);
        dateColumn.setPreferredWidth(120);
        TableColumn typeColumn = this.getTable().getColumnModel().getColumn(TYPE_COLUMN_INDEX);
        typeColumn.setCellRenderer(new TransactionTypeRenderer());
        TableColumn amountColumn = this.getTable().getColumnModel().getColumn(AMOUNT_INDEX);
        amountColumn.setCellRenderer(new TransactionAmountRenderer());
    }

    @Override
    protected Object[] mapToRow(Transaction data) {
        return new Object[]{
                data.getId(),
                DATE_FORMAT.format(data.getCreationDate()),
                data.getType(),
                data.getPaymentMethod().getPrettyName(),
                data,
                data.getSourceProductId(),
                data.getDestinationProductId()
        };
    }
}
