package lol.cicco.admin.common.interceptor;

import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.NoLogin;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.common.util.GsonUtils;
import lol.cicco.admin.common.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            log.debug("login interceptor...");

            HandlerMethod method = (HandlerMethod) handler;

            if(method.getMethod().getAnnotation(NoLogin.class) != null){
                return true;
            }

            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                sendLoginError(response, method);
                return false;
            }
            boolean has = false;
            for (Cookie c : cookies) {
                if (c.getName().equals("ADMIN_TOKEN")) {
                    try {
                        String value = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                        String tokenStr = RSAUtils.decrypt(value, Constants.RSA.TOKEN_PRIVATE_KEY);
                        Token token = GsonUtils.gson().fromJson(tokenStr, Token.class);
                        if (token == null || !token.check()) {
                            throw new Exception();
                        }

                        request.setAttribute(Constants.ADMIN_USER_TOKEN, token);
                    } catch (Exception e) {
                        sendLoginError(response, method);
                        return false;
                    }
                    has = true;
                }
            }
            if (!has) {
                sendLoginError(response, method);
                return false;
            }
        }
        return true;
    }

    private void sendLoginError(HttpServletResponse response, HandlerMethod method) throws IOException {
        if(method.getMethod().getAnnotation(ResponseBody.class) != null) {
            // json
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(GsonUtils.gson().toJson(R.login()));
            response.flushBuffer();

            return;
        }

        if(method.getMethod().getDeclaringClass().getAnnotation(RestController.class) != null) {
            // json
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(GsonUtils.gson().toJson(R.login()));
            response.flushBuffer();

            return;
        }


        // view
        response.sendRedirect("/");
        response.flushBuffer();
    }
}
