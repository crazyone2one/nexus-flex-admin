package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.ProjectApplication;
import com.nexus.flex.modules.system.mapper.ProjectApplicationMapper;
import com.nexus.flex.modules.system.service.ProjectApplicationService;
import org.springframework.stereotype.Service;

/**
 * 项目应用 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class ProjectApplicationServiceImpl extends ServiceImpl<ProjectApplicationMapper, ProjectApplication>  implements ProjectApplicationService{

}
