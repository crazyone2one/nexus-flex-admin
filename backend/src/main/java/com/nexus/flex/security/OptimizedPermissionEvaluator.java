package com.nexus.flex.security;

import org.jspecify.annotations.NullMarked;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author : 11's papa
 * @since : 2026/2/27, 星期五
 **/
@NullMarked
@Component
public class OptimizedPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (!authentication.isAuthenticated()) {
            return false;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            return false;
        }
        if (principal instanceof CustomUserDetails userDetail) {
            List<String> permissions = userDetail.getPermissions();
            String resource = (String) targetDomainObject;
            String requiredOp = (String) permission;
            String fullPermission = resource + ":" + requiredOp;
            return permissions.stream().anyMatch(p -> Objects.equals(p, fullPermission));
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
