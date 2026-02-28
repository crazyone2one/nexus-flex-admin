package com.nexus.flex;

import com.nexus.flex.modules.system.entity.SystemUser;
import com.nexus.flex.modules.system.mapper.SystemUserMapper;
import com.nexus.flex.modules.system.service.impl.SystemUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
@ExtendWith(MockitoExtension.class)
public class SystemUserServiceImplTest {
    @Mock
    private SystemUserMapper systemUserMapper;
    @InjectMocks
    private SystemUserServiceImpl systemUserService;
    private SystemUser testUser;
    @Mock
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    void setUp() {
        // 创建测试用户对象
        testUser = SystemUser.builder()
                .id("100001")
                .name("admin")
                .email("admin@example.com")
                .password("123456")
                .enable(true)
                .language("zh-CN")
                .phone("13800138000")
                .source("LOCAL")
                .createUser("admin")
                .createTime(LocalDateTime.now())
                .build();
    }
    @Test
    void testAddUser_Success() {
        // Given
        String plainPassword = "123456";
        String encodedPassword = "$2a$10$DJFQtaQzzJNuV/FrHLuux.CbbC5Otd8di0OUrAho2M3hMpTSPli7C";
        when(passwordEncoder.encode(plainPassword)).thenReturn(encodedPassword);
        when(systemUserMapper.insert(any(SystemUser.class))).thenReturn(1);

        // When
        systemUserService.addUser(testUser);

        // Then
        verify(passwordEncoder, times(1)).encode(plainPassword);
        assertEquals(encodedPassword, testUser.getPassword());
        verify(systemUserMapper, times(1)).insert(testUser);
    }
}
