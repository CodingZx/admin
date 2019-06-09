package lol.cicco.admin.mapper;

import lol.cicco.admin.entity.AdminEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AdminMapper {

    void add(AdminEntity adminEntity);

    void update(AdminEntity adminEntity);

    AdminEntity findByUserName(@Param("userName")String userName);

    List<AdminEntity> findAdminList(@Param("size")int size,@Param("start") int start,@Param("userName")String userName);

    int findAdminCount(@Param("userName")String userName);

    AdminEntity findOne(@Param("id")UUID id);

    void updateStatus(@Param("id") UUID id, @Param("status")boolean status);

    void remove(@Param("id")UUID id);

    AdminEntity checkRole(@Param("roleId")UUID roleId);
}
