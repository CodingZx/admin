package lol.cicco.admin.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MenuEntity {
    private UUID id;
    private String menuName;
    private String menuUrl;
    private String icon;
    private int sortBy;
    private String menuType;
    private UUID parentId;
    private LocalDateTime createTime;
    private String permission;
}
