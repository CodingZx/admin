package lol.cicco.admin.dto.response;

import lol.cicco.admin.entity.AdminEntity;
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

    public AdminResponse(AdminEntity admin){
        this.id = admin.getId();
        this.userName = admin.getUserName();
        this.realName = admin.getRealName();
        this.createTime = admin.getCreateTime();
        this.status = admin.isStatus();
    }
}
