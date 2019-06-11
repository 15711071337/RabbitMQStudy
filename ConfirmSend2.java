package com.zyf.transtion.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * Confirm
 * 
 * @author zhang
 * 
 *         编程模式： 2.批量的 发一批 waitForCOnfirms
 * 
 * 
 * 
 * 
 * 
 */
public class ConfirmSend2 {
	public static final String QUEUE_NAME = "p_confirmsend";

	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

		// 获取连接
		Connection connection = ConnectHelp.getConnect();
		// 创建通道
		Channel channel = connection.createChannel();
		// 创建队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 开启事务
		channel.confirmSelect();
		for (int i = 0; i < 20; i++) {
			// 数据
			String msg = "this is data" + i;
			// 发送数据
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			System.out.println("ready send data:" + msg);
		}

		// 监听发送成功与失败
		if (channel.waitForConfirms()) {
			System.out.println("send is ok");
		} else {
			System.out.println("send is fail");
		}

		// 关闭
		channel.close();
		connection.close();
	}

}
