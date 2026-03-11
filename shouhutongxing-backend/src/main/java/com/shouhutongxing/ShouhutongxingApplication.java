package com.shouhutongxing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 守护童行 - 留守儿童救助平台
 * 主应用类
 *
 * @author washingtonwood
 * @version 1.0.0
 * @since 2025-01-25
 */
@SpringBootApplication
public class ShouhutongxingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShouhutongxingApplication.class, args);
        System.out.println("========================================");
        System.out.println("守护童行 - 留守儿童救助平台启动成功！");
        System.out.println("Swagger 文档地址: http://localhost:8080/api/swagger-ui.html");
        System.out.println("========================================");
    }
}
