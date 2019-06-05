package com.zyf.subscribe.fanout;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.zyf.help.ConnectHelp;

/**
 * 订阅 消费者1
 * 
 * @author zhang
 *
 */
public class SubScribeRec1 {
	public static final String EXCHANGE_NAME = "test_exchange";
	public static final String QUEUE_NAME = "test_queue_1";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 连接
		Connection connect = ConnectHelp.getConnect();

		// 获取通道
		Channel channel = connect.createChannel();

		// 创建队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 绑定队列
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

		// 只接受一个
		channel.basicQos(1);
		// 接收数据
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {

				String msg = new String(body);
				System.out.println("订阅1 ：" + msg);

			}
		};

		// 监听数据
		channel.basicConsume(QUEUE_NAME, false, consumer);

	}

}
