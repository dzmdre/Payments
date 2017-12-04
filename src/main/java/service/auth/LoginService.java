package service.auth;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import model.PaymentUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource.BaseResponse;
import resource.ResponseType;
import utll.Common;
import utll.RestParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by computer on 28.11.2017.
 */
public class LoginService extends HttpServlet {

    private final static Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private UserDao userDao = null;
    private final static String BAD_PARAMETERS_ERROR = "Username or password is incorrect";

    @Override
    public void init() throws ServletException {
        userDao = new UserDaoImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BaseResponse baseResponse = new BaseResponse(ResponseType.OK);

        JsonObject body = RestParamsUtil.getJsonFromRequest(request);

        if (body == null || body.get("username") == null || body.get("password") == null) {
            sendLoginFailed(baseResponse, response);
            LOGGER.warn(BAD_PARAMETERS_ERROR);
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
           request.getSession(true).setAttribute(Common.USER, user);
           RestParamsUtil.sendResponse(response, baseResponse ,new TypeToken<BaseResponse>() {}.getType());

       } else {
           sendLoginFailed(baseResponse, response);
           LOGGER.warn("Login failed");
       }
    }


    private void sendLoginFailed(BaseResponse baseResponse, HttpServletResponse response) {
        baseResponse.addMessage(BAD_PARAMETERS_ERROR);
        baseResponse.setStatus(ResponseType.ERROR);
        RestParamsUtil.sendResponse(response,baseResponse,new TypeToken<BaseResponse>() {}.getType());
    }

}
