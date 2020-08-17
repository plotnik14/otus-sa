package com.alexp.controller;

import com.alexp.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Response healthCheck() {
        return new Response("OK");
    }
}
