package com.tangjianghua.common.util;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ScheduledExecutorService;

public class MyMqttClient2 implements Runnable{

	//	public static final String HOST = "tcp://115.182.90.210:1883";
	public static final String HOST = "tcp://192.168.1.20:1883";
//	public static final String HOST = "tcp://47.95.72.154:2883";
	//	public static final String HOST = "tcp://47.95.72.154:1883";
//	public static final String HOST = "tcp://39.97.168.28:1883";
	public  String topic;
	private String clientid ;
	private MqttClient client;
	private MqttConnectOptions options;
	private String content;
	private String userName = "admin";
	private String passWord = "password";

	private ScheduledExecutorService scheduler;

	@Override
	public void run() {
		try {
			// host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(HOST, clientid, new MemoryPersistence());
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(true);
			// 设置连接的用户名
			options.setUserName(userName);
			// 设置连接的密码
			options.setPassword(passWord.toCharArray());
			// 设置超时时间 单位为秒
//			options.setConnectionTimeout(10);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//			options.setKeepAliveInterval(20);
			// 设置回调
			client.setCallback(new MqttPushCallback(clientid));
//			MqttTopic topic = client.getTopic(TOPIC);
			//setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//			options.setWill(topic, "close".getBytes(), 2, true);
			System.out.println("Connecting to broker: "+HOST);
			client.connect(options);
			System.out.println("Connected");
			System.out.println("Publishing message: "+content);
			MqttMessage message = new MqttMessage(content.getBytes());
			message.setQos(2);
			client.publish(topic, message);
			System.out.println("Message published");
			client.disconnect();
			System.out.println("Disconnected");
			System.exit(0);
		} catch(MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}

	public MyMqttClient2(String topic, String clientid, String content) {
		this.topic = topic;
		this.clientid = clientid;
		this.content = content;
	}

	public static void main(String[] args) throws MqttException {
	/*	for (int i = 0; i < 30; i++) {
			Thread thread = new Thread(new MyMqttClient2(i+"",i+40+"",i+""));
			thread.start();
		}*/
	/*	Thread thread = new Thread(new MyMqttClient2("123/q","123","123"));
		thread.start();*/
		Thread thread2 = new Thread(new MyMqttClient2("123/w","1233","123"));
		thread2.start();
	}
}
