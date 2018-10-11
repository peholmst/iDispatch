package net.pkhapps.idispatch.web.ui.common.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * TODO Document me
 */
@Configuration
class UIEventConfiguration {

    @Value("${ui-push.thread-pool.size}")
    private int uiPushThreadPoolSize;

    @Bean
    @UIPushQualifier
    ScheduledExecutorService uiPushThreadPool() {
        return Executors.newScheduledThreadPool(uiPushThreadPoolSize);
    }
}
