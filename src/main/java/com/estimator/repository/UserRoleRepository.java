package com.estimator.repository;

import com.estimator.model.User;
import com.estimator.model.UserRole;
import com.estimator.model.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {
    List<UserRole> findByUser(User user);
}
