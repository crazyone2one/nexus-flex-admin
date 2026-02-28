package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.UserRolePermission;
import com.nexus.flex.modules.system.mapper.UserRolePermissionMapper;
import com.nexus.flex.modules.system.service.UserRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * 用户组权限 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class UserRolePermissionServiceImpl extends ServiceImpl<UserRolePermissionMapper, UserRolePermission>  implements UserRolePermissionService{

}
