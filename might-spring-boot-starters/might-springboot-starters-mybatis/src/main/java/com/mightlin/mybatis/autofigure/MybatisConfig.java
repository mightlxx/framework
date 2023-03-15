package com.mightlin.mybatis.autofigure;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.mightlin.mybatis.interceptor.FieldLogInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {

    /**
     * 插件配置
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 获取操作日志字段内容拦截器
        interceptor.addInnerInterceptor(new FieldLogInterceptor());
        return interceptor;
    }

    /**
     * 字段填充
     *
     * @return
     */
    @Bean
    public DataAutoFillConfig dataAutoFillConfig() {
        return new DataAutoFillConfig();
    }

}
