package com.alexp.adapter;

import com.alexp.model.User;
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
        User user = restTemplate.getForObject(
                "http://"+ USER_MANAGEMENT_URL +"/api/v1/users/"+userId,
                User.class
        );
        return user;
    }
}
