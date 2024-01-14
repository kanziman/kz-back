package net.kanzi.kz.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "guest"),
    USER("ROLE_USER", "user"),
    ADMIN("ROLE_ADMIN", "admin");

    private final String key;
    private final String value;
}
