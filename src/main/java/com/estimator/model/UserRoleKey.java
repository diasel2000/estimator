package com.estimator.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class UserRoleKey implements Serializable {
    @Column(name = "userID")
    private Long userID;

    @Column(name = "roleID")
    private Long roleID;
}