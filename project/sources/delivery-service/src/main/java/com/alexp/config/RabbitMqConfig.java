package com.alexp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Queue orderStatusQueue() {
        return new Queue("order.status.ds.queue", false);
    }

    @Bean
    public TopicExchange orderStatusExchange() {
        return new TopicExchange("order.status.exchange");
    }

    @Bean
    public Binding bindingOrderStatus(Queue orderStatusQueue, TopicExchange orderStatusExchange) {
        return BindingBuilder.bind(orderStatusQueue).to(orderStatusExchange).with("order.status.*");
    }
}
