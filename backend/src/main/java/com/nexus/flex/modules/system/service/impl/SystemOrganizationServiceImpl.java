package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.SystemOrganization;
import com.nexus.flex.modules.system.mapper.SystemOrganizationMapper;
import com.nexus.flex.modules.system.service.SystemOrganizationService;
import org.springframework.stereotype.Service;

/**
 * 组织 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class SystemOrganizationServiceImpl extends ServiceImpl<SystemOrganizationMapper, SystemOrganization>  implements SystemOrganizationService{

}
