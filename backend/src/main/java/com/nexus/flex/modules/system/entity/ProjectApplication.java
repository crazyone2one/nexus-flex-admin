package com.nexus.flex.modules.system.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;

import java.io.Serial;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目应用 实体类。
 *
 * @author 11's papa
 * @since 2026-02-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "项目应用")
@Table("project_application")
public class ProjectApplication implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Schema(description = "ID")
    private String id;

    /**
     * 项目ID
     */
    @Id
    @Schema(description = "项目ID")
    private String projectId;

    /**
     * 配置项
     */
    @Id
    @Schema(description = "配置项")
    private String type;

    /**
     * 配置值
     */
    @Schema(description = "配置值")
    private String typeValue;

}
