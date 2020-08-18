package com.alexp.controller;

import org.springframework.web.bind.annotation.*;
import com.alexp.model.Response;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Response healthCheck() {
        return new Response("OK");
    }
}
