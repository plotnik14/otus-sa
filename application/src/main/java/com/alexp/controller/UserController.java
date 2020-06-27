package com.alexp.controller;

import com.alexp.model.User;
import com.alexp.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Timed
@RestController
@RequestMapping("/user")
public class UserController{

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("userId") Long userId){
        return userRepository.findById(userId).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable("userId") Long userId, @RequestBody User user){
        user.setId(userId);
        return userRepository.save(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("userId") Long userId){
        userRepository.deleteById(userId);
    }
}
