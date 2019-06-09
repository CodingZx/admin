package lol.cicco.admin.common;

import lol.cicco.admin.common.thymeleaf.PermissionDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfiguration {

    @Bean
    public PermissionDialect permissionDialect(){
        return new PermissionDialect();
    }
}
