package lol.cicco.admin.controller;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.Permission;
import lol.cicco.admin.common.exception.AlreadyUseException;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.dto.request.RoleRequest;
import lol.cicco.admin.dto.response.RoleResponse;
import lol.cicco.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/role")
public class RoleController {
    public static final String ROLE_LIST = "sys:role:list";
    public static final String ROLE_ADD = "sys:role:add";
    public static final String ROLE_EDIT = "sys:role:edit";
    public static final String ROLE_REMOVE = "sys:role:remove";

    @Autowired
    private RoleService roleService;

    @Permission(ROLE_LIST)
    @GetMapping("/role-list")
    public String roleList() {
        return "role/role-list";
    }

    @Permission(ROLE_ADD)
    @GetMapping("/role-add")
    public String roleAdd(){
        return "role/role-add";
    }

    @Permission(ROLE_EDIT)
    @GetMapping("/role-edit")
    public String roleEdit(@RequestParam("id")UUID id, Model model){
        RoleResponse response = roleService.findById(id);
        if(response == null) {
            return Constants.NOT_FOUND_PAGE;
        }

        model.addAttribute("role", response);
        return "role/role-edit";
    }

    @Permission(ROLE_LIST)
    @ResponseBody
    @GetMapping("/list")
    public R list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("roleName") String roleName) {
        return roleService.list(new Page(page, size), roleName);
    }

    @Permission(ROLE_ADD)
    @ResponseBody
    @PostMapping("/add")
    public R add(@Valid RoleRequest role){
        if(role.getId() != null) {
            return R.other("非法请求");
        }
        JsonArray menus = new JsonArray();
        try{
            for (String s : role.getMenus().split(",")){
                menus.add(UUID.fromString(s).toString());
            }
        }catch (Exception e){
            return R.other("非法请求");
        }
        return roleService.save(role, menus);
    }

    @Permission(ROLE_EDIT)
    @ResponseBody
    @PostMapping("/edit")
    public R edit(@Valid RoleRequest role){
        if(role.getId() == null) {
            return R.other("非法请求");
        }
        JsonArray menus = new JsonArray();
        try{
            for (String s : role.getMenus().split(",")){
                menus.add(UUID.fromString(s).toString());
            }
        }catch (Exception e){
            return R.other("非法请求");
        }
        return roleService.save(role, menus);
    }

    @Permission(ROLE_REMOVE)
    @ResponseBody
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable("ids") String ids){
        List<UUID> uuids = Lists.newLinkedList();
        for(String id : ids.split(",")){
            try{
                UUID fromId = UUID.fromString(id);

                if(fromId.equals(Constants.DEFAULT_ID)){
                    return R.other("无法删除系统管理员!");
                }
                uuids.add(fromId);
            } catch (Exception ignored){}
        }
        try {
            return roleService.remove(uuids);
        }catch (AlreadyUseException e) {
            return R.other(e.getMsg());
        }
    }

    @Permission({ROLE_EDIT, ROLE_ADD})
    @GetMapping("/menus")
    @ResponseBody
    public R menus(@RequestParam(value = "roleId", required = false) UUID roleId){
        return roleService.menus(roleId);
    }
}
