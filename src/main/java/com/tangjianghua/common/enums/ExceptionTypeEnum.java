package com.tangjianghua.common.enums;

public enum ExceptionTypeEnum {

	MULTIPLE_COLUMN("0001","数据库多条记录"),
	SQL_EXCEPTION("0002","sql异常"),
	SQL_FAIL("0003","sql失败"),
	VO_EXCEPTION("0004","异常传递");

	private String code;

	private String msg;

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	ExceptionTypeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	
}
