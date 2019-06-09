package lol.cicco.admin.common.em;

import lombok.Getter;

public enum MenuType {
    LINK("链接"),
    BUTTON("按钮"),
    DIC("目录")
    ;

    @Getter
    private String name;

    MenuType(String name) {
        this.name = name;
    }
}
