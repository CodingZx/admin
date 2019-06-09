package lol.cicco.admin.service;

import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.model.Token;
import lol.cicco.admin.common.util.PasswordUtils;
import lol.cicco.admin.dto.request.LoginRequest;
import lol.cicco.admin.dto.response.LoginResponse;
import lol.cicco.admin.entity.AdminEntity;
import lol.cicco.admin.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class LoginService {
    @Autowired
    private AdminMapper adminMapper;

    public R login(LoginRequest login){
        AdminEntity admin = adminMapper.findByUserName(login.getUserName());
        if(admin == null) {
            return R.other("账号密码不正确");
        }
        if(!PasswordUtils.verify(login.getPassword(), admin.getPassword())){
            return R.other("账号密码不正确");
        }

        LoginResponse response = new LoginResponse();
        response.setForward("main");
        response.setToken(Token.createToken(admin));
        return R.ok(response);
    }
}
