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
@Table(name = "a_config")
public class ConfigEntity {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "property_key")
    private String propertyKey;
    @Column(name = "property_value")
    private String propertyValue;
    @Column(name = "desc_text")
    private String descText;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
