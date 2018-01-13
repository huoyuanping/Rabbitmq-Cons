package com.et;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


/**
 *  路由模式 类型 direct
 * @author Administrator
 *
 */

public class Log_Cons_Route {

	
	
	/**
	 *交换器的名字 交换器有很多类型
	 */
	private static String EXCHANGE_NAME="amp_log_route";
    public static void main(String[] args) throws Exception, TimeoutException {
        //创建一个ConnectionFactory连接工厂
         ConnectionFactory factory = new ConnectionFactory();
         //通过ConnectionFactory设置Rabbitmq所在ip等信息
            factory.setHost("192.168.73.130");
            //通过ConnectionFactory创建一个连接connection
            Connection connection = factory.newConnection();
            //通过connection创建一个频道channel
             Channel channel = connection.createChannel();  
            //消费者也需要定义队列 有可能消费者先于生产者启动   
            channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);  
        	//channel.basicQos(1);
      		//产生一个随机的队列 该队列用于从交换器获取消息
      		String queueName = channel.queueDeclare().getQueue();
      		//将队列和某个交换机丙丁 就可以正式获取消息了 routingkey和交换器的一样都设置成空
      	    channel.queueBind(queueName, EXCHANGE_NAME, "error");
      	    
      		//定义回调抓取消息
      		Consumer consumer = new DefaultConsumer(channel) {
      			@Override
      			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
      					byte[] body) throws IOException {
      				String message = new String(body, "UTF-8");
      				System.out.println(message);
      				//参数2 true表示确认该队列所有消息  false只确认当前消息 每个消息都有一个消息标记
      			}
      		};
      		//参数2 表示手动确认
      		channel.basicConsume(queueName, true, consumer);
           
	}
}
