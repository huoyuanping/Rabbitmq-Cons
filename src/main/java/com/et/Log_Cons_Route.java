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
 *  ·��ģʽ ���� direct
 * @author Administrator
 *
 */

public class Log_Cons_Route {

	
	
	/**
	 *������������ �������кܶ�����
	 */
	private static String EXCHANGE_NAME="amp_log_route";
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
            channel.exchangeDeclare(EXCHANGE_NAME, "direct",true);  
        	//channel.basicQos(1);
      		//����һ������Ķ��� �ö������ڴӽ�������ȡ��Ϣ
      		String queueName = channel.queueDeclare().getQueue();
      		//�����к�ĳ������������ �Ϳ�����ʽ��ȡ��Ϣ�� routingkey�ͽ�������һ�������óɿ�
      	    channel.queueBind(queueName, EXCHANGE_NAME, "error");
      	    
      		//����ص�ץȡ��Ϣ
      		Consumer consumer = new DefaultConsumer(channel) {
      			@Override
      			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
      					byte[] body) throws IOException {
      				String message = new String(body, "UTF-8");
      				System.out.println(message);
      				//����2 true��ʾȷ�ϸö���������Ϣ  falseֻȷ�ϵ�ǰ��Ϣ ÿ����Ϣ����һ����Ϣ���
      			}
      		};
      		//����2 ��ʾ�ֶ�ȷ��
      		channel.basicConsume(queueName, true, consumer);
           
	}
}
