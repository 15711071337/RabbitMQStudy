package com.zyf.workqueue;

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
 * @author zhang
 *
 */
public class Recv2 {
	public static final String QUEUE_NAME="test_work_queue";
	public static void main(String[] args) throws IOException, TimeoutException {
		//获取链接
		Connection connect=ConnectHelp.getConnect();
		
		//建立通道
		Channel channel=connect.createChannel();
		
		//建立队列
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		
		//接受消息
		
		DefaultConsumer consumer=new DefaultConsumer(channel) {
			
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg=new String(body,"utf-8");
				System.out.println("Recv2 :"+msg);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					System.out.println("Recv2  done！");
				}
			}
		};
		
		//监听
		channel.basicConsume(QUEUE_NAME, true, consumer);
		
	}

}
