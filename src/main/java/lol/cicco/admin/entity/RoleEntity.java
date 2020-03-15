package lol.cicco.admin.entity;

import com.google.gson.JsonObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "a_role")
public class RoleEntity {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "menus")
    private JsonObject menus; // json :  {"menus":["id1","id2"]}
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
