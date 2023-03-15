package com.mightlin.common.log;

import lombok.Data;

@Data
public class OptLogFieldContentThreadLocal {
    private static final ThreadLocal<StringBuilder> CONTENT = ThreadLocal.withInitial(() -> new StringBuilder());

    public static void append(StringBuilder sb) {
        CONTENT.get().append(sb);
    }

    public static String getLogString() {
        return CONTENT.get().toString();
    }

    public static void remove() {
        CONTENT.remove();
    }
}