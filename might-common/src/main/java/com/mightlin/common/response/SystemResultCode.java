package com.mightlin.common.response;

import com.mightlin.common.model.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemResultCode implements ResultCode {

	// ================ 基础部分，参考 HttpStatus =============
	/**
	 * 成功
	 */
	SUCCESS(200, "OK"),
	/**
	 * 参数错误
	 */
	BAD_REQUEST(400, "Bad Request"),
	/**
	 * 未认证
	 */
	UNAUTHORIZED(401, "Unauthorized"),
	/**
	 * 未授权
	 */
	FORBIDDEN(403, "Forbidden"),

	/**
	 * 资源不存在
	 */
	NOT_FOUND(404,"Not Found"),

	/**
	 * 冲突
	 */
	CONFLICT(409, "Conflict"),

	/**
	 * 请求太多
	 */
	TOO_MANY_REQUEST(429,"Too Many Requests"),

	/**
	 * 服务异常
	 */
	SERVER_ERROR(500, "Internal Server Error"),

	/**
	 * 参数异常
	 */
	PARAM_ERROR(50003, "参数异常");

	private final Integer code;

	private final String message;

}
