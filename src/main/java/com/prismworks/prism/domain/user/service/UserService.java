package com.prismworks.prism.domain.user.service;

import com.prismworks.prism.domain.user.dto.UserDetailInfo;
import com.prismworks.prism.domain.user.dto.UserInfo;
import com.prismworks.prism.domain.user.dto.command.CreateUserCommand;
import com.prismworks.prism.domain.user.dto.command.UpdateProfileCommand;
import com.prismworks.prism.domain.user.model.UserProfile;
import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
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
    public UserInfo getUser(String userId) {
        Users user = this.findUserById(userId);
        return new UserInfo(user);
    }

    @Transactional(readOnly = true)
    public UserDetailInfo getUserDetail(String userId) {
        Users user = this.findUserById(userId);
        UserProfile userProfile = user.getUserProfile();

        return new UserDetailInfo(user, userProfile);
    }

    @Transactional
    public UserInfo createUser(CreateUserCommand command) {
        Users user = new Users(command);
        Users saveUser = userRepository.saveUser(user);

        return new UserInfo(saveUser);
    }

    @Transactional
    public UserDetailInfo updateUserProfile(UpdateProfileCommand command) {
        Users user = this.findUserById(command.getUserId());
        UserProfile userProfile = user.getUserProfile();
        userProfile.updateProfile(command);

        return new UserDetailInfo(user, userProfile);
    }

    @Transactional
    public UserInfo updateUserPassword(String email, String encodedPassword) {
        Users user = this.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user not found by email: " + email));
        user.updatePassword(encodedPassword);

        return new UserInfo(user);
    }
}
