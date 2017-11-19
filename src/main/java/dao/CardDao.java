package dao;

import model.Card;

import java.util.List;

/**
 * Created by computer on 16.11.2017.
 */
public interface CardDao extends CRUD<Card>  {
    List<Card> getCardByUser(Long userId);
    Card getCardByAccount(Long accountId);
}
