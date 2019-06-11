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
 *         生产者将信通设置成 confirm模式，一旦信通进入confirm模式 所有在该信通上面发布的消息都会被指派一个唯一的ID，
 *         一旦消息被投递到所有匹配的队列之后， broker就会发送一个确认给生产者，这就使得生产者知道消息已经
 *         正确到达目的队列了，如果消息和队列是可持久化的，那么确认消息会将消息写入
 *         次哦按之后发出，broker回传给生产者的确认消息中deliver-tag域包含了确认消息的序列号
 *         此外broker也可以设置basic.ack的multiple域，表示到这个序列号之前的所有消息都已经得到了处理
 * 
 * 
 *         编程模式： 1.普通 发一条 waitForConfirms 2.批量的 发一批 waitForCOnfirms
 * 
 * 
 * 
 * 
 *         3.异步 confirm模式：提供一个回调
 */
public class ConfirmSend {
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
		// 数据
		String msg = "this is data";
		// 发送数据
		channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
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
