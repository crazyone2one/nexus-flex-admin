package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.UserRoleRelation;
import com.nexus.flex.modules.system.mapper.UserRoleRelationMapper;
import com.nexus.flex.modules.system.service.UserRoleRelationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户组关系 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation>  implements UserRoleRelationService{

    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        return queryChain().where(UserRoleRelation::getUserId).eq(userId).list()
                .stream().map(UserRoleRelation::getRoleId).toList();
    }
}
