package lol.cicco.admin.controller;

import com.google.common.collect.Lists;
import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.Permission;
import lol.cicco.admin.common.exception.AlreadyUseException;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.dto.request.RoleRequest;
import lol.cicco.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Permission("sys:role:list")
    @GetMapping("/role-list")
    public String roleList() {
        return "role/role-list";
    }

    @Permission("sys:role:add")
    @GetMapping("/role-add")
    public String roleAdd(){
        return "role/role-add";
    }

    @Permission("sys:role:list")
    @ResponseBody
    @GetMapping("/list")
    public R list(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("roleName") String roleName) {
        return roleService.list(new Page(page, size), roleName);
    }

    @Permission("sys:role:add")
    @ResponseBody
    @PostMapping("/add")
    public R add(@Valid RoleRequest role, BindingResult result){
        if(result.hasErrors()){
            return R.other(result.getFieldError().getDefaultMessage());
        }
        if(role.getId() != null) {
            return R.other("非法请求");
        }
        return roleService.save(role);
    }

    @Permission("sys:role:remove")
    @ResponseBody
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable("ids") String ids){
        List<UUID> uuids = Lists.newLinkedList();
        for(String id : ids.split(",")){
            try{
                UUID fromId = UUID.fromString(id);

                if(fromId.equals(Constants.DEFAULT_ID)){
                    return R.other("无法删除最高管理员!");
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
}
