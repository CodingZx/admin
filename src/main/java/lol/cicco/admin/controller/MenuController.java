package lol.cicco.admin.controller;

import com.google.common.collect.Lists;
import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.Permission;
import lol.cicco.admin.common.em.MenuType;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.dto.request.MenuRequest;
import lol.cicco.admin.dto.response.MenuResponse;
import lol.cicco.admin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/menu")
public class MenuController {
    public static final String MENU_LIST = "sys:menu:list";
    public static final String MENU_ADD = "sys:menu:add";
    public static final String MENU_EDIT = "sys:menu:edit";
    public static final String MENU_REMOVE = "sys:menu:remove";
    @Autowired
    private MenuService menuService;

    @Permission(MENU_LIST)
    @GetMapping("/menu-list")
    public String menuList(){
        return "menu/menu-list";
    }

    @Permission(MENU_ADD)
    @GetMapping("/menu-add")
    public String menuAdd(@RequestParam(value = "parent", required = false) UUID parent, Model model){
        if(parent != null) {
            var menu = menuService.findOne(parent);
            if(menu == null) {
                return Constants.NOT_FOUND_PAGE;
            }
            model.addAttribute("parentId",menu.getId());
            model.addAttribute("parentName",menu.getMenuName());
        }
        return "menu/menu-add";
    }

    @Permission(MENU_EDIT)
    @GetMapping("/menu-edit")
    public String menuEdit(@RequestParam("id")UUID id, Model model){
        MenuResponse menu = menuService.findOne(id);
        if(menu == null) {
            return Constants.NOT_FOUND_PAGE;
        }
        if(menu.getParentId() != null && !menu.getParentId().equals("-1")) {
            var parent = menuService.findOne(UUID.fromString(menu.getParentId()));
            if(parent == null) {
                return Constants.NOT_FOUND_PAGE;
            }
            model.addAttribute("parentId",parent.getId());
            model.addAttribute("parentName",parent.getMenuName());
        }
        model.addAttribute("menu",menu);
        return "menu/menu-edit";
    }

    @Permission(MENU_LIST)
    @ResponseBody
    @GetMapping("/list")
    public R list(){
        return menuService.list();
    }


    @Permission(MENU_ADD)
    @ResponseBody
    @PostMapping("/add")
    public R add(@Valid MenuRequest menu){
        // 校验格式
        try {
            if(menu.getParentId() != null) {
                UUID.fromString(menu.getParentId());
            }
            MenuType.valueOf(menu.getMenuType().toUpperCase());
        }catch (Exception e){
            return R.other("非法请求");
        }
        if(menu.getId() != null) {
            return R.other("非法请求");
        }
        return menuService.save(menu);
    }

    @Permission(MENU_EDIT)
    @ResponseBody
    @PostMapping("/edit")
    public R update(@Valid MenuRequest menu){
        try {
            MenuType.valueOf(menu.getMenuType().toUpperCase());
        }catch (Exception e){
            return R.other("非法请求");
        }
        if(menu.getId() == null) {
            return R.other("非法请求");
        }
        return menuService.save(menu);
    }

    @Permission(MENU_REMOVE)
    @ResponseBody
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable("ids") String ids){
        List<UUID> uuidList = Lists.newLinkedList();
        for(String id : ids.split(",")){
            try{
                uuidList.add(UUID.fromString(id));
            } catch (Exception ignored){}
        }

        return menuService.remove(uuidList);
    }
}
