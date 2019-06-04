package com.zyf.fair;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * 工作队列中的 ：公平分发
 * 
 * 能者多劳，每次队列只发送一个消息给消费者，当消费者返回消息后，在给消费者发送下一个消息
 * 
 * 需要关闭消费者的自动应答
 * 
 * 
 * @author zhang
 *
 */
public class WorkQueueSendFair {

	public static final String QUEUE_NAME = "test_work_queue";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 获取链接
		Connection connection = ConnectHelp.getConnect();

		// 建立通道
		Channel channel = connection.createChannel();

		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 只允许发送1个消息给消费者,收到消费者的消息后，再次发送一个消息给消费者
		channel.basicQos(1);

		// 循环发送 50

		for (int i = 0; i < 50; i++) {
			String msg = "main send msg:" + i;
			System.out.println("main类：" + msg);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			try {
				Thread.sleep(i * 5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// 关闭
		channel.close();

		connection.close();
	}

}
