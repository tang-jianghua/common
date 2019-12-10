package com.tangjianghua.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tangjianghua.common.payload.OrderPayload;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class MqttPayloadUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MqttPayloadUtil.class);

	/**
	 * @param merchantName 商户名称
	 * @param merchantId   商户号
	 * @param payType      支付方式  微信支付/支付宝/云闪付
	 * @param orderNo      订单号
	 * @param amount       订单金额
	 * @param disAmount    商户优惠
	 * @param payAmount    实际支付金额
	 * @param payTime      交易时间
	 * @return
	 */
	public static String packagePayload(String merchantName, String merchantId, String payType, String orderNo, String amount, String disAmount, String payAmount, Date payTime) {

		LOGGER.info("开始封装报文");

		//格式化日期
		DateFormat dateTimeInstance = DateFormat.getDateTimeInstance();
		String payTimeFormat = dateTimeInstance.format(payTime);

		//格式化金额
		amount = packageAmount(amount);
		disAmount = packageAmount(disAmount);
		payAmount = packageAmount(payAmount);

		ByteBuffer byteBuffer = ByteBuffer.allocate(102400);

		setHorizontalAlign(byteBuffer, (byte) 1);

		setDoubleFontSize(byteBuffer, (byte) 2);

		byteBuffer.put("网联客".getBytes());

		byteBuffer.put("\n\n".getBytes());

		setHorizontalAlign(byteBuffer, (byte) 0);

		setDoubleFontSize(byteBuffer, (byte) 1);

		setDefaultLineSpace(byteBuffer);

		byteBuffer.put("——————交易凭证——————\n".getBytes());

		byteBuffer.put("商户名称:    ".getBytes());

		byteBuffer.put(merchantName.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("商户号:      ".getBytes());

		byteBuffer.put(merchantId.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("交易类型:    ".getBytes());

		byteBuffer.put(payType.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("订单号:      ".getBytes());

		byteBuffer.put(orderNo.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("订单金额:    ￥".getBytes());

		byteBuffer.put(amount.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("商户优惠:    ￥".getBytes());

		byteBuffer.put(disAmount.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("支付金额:    ￥".getBytes());

		byteBuffer.put(payAmount.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("交易时间:    ".getBytes());

		byteBuffer.put(payTimeFormat.getBytes());

		byteBuffer.put("\n".getBytes());

		byteBuffer.put("————————————————\n".getBytes());

		byteBuffer.put("微信支付成功，请留意资金变化".getBytes());

		byteBuffer.put("\n\n\n\n\n\n\n\n".getBytes());

		int writePos = byteBuffer.position();

		byte[] data = new byte[writePos];

		byteBuffer.flip();

		byteBuffer.get(data, 0, writePos);

		String result = new String(data);

		LOGGER.info("报文:");

		LOGGER.info(result);

		return result;
	}

	/**
	 * 设置水平对齐方式
	 * 0：左对齐
	 * 1：中间对齐
	 * 2：右对齐
	 */
	static void setHorizontalAlign(ByteBuffer byteBuffer, byte align) {
		byteBuffer.put((byte) 0x1B);
		byteBuffer.put((byte) 0x61);
		byteBuffer.put(align);
	}


	/**
	 * 设置字符倍宽倍高
	 *
	 * @param flag 1：字符不倍宽不倍高 2：字符宽度高度都放大两倍
	 */
	static void setDoubleFontSize(ByteBuffer byteBuffer, byte flag) {
		byteBuffer.put((byte) 0x1B);
		byteBuffer.put((byte) 0x57);
		byteBuffer.put(flag);
	}

	/**
	 * 设置字体大小
	 *
	 * @param fontSize 0：选择24点阵字号 1：选择16点阵字号
	 */
	static void setFontSize(ByteBuffer byteBuffer, byte fontSize) {
		byteBuffer.put((byte) 0x1B);
		byteBuffer.put((byte) 0x4D);
		byteBuffer.put(fontSize);
	}


	/**
	 * 设置默认水平对齐方式
	 */
	static void setDefaultHorizontalAlign(ByteBuffer byteBuffer) {
		byteBuffer.put((byte) 0x1B);
		byteBuffer.put((byte) 0x61);
		byteBuffer.put((byte) 0x00);
	}

	/**
	 * 设置默认行间距
	 *
	 * @return
	 */
	static void setDefaultLineSpace(ByteBuffer byteBuffer) {
		byteBuffer.put((byte) 0x1B);
		byteBuffer.put((byte) 0x32);
	}

	static String packageAmount(String amount) {
		if (amount.indexOf(".") != -1) {
			String[] split = amount.split("\\.");
			if (split[1].length() < 2) {
				String s = split[1];
				do {
					s = s + "0";
				} while (s.length() < 2);
				return split[0] + "." + s;
			}
			return amount;
		} else {
			return amount + ".00";
		}
	}

	public static void sendMqtt(String topic,Date date) {
		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("qos", "2");//只发送一次
		postParameters.add("cnt", "1");//只发送一次
		RestTemplate restTemplate = new RestTemplate();
		String payload = packagePayload("", "测试用户", "", "", "0.02", "0.01", "", date==null?new Date():date);
		System.out.println(HexUtils.bytesToHex(payload.getBytes()));
		postParameters.add("topic", topic);
		postParameters.add("payload", payload);
		String s = restTemplate.postForObject(url, postParameters, String.class);
		if (s.indexOf("0000") != -1) {
			System.out.println(s);
		} else {
			System.err.println(s);
		}
	}

	public static void sendMqtt(String topic) {
//				String url = "http://localhost:9091/mqtt/sendV1.1";
//		String url = "http://run.buybal.com:88/mqttPublishServer/mqtt/sendV1.1";
		String url = "http://all.buybal.com/mqttPublishServer/mqtt/send";
//		String url = "http://all.buybal.com/mqttPublishServer/mqtt/sendV1.1";
//		String url = "http://localhost:9091/mqtt/sendV1.1";
		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("qos", "2");//只发送一次
		RestTemplate restTemplate = new RestTemplate();
		String payload =topic+"|1007|2018121318060337212345|0.01|2001";
		postParameters.add("topic", topic);
		postParameters.add("payload", payload);
		String s = restTemplate.postForObject(url, postParameters, String.class);
		if (s.indexOf("0000") != -1) {
			System.out.println(s);
		} else {
			System.err.println(s);
		}
	}
//				static	String url = "http://192.168.1.201:8086/mqttPublishServer/mqtt/sendV1.1";
//	static	String url = "http://run.buybal.com:88/mqttPublishServer/mqtt/sendV1.1";
//	static	String url = "http://localhost:8080/mqtt/send";
//	static	String url = "http://47.95.72.154:8080/mqttPublishServer/mqtt/send";
	static	String url = "http://run.buybal.com/mqttPublishServer/mqtt/send";
//	static	String url = "http://all.buybal.com/mqttPublishServer/mqtt/send";
//	static	String url = "http://192.168.1.205:2180/mqttPublishServer/mqtt/sendV1.2";
//	static	String url = "http://run.buybal.com/mqttPublishServer/mqtt/sendV1.2";
//	static	String url = "http://all.buybal.com/mqttPublishServer/mqtt/sendV1.2";
//		String url = "http://localhost:9091/mqtt/sendV1.1";
	public static void sendMqtt(String topic,String payload) throws Exception{
		MultiValueMap<String, String> postParameters = new LinkedMultiValueMap<>();

		postParameters.add("qos", "2");//只发送一次
		RestTemplate restTemplate = new RestTemplate();
		postParameters.add("topic", topic);
		postParameters.add("payload", new String(payload.getBytes(),"GBK"));
		String s = restTemplate.postForObject(url, postParameters, String.class);
		if (s.indexOf("0000") != -1) {
			System.out.println(s);
		} else {
			System.err.println(s);
		}
	}
	public static void sendMqttJson(String topic,JSONObject payload) {
		HttpHeaders headers = new HttpHeaders();
		//定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
		headers.setContentType(MediaType.APPLICATION_JSON);
		//RestTemplate带参传的时候要用HttpEntity<?>对象传递
		HttpEntity<JSONObject> request = new HttpEntity<>(payload, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JSONObject> result = restTemplate.postForEntity(url, request, JSONObject.class);
		if (result.toString().indexOf("0000") != -1) {
			System.out.println(result.toString());
		} else {
			System.err.println(result.toString());
		}
	}


	public static void main(String[] args) throws Exception {
		sendMqtt("VB7300000135","VB7300000135|1007|2019030210214000574646|1.00|2001");
		sendMqtt("VB7300000135","VB7300000135|1007|2019030210214000574646|1.00|2002");
		sendMqtt("VB7300000135","VB7300000135|1007|2019030210214000574646|1.00|2003");
		sendMqtt("VB7300000135","VB7300000135|1007|2019030210214000574646|1.00|2004");
//		System.out.println(Charset.defaultCharset());
//			sendMqtt(,"352736082430359|1007|2018121318060337212345|"+i+"|2001");
/*		for (int i = 0; i < 3600/5/2; i++) {
			sendMqtt("352019040227799","352019040227799|1007|2019030210214000574646|"+i+"|2001");
			Thread.sleep(5000);
		}*/
//		sendMqtt("352736082469043","352019040227799|1007|2019030210214000574646|132|2001");
//		sendMqtt("869632030191076",new Date());
//		sendMqtt("869632030183925",new Date());
//		sendMqtt("352736082610463",new Date());
//		sendMqtt(MD5Encryptor.MD5Encode("352736082469043"),"352736082469043|1007|2018121318060337212345|888.00|2001");
//		sendMqtt(MD5Encryptor.MD5Encode("352736082427124"),"352736082427124|1007|2018121318060337212345|888.00|2001");
//		sendMqtt(MD5Encryptor.MD5Encode("352736082222277"),"352736082222277|1007|2018121318060337212345|888.00|2001");
//		sendMqtt(MD5Encryptor.MD5Encode("352736082430359"),"352736082430359|1007|20190116154755282352736082137871KqYou|1.00|2001");
//		System.out.println(MD5Encryptor.MD5Encode("352736082427124"));

	}

	public static String setFontSize(boolean spaceLattice,boolean high,boolean width,boolean bold,boolean underLine){
		ByteBuffer byteBuffer = ByteBuffer.allocate(100);
		byteBuffer.put((byte) 0x1B);
		byteBuffer.put((byte) 0x21);
		//拼装指令字节
		StringBuilder stringBuilder =new StringBuilder();
		//第0位 0--24点阵字号；1--16点阵字号
		stringBuilder.append(spaceLattice?"1":"0");
		//第1,2位未定义
		stringBuilder.append("00");
		//第3位 0--取消加粗模式；1--选择加粗模式
		stringBuilder.append(bold?"1":"0");
		//第4位 0--取消倍高模式；1--选择倍高模式
		stringBuilder.append(high?"1":"0");
		//第5位 0--取消倍宽模式；1--选择倍宽模式
		stringBuilder.append(width?"1":"0");
		//第6位未定义
		stringBuilder.append("0");
		//第7位 0--取消下划线模式；1--选择下划线模式
		stringBuilder.append(underLine?"1":"0");
		stringBuilder.reverse();
		byteBuffer.put((byte)Integer.parseInt(stringBuilder.toString(), 2));
		int position = byteBuffer.position();
		byte[] bytes = new byte[position];
		byteBuffer.flip();
		byteBuffer.get(bytes,0,position);
		byteBuffer.clear();
		return new String(bytes);
	}
}
