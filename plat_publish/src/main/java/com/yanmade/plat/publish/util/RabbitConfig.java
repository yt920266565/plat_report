package com.yanmade.plat.publish.util;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*@Configuration*/
public class RabbitConfig {

	/*
	 * public static final String TOP_QUEUE1 = "top.queque1"; public static final
	 * String TOP_EXCHAGE1 = "top.exchange1";
	 * 
	 * public static final String TOP_QUEUE2 = "top.queque2"; public static final
	 * String TOP_EXCHAGE2 = "top.exchange2";
	 * 
	 * public static final String TOP_QUEUE3 = "top.queque3"; public static final
	 * String TOP_EXCHAGE3 = "top.exchange3";
	 * 
	 * public static final String TOP_EXCHAGE4 = "top.exchange4";
	 * 
	 * public static final String QUEUE_NAME = "first.queque"; public static final
	 * String DIR_EXCHANGE = "dirExchange";
	 * 
	 * @Bean public Queue firstQueue() { return new Queue(QUEUE_NAME, true); }
	 * 
	 * @Bean public DirectExchange dirExchange() { return new
	 * DirectExchange(DIR_EXCHANGE); }
	 * 
	 * @Bean Queue topQueue1() { return new Queue(TOP_QUEUE1); }
	 * 
	 * @Bean Queue topQueue2() { return new Queue(TOP_QUEUE2); }
	 * 
	 * @Bean Queue topQueue3() { return new Queue(TOP_QUEUE3); }
	 * 
	 * @Bean public TopicExchange topicExchange1() { return new
	 * TopicExchange(TOP_EXCHAGE1); }
	 * 
	 * @Bean public TopicExchange topicExchange2() { return new
	 * TopicExchange(TOP_EXCHAGE2); }
	 */

	/*
	 * @Bean public Binding topBind1() { return
	 * BindingBuilder.bind(topQueue1()).to(topicExchange1()).with("top.#"); }
	 * 
	 * @Bean public Binding topBind2() { return
	 * BindingBuilder.bind(topQueue2()).to(topicExchange1()).with("top.test"); }
	 */

}
