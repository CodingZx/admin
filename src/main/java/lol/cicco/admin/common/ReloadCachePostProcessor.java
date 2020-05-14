package lol.cicco.admin.common;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lol.cicco.admin.common.annotation.ChangePropertyListener;
import lol.cicco.admin.common.util.GsonUtils;
import lol.cicco.admin.dto.response.ConfigResponse;
import lol.cicco.admin.service.ConfigService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "reload.property.enable",havingValue = "true")
public class ReloadCachePostProcessor implements BeanPostProcessor, ApplicationContextAware, InitializingBean, EnvironmentPostProcessor{
    @Autowired
    private ConfigService configService;

    private final Map<String, List<PropertyChangeListener>> listenChanges = Maps.newConcurrentMap();
    private ApplicationContext applicationContext;

    private final AtomicReference<JsonObject> cacheConfig = new AtomicReference<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> target;
        if(AopUtils.isAopProxy(bean)) {
            target = AopUtils.getTargetClass(bean);
        } else {
            target = bean.getClass();
        }
        for(Method method : target.getDeclaredMethods()) {
            ChangePropertyListener changePropertyListener = AnnotationUtils.getAnnotation(method, ChangePropertyListener.class);
            if(changePropertyListener == null) {
                continue;
            }
            String listenProperty = changePropertyListener.property().trim();
            if(listenProperty.isBlank()) {
                throw new BeanInitializationException("ChangeProperty监听Property不能为空! BeanName : " + beanName);
            }

            Class<?>[] parameterTypes = method.getParameterTypes();
            if(parameterTypes.length > 1) {
                throw new BeanInitializationException("ChangeProperty监听方法参数不能超过1个! BeanName : " + beanName);
            }
            List<PropertyChangeListener> listeners = listenChanges.getOrDefault(listenProperty, Lists.newLinkedList());

            PropertyChangeListener listener = new PropertyChangeListener();
            if(parameterTypes.length == 1) {
                listener.argType = parameterTypes[0];
            }
            listener.beanName = beanName;
            listener.method = method;
            listeners.add(listener);
            listenChanges.put(changePropertyListener.property().trim(), listeners);

            log.info("Add property change listener... beanName[{}] property[{}]", beanName, listenProperty);
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cacheConfig.set(getConfig());
        var scheduleThreadPool = Executors.newScheduledThreadPool(1, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            thread.setName("ReloadPropertyThread");
            return thread;
        });

        scheduleThreadPool.scheduleWithFixedDelay(() -> {
            log.debug("Start load config....");
            var nowConfig = getConfig();
            var preConfig = cacheConfig.get();
            Set<String> mergeKeys = new HashSet<>(nowConfig.keySet());
            mergeKeys.addAll(preConfig.keySet());
            for(String key : mergeKeys) {
                JsonElement nowVal = nowConfig.get(key);
                JsonElement preVal = preConfig.get(key);
                if(!Objects.equal(nowVal, preVal)) {
                    var listeners = listenChanges.get(key);
                    for(var listener : listeners) {
                        try {
                            Object target = applicationContext.getBean(listener.beanName);
                            if(listener.argType == null) {
                                listener.method.invoke(target);
                            } else {
                                // TODO 类型转换. 暂时只做string
                                listener.method.invoke(target, nowVal == null ? null : nowVal.getAsString());
                            }
                        }catch (Exception e) {
                            log.error("execute change listener error, msg is {}", e.getMessage(), e);
                        }
                    }
                }
            }
            cacheConfig.set(nowConfig);
        }, 10,10, TimeUnit.SECONDS);
    }

    private JsonObject getConfig(){
        var map = configService.all().stream().collect(Collectors.toMap(ConfigResponse::getPropertyKey, ConfigResponse::getPropertyValue));
        return JsonParser.parseString(GsonUtils.gson().toJson(map)).getAsJsonObject();
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("environment ................");
    }


    @Data
    private static class PropertyChangeListener {
        private String beanName;
        private Method method;
        private Class<?> argType; // 必须最多有一个
    }
}
