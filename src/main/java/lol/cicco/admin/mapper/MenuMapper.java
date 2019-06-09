package lol.cicco.admin.mapper;

import lol.cicco.admin.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface MenuMapper {

    List<MenuEntity> findList();

    void add(MenuEntity menu);

    void update(MenuEntity menu);

    MenuEntity findById(@Param("id") UUID id);

    void removeById(@Param("id")UUID id);

    List<MenuEntity> findByParentId(@Param("id")UUID id);
}
