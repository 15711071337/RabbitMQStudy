package com.zyf.transtion.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.zyf.help.ConnectHelp;

/**
 * RabbitMQ 之comfirm 消费者
 * 
 * @author zhang
 *
 */
public class ConfirmRevc {

	public static final String QUEUE_NAME = "p_confirmsend";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 获取连接
		Connection connetion = ConnectHelp.getConnect();

		// 创建通道
		Channel channel = connetion.createChannel();

		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 接受数据，并监听
		channel.basicConsume(QUEUE_NAME, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {

				System.out.println("confirm Revc：" + new String(body, "utf-8"));

			}
		});

	}

}
