package com.estimator.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

// Model for Permissions
@Data
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionID;

    @NotNull
    @Column(nullable = false, unique = true)
    private String permissionName;

    private String description;
}