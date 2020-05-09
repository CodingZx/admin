package lol.cicco.admin.dto.response;

import lol.cicco.admin.common.util.time.LocalDateTimeUtils;
import lol.cicco.admin.entity.ConfigEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class ConfigResponse {
    private UUID id;
    private String propertyKey;
    private String propertyValue;
    private String descText;
    private String createTime;
    private String updateTime;

    public ConfigResponse(ConfigEntity config) {
        this.id = config.getId();
        this.propertyKey = config.getPropertyKey();
        this.propertyValue = config.getPropertyValue();
        this.descText = config.getDescText();
        this.createTime = LocalDateTimeUtils.format(config.getCreateTime());
        this.updateTime = LocalDateTimeUtils.format(config.getUpdateTime());
    }
}
