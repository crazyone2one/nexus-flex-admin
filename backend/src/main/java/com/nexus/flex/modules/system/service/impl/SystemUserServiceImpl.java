package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.SystemUser;
import com.nexus.flex.modules.system.mapper.SystemUserMapper;
import com.nexus.flex.modules.system.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {
    private final PasswordEncoder passwordEncoder;

    @Override
    public void addUser(SystemUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        mapper.insert(user);
    }
}
