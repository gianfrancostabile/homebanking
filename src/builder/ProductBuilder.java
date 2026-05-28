package builder;

import enums.CardBrand;
import enums.CardType;
import enums.ProductType;
import model.Card;
import model.Client;
import model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductBuilder {

    public static Product build(ProductType productType, Client client) {
        return new Product.Builder()
                .withClientId(client.getId())
                .withProductType(productType)
                .withCards(generateCards(productType, client))
                .build();
    }

    private static List<Card> generateCards(ProductType productType, Client client) {
        List<Card> cards = new ArrayList<>();
        if (Objects.requireNonNull(productType) == ProductType.CUENTA_CORRIENTE) {
            cards.add(CardBuilder.build(CardType.DEBIT, CardBrand.VISA, client));
            cards.add(CardBuilder.build(CardType.CREDIT, CardBrand.VISA, client));
            cards.add(CardBuilder.build(CardType.CREDIT, CardBrand.MASTERCARD, client));
        }
        return cards;
    }
}
