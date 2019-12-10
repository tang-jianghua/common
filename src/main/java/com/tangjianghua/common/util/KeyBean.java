package com.tangjianghua.common.util;

public class KeyBean {
	private String mac;// 签名用
	private String pik;// 加密敏感数据用

	public String getMac() {
		return mac;
	}

	public String getPik() {
		return pik;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public void setPik(String pik) {
		this.pik = pik;
	}
}