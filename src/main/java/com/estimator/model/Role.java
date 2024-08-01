package com.estimator.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleID;

    @Column(nullable = false, unique = true)
    private String roleName;

    private String description;
}