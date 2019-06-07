package lol.cicco.admin.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.dto.request.AdminRequest;
import lol.cicco.admin.dto.response.AdminResponse;
import lol.cicco.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/admin-list")
    public String adminList(){
        return "admin/admin-list";
    }

    @GetMapping("/admin-add")
    public String adminAdd(){
        return "admin/admin-add";
    }

    @GetMapping("/admin-edit")
    public String adminEdit(@RequestParam("id")UUID id, Model model){
        AdminResponse response = adminService.findById(id);
        if(response == null) {
            return Constants.NOT_FOUND_PAGE;
        }
        model.addAttribute("admin", response);
        return "admin/admin-edit";
    }

    @ResponseBody
    @GetMapping("/list")
    public R list(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam("userName")String userName){
        return adminService.list(new Page(page, size), userName);
    }

    @ResponseBody
    @PostMapping("/add")
    public R add(AdminRequest admin, BindingResult result){
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

    @ResponseBody
    @PostMapping("/update")
    public R update(AdminRequest admin, BindingResult result){
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
        return adminService.save(admin);
    }

    @ResponseBody
    @PostMapping("/disable/{id}")
    public R disable(@PathVariable("id")UUID id){
        return adminService.updateStatus(id, false);
    }

    @ResponseBody
    @PostMapping("/active/{id}")
    public R active(@PathVariable("id")UUID id){
        return adminService.updateStatus(id, true);
    }

    @ResponseBody
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable("ids") String ids){
        List<UUID> uuids = Lists.newLinkedList();
        for(String id : ids.split(",")){
            try{
                uuids.add(UUID.fromString(id));
            } catch (Exception ignored){}
        }

        return adminService.remove(uuids);
    }

}
