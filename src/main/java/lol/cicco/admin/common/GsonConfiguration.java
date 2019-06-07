package lol.cicco.admin.common;

import com.google.gson.Gson;
import lol.cicco.admin.common.util.GsonUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class GsonConfiguration {

    @Bean
    public Gson gson(){
        return GsonUtils.gson();
    }
}
