package com.prismworks.prism.domain.user.service;

import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public Users createUser(UserDto.Create dto) {
        Users user = Users.builder()
                .userId(UUID.randomUUID().toString())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getEncodedPassword()) // todo: encoding 검증
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Users findById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found by id : " + userId));
    }

    @Transactional(readOnly = true)
    public Optional<Users> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
