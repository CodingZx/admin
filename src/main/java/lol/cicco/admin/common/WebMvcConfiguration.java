package lol.cicco.admin.common;

import com.google.common.collect.Lists;
import lol.cicco.admin.common.interceptor.LoginInterceptor;
import lol.cicco.admin.common.interceptor.PermissionInterceptor;
import lol.cicco.admin.common.util.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Autowired
	private LoginInterceptor loginInterceptor;
	@Autowired
	private PermissionInterceptor permissionInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginInterceptor);
		registry.addInterceptor(permissionInterceptor);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.removeIf(converter -> converter instanceof GsonHttpMessageConverter);

		GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter(GsonUtils.gson());
		gsonHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
		gsonHttpMessageConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON));

		converters.add(gsonHttpMessageConverter);
	}

}
