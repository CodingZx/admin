package lol.cicco.admin.entity;

import com.google.gson.JsonObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleEntity {
    private UUID id;
    private String roleName;
    private JsonObject menus; // json :  {"menus":["id1","id2"]}
    private LocalDateTime createTime;
}
