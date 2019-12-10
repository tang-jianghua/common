package com.tangjianghua.common.util;

/**
 * @author tangjianghua
 *
 */
public class MyRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public MyRuntimeException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public MyRuntimeException(String code, String msg, Throwable throwable) {
		super(throwable);
		this.code = code;
		this.msg = msg;
	}
}
