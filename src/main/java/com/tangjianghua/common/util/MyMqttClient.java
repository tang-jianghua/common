package com.tangjianghua.common.util;

import java.time.LocalDateTime;
import java.util.concurrent.ScheduledExecutorService;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MyMqttClient implements Runnable{

//	public static final String HOST = "tcp://115.182.90.210:1883";
//	public static final String HOST = "tcp://221.221.138.218:18836";
//	public static final String HOST = "tcp://192.168.1.10:1883";
//	public static final String HOST = "tcp://221.221.138.218:3161";
	public static final String HOST = "tcp://192.168.1.20:1883";
//	public static final String HOST = "tcp://47.95.72.154:2883";
//	public static final String HOST = "tcp://47.95.72.154:1883";
//	public static final String HOST = "tcp://39.97.168.28:1883";
	public  String topic;
	private String clientid ;
	private MqttClient client;
	private MqttConnectOptions options;
	private String userName = "admin";
	private String passWord = "password";

	private ScheduledExecutorService scheduler;

	public MyMqttClient(String topic, String clientid) {
		this.topic = topic;
		this.clientid = clientid;
	}
@Override
	public void run() {
		try {
			// host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
			client = new MqttClient(HOST, clientid, new MemoryPersistence());
			// MQTT的连接设置
			options = new MqttConnectOptions();
			// 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			options.setCleanSession(false);
			// 设置连接的用户名
			options.setUserName(userName);
			// 设置连接的密码
			options.setPassword(passWord.toCharArray());
			// 设置超时时间 单位为秒
			options.setConnectionTimeout(30);
			// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
			options.setKeepAliveInterval(30);
			// 设置回调
			client.setCallback(new MqttPushCallback(clientid));
//			MqttTopic topic = client.getTopic(TOPIC);
			//setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
//			options.setWill(topic, "close".getBytes(), 2, true);
			client.connect(options);
			n+=1;
			System.out.println(LocalDateTime.now()+"-----------"+clientid+"----------连接成功");
			//订阅消息
			int[] Qos  = {2};
			String[] topic1 = {topic};
			client.subscribe(topic1, Qos);

		} catch (MqttException me) {
			System.out.println("reason "+me.getReasonCode());
			System.out.println("msg "+me.getMessage());
			System.out.println("loc "+me.getLocalizedMessage());
			System.out.println("cause "+me.getCause());
			System.out.println("excep "+me);
			me.printStackTrace();
		}
	}

	private static int n = 0;

	public static void main(String[] args) throws MqttException {

/*		for (int i = 0; i < 30; i++) {
			Thread thread = new Thread(new MyMqttClient(i+"","tangjianghua--"+i));
			thread.start();
		}*/
		Thread thread = new Thread(new MyMqttClient("123/q","tangjianghua"));
		thread.start();
		Thread thread2 = new Thread(new MyMqttClient("123/w","tangjianghua2"));
		thread2.start();
	}
}
