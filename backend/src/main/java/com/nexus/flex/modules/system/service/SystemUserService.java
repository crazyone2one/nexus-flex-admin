package com.nexus.flex.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.nexus.flex.modules.system.entity.SystemUser;

/**
 * 用户 服务层。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
public interface SystemUserService extends IService<SystemUser> {
    void addUser(SystemUser user);
}
