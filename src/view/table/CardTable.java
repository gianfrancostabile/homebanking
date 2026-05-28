package view.table;

import constant.CommonConstant;
import constant.TableHeaderConstant;
import model.Card;
import view.custom.CustomTable;

import javax.swing.table.TableColumn;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CardTable extends CustomTable<Card> {

    private static final String[] COLUMNS = {
            TableHeaderConstant.BRAND,
            TableHeaderConstant.TYPE,
            TableHeaderConstant.NUMBER,
            TableHeaderConstant.SECURITY_CODE,
            TableHeaderConstant.EXPIRATION_DATE,
            TableHeaderConstant.OWNER_NAME
    };

    private static final DateFormat EXPIRATION_DATE_FORMAT = new SimpleDateFormat(CommonConstant.CARD_EXPIRATION_DATE_FORMAT);
    private static final int SECURITY_CODE_COLUMN_INDEX = 3;
    private static final int PREFERRED_SECURITY_COLUMN_WIDTH = 125;

    public CardTable() {
        super(COLUMNS);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        TableColumn securityColumn = this.getTable().getColumnModel().getColumn(SECURITY_CODE_COLUMN_INDEX);
        securityColumn.setPreferredWidth(PREFERRED_SECURITY_COLUMN_WIDTH);
    }

    @Override
    protected Object[] mapToRow(Card data) {
        return new Object[]{
                data.getBrand().name(),
                data.getType().getPrettyName(),
                data.getCardNumber(),
                data.getSecurityCode(),
                EXPIRATION_DATE_FORMAT.format(data.getExpirationDate()),
                data.getOwnerName()
        };
    }
}