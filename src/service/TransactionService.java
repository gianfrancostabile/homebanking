package service;

import constant.TransactionType;
import exception.JDBCException;
import model.Product;
import model.Transaction;
import repository.ProductRepository;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionService {
    private final TransactionRepository repository;

    public TransactionService() {
        this.repository = new TransactionRepository("root", "nosequeponer");
    }

    public void transfer(String sourceId, String destinationId, double amount) throws JDBCException {
        Date now = new Date();
        if ((sourceId == null || sourceId.isBlank()) && (destinationId != null && !destinationId.isBlank())) {
            Transaction charge = new Transaction(now, TransactionType.CHARGE, amount, null, destinationId);
            this.repository.insert(charge);
        }
        Transaction debit = new Transaction(now, TransactionType.DEBIT, amount, sourceId, destinationId);
        Transaction charge = new Transaction(now, TransactionType.CHARGE, amount, sourceId, destinationId);
        this.repository.insert(debit);
        this.repository.insert(charge);
    }

    public void payWithDebit(String sourceId, double amount) throws JDBCException {
        Transaction debit = new Transaction(new Date(), TransactionType.DEBIT, amount, sourceId, null);
        this.repository.insert(debit);
    }

    public void payWithCredit(String sourceId, double amount) throws JDBCException {
        Transaction toPay = new Transaction(new Date(), TransactionType.TO_PAY, amount, sourceId, null);
        this.repository.insert(toPay);
    }
}
