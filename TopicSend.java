package com.zyf.subscribe.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * topic 队列
 * 
 * 将路由键和某种匹配模式一起使用
 * 
 * #表示多个 *表示1个
 * 
 * @author zhang
 *
 */
public class TopicSend {

	public static final String TOPIC_NAME = "topic_test";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 获取链接
		Connection connection = ConnectHelp.getConnect();

		// 建立通道
		Channel channel = connection.createChannel();

		// 建立转换机 指定类型
		channel.exchangeDeclare(TOPIC_NAME, "topic");

		// 数据的定义
		String msg = "send msg test &&";

		// 发送数据
		channel.basicPublish(TOPIC_NAME, "food.add", null, msg.getBytes());

		System.out.println("main ：" + msg);
		// 关闭
		channel.close();
		connection.close();

	}

}
