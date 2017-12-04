package service.auth;

import com.google.gson.reflect.TypeToken;
import resource.BaseResponse;
import resource.ResponseType;
import utll.RestParamsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by computer on 30.11.2017.
 */
public class LogoutService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        if (session != null) {
            session.invalidate();
        }
        BaseResponse baseResponse = new BaseResponse(ResponseType.OK);
        RestParamsUtil.sendResponse(resp,baseResponse,new TypeToken<BaseResponse>() {}.getType());
    }
}
