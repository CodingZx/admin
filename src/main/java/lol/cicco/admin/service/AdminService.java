package lol.cicco.admin.service;

import com.google.common.base.Strings;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.util.PasswordUtils;
import lol.cicco.admin.common.util.SQLUtils;
import lol.cicco.admin.dto.request.AdminRequest;
import lol.cicco.admin.dto.response.AdminResponse;
import lol.cicco.admin.entity.AdminEntity;
import lol.cicco.admin.entity.RoleEntity;
import lol.cicco.admin.mapper.AdminMapper;
import lol.cicco.admin.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private RoleMapper roleMapper;

    public AdminResponse findById(UUID id){
        AdminEntity entity = adminMapper.findOne(id);
        if(entity == null) {
            return null;
        }
        RoleEntity role = roleMapper.findById(entity.getRoleId());
        return new AdminResponse(entity, role);
    }

    public R list(Page page, String userName){
        var list = adminMapper.findAdminList(page.getSize(),page.getStart(), SQLUtils.fuzzyAll(userName));
        var count = adminMapper.findAdminCount(SQLUtils.fuzzyAll(userName));

        return R.ok(list.stream().map(r -> new AdminResponse(r, roleMapper.findById(r.getRoleId()))).collect(Collectors.toList()), count);
    }

    public R save(AdminRequest admin){
        UUID checkId = adminMapper.checkUserName(admin.getUserName().trim());
        if(admin.getId() == null && checkId != null){
            return R.other("用户名重复!");
        }
        if(admin.getId() != null && checkId != null && !checkId.equals(admin.getId())){
            return R.other("用户名重复!");
        }

        AdminEntity entity = new AdminEntity();
        entity.setId(admin.getId());
        entity.setUserName(admin.getUserName().trim());
        entity.setCreateTime(LocalDateTime.now());
        if(!Strings.isNullOrEmpty(admin.getPassword().trim())){
            entity.setPassword(PasswordUtils.generate(admin.getPassword().trim()));
        } else {
            entity.setPassword(null);
        }
        entity.setRealName(admin.getRealName().trim());
        entity.setStatus(true);
        entity.setRoleId(admin.getRoleId());

        if(entity.getId() == null) {
            entity.setId(UUID.randomUUID());
            adminMapper.add(entity);
        } else {
            adminMapper.update(entity);
        }

        return R.ok();
    }

    public R updateStatus(UUID adminId, boolean status){

        adminMapper.updateStatus(adminId, status);
        return R.ok();
    }

    public R remove(List<UUID> ids){
        for(UUID id : ids) {
            adminMapper.remove(id);
        }
        return R.ok();
    }
}
