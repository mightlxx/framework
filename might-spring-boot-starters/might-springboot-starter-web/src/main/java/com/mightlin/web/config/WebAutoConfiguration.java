package com.mightlin.web.config;

import com.mightlin.common.domain.event.DomainEventBus;
import com.mightlin.common.log.AccessLogHandler;
import com.mightlin.common.log.OperateLogHandler;
import com.mightlin.web.auth.AuthHandler;
import com.mightlin.web.event.DefaultEventBus;
import com.mightlin.web.filter.GlobalFilter;
import com.mightlin.web.interceptor.AuthInterceptor;
import com.mightlin.web.log.DefaultAccessLogHandler;
import com.mightlin.web.log.OperateLogAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

@AutoConfiguration
@EnableAspectJAutoProxy
@Import({WebMvcConfig.class})
public class WebAutoConfiguration {


    /**
     * 消息总线配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DomainEventBus.class)
    public DomainEventBus domainEventBus() {
        return new DefaultEventBus();
    }


    /**
     * 全局过滤器
     *
     * @return
     */
    @Bean
    public GlobalFilter globalFilter() {
        return new GlobalFilter();
    }

    /**
     * 注册全局过滤器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<GlobalFilter> globalFilterRegistration() {
        FilterRegistrationBean<GlobalFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(globalFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * 访问日志处理器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AccessLogHandler.class)
    public DefaultAccessLogHandler accessLogHandler() {
        return new DefaultAccessLogHandler();
    }

    /**
     * 用户操作日志切面
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(OperateLogHandler.class)
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

    /**
     * 权限校验拦截器
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(AuthHandler.class)
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }
}
