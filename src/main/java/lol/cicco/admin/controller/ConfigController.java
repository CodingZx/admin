package lol.cicco.admin.controller;

import com.google.common.collect.Lists;
import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.NoLogin;
import lol.cicco.admin.common.annotation.Permission;
import lol.cicco.admin.common.exception.AlreadyUseException;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.util.GsonUtils;
import lol.cicco.admin.dto.request.ConfigRequest;
import lol.cicco.admin.dto.response.ConfigResponse;
import lol.cicco.admin.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/config")
public class ConfigController {
    public static final String CONFIG_LIST = "sys:config:list";
    public static final String CONFIG_ADD = "sys:config:add";
    public static final String CONFIG_EDIT = "sys:config:edit";
    public static final String CONFIG_REMOVE = "sys:config:remove";

    @Autowired
    private ConfigService configService;

    @NoLogin
    @GetMapping
    @ResponseBody
    public String config() {
        var map = configService.all().stream().collect(Collectors.toMap(ConfigResponse::getPropertyKey, ConfigResponse::getPropertyValue));
        return GsonUtils.gson().toJson(map);
    }

    @Permission(CONFIG_LIST)
    @GetMapping("/config-list")
    public String configList() {
        return "config/config-list";
    }

    @Permission(CONFIG_ADD)
    @GetMapping("/config-add")
    public String configAdd(){
        return "config/config-add";
    }

    @Permission(CONFIG_EDIT)
    @GetMapping("/config-edit")
    public String configEdit(@RequestParam("id") UUID id, Model model){
        ConfigResponse response = configService.findById(id);
        if(response == null) {
            return Constants.NOT_FOUND_PAGE;
        }

        model.addAttribute("config", response);
        return "config/config-edit";
    }

    @Permission(CONFIG_LIST)
    @ResponseBody
    @GetMapping("/list")
    public R list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("propertyKey") String propertyKey) {
        return configService.list(new Page(page, size), propertyKey);
    }

    @Permission(CONFIG_ADD)
    @ResponseBody
    @PostMapping("/add")
    public R add(@Valid ConfigRequest config){
        return configService.save(config);
    }

    @Permission(CONFIG_EDIT)
    @ResponseBody
    @PostMapping("/edit")
    public R edit(@Valid ConfigRequest config){
        return configService.save(config);
    }

    @Permission(CONFIG_REMOVE)
    @ResponseBody
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable("ids") String ids){
        List<UUID> list = Lists.newLinkedList();
        for(String id : ids.split(",")){
            try{
                list.add(UUID.fromString(id));
            } catch (Exception ignored){}
        }
        try {
            return configService.remove(list);
        }catch (AlreadyUseException e) {
            return R.other(e.getMsg());
        }
    }
}
