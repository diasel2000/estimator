package com.estimator.repository;

import com.estimator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByGoogleID(String googleID);

    User findByEmail(String email);

    User findByUsername(String username);

    void deleteByEmail(String email);
}