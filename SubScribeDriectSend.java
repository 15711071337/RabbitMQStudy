package com.zyf.subscribe.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * 路由 什么是？ 生产者发送消息的时候，绑定一个key 消费者在接受消息的时候，也绑定一个key，两个key相同，那么就可以获取到 生产者发送的消息
 * Routing Key：路由关键字，exchange根据这个关键字进行消息投递。
 * exchange接收到消息后，就根据消息的key和已经设置的binding，进行消息路由，将消息投递到一个或多个队列里。
 * 
 * 特点： 一对多
 * 
 * @author zhang
 *
 */
public class SubScribeDriectSend {
	public static final String EXCHANGE_NAME = "test_exchange_direct";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 连接
		Connection connect = ConnectHelp.getConnect();

		// 获取通道
		Channel channel = connect.createChannel();

		// 创建交换机
		channel.exchangeDeclare(EXCHANGE_NAME, "direct");// 路由

		// 消息
		String msg = "send a msg is error;";

		// 创建路由的key

		channel.basicPublish(EXCHANGE_NAME, "successful", null, msg.getBytes());

		System.out.println("交互机发送数据：" + msg);

		channel.close();
		connect.close();

	}

}
