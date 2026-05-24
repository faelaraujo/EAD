package com.ead.authuser.models;

import com.ead.authuser.enums.RoleType;
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
    @Column(nullable = false,  unique = true, length = 30)
    private RoleType role_name;

    @Override
    public String getAuthority() {
        return this.role_name.toString();
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public RoleType getRolename() {
        return role_name;
    }

    public void setRolename(RoleType rolename) {
        this.role_name = rolename;
    }
}
