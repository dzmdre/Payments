package controller.payment;

import com.google.gson.reflect.TypeToken;
import dao.impl.AccountDaoImpl;
import dao.impl.PaymentDaoImpl;
import model.Account;
import model.Payment;
import model.PaymentUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource.BaseResponseValue;
import resource.ResponseType;
import service.payment.PaymentService;
import utll.Common;
import utll.RestParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by computer on 09.12.2017.
 */
public class PaymentController extends HttpServlet {


    private PaymentService paymentService;
    private static final String NO_PAYEMNENTS_FOUND = "No payments found";
    private static final String PAYMENTS_ERROR= "Error updating or creating payments";
    private final static Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);


    @Override
    public void init() throws ServletException {
        super.init();
        paymentService = new PaymentService(new PaymentDaoImpl(), new AccountDaoImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter(Common.ID);
        String date = req.getParameter(Common.DATE);
        String accountId = req.getParameter(Common.ACCOUNT_ID);
        List<Payment> payments = new ArrayList<>();

        if (StringUtils.isNotEmpty(id)) {
            Payment payment = paymentService.getById(Long.parseLong(id));
            if (payment == null) {
                sendNoPaymentResponse(resp);
                return;
            } else {
                sendResponse(payment, resp);
                return;
            }
        } else if (StringUtils.isNotEmpty(date) && StringUtils.isNotEmpty(accountId)) {
            payments = paymentService.getByAccountAndDate(Long.parseLong(accountId), LocalDate.parse(date));

        } else if (StringUtils.isNotEmpty(accountId)) {
            payments = paymentService.getByAccount(Long.parseLong(accountId));
        }

        if (payments != null) {
            sendResponse(payments, resp);
            return;
        }
        sendNoPaymentResponse(resp);
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Payment payment = RestParamsUtil.getEntityFromRequest(req, Payment.class);
        if (payment == null || payment.getAccountId() == null || payment.getPaymentId() == null) {
            sendErrorResponse(resp, PAYMENTS_ERROR);
            return;
        }

        payment = paymentService.createPayment(payment);
        sendResponse(payment, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Payment payment = RestParamsUtil.getEntityFromRequest(req, Payment.class);
        if (payment == null || payment.getAccountId() == null) {
            sendErrorResponse(resp, PAYMENTS_ERROR);
            return;
        }

        if (payment.getDate() == null) {
            payment.setDate(LocalDate.now());
        }

        payment = paymentService.createPayment(payment);
        sendResponse(payment, resp);
    }

    private void sendNoPaymentResponse(HttpServletResponse resp) {
        this.sendErrorResponse(resp, NO_PAYEMNENTS_FOUND);
    }


    private void sendErrorResponse(HttpServletResponse resp, String errorMessage) {
        BaseResponseValue<Payment> responseValue = new BaseResponseValue<>(ResponseType.ERROR);
        responseValue.getMessages().add(errorMessage);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Payment>>() {
        }.getType());
        LOGGER.warn("Payment error req");
    }

    private void sendResponse(List<Payment> payments, HttpServletResponse response) {
        BaseResponseValue<List<Payment>> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(payments);
        RestParamsUtil.sendResponse(response, responseValue, new TypeToken<BaseResponseValue<List<Payment>>>() {}.getType());
    }

    private void sendResponse(Payment payment, HttpServletResponse response) {
        BaseResponseValue<Payment> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(payment);
        RestParamsUtil.sendResponse(response, responseValue, new TypeToken<BaseResponseValue<Payment>>() {}.getType());
    }

}
