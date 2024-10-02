package com.estimator.userservice.facade;

import com.estimator.userservice.dto.UserRoleDTO;
import com.estimator.userservice.dto.UserRoleKeyDTO;
import com.estimator.userservice.model.UserRole;
import com.estimator.userservice.model.UserRoleKey;
import org.springframework.stereotype.Component;

@Component
public class UserRoleFacade {

    private final RoleFacade roleFacade;

    public UserRoleFacade(RoleFacade roleFacade) {
        this.roleFacade = roleFacade;
    }

    public UserRoleDTO userRoleToUserRoleDTO(UserRole userRole) {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setId(userRoleKeyToUserRoleKeyDTO(userRole.getId()));
        userRoleDTO.setRole(roleFacade.roleToRoleDTO(userRole.getRole()));
        return userRoleDTO;
    }

    public UserRole userRoleDTOToUserRole(UserRoleDTO userRoleDTO) {
        UserRole userRole = new UserRole();
        userRole.setId(userRoleKeyDTOToUserRoleKey(userRoleDTO.getId()));
        userRole.setRole(roleFacade.roleDTOToRole(userRoleDTO.getRole()));
        return userRole;
    }

    private UserRoleKeyDTO userRoleKeyToUserRoleKeyDTO(UserRoleKey key) {
        UserRoleKeyDTO keyDTO = new UserRoleKeyDTO();
        keyDTO.setUserID(key.getUserID());
        keyDTO.setRoleID(key.getRoleID());
        return keyDTO;
    }

    private UserRoleKey userRoleKeyDTOToUserRoleKey(UserRoleKeyDTO keyDTO) {
        UserRoleKey key = new UserRoleKey();
        key.setUserID(keyDTO.getUserID());
        key.setRoleID(keyDTO.getRoleID());
        return key;
    }
}
