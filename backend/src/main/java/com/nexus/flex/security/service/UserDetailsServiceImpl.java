package com.nexus.flex.security.service;

import com.mybatisflex.core.query.QueryChain;
import com.nexus.flex.modules.system.entity.SystemUser;
import com.nexus.flex.modules.system.entity.UserRolePermission;
import com.nexus.flex.modules.system.entity.UserRoleRelation;
import com.nexus.flex.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/2/27, 星期五
 **/
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserCacheService userCacheService;

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return QueryChain.of(SystemUser.class).where(SystemUser::getName).eq(username)
                .oneOpt()
                .map(user -> {
                    List<String> roles = QueryChain.of(UserRoleRelation.class)
                            .where(UserRoleRelation::getUserId).eq(user.getId()).list()
                            .stream().map(UserRoleRelation::getRoleId).toList();
                    List<String> permissions = QueryChain.of(UserRolePermission.class).where(UserRolePermission::getRoleId).in(roles).list().stream()
                            .map(UserRolePermission::getPermissionId).toList();
                    return new CustomUserDetails(user, roles, permissions);
                })
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
    }
}
