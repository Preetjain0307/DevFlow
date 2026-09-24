package com.devflow.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private Timestamp createdAt;

    public Role() {}

    public Role(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoleId() { return id; }
    public void setRoleId(int roleId) { this.id = roleId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRoleName() { return name; }
    public void setRoleName(String roleName) { this.name = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
