package lol.cicco.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleEntity {
    private UUID id;
    private String roleName;
    private LocalDateTime createTime;
}
