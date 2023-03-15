package com.mightlin.common.response;

import cn.hutool.core.util.StrUtil;
import com.mightlin.common.model.ResultCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String message;

    private final int code;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public BusinessException(ResultCode resultCode, String msg) {
        this(resultCode.getCode(), msg);
    }

    public BusinessException(String msg) {
        this(SystemResultCode.SERVER_ERROR.getCode(), msg);
    }

    public BusinessException(ResultCode resultCode, Throwable e) {
        super(resultCode.getMessage(), e);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    /**
     * 用于需要format返回结果的异常
     *
     * @author lingting 2021-04-13 14:25
     */
    public BusinessException(ResultCode resultCode, Throwable e, Object... args) {
        this(resultCode.getCode(), StrUtil.format(resultCode.getMessage(), args), e);
    }

    public BusinessException(int code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public BusinessException(int code, String message, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }
}

