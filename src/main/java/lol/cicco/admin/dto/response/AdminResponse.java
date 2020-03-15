package lol.cicco.admin.dto.response;

import lol.cicco.admin.entity.AdminEntity;
import lol.cicco.admin.entity.RoleEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AdminResponse {
    private UUID id;
    private String userName;
    private String realName;
    private LocalDateTime createTime;
    private boolean status;
    private UUID roleId;
    private String roleName;

    public AdminResponse(AdminEntity admin, RoleEntity roleEntity){
        this.id = admin.getId();
        this.userName = admin.getUserName();
        this.realName = admin.getRealName();
        this.createTime = admin.getCreateTime();
        this.status = admin.getStatus();
        if(roleEntity != null) {
            this.roleId = roleEntity.getId();
            this.roleName = roleEntity.getRoleName();
        } else {
            this.roleName = "<未知角色>";
        }
    }
}
