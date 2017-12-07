package controller.card;

import com.google.gson.reflect.TypeToken;
import model.Card;
import model.CardType;
import resource.BaseResponseValue;
import resource.ResponseType;
import utll.RestParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by computer on 08.12.2017.
 */
public class CardTypeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> cardTypes = Arrays.stream(CardType.values()).map(CardType::getCardType).collect(Collectors.toList());
        BaseResponseValue<List<String>> responseValue = new BaseResponseValue<>(ResponseType.OK);
        responseValue.setData(cardTypes);
        RestParamsUtil.sendResponse(resp, responseValue, new TypeToken<BaseResponseValue<List<String>>>() {
        }.getType());
    }
}
