package lol.cicco.admin.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lol.cicco.admin.common.exception.AlreadyUseException;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.model.Select;
import lol.cicco.admin.common.util.SQLUtils;
import lol.cicco.admin.dto.request.RoleRequest;
import lol.cicco.admin.dto.response.RoleResponse;
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
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminMapper adminMapper;

    public List<Select> all() {
        return roleMapper.findAll().stream().map(r -> new Select(r.getId(), r.getRoleName())).collect(Collectors.toList());
    }

    public R list(Page page, String userName) {
        var list = roleMapper.findList(SQLUtils.fuzzyAll(userName), page.getSize(), page.getStart());
        var count = roleMapper.findCount(SQLUtils.fuzzyAll(userName));

        return R.ok(list.stream().map(RoleResponse::new).collect(Collectors.toList()), count);
    }

    public R save(RoleRequest role) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(role.getId());
        roleEntity.setCreateTime(LocalDateTime.now());
        roleEntity.setRoleName(role.getRoleName());
        if (roleEntity.getId() == null) {
            roleEntity.setId(UUID.randomUUID());
            roleMapper.addRole(roleEntity);
        } else {
            roleMapper.updateRole(roleEntity);
        }
        return R.ok();
    }

    public R remove(List<UUID> ids){
        List<String> resultList = Lists.newLinkedList();
        for (UUID id : ids){
            AdminEntity admin = adminMapper.checkRole(id);
            if(admin != null) { // 角色正在被使用
                RoleEntity role = roleMapper.findById(id);
                resultList.add(role.getRoleName());
            } else {
                roleMapper.remove(id);
            }
        }
        if(!resultList.isEmpty()){
            throw new AlreadyUseException(Joiner.on(",").join(resultList) +" 已被使用!");
        }
        return R.ok();
    }
}
