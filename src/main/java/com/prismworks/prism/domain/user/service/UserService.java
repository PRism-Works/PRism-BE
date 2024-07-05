package com.prismworks.prism.domain.user.service;

import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.User;
import com.prismworks.prism.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(UserDto.Create dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .userId(UUID.randomUUID().toString())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encodedPassword)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found by id : " + userId));
    }
}
