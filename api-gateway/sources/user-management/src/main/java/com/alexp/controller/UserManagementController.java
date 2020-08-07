package com.alexp.controller;

import com.alexp.model.Response;
import com.alexp.model.User;
import com.alexp.repositoriy.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class UserManagementController {

    private Logger logger = LoggerFactory.getLogger(UserManagementController.class);

    private RestTemplate restTemplate;
    private UserRepository userRepository;

    public UserManagementController(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") String userId, HttpServletRequest httpServletRequest) {
        User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!authorizedUser(httpServletRequest, user.getUserId().toString())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") String userId, @RequestBody User user, HttpServletRequest httpServletRequest){
        User userForUpdate = userRepository.findById(UUID.fromString(userId)).orElse(null);
        if (userForUpdate == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!authorizedUser(httpServletRequest, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userForUpdate.setFirstName(user.getFirstName());
        userForUpdate.setLastName(user.getLastName());

        userRepository.save(userForUpdate);

        return new ResponseEntity<>(userForUpdate, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/health")
    public Response healthCheck() {
        return new Response("OK");
    }

    private boolean authorizedUser(HttpServletRequest httpServletRequest, String userId) {
        if (httpServletRequest.getCookies() == null){
            logger.debug("cookies are null");
            return false;
        }

        List<Cookie> cookies = new ArrayList<Cookie>(Arrays.asList(httpServletRequest.getCookies()));
        Cookie sessionIdCookie = cookies.stream()
                .filter(cookie -> "sessionId".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (sessionIdCookie == null){
            logger.debug("sessionId hasn't been found in cookies:{}", cookies);
            return false;
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.COOKIE, "sessionId="+sessionIdCookie.getValue());

        logger.debug("before rest to auth");

        // ToDo move url to ENV
        ResponseEntity<Object> responseEntity = restTemplate.exchange("http://auth:9000/auth", HttpMethod.GET, new HttpEntity<>(httpHeaders), Object.class);

        logger.debug("after rest to auth");

        String userIdHeader = responseEntity.getHeaders().get("X-UserId").get(0);

        return userIdHeader.equals(userId);
    }


}
