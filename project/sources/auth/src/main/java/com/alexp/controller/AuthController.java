package com.alexp.controller;

import com.alexp.model.AuthUser;
import com.alexp.model.Request;
import com.alexp.model.Response;
import com.alexp.model.Session;
import com.alexp.repository.AuthUserRepository;
import com.alexp.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.*;

@RestController
public class AuthController {
    private final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final RestTemplate restTemplate;
    private final AuthUserRepository authUserRepository;
    private final SessionRepository sessionRepository;

    public AuthController(RestTemplate restTemplate, AuthUserRepository authUserRepository, SessionRepository sessionRepository) {
        this.restTemplate = restTemplate;
        this.authUserRepository = authUserRepository;
        this.sessionRepository = sessionRepository;
        initUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody Request request) {
        AuthUser user = new AuthUser();
        user.setLogin(request.getLogin());
        user.setPassword(securePassword(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole("User");

        AuthUser existingUser = authUserRepository.findByLogin(user.getLogin());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AuthUser createdUser = authUserRepository.save(user);

        createUserInUM(createdUser);

        Response response = new Response();
        response.setId(createdUser.getUserId().toString());

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    private void createUserInUM(AuthUser user){
        // ToDo move url to ENV
        restTemplate.postForEntity("http://user-management:9000/api/v1/users", user, Object.class);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Request request, HttpServletResponse httpServletResponse) {
        if (request.getLogin() == null || request.getPassword() == null) {
            throw new NullPointerException("Bla bla");
        }

        AuthUser user = authUserRepository.findByLoginAndPassword(
                request.getLogin(),
                securePassword(request.getPassword())
        );

        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String sessionId = createSession(user);

        Cookie cookie = new Cookie("sessionId", sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        Response response = new Response();
        response.setId(user.getUserId().toString());

        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    @GetMapping("/auth")
    public ResponseEntity<Response> auth(HttpServletRequest httpServletRequest) {
        if (httpServletRequest.getCookies() == null) {
            LOGGER.debug("cookies are null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Cookie> cookies = new ArrayList<Cookie>(Arrays.asList(httpServletRequest.getCookies()));
        Cookie sessionIdCookie = cookies.stream()
                .filter(cookie -> "sessionId".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (sessionIdCookie == null){
            LOGGER.debug("sessionId hasn't been found in cookies:{}", cookies);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String sessionId = sessionIdCookie.getValue();
        Session session = sessionRepository.findById(UUID.fromString(sessionId)).orElse(null);

        if (session == null) {
            LOGGER.debug("Session with this id:{} doesn't exist", sessionId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // ToDo if session expired -> 401

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-UserId", session.getUserId().toString());
        httpHeaders.add("X-Login", session.getLogin());
        httpHeaders.add("X-Email", session.getEmail());
        httpHeaders.add("X-First-Name", session.getFirstName());
        httpHeaders.add("X-Last-Name", session.getLastName());
        httpHeaders.add("X-Role", session.getRole());

        Response response = new Response();
        response.setId(session.getUserId().toString());

        LOGGER.debug("Headers added");

        return new ResponseEntity<Response>(response, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if (httpServletRequest.getCookies() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Cookie> cookies = new ArrayList<Cookie>(Arrays.asList(httpServletRequest.getCookies()));
        Cookie sessionIdCookie = cookies.stream()
                .filter(cookie -> "sessionId".equals(cookie.getName()))
                .findFirst()
                .orElse(null);

        if (sessionIdCookie == null){
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        String sessionId = sessionIdCookie.getValue();

        sessionRepository.deleteById(UUID.fromString(sessionId));

        Cookie cookie = new Cookie("sessionId", "");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);

        return new ResponseEntity<Response>(new Response("ok"), HttpStatus.OK);
    }

    @GetMapping("/sessions")
    public Iterable<Session> sessions() {
        return sessionRepository.findAll();
    }

    @GetMapping("/users/authTest")
    public Iterable<AuthUser> users() {
        return authUserRepository.findAll();
    }
    
    @GetMapping("/health")
    public Response healthCheck() {
        return new Response("OK");
    }

    private String securePassword(String password) {
        // ToDo Something better in the next release
        password = password + "salt";
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    private String createSession(AuthUser user){
        Session session = new Session();
        session.setUserId(user.getUserId());
        session.setLogin(user.getLogin());
        session.setPassword(user.getPassword());
        session.setEmail(user.getEmail());
        session.setFirstName(user.getFirstName());
        session.setLastName(user.getLastName());
        session.setRole(user.getRole());
        session.setCreatedWhen(new Timestamp(System.currentTimeMillis()));
        Session createdSession = sessionRepository.save(session);
        return createdSession.getSessionId().toString();
    }

    public void initUsers() {
        AuthUser user = new AuthUser();
        user.setUserId(UUID.fromString("036d9622-e3e3-11ea-87d0-0242ac130003"));
        user.setLogin("admin");
        user.setPassword(securePassword("admin"));
        user.setEmail("admin@admin.com");
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setRole("Admin");

        AuthUser existingUser = authUserRepository.findByLogin(user.getLogin());
        if (existingUser == null) {
            AuthUser createdUser = authUserRepository.save(user);
            createUserInUM(createdUser);
        }

        user = new AuthUser();
        user.setUserId(UUID.fromString("111e226e-e3e3-11ea-87d0-0242ac130003"));
        user.setLogin("courier");
        user.setPassword(securePassword("courier"));
        user.setEmail("courier@courier.com");
        user.setFirstName("Courier");
        user.setLastName("Courier");
        user.setRole("Courier");

        existingUser = authUserRepository.findByLogin(user.getLogin());
        if (existingUser == null) {
            AuthUser createdUser = authUserRepository.save(user);
            createUserInUM(createdUser);
        }
    }
}
