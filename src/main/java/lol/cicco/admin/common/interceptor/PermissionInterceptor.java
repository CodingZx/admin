package lol.cicco.admin.common.interceptor;

import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.Permission;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.common.util.CurrentAdminUtils;
import lol.cicco.admin.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Stream;

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

            Token token = CurrentAdminUtils.getToken();

            List<String> permissions = roleService.getPermissions(token);
            var option = Stream.of(permission.value()).filter(permissions::contains).findAny();
            if(option.isPresent()){
                request.setAttribute(Constants.CICCO_PERMISSION_LIST, permissions);
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
