package lol.cicco.admin.dto.response;

import lol.cicco.admin.common.em.MenuType;
import lol.cicco.admin.entity.MenuEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MenuResponse {
    private UUID id;
    private String menuName;
    private String menuUrl;
    private String icon;
    private int sortBy;
    private String menuType;
    private String parentId;
    private String permission;
    private LocalDateTime createTime;

    public MenuResponse(MenuEntity menu) {
        this.id = menu.getId();
        this.menuName = menu.getMenuName();
        this.menuUrl = menu.getMenuUrl();
        this.icon = menu.getIcon();
        this.sortBy = menu.getSortBy();
        this.menuType = MenuType.valueOf(menu.getMenuType()).getName();
        this.createTime = menu.getCreateTime();
        this.permission = menu.getPermission();
        this.parentId = menu.getParentId() == null ? "-1" : menu.getParentId().toString();
    }
}
