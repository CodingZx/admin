package lol.cicco.admin.service;

import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.util.SQLUtils;
import lol.cicco.admin.dto.response.AdminResponse;
import lol.cicco.admin.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public R list(Page page, String userName){
        var list = adminMapper.findAdminList(page.getSize(),page.getStart(), SQLUtils.fuzzyAll(userName));
        var count = adminMapper.findAdminCount(SQLUtils.fuzzyAll(userName));

        return R.ok(list.stream().map(AdminResponse::new).collect(Collectors.toList()), count);
    }

    public R updateStatus(UUID adminId, boolean status){
        adminMapper.updateStatus(adminId, status);
        return R.ok();
    }

    public R remove(UUID id){
        return R.ok();
    }
}
