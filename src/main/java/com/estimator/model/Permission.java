package com.estimator.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionID;

    @Column(unique = true, nullable = false)
    private String permissionName;

    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
}