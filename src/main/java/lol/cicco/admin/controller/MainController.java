package lol.cicco.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/", "/login"})
    public String login(){
        return "login";
    }

    @GetMapping("/main")
    public String main(){
        return "main";
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "welcome";
    }


}
