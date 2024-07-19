package com.lovebugs.auth.domain.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    ROLE_USER("USER"), ROLE_IMPORTANT("IMPORTANT"), ROLE_VIP("VIP"), ROLE_ADMIN("ADMIN");

    private final String role;

    RoleType(String role) {
        this.role = role;
    }
}
