package lol.cicco.admin.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class AdminRequest {
    private UUID id;
    @NotEmpty(message = "账号不能为空")
    private String userName;
    @NotEmpty(message = "姓名不能为空")
    private String realName;
    private String password;
    @NotNull(message = "角色ID不能为空")
    private UUID roleId;
}
