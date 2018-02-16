package com.timywimy.model.security;

import com.timywimy.model.common.NamedEntity;
import com.timywimy.model.security.converters.Role;

public interface User extends NamedEntity {

    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);

    Role getRole();

    void setRole(Role role);

    boolean isActive();

    void setActive(boolean active);

//    boolean isBanned();
//
//    void setBanned();
//    todo implement banned, bannedBy, bannedTill (null is eternity), bannedReason
    //todo user groups (shared tasks and events), custom roles
}
