package lol.cicco.admin.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Data
public class RoleRequest {
    private UUID id;
    @NotEmpty(message = "角色名称不能为空")
    private String roleName;
}
