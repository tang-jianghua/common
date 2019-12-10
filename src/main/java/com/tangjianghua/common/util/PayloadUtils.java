package com.tangjianghua.common.util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PayloadUtils {

	private static Logger logger = LoggerFactory.getLogger(PayloadUtils.class);

	private PayloadUtils(){
		logger.warn("公用类，禁止创建对象！");
	}
	/**
	 * 组装打印多联指令
	 * @param cnt
	 * @param payload
	 * @return
	 */
	public static byte[] makeCntPayload(int cnt, String payload) {
		logger.info("组装打印多联指令");
		byte[] bytes = payload.getBytes();
		//报文长度
		int length = bytes.length;
		//初始化一个5+bytes.length的byteBuffer  5是报文前需要添加的多联byte数
		ByteBuffer byteBuffer = ByteBuffer.allocate(5+ length);
		//多联协议指令
		byteBuffer.put((byte) 0x1e);
		byteBuffer.put((byte) 0x10);
		//联数
		byteBuffer.put((byte) cnt);
		int high = length >>> 8;
		int low = length & 0xFF;
		//报文长度高位字节
		byteBuffer.put((byte) high);
		//报文长度低位字节
		byteBuffer.put((byte) low);
		//报文
		byteBuffer.put(bytes);
		int writePos = byteBuffer.position();
		byte[] data = new byte[writePos];
		byteBuffer.flip();
		byteBuffer.get(data, 0, writePos);
		byteBuffer.clear();
		return data;
	}

	/**
	 * 组装打印多联指令
	 * @param cnt
	 * @param payload
	 * @return
	 */
	public static byte[] makeCntPayload(int cnt, byte[] payload) {
		logger.info("组装打印多联指令");
		//报文长度
		int payLoadLength = payload.length;
		//初始化一个5+payLoadLength的byteBuffer  5是报文前需要添加的多联byte数
		ByteBuffer byteBuffer = ByteBuffer.allocate(5+ payLoadLength);
		//多联协议指令
		byteBuffer.put((byte) 0x1e);
		byteBuffer.put((byte) 0x10);
		//联数
		byteBuffer.put((byte) cnt);
		int high = payLoadLength >>> 8 ;
		int low = payLoadLength & 0xFF;
		//报文长度高位字节
		byteBuffer.put((byte) high);
		//报文长度低位字节
		byteBuffer.put((byte) low);
		//报文
		byteBuffer.put(payload);
		int writePos = byteBuffer.position();
		byte[] data = new byte[writePos];
		byteBuffer.flip();
		byteBuffer.get(data, 0, writePos);
		byteBuffer.clear();
		return data;
	}
	/**
	 * 组装打印多联指令
	 * @param cnt
	 * @param payload
	 * @return
	 */
	public static byte[] makeCntPayloadWithCRC(int cnt, byte[] payload) {

		//crc指令---------------------------------------begin
		//长度4=指令*2+crc高位地位
		byte[] crcBytes = new byte[4];
		//控制纸传感器指令
		crcBytes[0]=(byte)0x1b;
		crcBytes[1]=(byte)0x63;
		//获取crc码
		byte[] crcBytesSuf = CRCUtils.crc16X25Bytes(payload);
		crcBytes[2]=crcBytesSuf[0];
		crcBytes[3]=crcBytesSuf[1];
		//crc指令---------------------------------------end

		//报文长度
		int payLoadLength = payload.length;
		//crc长度
		int crcLength = crcBytes.length;
		//初始化一个5+payLoadLength+crcLength的byteBuffer  5是报文前需要添加的多联byte数
		ByteBuffer byteBuffer = ByteBuffer.allocate(5+ payLoadLength+crcLength);

		//多联协议指令----------------------------------begin
		logger.info("组装打印多联指令");
		byteBuffer.put((byte) 0x1e);
		byteBuffer.put((byte) 0x10);
		//联数
		byteBuffer.put((byte) cnt);
		int high = payLoadLength >>> 8 ;
		int low = payLoadLength & 0xFF;
		//报文长度高位字节
		byteBuffer.put((byte) high);
		//报文长度低位字节
		byteBuffer.put((byte) low);
		//多联协议指令----------------------------------end

		//报文
		logger.info("组装报文");
		byteBuffer.put(payload);
		logger.info("组装crc");
		byteBuffer.put(crcBytes);
		int writePos = byteBuffer.position();
		byte[] data = new byte[writePos];
		byteBuffer.flip();
		byteBuffer.get(data, 0, writePos);
		byteBuffer.clear();
		return data;
	}
}
