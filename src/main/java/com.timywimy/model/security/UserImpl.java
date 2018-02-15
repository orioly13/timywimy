package com.timywimy.model.security;

import com.timywimy.model.common.NamedEntityImpl;

import javax.persistence.Entity;

@Entity
public class UserImpl extends NamedEntityImpl implements User {

    private String email;

    private String password;

    private Role role;

    private boolean activated;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    @Override
    public void setActivated(boolean activated) {
        this.activated = activated;
    }

}
