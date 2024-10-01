package com.estimator.userservice.facade;

import com.estimator.dto.RolePermissionDTO;
import com.estimator.dto.RolePermissionKeyDTO;
import com.estimator.model.RolePermission;
import com.estimator.model.RolePermissionKey;
import org.springframework.stereotype.Component;

@Component
public class RolePermissionFacade {

    private final RoleFacade roleFacade;
    private final PermissionFacade permissionFacade;

    public RolePermissionFacade(RoleFacade roleFacade, PermissionFacade permissionFacade) {
        this.roleFacade = roleFacade;
        this.permissionFacade = permissionFacade;
    }

    public RolePermissionDTO rolePermissionToRolePermissionDTO(RolePermission rolePermission) {
        RolePermissionDTO rolePermissionDTO = new RolePermissionDTO();
        rolePermissionDTO.setId(rolePermissionKeyToRolePermissionKeyDTO(rolePermission.getId()));
        rolePermissionDTO.setRole(roleFacade.roleToRoleDTO(rolePermission.getRole()));
        rolePermissionDTO.setPermission(permissionFacade.permissionToPermissionDTO(rolePermission.getPermission()));
        return rolePermissionDTO;
    }

    public RolePermission rolePermissionDTOToRolePermission(RolePermissionDTO rolePermissionDTO) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setId(rolePermissionKeyDTOToRolePermissionKey(rolePermissionDTO.getId()));
        rolePermission.setRole(roleFacade.roleDTOToRole(rolePermissionDTO.getRole()));
        rolePermission.setPermission(permissionFacade.permissionDTOToPermission(rolePermissionDTO.getPermission()));
        return rolePermission;
    }

    private RolePermissionKeyDTO rolePermissionKeyToRolePermissionKeyDTO(RolePermissionKey key) {
        RolePermissionKeyDTO keyDTO = new RolePermissionKeyDTO();
        keyDTO.setRoleID(key.getRoleID());
        keyDTO.setPermissionID(key.getPermissionID());
        return keyDTO;
    }

    private RolePermissionKey rolePermissionKeyDTOToRolePermissionKey(RolePermissionKeyDTO keyDTO) {
        RolePermissionKey key = new RolePermissionKey();
        key.setRoleID(keyDTO.getRoleID());
        key.setPermissionID(keyDTO.getPermissionID());
        return key;
    }
}
