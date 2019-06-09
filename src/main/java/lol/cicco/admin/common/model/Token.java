package lol.cicco.admin.common.model;

import lol.cicco.admin.common.Constants;
import lol.cicco.admin.common.util.GsonUtils;
import lol.cicco.admin.common.util.RSAUtils;
import lol.cicco.admin.entity.AdminEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public class Token {
    @Getter
    private UUID adminId;
    @Getter
    private String realName;
    @Getter
    private UUID roleId;
    @Getter
    private LocalDateTime createTime;

    private Token(UUID adminId, String realName, UUID roleId){
        this.adminId = adminId;
        this.realName = realName;
        this.roleId = roleId;
        this.createTime = LocalDateTime.now();
    }

    public static String createToken(AdminEntity admin){
        return RSAUtils.ecrypt(GsonUtils.gson().toJson(new Token(admin.getId(), admin.getRealName(), admin.getRoleId())), Constants.RSA.TOKEN_PUBLIC_KEY);
    }
}
