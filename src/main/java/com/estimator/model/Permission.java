package com.estimator.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "Permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionID;

    @Column(nullable = false, unique = true)
    private String permissionName;

    private String description;
}