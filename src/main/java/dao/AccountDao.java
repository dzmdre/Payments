package dao;

import model.Account;

import java.util.List;

/**
 * Created by computer on 16.11.2017.
 */
public interface AccountDao extends CRUD<Account> {
    List<Account> getAccountByUser(Long userId);
    Account getAccountByCard(Long cardId);
}
