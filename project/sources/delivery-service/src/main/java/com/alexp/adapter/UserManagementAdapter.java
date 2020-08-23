package com.alexp.adapter;

import com.alexp.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class UserManagementAdapter {
    private static final String USER_MANAGEMENT_URL = "user-management:9000";

    private final RestTemplate restTemplate;

    public UserManagementAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUserById(UUID userId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-UserId", "delivery-service");
        httpHeaders.add("X-Role", "Application");
        HttpEntity request = new HttpEntity(httpHeaders);
        User user = restTemplate.exchange(
                "http://"+ USER_MANAGEMENT_URL +"/api/v1/users/{userId}",
                HttpMethod.GET,
                request,
                User.class,
                userId
        ).getBody();

        return user;
    }
}
