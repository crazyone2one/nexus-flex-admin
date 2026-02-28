package com.nexus.flex;

import com.nexus.flex.modules.system.entity.SystemUser;
import com.nexus.flex.modules.system.mapper.SystemUserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootTest
class NexusFlexAdminApplicationTests {
    @Resource
    PasswordEncoder passwordEncoder;
    @Resource
    SystemUserMapper systemUserMapper;
    @Test
    void contextLoads() {
        SystemUser user = SystemUser.builder()
                .id("100001")
                .name("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("123456"))
                .enable(true)
                .language("zh-CN")
                .phone("13800138000")
                .source("LOCAL")
                .createUser("admin")
                .updateUser("admin")
                .build();
        systemUserMapper.insertSelective(user);
    }

}
