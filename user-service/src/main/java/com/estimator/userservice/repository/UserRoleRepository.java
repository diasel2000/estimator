package com.estimator.userservice.repository;

import com.estimator.userservice.model.User;
import com.estimator.userservice.model.UserRole;
import com.estimator.userservice.model.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
    List<UserRole> findByUser(User user);
}
