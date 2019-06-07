package lol.cicco.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AdminEntity {
    private UUID id;
    private String userName;
    private String realName;
    private String password;
    private LocalDateTime createTime;
    private boolean status;
}
