package service;

import exception.JDBCException;
import model.Card;
import model.Product;
import repository.CardRepository;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CardService {
    private final CardRepository repository;

    public CardService() {
        this.repository = new CardRepository("root", "nosequeponer");
    }

    public void insert(Card model) throws JDBCException {
        String id = repository.insert(model);
        model.setId(id);
    }

    public void update(Card model) throws JDBCException {
        repository.update(model.getId(), model);
    }

    public List<Card> findByProductId(String productId) {
        try {
            return repository.findByProductId(productId);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Card> findByProductIdList(List<String> productIds) {
        if (productIds.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return repository.findByProductIds(productIds);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
