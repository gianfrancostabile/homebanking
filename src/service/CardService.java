package service;

import constant.CommonConstant;
import exception.JDBCException;
import model.Card;
import model.Product;
import repository.CardRepository;
import repository.ProductRepository;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CardService {
    private final CardRepository repository = CardRepository.getInstance();

    private static CardService INSTANCE;
    private CardService() {
    }

    public static CardService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CardService();
        }
        return INSTANCE;
    }

    public void insert(Card model) throws JDBCException {
        Integer suffixCardNumber = repository.getNextCardNumber(model.getCardNumber());
        String fullCardNumber = model.getCardNumber() + String.format(CommonConstant.CARD_PADDING_FORMAT, suffixCardNumber);
        model.setCardNumber(fullCardNumber);

        String id = repository.insert(model);
        model.setId(id);
    }

    public void update(Card model) throws JDBCException {
        repository.update(model.getId(), model);
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
