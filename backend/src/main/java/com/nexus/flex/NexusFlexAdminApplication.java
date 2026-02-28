package com.nexus.flex;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.nexus.flex.modules.**.mapper")
public class NexusFlexAdminApplication {

    static void main(String[] args) {
        SpringApplication application = new SpringApplication(NexusFlexAdminApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

}
