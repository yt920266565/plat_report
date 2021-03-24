package com.yanmade.plat.publish.util;

import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;


/*@Component*/
public class MQReceive {

	/*
	 * @RabbitListener( bindings = @QueueBinding( exchange
	 * = @Exchange(RabbitConfig.TOP_EXCHAGE1), key = "top1.*", value
	 * = @Queue(RabbitConfig.TOP_QUEUE1) ) ) public void receive(int i,Channel
	 * channel,Message message) { try { System.out.println("接收到的消息是：" + i);
	 * channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); }
	 * catch (IOException e) { System.out.println("回复ack异常了"); } }
	 * 
	 * @RabbitListener( bindings = @QueueBinding( exchange
	 * = @Exchange(RabbitConfig.TOP_EXCHAGE2), key = "top2.*", value
	 * = @Queue(RabbitConfig.TOP_QUEUE2) ) ) public void receive2(String msgString)
	 * { System.out.println("接收到的消息是：" + msgString); }
	 */

}
