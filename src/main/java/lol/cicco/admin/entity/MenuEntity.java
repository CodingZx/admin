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
@Table(name = "a_menu")
public class MenuEntity {
    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "menu_name")
    private String menuName;
    @Column(name = "menu_url")
    private String menuUrl;
    @Column(name = "icon")
    private String icon;
    @Column(name = "sort_by")
    private Integer sortBy;
    @Column(name = "menu_type")
    private String menuType;
    @Column(name = "parent_id")
    private UUID parentId;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "permission")
    private String permission;
}
