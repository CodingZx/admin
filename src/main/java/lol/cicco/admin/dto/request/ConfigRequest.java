package lol.cicco.admin.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ConfigRequest {
    private UUID id;
    @NotEmpty(message = "配置属性名称不能为空")
    private String propertyKey;
    @NotNull(message = "配置属性值不能为空")
    private String propertyValue;
    @NotNull(message = "配置属性说明不能为空")
    private String descText;
}
