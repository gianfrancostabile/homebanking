package builder;

import enums.CardBrand;
import enums.CardType;
import model.Card;
import model.Client;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CardBuilder {

    public static Card build(CardType type, CardBrand brand, Client client) {
        Card.Builder builder = new Card.Builder();
        builder.withBrand(brand);
        builder.withType(type);
        builder.withCardNumber(brand.getCardNumberPrefix());
        builder.withSecurityCode(generateSecurityCode());
        builder.withExpirationDate(generateExpirationDate());
        builder.withOwnerName(buildOwnerName(client));
        return builder.build();
    }

    private static String generateSecurityCode() {
        int number = new Random().nextInt(1000);
        return String.format("%03d", number);
    }

    private static Date generateExpirationDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 5);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    private static String buildOwnerName(Client client) {
        return (client.getName() + " " + client.getLastName()).toUpperCase();
    }
}
