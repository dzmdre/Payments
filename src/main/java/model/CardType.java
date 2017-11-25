package model;

/**
 * Created by computer on 25.11.2017.
 */
public enum  CardType {
    VISA("Visa"), MASTER_CARD("MasterCard"), MAESTRO("Maestro");

    public final static String _VISA = "Visa";
    public final static String _MASTER_CARD= "MasterCard";
    public final static String _MAESTRO = "Maestro";

    public final String cardType;

    CardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }
}
