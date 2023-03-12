package hqr.msCreateTenant.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {
	
	@Bean
	public ThreadPoolTaskExecutor getThreadPool() {
		ThreadPoolTaskExecutor tte = new ThreadPoolTaskExecutor();
		tte.setCorePoolSize(10);
		tte.setMaxPoolSize(15);
		tte.setQueueCapacity(9999);
		tte.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		tte.initialize();
		return tte;
	}
}
