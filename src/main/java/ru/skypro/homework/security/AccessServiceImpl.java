package ru.skypro.homework.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;

@Slf4j
@AllArgsConstructor
@Service
public class AccessServiceImpl implements AccessService {

    @Override
    public boolean isOwner(String username, Authentication authentication) {
        return authentication.getName().equals(username) || isAdmin(authentication);
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ADMIN.getRole()));
    }
}
