package com.estimator.controller;

import com.estimator.facade.UserFacade;
import com.estimator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private final UserFacade userFacade;

    @Autowired
    public AdminUserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userFacade.getAllUsers();
        logger.info("Managed users - Total users: {}", users.size());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        if (userFacade.existsById(id)) {
            userFacade.deleteUserById(id);
            response.put("message", "User deleted successfully");
            logger.info("Deleted user with ID: {}", id);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User not found");
            logger.warn("Attempted to delete user with ID: {} - User not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}