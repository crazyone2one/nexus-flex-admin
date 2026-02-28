package com.nexus.flex.modules.log.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.nexus.flex.modules.log.entity.OperationLog;
import com.nexus.flex.modules.log.mapper.OperationLogMapper;
import com.nexus.flex.modules.log.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务层实现。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>  implements OperationLogService{

}
