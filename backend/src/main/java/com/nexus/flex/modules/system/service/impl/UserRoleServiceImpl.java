package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.UserRole;
import com.nexus.flex.modules.system.mapper.UserRoleMapper;
import com.nexus.flex.modules.system.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户组 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>  implements UserRoleService{

}
