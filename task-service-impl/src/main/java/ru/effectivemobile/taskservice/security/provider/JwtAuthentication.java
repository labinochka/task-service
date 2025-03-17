package ru.effectivemobile.taskservice.security.provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;

import java.util.Collection;

public class JwtAuthentication implements Authentication {

    private CustomUserDetails userDetails;

    private boolean isAuthenticated;

    private final String token;

    public JwtAuthentication(String token) {
        this.token = token;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = (CustomUserDetails) userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userDetails.getPassword();
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        if (userDetails != null) {
            return userDetails.getId();
        } else return null;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return token;
    }
}
