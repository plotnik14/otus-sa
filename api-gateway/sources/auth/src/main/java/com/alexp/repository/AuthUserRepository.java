package com.alexp.repository;

import com.alexp.model.AuthUser;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AuthUserRepository extends CrudRepository<AuthUser, UUID> {
    AuthUser findByLoginAndPassword(String login, String password);
    AuthUser findByLogin(String login);
}
