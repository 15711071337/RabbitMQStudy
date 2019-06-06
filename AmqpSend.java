package com.zyf.transtion.amqp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * 事务之AMQP协议 解决RabbitMQ服务器异常的数据丢失问题 问题：生产者将消息发送出去之后，消息到底有没有到达RabbitMQ服务器，默认是不知道的
 * 
 * @author zhang
 * 
 *         AMQP
 * 
 *         txselect 将当前的channel设置成transtion模式 txCommit：用于提交事务 txRollback：回滚事务
 * 
 *         缺点：此种模式很是耗时，采用这种方式，降低了RabbitMQ的消息吐量。
 *
 */
public class AmqpSend {

	public static final String QUEUE_NAME = "test_transtion_amqp";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 获取连接
		Connection connection = ConnectHelp.getConnect();

		// 创建通道
		Channel channel = connection.createChannel();

		// 创建队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		try {
			// 开启事务
			channel.txSelect();
			// 数据
			String msg = "send tv select()";

			int s = 1 / 0;
			// 发送数据
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

			System.out.println("发送：" + msg);
			// 提交事务
			channel.txCommit();
		} catch (Exception e) {
			// 回滚
			channel.txRollback();// 回滚
			System.out.println("事务回滚！");
		}

		// 关闭
		channel.close();
		connection.close();
	}

}
