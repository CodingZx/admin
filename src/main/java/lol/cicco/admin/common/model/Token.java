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
    private final UUID adminId;
    @Getter
    private final String realName;
    @Getter
    private final UUID roleId;
    @Getter
    private final LocalDateTime createTime;

    private Token(UUID adminId, String realName, UUID roleId){
        this.adminId = adminId;
        this.realName = realName;
        this.roleId = roleId;
        this.createTime = LocalDateTime.now();
    }

    public boolean check(){
        if(adminId == null){
            return false;
        }

        if(roleId == null){
            return false;
        }

        if(createTime.isBefore(LocalDateTime.now().minusDays(7))){
            return false;
        }

        return true;
    }

    public static String createToken(AdminEntity admin){
        return RSAUtils.ecrypt(GsonUtils.gson().toJson(new Token(admin.getId(), admin.getRealName(), admin.getRoleId())), Constants.RSA.TOKEN_PUBLIC_KEY);
    }
}
