package com.alexp.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailAgentAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailAgentAdapter.class);

    public void sendEmail(String email, String fullName, String orderName) {
        LOGGER.info(
                "Dear {}, your {} has been payed and ready for delivery. Email:{}",
                fullName,
                orderName,
                email
        );
    }
}
