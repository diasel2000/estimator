package com.estimator.userservice.controller;

import com.estimator.userservice.dto.UserDTO;
import com.estimator.userservice.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@CrossOrigin(origins = "${cors.allowed.origins}")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    private static final String ATTEMPTED_TO_DELETE_USER_WITH_ID_USER_NOT_FOUND = "Attempted to delete user with ID: {} - User not found";
    private static final String MANAGED_USERS_TOTAL_USERS = "Managed users - Total users: {}";
    private static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private static final String DELETED_USER_WITH_ID = "Deleted user with ID: {}";
    private static final String USER_NOT_FOUND = "User not found";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";

    private final UserFacade userFacade;

    @Autowired
    public AdminUserController(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDtos = userFacade.getAllUsers().stream()
                .map(userFacade::userToUserDTO)
                .collect(Collectors.toList());

        logger.info(MANAGED_USERS_TOTAL_USERS, userDtos.size());
        return ResponseEntity.ok(userDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        if (!userFacade.existsById(id)) {
            return handleUserNotFound(id);
        }

        userFacade.deleteUserById(id);
        return handleUserDeletionSuccess(id);
    }

    private ResponseEntity<Map<String, String>> handleUserNotFound(Long id) {
        logger.warn(ATTEMPTED_TO_DELETE_USER_WITH_ID_USER_NOT_FOUND, id);
        Map<String, String> response = Collections.singletonMap(ERROR, USER_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    private ResponseEntity<Map<String, String>> handleUserDeletionSuccess(Long id) {
        logger.info(DELETED_USER_WITH_ID, id);
        Map<String, String> response = Collections.singletonMap(MESSAGE, USER_DELETED_SUCCESSFULLY);
        return ResponseEntity.ok(response);
    }
}