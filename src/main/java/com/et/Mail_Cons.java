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
 *  ��ģʽ
 * @author Administrator
 *
 */
@RestController
public class Mail_Cons {
	@Autowired
	private  JavaMailSender jms;
	/**
	 * ���л�
	 * ���������л�תΪ�ֽ�����
	 */
	public static byte[] ser(Object obj)throws Exception{
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(bos);
		oos.writeObject(obj);
		return bos.toByteArray();
	}
	/**
	 * �����л�
	 * ���ֽ����鷴���л�תΪ�ֽ�����
	 */
	public static Object dser(byte[] src)throws Exception{
		ByteArrayInputStream bis=new ByteArrayInputStream(src);
		ObjectInputStream ois=new ObjectInputStream(bis);
		return ois.readObject();
	}
	/**
	 *�첽����
	 */
	static String QUEUE_NAME="MAIL_QUEUE";
	@GetMapping("/send")
	public String send() throws Exception{
		
		//����һ��ConnectionFactory���ӹ���
		 ConnectionFactory factory = new ConnectionFactory();
		 //ͨ��ConnectionFactory����Rabbitmq����ip����Ϣ
	        factory.setHost("192.168.73.130");
	        //ͨ��ConnectionFactory����һ������connection
	        Connection connection = factory.newConnection();
	        //ͨ��connection����һ��Ƶ��channel
	        Channel channel = connection.createChannel();  
	        //������Ҳ��Ҫ������� �п�����������������������   
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
	        //����ص�ץȡ��Ϣ  
	        Consumer consumer = new DefaultConsumer(channel) {  
	        	
	            @Override  
	            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,  
	                    byte[] body) throws IOException {  
	            	
		               try {
						Map map=(Map)Mail_Cons.dser(body);
						System.out.println(map.get("content"));
						//��ʵ�����ʼ�
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
	        //Ϊchannelָ��������
	        channel.basicConsume(QUEUE_NAME, true, consumer); 
	        return "1";
	}
}
