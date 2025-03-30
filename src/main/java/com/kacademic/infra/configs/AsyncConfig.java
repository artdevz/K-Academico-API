// package com.kacademic.infra.configs;

// import java.util.concurrent.Executor;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.scheduling.annotation.AsyncConfigurer;
// import org.springframework.scheduling.annotation.EnableAsync;
// import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
// import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

// @Configuration
// @EnableAsync
// public class AsyncConfig implements AsyncConfigurer {
    
//     @Bean("taskExecutor")
//     public Executor getAsyncExecutor() {
        
//         ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//         executor.setCorePoolSize(2);
//         executor.setMaxPoolSize(30);
//         executor.setQueueCapacity(100);
//         executor.setThreadNamePrefix("AsyncThread-");
//         executor.initialize();

//         return executor;
//     }

//     @Bean
//     public DelegatingSecurityContextAsyncTaskExecutor delegatingSecurityContextAsyncTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
//         return new DelegatingSecurityContextAsyncTaskExecutor(taskExecutor);
//     }
    
// }
