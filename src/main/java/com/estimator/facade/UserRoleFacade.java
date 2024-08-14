package com.estimator.facade;

import com.estimator.dto.UserRoleDTO;
import com.estimator.dto.UserRoleKeyDTO;
import com.estimator.model.UserRole;
import com.estimator.model.UserRoleKey;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class UserRoleFacade {

    private final RoleFacade roleFacade;
    private final UserFacade userFacade;

    public UserRoleFacade(RoleFacade roleFacade, @Lazy UserFacade userFacade) {
        this.roleFacade = roleFacade;
        this.userFacade = userFacade;
    }

    public UserRoleDTO userRoleToUserRoleDTO(UserRole userRole) {
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setId(userRoleKeyToUserRoleKeyDTO(userRole.getId()));
        userRoleDTO.setUser(userFacade.userToUserDTO(userRole.getUser()));
        userRoleDTO.setRole(roleFacade.roleToRoleDTO(userRole.getRole()));
        return userRoleDTO;
    }

    public UserRole userRoleDTOToUserRole(UserRoleDTO userRoleDTO) {
        UserRole userRole = new UserRole();
        userRole.setId(userRoleKeyDTOToUserRoleKey(userRoleDTO.getId()));
        userRole.setUser(userFacade.userDTOToUser(userRoleDTO.getUser()));
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
