package com.zyf.help;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * RabbitMQ的帮助类
 * @author zhang
 *
 */
public class ConnectHelp {
	
	
	public static Connection getConnect() throws IOException, TimeoutException {
		
		
		//创建连接工厂
		ConnectionFactory  connect=new ConnectionFactory();
		
		//设置服务器连接地址
		connect.setHost("127.0.0.1");
		
		//服务器的端口
		connect.setPort(5672);
		
		//连接vhost
		connect.setVirtualHost("/testdb");
		
		//设置用户名
		connect.setUsername("zyf");
		
		//设置密码
		connect.setPassword("123");
		
		//返回当前的连接
		
		return connect.newConnection();
		
		
		
	}

}
