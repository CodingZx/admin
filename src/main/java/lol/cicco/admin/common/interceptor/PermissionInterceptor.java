package lol.cicco.admin.common.interceptor;

import com.google.common.collect.Lists;
import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.Permission;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class PermissionInterceptor extends BaseInterceptor {

    @Autowired
    private RoleService roleService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            log.debug(request.getRequestURI() + " -> permission....");

            HandlerMethod method = (HandlerMethod) handler;

            Permission permission = method.getMethod().getAnnotation(Permission.class);
            if(permission == null) {
                return true;
            }

            Token token = (Token) request.getAttribute(Constants.ADMIN_USER_TOKEN);

            if(roleService.hasPermission(token, Lists.newArrayList(permission.value()))){
                return true;
            } else {
                // 无权限
                sendNoPermissionError(response, method);
                return false;
            }
        }
        return true;
    }
}
