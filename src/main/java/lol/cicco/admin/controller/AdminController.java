package lol.cicco.admin.controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.dto.request.AdminRequest;
import lol.cicco.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    public R list(@RequestParam("page")int page,@RequestParam("size")int size, @RequestParam("userName")String userName){
        return adminService.list(new Page(page, size), userName);
    }

    @PostMapping("/add")
    public R save(AdminRequest admin, BindingResult result){
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

    @PostMapping("/disable/{id}")
    public R disable(@PathVariable("id")UUID id){
        return adminService.updateStatus(id, false);
    }

    @PostMapping("/active/{id}")
    public R active(@PathVariable("id")UUID id){
        return adminService.updateStatus(id, true);
    }


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
