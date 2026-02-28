package com.nexus.flex.security;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nexus.flex.modules.system.entity.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author : 11's papa
 * @since : 2026/2/27, 星期五
 **/
@Getter
@NoArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {
    private SystemUser user;
    // 权限集合 (Roles + Permissions)
    private List<String> authorities;
    private List<String> permissions;

    public CustomUserDetails(SystemUser user, List<String> targetRoles, List<String> targetPermissions) {
        this.user = user;
        if (Objects.nonNull(targetPermissions)) {
            this.permissions = targetPermissions;
        }
        if (Objects.nonNull(targetRoles)) {
            this.authorities = targetRoles;
        }

    }

    public String getUserId() {
        return user.getId();
    }

    @Override
    @NullMarked
    @JsonDeserialize(contentUsing = CustomGrantedAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getEnable();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getEnable();
    }

    @Override
    public boolean isEnabled() {
        return user.getEnable();
    }
}
