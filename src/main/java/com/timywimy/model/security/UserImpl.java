package com.timywimy.model.security;

import com.timywimy.model.common.NamedEntityImpl;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class UserImpl extends NamedEntityImpl implements User {

    private String email;

    private String password;

    private Role role;

    private boolean activated;

    public UserImpl() {

    }

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

    @Override
    public String toString() {
        return String.format("User(%s, activated:%s)", getId(), activated);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof User)) {
            return false;
        }
        UserImpl that = (UserImpl) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(role, that.role) &&
                activated == that.activated &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password, role, activated, super.hashCode());
    }
}
