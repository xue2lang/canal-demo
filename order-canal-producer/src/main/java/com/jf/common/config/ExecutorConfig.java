package com.jf.common.config;

import com.jf.common.constant.KeysConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @package com.nfbank.mq.consumer.config
 *
 * @Description:    线程池
 * @author:         sunwei
 * @CreateDate:     2018/9/25 18:17
 * @Version:        1.0
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

	/** Set the ThreadPoolExecutor's core pool size. */
	@Value("${thread.query.corePoolSize}")
	private int corePoolSize ;
	/** Set the ThreadPoolExecutor's maximum pool size. */
	@Value("${thread.query.maxPoolSize}")
	private int maxPoolSize ;
	/** Set the capacity for the ThreadPoolExecutor's BlockingQueue. */
	@Value("${thread.query.queueCapacity}")
	private int queueCapacity;

	
	@Bean(KeysConstant.ASYNC_EXECUTOR)
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("YZTExecutor-");

		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;
	}
}