package com.nexus.flex.modules.system.controller;

import com.mybatisflex.core.paginate.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.nexus.flex.modules.system.entity.ProjectParameter;
import com.nexus.flex.modules.system.service.ProjectParameterService;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

/**
 * 项目级参数 控制层。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@RestController
@Tag(name = "项目级参数接口")
@RequestMapping("/projectParameter")
public class ProjectParameterController {

    @Autowired
    private ProjectParameterService projectParameterService;

    /**
     * 保存项目级参数。
     *
     * @param projectParameter 项目级参数
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    @Operation(description="保存项目级参数")
    public boolean save(@RequestBody @Parameter(description="项目级参数")ProjectParameter projectParameter) {
        return projectParameterService.save(projectParameter);
    }

    /**
     * 根据主键删除项目级参数。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @Operation(description="根据主键删除项目级参数")
    public boolean remove(@PathVariable @Parameter(description="项目级参数主键") String id) {
        return projectParameterService.removeById(id);
    }

    /**
     * 根据主键更新项目级参数。
     *
     * @param projectParameter 项目级参数
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    @Operation(description="根据主键更新项目级参数")
    public boolean update(@RequestBody @Parameter(description="项目级参数主键") ProjectParameter projectParameter) {
        return projectParameterService.updateById(projectParameter);
    }

    /**
     * 查询所有项目级参数。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @Operation(description="查询所有项目级参数")
    public List<ProjectParameter> list() {
        return projectParameterService.list();
    }

    /**
     * 根据主键获取项目级参数。
     *
     * @param id 项目级参数主键
     * @return 项目级参数详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(description="根据主键获取项目级参数")
    public ProjectParameter getInfo(@PathVariable @Parameter(description="项目级参数主键") String id) {
        return projectParameterService.getById(id);
    }

    /**
     * 分页查询项目级参数。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    @Operation(description="分页查询项目级参数")
    public Page<ProjectParameter> page(@Parameter(description="分页信息") Page<ProjectParameter> page) {
        return projectParameterService.page(page);
    }

}
