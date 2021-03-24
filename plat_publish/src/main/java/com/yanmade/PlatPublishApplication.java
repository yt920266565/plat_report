package com.yanmade;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@MapperScan("com.yanmade.plat.publish.dao")
@ComponentScan("com.yanmade")
public class PlatPublishApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatPublishApplication.class);
    }

}
