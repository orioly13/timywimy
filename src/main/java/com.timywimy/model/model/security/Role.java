package com.timywimy.model.model.security;

import com.timywimy.util.StringUtil;

public enum Role {
    UNKNOWN(-1, "unknown"),
    USER(0, "user"),
    ADMIN(1, "admin");

    int code;
    String name;

    Role(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Role fromCode(int code) {
        switch (code) {
            case 0:
                return USER;
            case 1:
                return ADMIN;
            default:
                return UNKNOWN;
        }
    }

    public static Role fromName(String name) {
        if (StringUtil.isEmpty(name)) {
            return UNKNOWN;
        }
        switch (name) {
            case "user":
                return USER;
            case "admin":
                return ADMIN;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return String.format("Role[%s,%s]", this.code, this.name);
    }
}