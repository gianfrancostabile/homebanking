package service;

import constant.CardType;
import exception.JDBCException;
import model.Card;
import model.Product;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private static ProductService INSTANCE;
    private final ProductRepository repository = ProductRepository.getInstance();
    private final CardService cardService = CardService.getInstance();
    private final TransactionService transactionService = TransactionService.getInstance();

    private ProductService() {
    }

    public static ProductService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProductService();
        }
        return INSTANCE;
    }

    public void insert(Product model) throws JDBCException {
        String id = repository.insert(model);
        model.setId(id);
    }

    // TODO: transactional
    public void deposit(String destinationId, double amount) throws JDBCException {
        Product product = this.addBalance(destinationId, amount);
        this.transactionService.deposit(destinationId, product.getType().getCurrency(), amount);
    }

    // TODO: transactional
    public void transfer(String sourceId, String destinationId, double amount) throws JDBCException {
        if (sourceId != null && !sourceId.isBlank()) {
            this.removeBalance(sourceId, amount);
        }
        Product product = this.addBalance(destinationId, amount);
        this.transactionService.transfer(sourceId, destinationId, product.getType().getCurrency(), amount);
    }

    // TODO: transactional
    public void payWithCard(String productId, Card card, double amount) throws JDBCException {
        if (CardType.CREDIT.equals(card.getType())) {
            Product source = repository.findOneById(productId);
            card.pay(amount);
            this.cardService.update(card);
            this.transactionService.payWithCredit(productId, card.getId(), source.getType().getCurrency(), amount);
        } else {
            Product product = this.removeBalance(productId, amount);
            this.transactionService.payWithDebit(productId, card.getId(), product.getType().getCurrency(), amount);
        }
    }

    private Product addBalance(String id, double amount) throws JDBCException {
        Product source = repository.findOneById(id);
        source.addBalance(amount);
        repository.update(source.getId(), source);
        return source;
    }

    private Product removeBalance(String id, double amount) throws JDBCException {
        Product source = repository.findOneById(id);
        source.removeBalance(amount);
        repository.update(source.getId(), source);
        return source;
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

    public void applyInterestOnAllProducts() throws JDBCException {
        List<Product> products = this.repository.findAll();
        for (Product product : products) {
            try {
                double newBalanceWithInterest = product.getBalance() * product.getType().getInterest();
                double interestGenerated = newBalanceWithInterest - product.getBalance();
                if (interestGenerated == 0) {
                    continue;
                }
                product.setBalance(newBalanceWithInterest);
                this.repository.update(product.getId(), product);
                this.transactionService.interest(product.getId(), product.getType().getCurrency(), interestGenerated);
            } catch (JDBCException e) {
            }
        }
    }

    public void deleteByClientId(String clientId) throws JDBCException {
        repository.deleteByClientId(clientId);
    }
}
