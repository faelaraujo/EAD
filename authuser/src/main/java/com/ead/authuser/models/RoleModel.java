package com.ead.authuser.models;

import com.ead.authuser.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.hateoas.aot.ControllerMethodReturnTypeAotProcessor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "TB_ROLES")
public class RoleModel implements Serializable, GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", nullable = false,  unique = true, length = 30)
    private RoleType roleName;

    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.roleName.toString();
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public RoleType getRoleName() {
        return roleName;
    }

    public void setRole_name(RoleType roleName) {
        this.roleName = roleName;
    }
}
