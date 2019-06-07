package lol.cicco.admin.controller;

import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/disable/{id}")
    public R disable(@PathVariable("id")UUID id){
        return adminService.updateStatus(id, false);
    }

    @PostMapping("/active/{id}")
    public R active(@PathVariable("id")UUID id){
        return adminService.updateStatus(id, true);
    }


    @DeleteMapping("/{id}")
    public R remove(@PathVariable("id") UUID id){
        return adminService.remove(id);
    }

}
