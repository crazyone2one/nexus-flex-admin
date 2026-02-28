package com.nexus.flex.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.system.entity.ProjectParameter;
import com.nexus.flex.modules.system.mapper.ProjectParameterMapper;
import com.nexus.flex.modules.system.service.ProjectParameterService;
import org.springframework.stereotype.Service;

/**
 * 项目级参数 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class ProjectParameterServiceImpl extends ServiceImpl<ProjectParameterMapper, ProjectParameter>  implements ProjectParameterService{

}
