package lol.cicco.admin.dto.response;

import com.google.common.collect.Lists;
import lol.cicco.admin.entity.MenuEntity;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MenuTreeResponse {
    private UUID id;
    private String label;
    private String icon;
    private boolean checked;
    private boolean leaf;
    private List<MenuTreeResponse> children;

    public MenuTreeResponse(MenuEntity menu, boolean checked, boolean leaf){
        this.id = menu.getId();
        this.label = menu.getMenuName();
        this.icon = menu.getIcon();
        this.checked = checked;
        this.children = Lists.newLinkedList();
        this.leaf = leaf;
    }
}
