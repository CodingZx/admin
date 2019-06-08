package lol.cicco.admin.dto.response;

import lol.cicco.admin.entity.RoleEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleResponse {

    private UUID id;
    private String roleName;
    private LocalDateTime createTime;


    public RoleResponse(RoleEntity roleEntity){
        this.id = roleEntity.getId();
        this.roleName = roleEntity.getRoleName();
        this.createTime = roleEntity.getCreateTime();
    }
}
