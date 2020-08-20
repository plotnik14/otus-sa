package com.alexp.rabbitmq;

import com.alexp.adapter.MailAgentAdapter;
import com.alexp.adapter.UserManagementAdapter;
import com.alexp.model.User;
import com.alexp.rabbitmq.event.OrderStatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@EnableRabbit
@Component
public class RabbitMqListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMqListener.class);

    private final UserManagementAdapter userManagementAdapter;
    private final MailAgentAdapter mailAgentAdapter;

    public RabbitMqListener(UserManagementAdapter userManagementAdapter, MailAgentAdapter mailAgentAdapter) {
        this.userManagementAdapter = userManagementAdapter;
        this.mailAgentAdapter = mailAgentAdapter;
    }

    @RabbitListener(queues = "order.status.om.queue")
    public void receiveOrder(OrderStatusChangedEvent orderStatusChangedEvent) {
        LOGGER.debug("Message:{}", orderStatusChangedEvent);
        User user = userManagementAdapter.getUserById(orderStatusChangedEvent.getCustomerId());
        mailAgentAdapter.sendEmail(
                user.getEmail(),
                user.getFirstName()+" "+user.getLastName(),
                orderStatusChangedEvent.getOrderName()
        );
    }
}
