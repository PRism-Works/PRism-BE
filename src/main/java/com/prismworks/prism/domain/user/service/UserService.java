package com.prismworks.prism.domain.user.service;

import com.prismworks.prism.domain.user.model.UserProfile;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.repository.UserRepository;
import com.prismworks.prism.interfaces.user.dto.UserDto;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Users findUserById(String userId) {
        return userRepository.getUserById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found by id : " + userId));
    }

    @Transactional(readOnly = true)
    public Optional<Users> findUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public UserDto.UserDetail getUserDetail(String userId) {
        Users user = this.findUserById(userId);
        UserProfile userProfile = user.getUserProfile();

        return UserDto.UserDetail.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(userProfile.getUsername())
                .interestJobs(userProfile.getInterestJobs())
                .skills(userProfile.getSkills())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDto.UserProfileDetail getUserProfileDetail(String userId) {
        Users user = this.findUserById(userId);
        UserProfile userProfile = user.getUserProfile();

        return UserDto.UserProfileDetail.builder()
                .userId(user.getUserId())
                .username(userProfile.getUsername())
                .email(user.getEmail())
                .interestJobs(userProfile.getInterestJobs())
                .skills(userProfile.getSkills())
                .introduction(userProfile.getIntroduction())
                .build();
    }

    @Transactional
    public Users createUser(UserDto.CreateInfo dto) {
        String userId = UUID.randomUUID().toString();

        UserProfile userProfile = UserProfile.builder()
            .userId(userId)
            .username(dto.getUsername())
            .build();

        Users user = Users.builder()
                .userId(userId)
                .email(dto.getEmail())
                .password(dto.getEncodedPassword()) // todo: encoding 검증
                .userProfile(userProfile)
                .isActive(true)
                .build();

        return userRepository.saveUser(user);
    }

    @Transactional
    public void updateUserProfile(String userId, UserDto.UpdateProfileRequest dto) {
        Users user = this.findUserById(userId);
        UserProfile userProfile = user.getUserProfile();
        userProfile.updateProfile(dto);
    }

    @Transactional
    public void updateUserPassword(String email, String encodedPassword) {
        Users user = this.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user not found by email: " + email));

        user.updatePassword(encodedPassword);
    }
}
