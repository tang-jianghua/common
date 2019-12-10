package com.tangjianghua.common.util;


import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SignUtils {

	//获取签名原串
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getSource(Map paramMap) {
		SortedMap<String, String> smap = new TreeMap<String, String>(paramMap);

		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, String> m : smap.entrySet()) {
			Object value = m.getValue();
			if (value != null && !"signature".equals(m.getKey())) {
				stringBuffer.append(m.getKey()).append("=").append(value).append("&");
			}
		}
		return stringBuffer.substring(0, stringBuffer.length() - 1);
	}

	//验签
	@SuppressWarnings("rawtypes")
	public static Boolean verifySign(String key, String signature, Map map) {
		if (StringUtils.isEmpty(signature)) {
			return false;
		}
		//签名
		String sign = sign(map,key);
		if (sign.equals(signature)) {
			return true;
		}
		return false;
	}
	
	//签名
	public static String sign(Map paramMap,String workey) {
		// 对字段进行过滤排序，构建加密原串
		String source = getSource(paramMap);
		// 对加密原串进行MD5计算
		String md5Encode = MD5Encryptor.MD5Encode(source);
		//workey进行3desc加密
		String retSignature = Des3Util.encryptStr(workey, md5Encode);
		
		return retSignature;
	}
}
