package com.mightlin.web.log;

import com.mightlin.common.constant.SysConstant;
import lombok.Builder;
import lombok.Data;

@Builder
public class AccessLog {

    /**
     * 追踪ID
     */
    private String traceId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 访问IP地址
     */
    private String ip;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 请求URI
     */
    private String uri;

    /**
     * 请求映射地址
     */
    private String matchingPattern;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求body
     */
    private String requestBody;

    /**
     * 响应状态码
     */
    private Integer httpStatus;

    /**
     * 响应信息
     */
    private String result;

    /**
     * 错误消息
     */
    private String errorMsg;

    private Long executionTime;

    /**
     * 创建时间
     */
    private Long createTime;

    @Override
    public String toString() {
        StringBuffer logString = new StringBuffer();
        // 打印请求相关参数
        logString.append(SysConstant.LINE_SEPARATOR + "=================== Start ===================" + SysConstant.LINE_SEPARATOR);
        logString.append("请求地址: " + this.uri + SysConstant.LINE_SEPARATOR);
        logString.append("请求方式: " + this.method + SysConstant.LINE_SEPARATOR);
        logString.append("请求来源: " + this.ip + SysConstant.LINE_SEPARATOR);
        logString.append("请求参数: " + this.requestParams + SysConstant.LINE_SEPARATOR);
        logString.append("请求body: " + this.requestBody + SysConstant.LINE_SEPARATOR);
        logString.append("返回参数: " + this.result + SysConstant.LINE_SEPARATOR);
        logString.append("执行耗时: " + this.executionTime + "_ms" + SysConstant.LINE_SEPARATOR);
        logString.append("=================== End ===================" + SysConstant.LINE_SEPARATOR + SysConstant.LINE_SEPARATOR);
        return logString.toString();
    }
}
