package service.card;

import dao.CardDao;
import model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by computer on 04.12.2017.
 */
public class CardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

    private final CardDao cardDao;

    public CardService(final CardDao cardDao) {
        this.cardDao = cardDao;
    }

    public Card save(Card card) {
        return cardDao.save(card);
    }

    public List<Card> getAccountByUser(Long userId) {
        return cardDao.getCardByUser(userId);
    }

    public Card getCardyCard(Long accountId) {
        return cardDao.getCardByAccount(accountId);
    }

}
