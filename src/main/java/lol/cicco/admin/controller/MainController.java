package lol.cicco.admin.controller;

import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.NoLogin;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.common.util.GsonUtils;
import lol.cicco.admin.dto.request.LoginRequest;
import lol.cicco.admin.service.LoginService;
import lol.cicco.admin.service.MenuService;
import lol.cicco.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class MainController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RoleService roleService;

    @NoLogin
    @GetMapping({"/", "/login"})
    public String login(){
        return "login";
    }

    @NoLogin
    @ResponseBody
    @PostMapping("/login")
    public R login(@Valid LoginRequest login, BindingResult result){
        if(result.hasErrors()){
            return R.other(result.getFieldError().getDefaultMessage());
        }
        return loginService.login(login);
    }

    @GetMapping("/main")
    public String main(HttpServletRequest request, Model model){
        Token token = (Token)request.getAttribute(Constants.ADMIN_USER_TOKEN);
        var menus = roleService.menuTree(token);
        model.addAttribute("menus", menus);
        return "main";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @GetMapping("/404")
    public String notfound(){
        return "404";
    }

}
