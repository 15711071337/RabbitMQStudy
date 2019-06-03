package com.zyf.rabbit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * 发送内容 生产者
 * 
 * 简单队列 简单队列是一对一的关系 缺点就是耦合性比较高
 * 
 * @author zhang
 *
 */
public class Send {

	// 声明一个队列名字
	public static final String QUEUE_NAME = "test_simple_queue";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 获取链接
		Connection connect = ConnectHelp.getConnect();

		// 从连接中获取通道 channel
		Channel channel = connect.createChannel();

		// 声明一个序列 queuedeclare
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 声明要发送的数据 string类型
		String msg = "hello  test rabbitMQ！";

		// push
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

		System.out.println("发送的信息：" + msg);
		// 关闭通道
		channel.close();
		// 关闭连接
		connect.close();

	}

}
