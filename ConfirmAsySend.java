package com.zyf.transtion.confirm.asy;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import javax.sql.ConnectionPoolDataSource;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.zyf.help.ConnectHelp;

/**
 * RabbitMQ 之confirm异步
 * 
 * Channel 对象提供的ConfirmListener（）回调方法只包含 deliveryTag（当前Chanel发出的消息序号），我们需要自己
 * 为每一个Channel维护一个unconfirm的消息序号集合，每publish
 * 一条数据，集合中元素加1，每回调一次handleAck方法，unconfirm集合
 * 删除相应的一条（multiple=false）或多条（multiple=true）记录，从 程序运行效率上看，这个unconfirm集合最好采用有序集合
 * SortedSet存储结构
 * 
 * @author zhang
 *
 */
public class ConfirmAsySend {

	public static final String QUEUE_NAME = "asy_test_confirm";

	public static void main(String[] args) throws IOException, TimeoutException {

		// 获取连接
		Connection connection = ConnectHelp.getConnect();

		// 建立通道
		Channel channel = connection.createChannel();

		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		// 开启事务
		channel.confirmSelect();

		// 创建set
		final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

		// 监听事务
		channel.addConfirmListener(new ConfirmListener() {

			// handleNack
			public void handleNack(long arg0, boolean arg1) throws IOException {
				// TODO Auto-generated method stub

				if (arg1) {
					System.out.println("handleNack : " + arg1);
					confirmSet.headSet(arg0 + 1).clear();
				} else {
					System.out.println("handleNack : " + arg1);
					confirmSet.remove(arg0);
				}
			}

			// 没有问题
			public void handleAck(long arg0, boolean arg1) throws IOException {
				// TODO Auto-generated method stub

				if (arg1) {
					System.out.println("handleAck :  " + arg1);
					confirmSet.headSet(arg0 + 1).clear();
				} else {
					System.out.println("handleAck :  " + arg1);
					confirmSet.remove(arg0);
				}

			}
		});

		// 发送的数据
		String msg = "msg data";

		while (true) {
			long seqNo = channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
			confirmSet.add(seqNo);
		}

	}

}
