package lol.cicco.admin.common.interceptor;

import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.NoLogin;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.common.util.CurrentAdminUtils;
import lol.cicco.admin.common.util.GsonUtils;
import lol.cicco.admin.common.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LoginInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            log.debug("login interceptor...");

            HandlerMethod method = (HandlerMethod) handler;

            boolean skipLogin = false;
            if(method.getMethod().getAnnotation(NoLogin.class) != null){
                skipLogin = true;
            }

            Token adminToken = getAdminToken(request.getCookies());
            if(adminToken != null) {
                CurrentAdminUtils.setToken(adminToken);
            }
            if(adminToken == null && !skipLogin){
                sendLoginError(response, method);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        CurrentAdminUtils.clear();
    }

    private Token getAdminToken(Cookie[] cookies) {
        if(cookies == null) {
            return null;
        }
        for (Cookie c : cookies) {
            if (c.getName().equals("ADMIN_TOKEN")) {
                try {
                    String value = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8);
                    String tokenStr = RSAUtils.decrypt(value, Constants.RSA.TOKEN_PRIVATE_KEY);
                    return GsonUtils.gson().fromJson(tokenStr, Token.class);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
}
