package service.impl;

import constant.CommonConstant;
import constant.TableHeaderConstant;
import model.Transaction;
import service.ReportService;

public class ReportTransactionService extends ReportService<Transaction> {
    private static ReportTransactionService INSTANCE;

    private ReportTransactionService() {
    }

    public static ReportTransactionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ReportTransactionService();
        }
        return INSTANCE;
    }

    @Override
    public String getHeaders() {
        return TableHeaderConstant.ID + CommonConstant.REPORT_DELIMITER +
                TableHeaderConstant.DATE + CommonConstant.REPORT_DELIMITER +
                TableHeaderConstant.TYPE + CommonConstant.REPORT_DELIMITER +
                TableHeaderConstant.PAYMENT_METHOD + CommonConstant.REPORT_DELIMITER +
                TableHeaderConstant.AMOUNT + CommonConstant.REPORT_DELIMITER +
                TableHeaderConstant.ORIGIN + CommonConstant.REPORT_DELIMITER +
                TableHeaderConstant.DESTINATION + CommonConstant.REPORT_DELIMITER +
                TableHeaderConstant.USED_CARD;
    }

    @Override
    public String mapToString(Transaction data) {
        return data.toString();
    }
}
