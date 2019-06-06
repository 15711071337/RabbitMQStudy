package com.zyf.subscribe.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.zyf.help.ConnectHelp;

/**
 * Topic 消费者1
 * 
 * @author zhang
 *
 */
public class TopicRevc2 {
	public static final String TOPIC_NAME = "topic_test";
	public static final String QUEUE_NAME = "topic_queue_2";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 建立连接
		Connection connection = ConnectHelp.getConnect();

		// 建立通道
		final Channel channel = connection.createChannel();

		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 绑定队列
		channel.queueBind(QUEUE_NAME, TOPIC_NAME, "food.#");
		// 每次只接受一个数据
		channel.basicQos(1);

		// 获取数据
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {

				String msg = new String(body, "utf-8");
				System.out.println("TOP2:" + msg);
				// 可以接受下一个消息
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};

		// 监听队列
		channel.basicConsume(QUEUE_NAME, false, consumer);

	}

}
