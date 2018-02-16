package com.timywimy.model.bo.tasks.converters;

public enum Priority {
    UNKNOWN(-1, "unknown"),
    LOWEST(0, "lowest"),
    LOW(1, "low"),
    MEDIUM(2, "medium"),
    HIGH(3, "high"),
    HIGHEST(4, "highest");

    int code;
    String name;

    Priority(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Priority fromCode(int code) {
        switch (code) {
            case 0:
                return LOWEST;
            case 1:
                return LOW;
            case 2:
                return MEDIUM;
            case 3:
                return HIGH;
            case 4:
                return HIGHEST;
            default:
                return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        return String.format("Role[%s,%s]", this.code, this.name);
    }
}