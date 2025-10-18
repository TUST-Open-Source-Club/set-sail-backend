package com.tustosc.setsail.Enums;

public enum RedisPrefix {

    DELETE_TUTORIAL("delete_tutorial"),
    DELETE_COURSE("delete_course");

    private String prefix;
    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }
    @Override
    public String toString() {
        return prefix;
    }
}
