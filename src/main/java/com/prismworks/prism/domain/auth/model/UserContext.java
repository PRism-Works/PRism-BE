package com.prismworks.prism.domain.auth.model;

import com.prismworks.prism.domain.user.model.Users;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class UserContext extends User {

    private final String userId;
    private final String email;

    public UserContext(Users user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER"))); //todo: 추후 role 확장
        this.userId = user.getUserId();
        this.email = user.getEmail();
    }

    public UserContext(String userId, String email, GrantedAuthority authority) {
        super(email, "N/A", Collections.singletonList(authority));
        this.userId = userId;
        this.email = email;
    }

    public static UserContext guest() {
        return new UserContext("GUEST", "guest@guest.com", new SimpleGrantedAuthority("ROLE_GUEST"));
    }

}
