package com.mightlin.mybatis.datasource;


import cn.hutool.core.util.StrUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
    private Long currTime = 0L;

    @Override
    protected Object determineCurrentLookupKey() {
        if (getDataSource() == null) {
            currTime = System.currentTimeMillis() + 10000;
        }
        if (System.currentTimeMillis() < currTime) {
            return null;
        }
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
        if (StrUtil.isBlank(getDataSource())) {
            CONTEXT_HOLDER.set(dataSource);
        }
    }

    public static String getDataSource() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }

}
