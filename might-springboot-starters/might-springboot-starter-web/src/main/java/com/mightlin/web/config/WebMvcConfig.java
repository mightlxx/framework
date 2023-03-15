package com.mightlin.web.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.mightlin.web.interceptor.AutoRegHandlerInterceptor;
import com.mightlin.web.response.GlobalControllerAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * ·
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                //加载静态目录
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Map<String, AutoRegHandlerInterceptor> interceptorMap = SpringUtil.getBeansOfType(AutoRegHandlerInterceptor.class);
        interceptorMap.values().forEach(interceptor -> {
            // 添加拦截器
            if (CollectionUtil.isNotEmpty(interceptor.getPathPatterns())) {
                InterceptorRegistration interceptorRegistration = registry.addInterceptor(interceptor)
                        .addPathPatterns(interceptor.getPathPatterns());
                // 添加排除path
                if (CollectionUtil.isNotEmpty(interceptor.getExcludePathPatterns())) {
                    interceptorRegistration.excludePathPatterns(interceptor.getExcludePathPatterns());
                }
            }
        });
    }

    /**
     * 全局控制器增强
     *
     * @return
     */
    @Bean
    public GlobalControllerAdvice globalControllerAdvice() {
        return new GlobalControllerAdvice();
    }

    /**
     * json序列化配置
     *
     * @param builder
     * @return
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        SimpleModule simpleModule = new SimpleModule();
        // 直接将所有的Long类型转换为String 防止过长失真问题
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);
        simpleModule.addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

}
