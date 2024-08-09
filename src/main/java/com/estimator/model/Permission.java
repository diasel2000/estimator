package com.estimator.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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