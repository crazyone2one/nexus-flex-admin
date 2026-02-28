package com.nexus.flex.modules.system.service;

import com.mybatisflex.core.service.IService;
import com.nexus.flex.modules.system.entity.UserRoleRelation;

import java.util.List;

/**
 * 用户组关系 服务层。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
public interface UserRoleRelationService extends IService<UserRoleRelation> {
    List<String> getRoleIdsByUserId(String userId);
}
