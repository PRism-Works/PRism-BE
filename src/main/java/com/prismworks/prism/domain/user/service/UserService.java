package com.prismworks.prism.domain.user.service;

import com.prismworks.prism.domain.user.dto.UserDto;
import com.prismworks.prism.domain.user.model.UserProfile;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.repository.UserProfileRepository;
import com.prismworks.prism.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Transactional(readOnly = true)
    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public Users findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user not found by id : " + userId));
    }

    @Transactional(readOnly = true)
    public Users findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user not found by email: " + email));
    }

    @Transactional(readOnly = true)
    public UserProfile findProfileById(String userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("userProfile not found by id: " + userId));
    }

    @Transactional(readOnly = true)
    public UserDto.UserProfileDetail getUserProfileDetail(String userId) {
        Users user = this.findUserById(userId);
        UserProfile userProfile = user.getUserProfile();

        return UserDto.UserProfileDetail.builder()
                .username(userProfile.getUsername())
                .email(user.getEmail())
                .interestJobs(userProfile.getInterestJobs())
                .skills(userProfile.getSkills())
                .build();
    }

    @Transactional
    public Users createUser(UserDto.CreateInfo dto) {
        Users user = Users.builder()
                .userId(UUID.randomUUID().toString())
                .email(dto.getEmail())
                .password(dto.getEncodedPassword()) // todo: encoding 검증
                .isActive(true)
                .build();

        UserProfile userProfile = UserProfile
                .builder()
                .username(dto.getUsername())
                .build();

        user.setUserProfile(userProfile);
        return userRepository.save(user);
    }

    @Transactional
    public void updateUserProfile(String userId, UserDto.UpdateProfileRequest dto) {
        UserProfile userProfile = this.findProfileById(userId);
        userProfile.updateProfile(dto);
    }
}
