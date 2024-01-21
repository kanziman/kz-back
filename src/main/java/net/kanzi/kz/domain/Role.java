package net.kanzi.kz.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER", "user"),
    MANAGER("ROLE_MANAGER", "manager"),
    ADMIN("ROLE_ADMIN", "admin");

    private final String key;
    private final String value;
}
