package lol.cicco.admin.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class MenuRequest {
    private UUID id;
    private String menuName;
    private String menuUrl;
    private String icon;
    private int sortBy;
    private String menuType;
    private String parentId;
    private String permission;
}
