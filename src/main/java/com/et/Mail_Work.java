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
 *  ��������ģʽ
 * @author Administrator
 *
 */

public class Mail_Work {

	 /**
     *�첽����
     */
    static String QUEUE_NAME="WORK_QUEUE";
    public static void main(String[] args) throws Exception, TimeoutException {
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
                     
                        System.out.println(new String(body,"UTF-8"));
                   
                }  
            };  
       //Ϊchannelָ��������
        channel.basicConsume(QUEUE_NAME, true, consumer);  
	}
}
