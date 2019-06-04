package com.zyf.fair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.zyf.help.ConnectHelp;

/**
 * 消费者2
 * 
 * @author zhang
 *
 */
public class Recv2Fair {
	public static final String QUEUE_NAME = "test_work_queue";

	public static void main(String[] args) throws IOException, TimeoutException {
		// 获取链接
		Connection connect = ConnectHelp.getConnect();

		// 建立通道
		final Channel channel = connect.createChannel();

		// 建立队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 只接受一个1消息
		channel.basicQos(1);

		// 接受消息

		DefaultConsumer consumer = new DefaultConsumer(channel) {

			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body, "utf-8");
				System.out.println("Recv2 :" + msg);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					System.out.println("Recv2  done！");
					// 当这个消息接受成功后，然后手动的给生产者发送一个消息，然后继续接受新消息
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};

		// 监听
		channel.basicConsume(QUEUE_NAME, false, consumer);// 关闭自动应答

	}

}
