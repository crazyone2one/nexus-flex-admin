package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.Project;
import com.nexus.flex.modules.system.mapper.ProjectMapper;
import com.nexus.flex.modules.system.service.ProjectService;
import org.springframework.stereotype.Service;

/**
 * 项目 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project>  implements ProjectService{

}
