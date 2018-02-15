package com.timywimy.model.model.security;

import com.timywimy.model.model.common.NamedEntity;

public interface User extends NamedEntity {

    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);

    Role getRole();

    void setRole(Role role);

    boolean isActivated();

    void setActivated(boolean activated);

//    boolean isBanned();
//
//    void setBanned();
//    todo implement banned, bannedBy, bannedTill (null is eternity), bannedReason
}
