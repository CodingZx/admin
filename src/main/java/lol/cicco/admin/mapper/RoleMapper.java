package lol.cicco.admin.mapper;

import lol.cicco.admin.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RoleMapper {

    void addRole(RoleEntity role);

    void updateRole(RoleEntity role);

    List<RoleEntity> findAll();

    List<RoleEntity> findList(@Param("roleName")String roleName,@Param("size")int size,@Param("start")int start);

    int findCount(@Param("roleName")String roleName);

    RoleEntity findById(@Param("id") UUID id);

    void remove(@Param("id")UUID id);
}
