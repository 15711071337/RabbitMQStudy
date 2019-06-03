package com.zyf.help;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

/**
 * 消费者 接受数据
 * 
 * @author zhang
 *
 */
public class ReviceMsg {
	// 声明一个队列名字
	public static final String QUEUE_NAME = "test_simple_queue";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 获取链接

		Connection connection = ConnectHelp.getConnect();

		// 创建通道
		Channel cannel = connection.createChannel();
		// 声明队列
		cannel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 执行监听方法
		DefaultConsumer consumer = new DefaultConsumer(cannel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body);
				System.out.println("接收到的信息：" + msg);
			}
		};

		// 获取数据
		cannel.basicConsume(QUEUE_NAME, true, consumer);
	}
}
