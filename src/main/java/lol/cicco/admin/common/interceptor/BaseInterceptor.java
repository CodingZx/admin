package lol.cicco.admin.common.interceptor;

import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.util.GsonUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseInterceptor implements HandlerInterceptor {

    protected void sendLoginError(HttpServletResponse response, HandlerMethod method) throws IOException {
        sendError(response, method, R.login(), Constants.LOGIN_PAGE);
    }

    protected void sendNoPermissionError(HttpServletResponse response, HandlerMethod method) throws IOException {
        sendError(response, method, R.noPermission(), Constants.NOT_FOUND_PAGE);
    }


    private void sendError(HttpServletResponse response, HandlerMethod method, R result, String page) throws IOException {
        if(method.getMethod().getAnnotation(ResponseBody.class) != null) {
            // json
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(GsonUtils.gson().toJson(result));
            response.flushBuffer();

            return;
        }

        if(method.getMethod().getDeclaringClass().getAnnotation(RestController.class) != null) {
            // json
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(GsonUtils.gson().toJson(result));
            response.flushBuffer();

            return;
        }

        // view
        response.sendRedirect(page);
        response.flushBuffer();
    }
}
