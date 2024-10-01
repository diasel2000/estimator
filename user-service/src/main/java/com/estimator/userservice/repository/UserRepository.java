package com.estimator.userservice.repository;

import com.estimator.userservice.model.Role;
import com.estimator.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByGoogleID(String googleID);

    User findByEmail(String email);

    User findByUsername(String username);

    void deleteByEmail(String email);

    @Query("SELECT ur.role FROM User u JOIN u.userRoles ur WHERE u.userID = :userID")
    List<Role> findRolesByUserID(@Param("userID") Long userID);
}