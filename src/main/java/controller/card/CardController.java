package controller.card;

import com.google.gson.reflect.TypeToken;
import dao.impl.CardDaoImpl;
import model.Card;
import model.CardType;
import model.PaymentUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource.BaseResponseValue;
import resource.ResponseType;
import service.card.CardService;
import utll.Common;
import utll.RestParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Created by computer on 07.12.2017.
 */
public class CardController extends HttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    private CardService cardService;
    private final String NO_CARD = "Card is not found. Account number is required";

    public CardController() {

    }

    @Override
    public void init() throws ServletException {
        cardService = new CardService(new CardDaoImpl());
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter(Common.ID);

        if (StringUtils.isNotEmpty(id)) {
            Card card = cardService.getCardById(Long.parseLong(id));
            if (card == null) {
                sendNoCardResponse(resp);
                return;
            } else {
                BaseResponseValue<Card> responseValue = new BaseResponseValue<>(ResponseType.OK);
                responseValue.setData(card);
                RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Card>>() {
                }.getType());
                return;
            }
        }

        PaymentUser user = (PaymentUser) req.getSession().getAttribute(Common.USER);
        List<Card> cards = cardService.getCardByUser(user.getUserId());
        BaseResponseValue<List<Card>> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(cards);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<List<Card>>>() {
        }.getType());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Card card = RestParamsUtil.getEntityFromRequest(req, Card.class);
        if (card == null || card.getAccountId() == null) {
            sendNoCardResponse(resp);
            return;
        }

        PaymentUser user = (PaymentUser) req.getSession().getAttribute(Common.USER);
        card.setUserId(user.getUserId());
        if (StringUtils.isEmpty(card.getCardNumber())) {
            card.setCardNumber(UUID.randomUUID().toString());
        }

        if (card.getCardDate() == null) {
            card.setCardDate(LocalDate.now());
        }

        if (StringUtils.isEmpty(card.getCardType())) {
            card.setCardType(CardType._VISA);
        }

        card = cardService.save(card);

        if (card == null) {
            sendNoCardResponse(resp);
            return;
        }

        BaseResponseValue<Card> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(card);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Card>>() {
        }.getType());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Card card = RestParamsUtil.getEntityFromRequest(req, Card.class);
        if (card == null) {
            sendNoCardResponse(resp);
            return;
        }

        card = cardService.save(card);

        if (card == null) {
            sendNoCardResponse(resp);
            return;
        }

        BaseResponseValue<Card> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(card);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Card>>() {
        }.getType());
    }

    private void sendNoCardResponse(HttpServletResponse resp) {
        BaseResponseValue<Card> responseValue = new BaseResponseValue<>(ResponseType.ERROR);
        responseValue.getMessages().add(NO_CARD);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<Card>>() {
        }.getType());
        LOGGER.warn("Card error req");
    }
}