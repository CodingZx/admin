package lol.cicco.admin.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lol.cicco.admin.common.exception.AlreadyUseException;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.model.Select;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.common.util.SQLUtils;
import lol.cicco.admin.dto.request.RoleRequest;
import lol.cicco.admin.dto.response.MenuTreeResponse;
import lol.cicco.admin.dto.response.RoleResponse;
import lol.cicco.admin.entity.AdminEntity;
import lol.cicco.admin.entity.MenuEntity;
import lol.cicco.admin.entity.RoleEntity;
import lol.cicco.admin.mapper.AdminMapper;
import lol.cicco.admin.mapper.MenuMapper;
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
    @Autowired
    private MenuMapper menuMapper;

    public List<Select> all() {
        return roleMapper.findAll().stream().map(r -> new Select(r.getId(), r.getRoleName())).collect(Collectors.toList());
    }

    public RoleResponse findById(UUID roleId) {
        RoleEntity role = roleMapper.findById(roleId);
        if (role == null) {
            return null;
        }
        return new RoleResponse(role);
    }

    public R list(Page page, String userName) {
        var list = roleMapper.findList(SQLUtils.fuzzyAll(userName), page.getSize(), page.getStart());
        var count = roleMapper.findCount(SQLUtils.fuzzyAll(userName));

        return R.ok(list.stream().map(RoleResponse::new).collect(Collectors.toList()), count);
    }

    public R save(RoleRequest role, JsonArray menus) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(role.getId());
        roleEntity.setCreateTime(LocalDateTime.now());
        roleEntity.setRoleName(role.getRoleName());

        JsonObject menuObj = new JsonObject();
        menuObj.add("menus", menus);
        roleEntity.setMenus(menuObj);

        if (roleEntity.getId() == null) {
            roleEntity.setId(UUID.randomUUID());
            roleMapper.addRole(roleEntity);
        } else {
            roleMapper.updateRole(roleEntity);
        }
        return R.ok();
    }

    public R remove(List<UUID> ids) {
        List<String> resultList = Lists.newLinkedList();
        for (UUID id : ids) {
            AdminEntity admin = adminMapper.checkRole(id);
            if (admin != null) { // 角色正在被使用
                RoleEntity role = roleMapper.findById(id);
                resultList.add(role.getRoleName());
            } else {
                roleMapper.remove(id);
            }
        }
        if (!resultList.isEmpty()) {
            throw new AlreadyUseException(Joiner.on(",").join(resultList) + " 已被使用!");
        }
        return R.ok();
    }

    public R menus(UUID roleId) {
        List<UUID> roleMenus = Lists.newLinkedList();
        if (roleId != null) {
            RoleEntity role = roleMapper.findById(roleId);
            if (role == null) {
                return R.other("非法请求");
            }
            roleMenus.addAll(Streams.stream(role.getMenus().getAsJsonArray("menus")).map(r -> UUID.fromString(r.getAsString())).collect(Collectors.toList()));
        }
        List<MenuEntity> list = menuMapper.findList();

        // 第一层目录
        var dicList = list.stream()
                .filter(r -> r.getParentId() == null)
                .map(r -> new MenuTreeResponse(r, roleMenus.contains(r.getId()), false))
                .collect(Collectors.toList());
        dicList.forEach(dic -> {
            // 第二层链接
            dic.setChildren(list.stream().filter(m -> dic.getId().equals(m.getParentId())).map(r -> new MenuTreeResponse(r, roleMenus.contains(r.getId()), false)).collect(Collectors.toList()));

            dic.getChildren().forEach(c -> {
                // 第三层按钮
                c.setChildren(list.stream().filter(m -> c.getId().equals(m.getParentId())).map(r -> new MenuTreeResponse(r, roleMenus.contains(r.getId()), true)).collect(Collectors.toList()));

                if(c.getChildren().size() > 0){
                    c.setChecked(true);
                }
            });

            if(dic.getChildren().size() > 0){
                dic.setChecked(true);
            }
        });

        return R.ok(dicList);
    }

    public List<MenuTreeResponse> menuTree(Token token) {
        RoleEntity role = roleMapper.findById(token.getRoleId());
        var roleMenus = Streams.stream(role.getMenus().getAsJsonArray("menus")).map(r -> UUID.fromString(r.getAsString())).collect(Collectors.toList());

        List<MenuEntity> list = menuMapper.findList();
        // 第一层目录
        var dicList = list.stream()
                .filter(r -> r.getParentId() == null && roleMenus.contains(r.getId()))
                .map(MenuTreeResponse::new)
                .collect(Collectors.toList());
        dicList.forEach(dic -> {
            // 第二层链接
            dic.setChildren(list.stream().filter(m -> dic.getId().equals(m.getParentId()) && roleMenus.contains(m.getId())).map(MenuTreeResponse::new).collect(Collectors.toList()));
        });
        return dicList;
    }

    public List<String> getPermissions(Token token) {
        var role = roleMapper.findById(token.getRoleId());
        var menuArr = role.getMenus().getAsJsonArray("menus");

        List<UUID> roleMenus = Lists.newLinkedList();
        for (int i = 0; i < menuArr.size(); i++) {
            var menuId = UUID.fromString(menuArr.get(i).getAsString());
            roleMenus.add(menuId);
        }

        if (roleMenus.isEmpty()) {
            return Lists.newLinkedList();
        }
        var menus = menuMapper.findByIds(roleMenus);

        return menus.stream().map(MenuEntity::getPermission).filter(a -> !Strings.isNullOrEmpty(a)).collect(Collectors.toList());
    }
}
