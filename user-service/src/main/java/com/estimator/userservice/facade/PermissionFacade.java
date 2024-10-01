package com.estimator.userservice.facade;

import com.estimator.dto.PermissionDTO;
import com.estimator.model.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionFacade {

    public PermissionDTO permissionToPermissionDTO(Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionID(permission.getPermissionID());
        permissionDTO.setPermissionName(permission.getPermissionName());
        permissionDTO.setDescription(permission.getDescription());
        return permissionDTO;
    }

    public Permission permissionDTOToPermission(PermissionDTO permissionDTO) {
        Permission permission = new Permission();
        permission.setPermissionID(permissionDTO.getPermissionID());
        permission.setPermissionName(permissionDTO.getPermissionName());
        permission.setDescription(permissionDTO.getDescription());
        return permission;
    }
}
