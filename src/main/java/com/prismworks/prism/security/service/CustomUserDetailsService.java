package com.prismworks.prism.security.service;

import com.prismworks.prism.domain.user.model.Users;
import com.prismworks.prism.domain.user.repository.UserRepository;
import com.prismworks.prism.security.model.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("user not found by id: " + userId));

        if(!user.isActive()) {
            throw new UsernameNotFoundException("user not active id: " + userId);
        }

        return new UserContext(user);
    }
}
