package lol.cicco.admin.controller;

import com.google.common.base.Strings;
import lol.cicco.admin.common.annotation.NoLogin;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.common.util.CurrentAdminUtils;
import lol.cicco.admin.dto.request.LoginRequest;
import lol.cicco.admin.service.LoginService;
import lol.cicco.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class MainController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private Environment env;

    @NoLogin
    @GetMapping({"/", "/login"})
    public String login(){
        return "login";
    }

    @NoLogin
    @ResponseBody
    @PostMapping("/login")
    public R login(@Valid LoginRequest login){
        return loginService.login(login);
    }

    @GetMapping("/main")
    public String main(Model model){
        Token token = CurrentAdminUtils.getToken();
        var menus = roleService.menuTree(token);
        model.addAttribute("menus", menus);
        model.addAttribute("adminName", token.getRealName());
        return "main";
    }

    @GetMapping("/welcome")
    public String welcome(Model model) throws UnknownHostException {
        Token token = CurrentAdminUtils.getToken();
        model.addAttribute("JavaVersion", System.getProperty("java.version"));
        model.addAttribute("AdminName", token.getRealName());
        model.addAttribute("OS", System.getProperty("os.name"));
        model.addAttribute("SpringVersion", SpringVersion.getVersion());
        String uploadLimitSize = env.getProperty("spring.servlet.multipart.max-file-size");
        if(Strings.isNullOrEmpty(uploadLimitSize)) {
            uploadLimitSize = "0M";
        }
        model.addAttribute("UploadLimitSize", uploadLimitSize);
        InetAddress localhost = InetAddress.getLocalHost();
        model.addAttribute("HostName", localhost.getHostName());
        model.addAttribute("SpringBootVersion", SpringBootVersion.getVersion());
        return "welcome";
    }

    @GetMapping("/404")
    public String notFound(){
        return "404";
    }

}
