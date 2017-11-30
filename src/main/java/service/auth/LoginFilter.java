package service.auth;

import com.google.gson.reflect.TypeToken;
import resource.BaseResponse;
import resource.ResponseType;
import utll.ValidationParamsUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by computer on 30.11.2017.
 */
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            BaseResponse baseResponse = new BaseResponse(ResponseType.ERROR);
            ValidationParamsUtil.sendResponse(response, baseResponse,new TypeToken<BaseResponse>() {}.getType());
        } else {
            chain.doFilter(req, res);
        }

    }

    @Override
    public void destroy() {

    }
}
