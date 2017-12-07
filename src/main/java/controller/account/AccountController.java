package controller.account;

import com.google.gson.reflect.TypeToken;
import dao.impl.AccountDaoImpl;
import model.Account;
import model.PaymentUser;
import model.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource.BaseResponseValue;
import resource.ResponseType;
import service.account.AccountService;
import utll.Common;
import utll.RestParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by computer on 04.12.2017.
 */
public class AccountController extends HttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private AccountService accountService;
    private final String NO_ACCOUNT = "Account is not found.";
    private final String INVALID_CREDENTIALS = "Invalid credentials";

    public AccountController() {
    }

    @Override
    public void init() throws ServletException {
        accountService = new AccountService(new AccountDaoImpl());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter(Common.ID);

        if (StringUtils.isNotEmpty(id)) {
            Account account = accountService.getById(Long.parseLong(id));
            if (account == null) {
                sendNoAccountsResponse(resp);
                return;
            } else {
                BaseResponseValue<Account> responseValue = new BaseResponseValue<>(ResponseType.OK);
                responseValue.setData(account);
                RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Account>>(){}.getType());
                return;
            }
        }

        PaymentUser user = (PaymentUser) req.getSession().getAttribute(Common.USER);
        List<Account> accounts = accountService.getAccountByUser(user.getUserId());
        if (accounts == null) {
            sendNoAccountsResponse(resp);
            return;
        }

        BaseResponseValue<List<Account>> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(accounts);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<List<Account>>>(){}.getType());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = RestParamsUtil.getEntityFromRequest(req, Account.class);
        if (account == null) {
            sendNoAccountsResponse(resp);
            return;
        }

        PaymentUser user = (PaymentUser) req.getSession().getAttribute(Common.USER);
        account.setUserId(user.getUserId());
        account.setLocked(false);
        if (StringUtils.isEmpty(account.getAccountNumber())) {
            account.setAccountNumber(UUID.randomUUID().toString());
        }

        account = accountService.save(account);

        if (account == null) {
            sendNoAccountsResponse(resp);
            return;
        }

        BaseResponseValue<Account> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(account);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Account>>(){}.getType());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account account = RestParamsUtil.getEntityFromRequest(req, Account.class);
        if (account == null) {
            sendNoAccountsResponse(resp);
            return;
        }

        if (!checkForCredentials(account, req)) {
            invalidCredentialRequest(resp);
            return;
        }

        account = accountService.save(account);

        if (account == null) {
            sendNoAccountsResponse(resp);
            return;
        }

        BaseResponseValue<Account> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(account);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Account>>(){}.getType());
    }

    private void sendNoAccountsResponse(HttpServletResponse resp) {
        BaseResponseValue<Account> responseValue = new BaseResponseValue<>(ResponseType.ERROR);
        responseValue.getMessages().add(NO_ACCOUNT);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Account>>(){}.getType());
        LOGGER.warn("Account error");
    }

    private void invalidCredentialRequest(HttpServletResponse resp) {
        BaseResponseValue<Account> responseValue = new BaseResponseValue<>(ResponseType.ERROR);
        responseValue.getMessages().add(INVALID_CREDENTIALS);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Account>>(){}.getType());
        LOGGER.warn(INVALID_CREDENTIALS);
    }

    private boolean checkForCredentials(Account account, HttpServletRequest request) {
        Account fromBase = accountService.getById(account.getAccountId());
        PaymentUser paymentUser = (PaymentUser) request.getSession().getAttribute("user");
        if (!fromBase.getLocked() && account.getLocked()) {
            return paymentUser.getUserRole().equals(UserRole.ADMIN);
        }
        return true;
    }

}
