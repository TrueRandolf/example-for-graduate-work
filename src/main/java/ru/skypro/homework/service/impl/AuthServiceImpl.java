package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entities.AuthEntity;
import ru.skypro.homework.entities.UserEntity;
import ru.skypro.homework.repository.AuthRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthRepository authRepository;


    @Override
    public boolean login(String userName, String password) {
        return authRepository.findByUser_UserName(userName)
                .map(a -> encoder.matches(password, a.getPassword()))
                .orElse(false);

    }

    @Transactional
    @Override
    public boolean register(Register register) {
        if (userRepository.existsByUserName(register.getUsername())) {
            return false;
        }
        UserEntity userEntity = UserEntity.builder()
                .userName(register.getUsername())
                .firstName(register.getFirstName())
                .lastName(register.getLastName())
                .phone(register.getPhone())
                .deletedAt(null)
                .build();
        UserEntity userSaved = userRepository.save(userEntity);
        AuthEntity authEntity = AuthEntity.builder()
                .user(userSaved)
                .password(encoder.encode(register.getPassword()))
                .role(register.getRole() == null ? Role.USER : register.getRole())
                .build();
        authRepository.save(authEntity);
        return true;
    }

}
