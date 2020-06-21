package com.alexp.controller;

import com.alexp.model.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class TestController {

    @GetMapping("/health")
    public Response healthCheck() {
        return new Response("OK");
    }

    @GetMapping("/")
    public String hello() {
        String hostName;
        try {
            InetAddress myHost = InetAddress.getLocalHost();
            hostName = myHost.getHostName();
        } catch (UnknownHostException e) {
            hostName = "UnknownHost";
        }
        return "Hello from " + hostName;
    }
}
