package com.device.server;

import com.device.server.config.NettyConfig;
import com.device.server.repository.TelemetryRepository;
import io.micrometer.common.util.internal.logging.InternalLoggerFactory;
import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory;
import io.netty.util.ResourceLeakDetector;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component("context")
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Context implements ApplicationContextAware {

    //region SPRING CONTEXT

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        Context.applicationContext = applicationContext;

        Locale.setDefault(Locale.ENGLISH);

        final NettyConfig nettyConfig = getBean(NettyConfig.class);
        InternalLoggerFactory.setDefaultFactory(Slf4JLoggerFactory.INSTANCE);
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.valueOf(nettyConfig.getLeakDetectionLevel().toUpperCase()));
    }

    public static <T> T getBean(final Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static <T> T getBean(final String beanName, final Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }

    public static Object getBean(final String beanName) {
        return applicationContext.getBean(beanName);
    }

    //endregion SPRING CONTEXT

    public static TelemetryRepository getTelemetryRepository() {
        return getBean("telemetryRepository", TelemetryRepository.class);
    }

}
