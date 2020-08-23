package com.alexp.controller;

import com.alexp.model.User;
import com.alexp.repositoriy.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserManagementController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserManagementController.class);

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public UserManagementController(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
        initDB();
    }

    @GetMapping
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
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

    @PutMapping("/{userId}")
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

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getUserId() == null){
            user.setUserId(UUID.randomUUID()); //hack for testing
        }

        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private boolean authorizedUser(HttpServletRequest httpServletRequest, String userId) {
        String userIdFromHeader = httpServletRequest.getHeader("X-UserId");
        String role = httpServletRequest.getHeader("X-Role");

        LOGGER.debug("userIdFromHeader:{}", userIdFromHeader);
        LOGGER.debug("role:{}", role);

        return userId.equals(userIdFromHeader) || role.equals("Admin") || role.equals("Application");
    }

    // Admin + Courier
    public void initDB() {
        User user = new User();
        user.setUserId(UUID.fromString("036d9622-e3e3-11ea-87d0-0242ac130003"));
        user.setLogin("admin");
        user.setPassword(securePassword("admin"));
        user.setEmail("admin@admin.com");
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setRole("Admin");

        User existingUser = userRepository.findById(user.getUserId()).orElse(null);
        if (existingUser == null) {
            User createdUser = userRepository.save(user);
        }

        user = new User();
        user.setUserId(UUID.fromString("111e226e-e3e3-11ea-87d0-0242ac130003"));
        user.setLogin("courier");
        user.setPassword(securePassword("courier"));
        user.setEmail("courier@courier.com");
        user.setFirstName("Courier");
        user.setLastName("Courier");
        user.setRole("Courier");

        existingUser = userRepository.findById(user.getUserId()).orElse(null);
        if (existingUser == null) {
            User createdUser = userRepository.save(user);
        }
    }

    // TODo change to init script
    private String securePassword(String password) {
        // ToDo Something better in the next release
        password = password + "salt";
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
