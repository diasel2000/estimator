package com.estimator.repository;


import com.estimator.model.Permission;
import com.estimator.model.Role;
import com.estimator.model.User;
import com.estimator.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByGoogleID(String googleID);

    User findByEmail(String email);
}