package com.github.bccyben.appsvr;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = { "com.github.bccyben.common", "com.github.bccyben.appsvr" })
@EnableAsync
@Slf4j
public class AppServerApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication app = new SpringApplication(AppServerApplication.class);
        app.run(args);
    }

}
