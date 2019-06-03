package com.zyf.workqueue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * 工作队列中的  ：轮询分发队列
 * 
 * 消费者1：奇数
 * 消费者2：偶数
 * 不管谁忙谁清闲，都不会多给一个消息，任务消息总是你一个我一个的接受
 * 
 * 
 * @author zhang
 *
 */
public class WorkQueueSend {
	
	public static final String QUEUE_NAME="test_work_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		
		//获取链接
		Connection connection=ConnectHelp.getConnect();
		
		//建立通道
		Channel channel=connection.createChannel();
		
		//声明队列
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		
		//循环发送 50
		
		for (int i = 0; i < 50; i++) {
			String msg="main send msg:"+i;
			System.out.println("main类："+msg);
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			try {
				Thread.sleep(i*20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//关闭
		channel.close();
		
		connection.close();
	}
	
	
	
	

}
