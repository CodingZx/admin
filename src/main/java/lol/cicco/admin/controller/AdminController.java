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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("/admin")
@Controller
public class AdminController {
    public static final String ADMIN_LIST = "sys:admin:list";
    public static final String ADMIN_ADD = "sys:admin:add";
    public static final String ADMIN_EDIT = "sys:admin:edit";
    public static final String ADMIN_REMOVE = "sys:admin:remove";
    public static final String ADMIN_STATUS = "sys:admin:status";

    @Autowired
    private RoleService roleService;
    @Autowired
    private AdminService adminService;

    @Permission(ADMIN_LIST)
    @GetMapping("/admin-list")
    public String adminList(){
        return "admin/admin-list";
    }

    @Permission(ADMIN_ADD)
    @GetMapping("/admin-add")
    public String adminAdd(Model model){
        model.addAttribute("roleList", roleService.all());
        return "admin/admin-add";
    }

    @Permission(ADMIN_EDIT)
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

    @Permission(ADMIN_LIST)
    @ResponseBody
    @GetMapping("/list")
    public R list(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam("userName")String userName){
        return adminService.list(new Page(page, size), userName);
    }

    @Permission(ADMIN_ADD)
    @ResponseBody
    @PostMapping("/add")
    public R add(@Valid AdminRequest admin){
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

    @Permission(ADMIN_EDIT)
    @ResponseBody
    @PostMapping("/update")
    public R update(@Valid AdminRequest admin){
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

    @Permission(ADMIN_STATUS)
    @ResponseBody
    @PostMapping("/disable/{id}")
    public R disable(@PathVariable("id")UUID id){
        if(id.equals(Constants.DEFAULT_ID)){
            return R.other("无法对最高管理员进行操作!");
        }
        return adminService.updateStatus(id, false);
    }

    @Permission(ADMIN_STATUS)
    @ResponseBody
    @PostMapping("/active/{id}")
    public R active(@PathVariable("id")UUID id){
        if(id.equals(Constants.DEFAULT_ID)){
            return R.other("无法对最高管理员进行操作!");
        }
        return adminService.updateStatus(id, true);
    }

    @Permission(ADMIN_REMOVE)
    @ResponseBody
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable("ids") String ids){
        List<UUID> uuidList = Lists.newLinkedList();
        for(String id : ids.split(",")){
            try{
                UUID fromId = UUID.fromString(id);

                if(fromId.equals(Constants.DEFAULT_ID)){
                    return R.other("无法删除最高管理员!");
                }
                uuidList.add(fromId);
            } catch (Exception ignored){}
        }

        return adminService.remove(uuidList);
    }

}
