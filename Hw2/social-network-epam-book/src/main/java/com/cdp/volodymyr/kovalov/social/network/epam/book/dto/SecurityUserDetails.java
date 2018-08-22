package com.cdp.volodymyr.kovalov.social.network.epam.book.dto;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.cdp.volodymyr.kovalov.social.network.epam.book.entity.User;

public class SecurityUserDetails extends User implements UserDetails {

    private static final String ROLE_PREFIX = "ROLE_";

    private List<String> userRoles;

    public SecurityUserDetails(User user) {
        super.setUsername(user.getUsername());
        super.setPassword(user.getPassword());
        this.userRoles = user.getRole().stream().map(role -> ROLE_PREFIX + role.getRoleName()).collect(toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roles = StringUtils.collectionToCommaDelimitedString(userRoles);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SecurityUserDetails that = (SecurityUserDetails) o;
        return Objects.equals(userRoles, that.userRoles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userRoles);
    }
}
