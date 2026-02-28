package com.nexus.flex.utils;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.EntityConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author : 11's papa
 * @since : 2026/2/25, 星期三
 **/
public class Codegen {
    public static void main(String[] args) {
        // 配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/nexus-flex?characterEncoding=utf-8");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");

        // 创建配置内容，两种风格都可以。
        GlobalConfig globalConfig = createGlobalConfigUseStyle1("user_role_relation");
        // GlobalConfig globalConfig = createGlobalConfigUseStyle2();

        // 通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        // 生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfigUseStyle1(String tableName) {
        // 创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.getJavadocConfig().setAuthor("11's papa");
        // 设置根包
        globalConfig.setSourceDir(System.getProperty("user.dir") + "/backend/src/main/java");
        globalConfig.getPackageConfig().setBasePackage("com.nexusflex.admin.modules.system");

        // globalConfig.getPackageConfig().setEntityPackage("cn.master.horde.model.entity"); // 设置实体类包
        // globalConfig.getPackageConfig().setMapperPackage("cn.master.horde.model.mapper");
        // 设置表前缀和只生成哪些表
        globalConfig.setTablePrefix("tb_");
        globalConfig.setGenerateTable(tableName);

        // 设置生成 entity 并启用 Lombok
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.setEntityWithLombok(true);
        // 设置项目的JDK版本，项目的JDK为14及以上时建议设置该项，小于14则可以不设置
        globalConfig.setEntityJdkVersion(25);
        globalConfig.getEntityConfig().setWithSwagger(true).setSwaggerVersion(EntityConfig.SwaggerVersion.DOC);
        globalConfig.getEntityConfig().setOverwriteEnable(true);
        // 设置生成 mapper
        globalConfig.setMapperGenerateEnable(true);
        globalConfig.setControllerGenerateEnable(true);
        globalConfig.setServiceGenerateEnable(true);
        globalConfig.setServiceImplGenerateEnable(true);
        // 可以单独配置某个列
        ColumnConfig columnConfig = new ColumnConfig();
        columnConfig.setColumnName("create_time");
        columnConfig.setOnInsertValue("now()");
        globalConfig.setColumnConfig(tableName, columnConfig);
        ColumnConfig columnConfig2 = new ColumnConfig();
        columnConfig2.setColumnName("update_time");
        columnConfig2.setOnInsertValue("now()");
        columnConfig2.setOnUpdateValue("now()");
        globalConfig.setColumnConfig(tableName, columnConfig2);
        return globalConfig;
    }
}
