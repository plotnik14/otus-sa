package com.alexp.controller;

import com.alexp.model.PaymentMethod;
import com.alexp.rabbitmq.RabbitMessagingService;
import com.alexp.rabbitmq.event.PaymentEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class PaymentController {

    private final RabbitMessagingService rabbitMessagingService;

    public PaymentController(RabbitMessagingService rabbitMessagingService) {
        this.rabbitMessagingService = rabbitMessagingService;
    }

    @GetMapping("paymentMethods")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethods(){
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod("Credit Card", "Payment by Credit Card"));
        paymentMethods.add(new PaymentMethod("Cash", "Payment by Cash"));
        return ResponseEntity.ok(paymentMethods);
    }

    @GetMapping("payment/emulate/{orderId}")
    public ResponseEntity<PaymentEvent> emulatePayment(@PathVariable("orderId") UUID orderId) {
        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(orderId);
        rabbitMessagingService.sendPaymentEvent(paymentEvent);
        return ResponseEntity.ok(paymentEvent);
    }
}
