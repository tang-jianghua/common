package com.tangjianghua.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constant {

	Logger logger = LoggerFactory.getLogger(Constant.class);

	private Constant(){
		logger.warn("公用类，禁止创建对象！");
	}
	public static final String RES_SUCCESS = "0000";
	public static final String RES_SUCCESS_MSG = "成功";

	public static final String RES_ERROR_LACK = "0001";
	public static final String RES_ERROR_LACK_MSG = "缺少参数";

	public static final String RES_FAILE = "0002";
	public static final String RES_FAILE_MSG = "失败";

	public static final String RES_A0 = "0003";
	public static final String RES_A0_MSG = "验签失败";

	// public static final String RES_NO_MEMBER = "0004";
	// public static final String RES_NO_MEMBER_MSG = "非会员";

	public static final String RES_CONNECT_FAILE = "0005";
	public static final String RES_CONNECT_FAILE_MSG = "连接异常";

	public static final String RES_SYS_REEOR = "9999";
	public static final String RES_SYS_REEOR_MSG = "系统异常";


	//打印机可接受最长报文长度 3840 ((4KB - 256))
	public static final int PAYLOAD_MAX_LENGTH = 3840;

	//tcp自动分包长度 2kb
	public static final int TCP_BUFFER_LENGTH= 2000;


	//日志开始
	public static final String LOG_END="--------------------------------结束";
	//日志结束
	public static final String LOG_START="--------------------------------开始";

}
