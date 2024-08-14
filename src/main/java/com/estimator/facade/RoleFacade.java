package com.estimator.facade;
import com.estimator.dto.RoleDTO;
import com.estimator.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleFacade {

    public RoleDTO roleToRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(role.getRoleID());
        roleDTO.setRoleName(role.getRoleName());
        roleDTO.setDescription(role.getDescription());
        return roleDTO;
    }

    public Role roleDTOToRole(RoleDTO roleDTO) {
        Role role = new Role();
        role.setRoleID(roleDTO.getRoleID());
        role.setRoleName(roleDTO.getRoleName());
        role.setDescription(roleDTO.getDescription());
        return role;
    }
}

