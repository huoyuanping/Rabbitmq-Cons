package com.et;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


/**
 *  简单模式
 * @author Administrator
 *
 */
@RestController
public class Mail_Cons {
	@Autowired
	private  JavaMailSender jms;
	/**
	 * 序列化
	 * 将对象序列化转为字节数组
	 */
	public static byte[] ser(Object obj)throws Exception{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(bos);
		oos.writeObject(obj);
		return bos.toByteArray();
	}
	/**
	 * 反序列化
	 * 将字节数组反序列化转为字节数组
	 */
	public static Object dser(byte[] src)throws Exception{
		ByteArrayInputStream bis=new ByteArrayInputStream(src);
		ObjectInputStream ois=new ObjectInputStream(bis);
		return ois.readObject();
	}
	/**
	 *异步接收
	 */
	static String QUEUE_NAME="MAIL_QUEUE";
	@GetMapping("/send")
	public String send() throws Exception{
		
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
	            	
		               try {
						Map map=(Map)Mail_Cons.dser(body);
						System.out.println(map.get("content"));
						//真实发送邮件
						SimpleMailMessage mailMessage  =   new  SimpleMailMessage();
						mailMessage.setFrom("hyp2549882772@126.com");
						mailMessage.setTo(map.get("sendTo").toString());
						mailMessage.setSubject(map.get("subject").toString());
						mailMessage.setText(map.get("content").toString());
						jms.send(mailMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }  
	        };  
	        //为channel指定消费者
	        channel.basicConsume(QUEUE_NAME, true, consumer); 
	        return "1";
	}
}
