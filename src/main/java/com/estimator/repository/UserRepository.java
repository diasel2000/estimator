package com.estimator.repository;

import com.estimator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByGoogleID(String googleID);

    User findByEmail(String email);

    User findByUsername(String username);

    void deleteByEmail(String email);
}