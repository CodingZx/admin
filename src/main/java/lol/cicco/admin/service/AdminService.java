package lol.cicco.admin.service;

import com.google.common.base.Strings;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.util.PasswordUtils;
import lol.cicco.admin.common.util.SQLUtils;
import lol.cicco.admin.dto.request.AdminRequest;
import lol.cicco.admin.dto.response.AdminResponse;
import lol.cicco.admin.entity.AdminEntity;
import lol.cicco.admin.mapper.AdminMapper;
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

    public R list(Page page, String userName){
        var list = adminMapper.findAdminList(page.getSize(),page.getStart(), SQLUtils.fuzzyAll(userName));
        var count = adminMapper.findAdminCount(SQLUtils.fuzzyAll(userName));

        return R.ok(list.stream().map(AdminResponse::new).collect(Collectors.toList()), count);
    }

    public R save(AdminRequest admin){
        if(admin.getId() == null) {
            UUID checkId = adminMapper.checkUserName(admin.getUserName().trim());
            if(checkId != null){
                return R.other("用户名重复!");
            }
        }

        AdminEntity entity = new AdminEntity();
        entity.setId(admin.getId());
        entity.setUserName(admin.getUserName().trim());
        entity.setCreateTime(LocalDateTime.now());
        if(!Strings.isNullOrEmpty(admin.getPassword().trim())){
            entity.setPassword(PasswordUtils.generate(admin.getPassword().trim()));
        }
        entity.setRealName(admin.getRealName().trim());
        entity.setStatus(true);

        if(entity.getId() == null) {
            entity.setId(UUID.randomUUID());
            adminMapper.add(entity);
        } else {

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
