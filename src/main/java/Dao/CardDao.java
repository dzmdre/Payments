package Dao;

import Model.Account;
import Model.Card;

import java.util.List;

/**
 * Created by computer on 16.11.2017.
 */
public interface CardDao extends CRUD<Card>  {
    List<Card> getCardByUser(Long userId);
    Card getCardByAccount(Long accountId);
}
