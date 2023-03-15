package com.mightlin.common.log;

public interface OperateLogHandler<T> {

    /**
     * 构建日志
     *
     * @param title
     * @param remark
     * @return
     */
    T buildOptLog(String title, String remark);

    /**
     * 保存
     *
     * @param optLog
     */
    void saveLog(T optLog);

    /**
     * 日志处理
     *
     * @param title
     * @param remark
     * @return
     */
    default void handlerLogOptLog(String title, String remark) {
        this.saveLog(this.buildOptLog(title, remark));
    }

}
