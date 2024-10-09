package com.github.bccyben.common.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * async関連の設定
 */
@Configuration
public class AsyncConfig {

    /**
     * s3系非同期処理の設定
     * @return
     */
    @Bean(destroyMethod = "shutdown")
    @Qualifier(value = "s3")
    public Executor getS3Executor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(50);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setQueueCapacity(1000);
        executor.initialize();
        return executor;
    }
}
