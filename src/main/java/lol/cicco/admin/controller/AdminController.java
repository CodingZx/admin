package lol.cicco.admin.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.annotation.Permission;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.dto.request.AdminRequest;
import lol.cicco.admin.dto.response.AdminResponse;
import lol.cicco.admin.service.AdminService;
import lol.cicco.admin.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;

    @Permission("sys:admin:list")
    @GetMapping("/admin-list")
    public String adminList(){
        return "admin/admin-list";
    }

    @Permission("sys:admin:add")
    @GetMapping("/admin-add")
    public String adminAdd(Model model){
        model.addAttribute("roleList", roleService.all());
        return "admin/admin-add";
    }

    @Permission("sys:admin:edit")
    @GetMapping("/admin-edit")
    public String adminEdit(@RequestParam("id")UUID id, Model model){
        AdminResponse response = adminService.findById(id);
        if(response == null) {
            return Constants.NOT_FOUND_PAGE;
        }
        model.addAttribute("roleList", roleService.all());
        model.addAttribute("admin", response);
        return "admin/admin-edit";
    }

    @Permission("sys:admin:list")
    @ResponseBody
    @GetMapping("/list")
    public R list(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam("userName")String userName){
        return adminService.list(new Page(page, size), userName);
    }

    @Permission("sys:admin:add")
    @ResponseBody
    @PostMapping("/add")
    public R add(@Valid AdminRequest admin, BindingResult result){
        if(result.hasErrors()){
            return R.other(result.getFieldError().getDefaultMessage());
        }
        if(admin.getId() != null){
            return R.other("非法请求");
        }
        if(Strings.isNullOrEmpty(admin.getPassword())){
            return R.other("密码不能为空");
        }
        if(admin.getPassword().length() < 3){
            return R.other("密码不能少于3位");
        }
        return adminService.save(admin);
    }

    @Permission("sys:admin:edit")
    @ResponseBody
    @PostMapping("/update")
    public R update(@Valid AdminRequest admin, BindingResult result){
        if(result.hasErrors()){
            return R.other(result.getFieldError().getDefaultMessage());
        }
        if(admin.getId() == null){
            return R.other("非法请求");
        }
        if(!Strings.isNullOrEmpty(admin.getPassword())){
            if(admin.getPassword().length() < 3){
                return R.other("密码不能少于3位");
            }
        }
        if(admin.getId().equals(Constants.DEFAULT_ID)){
            return R.other("无法对最高管理员进行操作!");
        }
        return adminService.save(admin);
    }

    @Permission("sys:admin:status")
    @ResponseBody
    @PostMapping("/disable/{id}")
    public R disable(@PathVariable("id")UUID id){
        if(id.equals(Constants.DEFAULT_ID)){
            return R.other("无法对最高管理员进行操作!");
        }
        return adminService.updateStatus(id, false);
    }

    @Permission("sys:admin:status")
    @ResponseBody
    @PostMapping("/active/{id}")
    public R active(@PathVariable("id")UUID id){
        if(id.equals(Constants.DEFAULT_ID)){
            return R.other("无法对最高管理员进行操作!");
        }
        return adminService.updateStatus(id, true);
    }

    @Permission("sys:admin:remove")
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

        return adminService.remove(uuids);
    }

}
