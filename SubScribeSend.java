package com.zyf.subscribe.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * 订阅模式
 * 
 * @author zhang
 *
 *         一个生产者，多个消费者 每一个消费者都有自己的队列 生产者没有直接将消息发送到队列中去，而是发送到了交换机或者转发器（exchange）中
 *         每个队列都要绑定到交换机上 生产者发送的消息，经过交互机到达队列就能实现一个消息被多个消费者消费
 *
 *
 */
public class SubScribeSend {

	public static final String EXCHANGE_NAME = "test_exchange";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 连接
		Connection connect = ConnectHelp.getConnect();

		// 获取通道
		Channel channel = connect.createChannel();

		// 创建一个交互机
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");// 分发

		// 消息
		String msg = "send a msg;";
		channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());

		System.out.println("交互机发送数据：" + msg);

		channel.close();
		connect.close();

	}

}
