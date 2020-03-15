package lol.cicco.admin.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "a_admin")
public class AdminEntity {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "real_name")
    private String realName;
    @Column(name = "password")
    private String password;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "role_id")
    private UUID roleId;
}
