package service;

import constant.CardType;
import exception.JDBCException;
import model.Card;
import model.Product;
import model.Transaction;
import repository.ProductRepository;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository repository;
    private final CardService cardService = new CardService();
    private final TransactionService transactionService = new TransactionService();

    public ProductService() {
        this.repository = new ProductRepository("root", "nosequeponer");
    }

    public void insert(Product model) throws JDBCException {
        String id = repository.insert(model);
        model.setId(id);
    }

    // TODO: transactional
    public void transfer(String sourceId, String destinationId, double amount) throws JDBCException {
        if (sourceId != null && !sourceId.isBlank()) {
            this.removeBalance(sourceId, amount);
        }
        this.addBalance(destinationId, amount);
        this.transactionService.transfer(sourceId, destinationId, amount);
    }

    // TODO: transactional
    public void payWithCard(String productId, Card card, double amount) throws JDBCException {
        if (CardType.CREDIT.equals(card.getType())) {
            card.pay(amount);
            this.cardService.update(card);
            this.transactionService.payWithCredit(productId, amount);
        } else {
            this.removeBalance(productId, amount);
            this.transactionService.payWithDebit(productId, amount);
        }
    }

    private void addBalance(String id, double amount) throws JDBCException {
        Product source = repository.findOneById(id);
        source.addBalance(amount);
        repository.update(source.getId(), source);
    }

    private void removeBalance(String id, double amount) throws JDBCException {
        Product source = repository.findOneById(id);
        source.removeBalance(amount);
        repository.update(source.getId(), source);
    }

    public List<Product> findByClientId(String clientId) {
        try {
            return repository.findByClientId(clientId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Optional<Product> findByIdOrAliasOrCbu(String input) {
        try {
            return repository.findByIdOrAliasOrCbu(input);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void deleteByClientId(String clientId) throws JDBCException {
        repository.deleteByClientId(clientId);
    }
}
