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
 *  工作队列模式
 * @author Administrator
 *
 */

public class Mail_Work {

	 /**
     *异步接收
     */
    static String QUEUE_NAME="WORK_QUEUE";
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
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
            //定义回调抓取消息  
            Consumer consumer = new DefaultConsumer(channel) {  
                @Override  
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,  
                        byte[] body) throws IOException {  
                     
                        System.out.println(new String(body,"UTF-8"));
                   
                }  
            };  
       //为channel指定消费者
        channel.basicConsume(QUEUE_NAME, true, consumer);  
	}
}
