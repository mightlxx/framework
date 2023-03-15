package com.mightlin.common.model;

import com.mightlin.common.response.SystemResultCode;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 返回类
 *
 * @param <T>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BaseResponse<T> implements Serializable {


	private static final long serialVersionUID = 1L;

	/**
	 * 返回状态码
	 */
	private int code;

	/**
	 * 返回信息
	 */
	private String msg;

	/**
	 * 数据
	 */
	private T data;

	public static <T> BaseResponse<T> ok() {
		return ok(null);
	}

	public static <T> BaseResponse<T> ok(T data) {
		return new BaseResponse<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data)
				.setMsg(SystemResultCode.SUCCESS.getMessage());
	}

	public static <T> BaseResponse<T> ok(T data, String message) {
		return new BaseResponse<T>().setCode(SystemResultCode.SUCCESS.getCode()).setData(data).setMsg(message);
	}


	public static <T> BaseResponse<T> failed() {
		return new BaseResponse<T>().setCode(SystemResultCode.SERVER_ERROR.getCode())
				.setMsg(SystemResultCode.SERVER_ERROR.getMessage());
	}


	public static <T> BaseResponse<T> failed(int code, String message) {
		return new BaseResponse<T>().setCode(code).setMsg(message);
	}

	public static <T> BaseResponse<T> failed(ResultCode failMsg) {
		return new BaseResponse<T>().setCode(failMsg.getCode()).setMsg(failMsg.getMessage());
	}

	public static <T> BaseResponse<T> failed(ResultCode failMsg, String message) {
		return new BaseResponse<T>().setCode(failMsg.getCode()).setMsg(message);
	}

	public static <T> BaseResponse<T> failed(String message) {
		return new BaseResponse<T>().setCode(SystemResultCode.SERVER_ERROR.getCode()).setMsg(message);
	}
}
