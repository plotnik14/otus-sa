package com.alexp.repository;

import com.alexp.model.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SessionRepository extends CrudRepository<Session, UUID> {
}
