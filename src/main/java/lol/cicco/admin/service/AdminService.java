package lol.cicco.admin.service;

import com.fasterxml.uuid.Generators;
import com.github.pagehelper.PageRowBounds;
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
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

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
        AdminEntity entity = adminMapper.selectByPrimaryKey(id);
        if(entity == null) {
            return null;
        }
        RoleEntity role = roleMapper.selectByPrimaryKey(entity.getRoleId());
        return new AdminResponse(entity, role);
    }

    public R list(Page page, String userName){
        var weekSql = WeekendSqls.<AdminEntity>custom();
        if(!Strings.isNullOrEmpty(userName)){
            weekSql.andLike(AdminEntity::getRealName, SQLUtils.fuzzyAll(userName));
        }
        var example = Example.builder(AdminEntity.class).where(weekSql).orderByDesc("createTime");

        var list = adminMapper.selectByExampleAndRowBounds(example.build(), page.getBounds());
        var count = adminMapper.selectCountByExample(example.build());

        return R.ok(list.stream().map(r -> new AdminResponse(r, roleMapper.selectByPrimaryKey(r.getRoleId()))).collect(Collectors.toList()), count);
    }

    public R save(AdminRequest admin){
        AdminEntity check = adminMapper.selectOneByExample(Example.builder(AdminEntity.class).where(WeekendSqls.<AdminEntity>custom().andEqualTo(AdminEntity::getUserName, admin.getUserName().trim())).build());
        if(admin.getId() == null && check != null){
            return R.other("用户名重复!");
        }
        if(admin.getId() != null && check != null && !check.getId().equals(admin.getId())){
            return R.other("用户名重复!");
        }

        AdminEntity entity = new AdminEntity();
        entity.setId(admin.getId());
        entity.setUserName(admin.getUserName().trim());
        if(!Strings.isNullOrEmpty(admin.getPassword().trim())){
            entity.setPassword(PasswordUtils.generate(admin.getPassword().trim()));
        } else {
            entity.setPassword(null);
        }
        entity.setRealName(admin.getRealName().trim());
        entity.setRoleId(admin.getRoleId());

        if(entity.getId() == null) {
            entity.setStatus(true);
            entity.setCreateTime(LocalDateTime.now());
            entity.setId(Generators.timeBasedGenerator().generate());
            adminMapper.insert(entity);
        } else {
            adminMapper.updateByPrimaryKeySelective(entity);
        }

        return R.ok();
    }

    public R updateStatus(UUID adminId, boolean status){
        AdminEntity admin = new AdminEntity();
        admin.setStatus(status);
        admin.setId(adminId);
        adminMapper.updateByPrimaryKeySelective(admin);
        return R.ok();
    }

    public R remove(List<UUID> ids){
        for(UUID id : ids) {
            adminMapper.deleteByPrimaryKey(id);
        }
        return R.ok();
    }
}
