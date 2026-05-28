package service;

import enums.Currency;
import enums.PaymentMethod;
import enums.TransactionType;
import exception.JDBCException;
import model.Transaction;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionService {
    private static TransactionService INSTANCE;
    private final TransactionRepository repository = TransactionRepository.getInstance();

    private TransactionService() {
    }

    public static TransactionService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TransactionService();
        }
        return INSTANCE;
    }

    public void interest(String productId, Currency currency, double amount) throws JDBCException {
        if (amount == 0) {
            return;
        }
        TransactionType type;
        String sourceId = null;
        String destinationId = null;
        if (amount < 0) {
            type = TransactionType.DEBIT;
            sourceId = productId;
        } else {
            type = TransactionType.CHARGE;
            destinationId = productId;
        }
        Transaction charge = new Transaction(new Date(), type, PaymentMethod.INTEREST, currency, amount, sourceId, destinationId, null);
        this.repository.insert(charge);
    }

    public void deposit(String destinationId, Currency currency, double amount) throws JDBCException {
        Transaction charge = new Transaction(new Date(), TransactionType.CHARGE, PaymentMethod.DEPOSIT, currency, amount, null, destinationId, null);
        this.repository.insert(charge);
    }

    public void transfer(String sourceId, String destinationId, Currency currency, double amount) throws JDBCException {
        Date now = new Date();
        if ((sourceId == null || sourceId.isBlank()) && (destinationId != null && !destinationId.isBlank())) {
            Transaction charge = new Transaction(now, TransactionType.CHARGE, PaymentMethod.TRANSFER, currency, amount, null, destinationId, null);
            this.repository.insert(charge);
        }
        Transaction debit = new Transaction(now, TransactionType.DEBIT, PaymentMethod.TRANSFER, currency, amount, sourceId, destinationId, null);
        Transaction charge = new Transaction(now, TransactionType.CHARGE, PaymentMethod.TRANSFER, currency, amount, sourceId, destinationId, null);
        this.repository.insert(debit);
        this.repository.insert(charge);
    }

    public void payWithDebit(String sourceId, String cardId, Currency currency, double amount) throws JDBCException {
        Transaction debit = new Transaction(new Date(), TransactionType.DEBIT, PaymentMethod.DEBIT_CARD, currency, amount, sourceId, null, cardId);
        this.repository.insert(debit);
    }

    public void payWithCredit(String sourceId, String cardId, Currency currency, double amount) throws JDBCException {
        Transaction toPay = new Transaction(new Date(), TransactionType.TO_PAY, PaymentMethod.CREDIT_CARD, currency, amount, sourceId, null, cardId);
        this.repository.insert(toPay);
    }

    public List<Transaction> findTransactionByClientIdAndDateAndType(String clientId, String from, String to, TransactionType type) {
        try {
            return this.repository.findTransactionByClientIdAndDateAndType(clientId, from, to, type);
        } catch (Exception _) {
            return new ArrayList<>();
        }
    }
}
