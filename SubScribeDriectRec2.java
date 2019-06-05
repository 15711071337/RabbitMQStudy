package com.zyf.subscribe.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.zyf.help.ConnectHelp;

/**
 * 路由的消费者 1
 * 
 * @author zhang
 *
 */
public class SubScribeDriectRec2 {
	public static final String EXCHANGE_NAME = "test_exchange_direct";
	public static final String DIRECT_NAME2 = "direct_2";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 连接
		Connection connect = ConnectHelp.getConnect();

		// 获取通道
		final Channel channel = connect.createChannel();

		// 创建队列
		channel.queueDeclare(DIRECT_NAME2, false, false, false, null);
		// 绑定队列
		channel.queueBind(DIRECT_NAME2, EXCHANGE_NAME, "error");
		channel.queueBind(DIRECT_NAME2, EXCHANGE_NAME, "successful");

		// 一次就接受一个值
		channel.basicQos(1);

		// 接收数据
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {

				String msg = new String(body, "utf-8");
				System.out.println("路由的消费者 2：" + msg);
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};

		// 监听队列
		channel.basicConsume(DIRECT_NAME2, false, consumer);

	}
}
