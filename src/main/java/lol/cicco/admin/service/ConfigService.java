package lol.cicco.admin.service;

import com.fasterxml.uuid.Generators;
import com.google.common.base.Strings;
import lol.cicco.admin.common.model.Page;
import lol.cicco.admin.common.model.R;
import lol.cicco.admin.common.util.SQLUtils;
import lol.cicco.admin.dto.request.ConfigRequest;
import lol.cicco.admin.dto.response.ConfigResponse;
import lol.cicco.admin.entity.ConfigEntity;
import lol.cicco.admin.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConfigService {
    @Autowired
    private ConfigMapper configMapper;

    public List<ConfigResponse> all() {
        return configMapper.selectAll().stream().map(ConfigResponse::new).collect(Collectors.toList());
    }

    public ConfigResponse findById(UUID id) {
        ConfigEntity config = configMapper.selectByPrimaryKey(id);
        if (config == null) {
            return null;
        }
        return new ConfigResponse(config);
    }


    public R list(Page page, String propertyKey) {
        var weekendSql = WeekendSqls.<ConfigEntity>custom();
        if(!Strings.isNullOrEmpty(propertyKey)) {
            weekendSql.andLike(ConfigEntity::getPropertyKey, SQLUtils.fuzzyAll(propertyKey));
        }
        var example = Example.builder(ConfigEntity.class).where(weekendSql).build();
        var list = configMapper.selectByExampleAndRowBounds(example, page.getBounds());
        var count = configMapper.selectCountByExample(example);

        return R.ok(list.stream().map(ConfigResponse::new).collect(Collectors.toList()), count);
    }

    public R save(ConfigRequest config) {
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setId(config.getId());
        configEntity.setDescText(config.getDescText());
        configEntity.setPropertyKey(config.getPropertyKey());
        configEntity.setPropertyValue(config.getPropertyValue());
        configEntity.setUpdateTime(LocalDateTime.now());

        if (configEntity.getId() == null) {
            configEntity.setId(Generators.timeBasedGenerator().generate());
            configEntity.setCreateTime(LocalDateTime.now());
            configMapper.insert(configEntity);
        } else {
            configMapper.updateByPrimaryKeySelective(configEntity);
        }
        return R.ok();
    }

    public R remove(List<UUID> ids) {
        for (UUID id : ids) {
            configMapper.deleteByPrimaryKey(id);
        }
        return R.ok();
    }
}
