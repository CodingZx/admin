package lol.cicco.admin.service;

import com.fasterxml.uuid.Generators;
import com.google.common.collect.Lists;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.dto.request.MenuRequest;
import lol.cicco.admin.dto.response.MenuResponse;
import lol.cicco.admin.entity.MenuEntity;
import lol.cicco.admin.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class MenuService {
    @Autowired
    private MenuMapper menuMapper;

    public MenuResponse findOne(UUID id){
        var menu = menuMapper.selectByPrimaryKey(id);
        if(menu == null) {
            return null;
        }
        return new MenuResponse(menu);
    }

    public R list(){
        var menus = menuMapper.selectAll();
        return R.ok(menus.stream().map(MenuResponse::new).sorted((o1,o2)->o2.getSortBy()-o1.getSortBy()).collect(Collectors.toList()));
    }

    public R save(MenuRequest menu) {
        MenuEntity menuEntity = new MenuEntity();
        menuEntity.setIcon(menu.getIcon());
        menuEntity.setId(menu.getId());
        menuEntity.setMenuName(menu.getMenuName());
        menuEntity.setMenuType(menu.getMenuType());
        menuEntity.setMenuUrl(menu.getMenuUrl());
        menuEntity.setPermission(menu.getPermission());
        menuEntity.setSortBy(menu.getSortBy());
        if(menuEntity.getId() == null){
            menuEntity.setId(Generators.timeBasedGenerator().generate());
            menuEntity.setCreateTime(LocalDateTime.now());
            menuEntity.setParentId(menu.getParentId() == null ? null : UUID.fromString(menu.getParentId()));
            menuMapper.insert(menuEntity);
        } else {
            menuMapper.updateByPrimaryKeySelective(menuEntity);
        }
        return R.ok();
    }

    public R remove(List<UUID> ids){
        Queue<UUID> queue = Lists.newLinkedList(ids);
        while(!queue.isEmpty()) {
            UUID take = queue.remove();
            var list = menuMapper.selectByExample(Example.builder(MenuEntity.class).where(WeekendSqls.<MenuEntity>custom().andEqualTo(MenuEntity::getParentId, take)).build());
            queue.addAll(list.stream().map(MenuEntity::getId).filter(Objects::nonNull).collect(Collectors.toList()));
            menuMapper.deleteByPrimaryKey(take);
        }
        return R.ok();
    }
}
