package service.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.PaymentUser;
import org.apache.commons.lang3.StringUtils;
import resource.BaseResponse;
import resource.ResponseType;
import utll.ValidationParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by computer on 28.11.2017.
 */
public class LoginService extends HttpServlet {

    private UserDao userDao = null;
    private final static String BAD_PARAMETERS_ERROR = "Username or password is incorrect";

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BaseResponse baseResponse = new BaseResponse(ResponseType.OK);

        JsonObject body = ValidationParamsUtil.getJsonFromRequest(request);

        if (body == null || body.get("username") == null || body.get("password") == null) {
            sendLoginFailed(baseResponse, response);
            return;
        }

        String username = body.get("username").getAsString();
        String password = body.get("password").getAsString();

       if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
           sendLoginFailed(baseResponse, response);
           return;
       }

       PaymentUser user = userDao.getUserByUsername(username);

       if (user != null && StringUtils.equals(password, user.getPassword())) {
           request.getSession(true).setAttribute("user", user);
           ValidationParamsUtil.sendResponse(response, baseResponse ,new TypeToken<BaseResponse>() {}.getType());

       } else {
           sendLoginFailed(baseResponse, response);
       }
    }


    private void sendLoginFailed(BaseResponse baseResponse, HttpServletResponse response) {
        baseResponse.addMessage(BAD_PARAMETERS_ERROR);
        baseResponse.setStatus(ResponseType.ERROR);
        ValidationParamsUtil.sendResponse(response,baseResponse,new TypeToken<BaseResponse>() {}.getType());
    }

}
