package cn.luckyh.bootdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@Configuration
public class ThreadPoolConfig implements AsyncConfigurer {


    private static final String EXECUTOR_NAME = "asyncExecutorBase";

    /**
     * 核心线程数
     */
    @Value("${base.thread.pool.config.corePoolSize:8}")
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    @Value("${base.thread.pool.config.maxPoolSize:16}")
    private Integer maxPoolSize;
    /**
     * 任务队列的容量
     */
    @Value("${base.thread.pool.config.queueCapacity:500}")
    private Integer queueCapacity;

    /**
     * 线程池的前缀名称
     */
    @Value("${base.thread.pool.config.threadNamePrefix:'Base-Business-Thread-Pool-'}")
    private String threadNamePrefix;
    /**
     * 线程最大空闲时间
     */
    @Value("${base.thread.pool.config.keepAliveSeconds:300}")
    private Integer keepAliveSeconds;

    /**
     * 拒绝策略
     */
    @Value("${base.thread.pool.config.rejectedExecutionHandler:'CallerRunsPolicy'}")
    private String rejectedExecutionHandler;

    @Bean(name = EXECUTOR_NAME)
    @Override
    public ThreadPoolTaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        // 最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        // 阻塞队列容量
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        // 待任务在关机时完成--表明等待所有线程执行完
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 非核心线程的存活时间
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        // 设置拒绝策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(getRejectedExecutionHandler(rejectedExecutionHandler));

        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> {
            log.error("[ThreadPool Exception]：Message [{}], Method [{}]", throwable.getMessage(), method.getName());
            for (Object param : obj) {
                log.error("Parameter value [{}] ", param);
            }
        };
    }

    /**
     * 根据传入的参数获取拒绝策略
     *
     * @param rejectedName 拒绝策略名，比如 CallerRunsPolicy
     * @return RejectedExecutionHandler 实例对象，没有匹配的策略时，默认取 CallerRunsPolicy 实例
     */
    public RejectedExecutionHandler getRejectedExecutionHandler(String rejectedName) {
        Map<String, RejectedExecutionHandler> rejectedExecutionHandlerMap = new HashMap<>();
        rejectedExecutionHandlerMap.put("CallerRunsPolicy", new ThreadPoolExecutor.CallerRunsPolicy());
        rejectedExecutionHandlerMap.put("AbortPolicy", new ThreadPoolExecutor.AbortPolicy());
        rejectedExecutionHandlerMap.put("DiscardPolicy", new ThreadPoolExecutor.DiscardPolicy());
        rejectedExecutionHandlerMap.put("DiscardOldestPolicy", new ThreadPoolExecutor.DiscardOldestPolicy());
        return rejectedExecutionHandlerMap.getOrDefault(rejectedName, new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
