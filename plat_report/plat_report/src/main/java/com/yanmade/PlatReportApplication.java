package com.yanmade;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

@EnableScheduling //定时任务
@SpringBootApplication
@MapperScan("com.yanmade.plat.report.dao")
@ComponentScan("com.yanmade")
public class PlatReportApplication {
    private static final Logger logger = LoggerFactory.getLogger(PlatReportApplication.class);

    public static void main(String[] args) throws FileNotFoundException {
//        String classpath = ResourceUtils.getURL("classpath:").getPath();
//        System.out.println(classpath);
        SpringApplication.run(PlatReportApplication.class);
    }

    //程序启动时创建数据库（热部署原因会调两次）
    static{
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "666666");
        properties.setProperty("characterEncoding", "utf-8");
        properties.setProperty("serverTimezone", "GMT+8");
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost", properties);
             final Statement statement = con.createStatement()) {
            //创建数据库
            statement.execute("create database if not exists plat_report");
            logger.info("创建数据库成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
