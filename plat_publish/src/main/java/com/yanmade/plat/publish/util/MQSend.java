package com.yanmade.plat.publish.util;

import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*@Component*/
public class MQSend /* implements RabbitTemplate.ConfirmCallback, ReturnCallback */{

	/*
	 * private RabbitTemplate rabbitTemplate;
	 * 
	 * @Autowired public MQSend(RabbitTemplate rabbitTemplate) { this.rabbitTemplate
	 * = rabbitTemplate; rabbitTemplate.setConfirmCallback(this);
	 * rabbitTemplate.setReturnCallback(this); }
	 * 
	 * public void sendTest() {
	 * 
	 * for (int i = 0; i < 5; i++) { CorrelationData correlationData = new
	 * CorrelationData(UUID.randomUUID().toString()); System.out.println("发送第" + i +
	 * "条"); rabbitTemplate.convertSendAndReceive(RabbitConfig.TOP_EXCHAGE1,
	 * "top1.tt", i, correlationData); //System.out.println("消费者响应 : " + response +
	 * " 消息处理完成"); try { Thread.sleep(1000); } catch (Exception e) { } }
	 * 
	 * }
	 * 
	 * public void sendTest2() {
	 * 
	 * for (int i = 0; i < 5; i++) { String msgString = "i am " + i; CorrelationData
	 * correlationData = new CorrelationData(UUID.randomUUID().toString());
	 * System.out.println("发送第" + i + "条");
	 * rabbitTemplate.convertAndSend(RabbitConfig.TOP_EXCHAGE2, "top2.tt",
	 * msgString, correlationData); try { Thread.sleep(1000); } catch (Exception e)
	 * { } }
	 * 
	 * }
	 * 
	 * @Override public void confirm(CorrelationData correlationData, boolean ack,
	 * String cause) { if(ack) { System.out.println("correlationData=" +
	 * correlationData.getId() + " ack=" + ack + " cause=" + cause); }else {
	 * System.out.println("消息没有送达对应的队列消费者"); System.out.println("correlationData=" +
	 * correlationData.getId() + " ack=" + ack + " cause=" + cause); } }
	 * 
	 * @Override public void returnedMessage(Message message, int replyCode, String
	 * replyText, String exchange, String routingKey) {
	 * System.out.println("消息主体message: " + message);
	 * System.out.println("消息主体message: " + replyCode); System.out.println("描述: " +
	 * replyText); System.out.println("消息使用的交换器exchange: " + exchange);
	 * System.out.println("消息使用的路由键routing: " + routingKey);
	 * 
	 * }
	 */

}
