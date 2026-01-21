package ru.skypro.homework.security;

import org.springframework.security.core.Authentication;

public interface AccessService {
    boolean isOwner(String username, Authentication authentication);
}
