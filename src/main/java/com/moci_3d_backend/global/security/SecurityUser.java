package com.moci_3d_backend.global.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

public class SecurityUser extends User implements OAuth2User, Principal {
    @Getter
    private long id;
    @Getter
    private String nickname;

    public SecurityUser(
            long id,
            String username,
            String password,
            String nickname,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password != null ? password : "", authorities);
        this.id = id;
        this.nickname = nickname;
    }

    @Override
    public Map<String , Object> getAttributes(){
        return Map.of();
    }

    @Override
    public String getName(){
        return getUsername();
    }
}