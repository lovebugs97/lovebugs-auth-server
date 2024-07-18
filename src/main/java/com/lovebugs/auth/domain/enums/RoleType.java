package com.lovebugs.auth.domain.enums;

import lombok.Getter;

@Getter
public enum RoleType {
    USER("ROLE_USER"), IMPORTANT("ROLE_IMPORTANT"), VIP("ROLE_VIP"), ADMIN("ROLE_ADMIN");

    private final String role;

    RoleType(String role) {
        this.role = role;
    }
}
