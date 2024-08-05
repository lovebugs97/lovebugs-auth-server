package com.lovebugs.auth.domain.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_USER("ROLE_USER"), ROLE_IMPORTANT("ROLE_IMPORTANT"), ROLE_VIP("ROLE_VIP"), ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    RoleType(String role) {
        this.role = role;
    }
}