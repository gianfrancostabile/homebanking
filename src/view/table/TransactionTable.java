package view.table;

import constant.CommonConstant;
import constant.TableHeaderConstant;
import model.Transaction;
import view.custom.CustomTable;
import view.submenu.TransactionTypeRenderer;

import javax.swing.table.TableColumn;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private static final int TYPE_COLUMN_INDEX = 2;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(CommonConstant.TRANSACTION_DATE_FORMAT);

    public TransactionTable() {
        super(COLUMNS);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        TableColumn typeColumn = this.getTable().getColumnModel().getColumn(TYPE_COLUMN_INDEX);
        typeColumn.setCellRenderer(new TransactionTypeRenderer());
        typeColumn.setPreferredWidth(110);
    }

    @Override
    protected Object[] mapToRow(Transaction data) {
        return new Object[]{
                data.getId(),
                DATE_FORMAT.format(data.getCreationDate()),
                data.getType(),
                data.getPaymentMethod().getPrettyName(),
                data.getCurrency().getSign() + data.getAmount(),
                data.getSourceProductId(),
                data.getDestinationProductId()
        };
    }
}
