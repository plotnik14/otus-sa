package com.alexp.controller;

import com.alexp.model.PaymentMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/")
public class PaymentController {

    @GetMapping("paymentMethods")
    public ResponseEntity<List<PaymentMethod>> getPaymentMethods(){
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod("Credit Card", "Payment by Credit Card"));
        paymentMethods.add(new PaymentMethod("Cash", "Payment by Cash"));
        return ResponseEntity.ok(paymentMethods);
    }
}
