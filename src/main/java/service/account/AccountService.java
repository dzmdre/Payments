package service.account;

import dao.AccountDao;
import model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by computer on 04.12.2017.
 */
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountDao accountDao;

    public AccountService(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account setLocked(Account account, Boolean locked) {
        account.setLocked(locked);
        return accountDao.save(account);
    }

    public Account save(Account account) {
        return accountDao.save(account);
    }

    public List<Account> getAccountByUser(Long userId) {
        return accountDao.getAccountByUser(userId);
    }


    public Account getById(Long id) {
        return accountDao.getById(id);
    }
}
