package com.estimator.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class RolePermissionKey implements Serializable {
    @Column(name = "roleID")
    private Long roleID;

    @Column(name = "permissionID")
    private Long permissionID;
}
