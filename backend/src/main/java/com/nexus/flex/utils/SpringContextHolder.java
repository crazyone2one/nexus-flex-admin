package com.nexus.flex.utils;

import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NullMarked;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author : 11's papa
 * @since : 2026/2/28, 星期六
 **/
@Component
public class SpringContextHolder implements ApplicationContextAware {
    private static volatile ApplicationContext applicationContext;

    @Override
    @NullMarked
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static <T> T getBean(Class<T> requiredType) {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext has not been initialized yet");
        }
        return applicationContext.getBean(requiredType);
    }

    public static Object getBean(String beanName) {
        if (applicationContext == null || StringUtils.isBlank(beanName)) {
            return null;
        }
        return applicationContext.getBean(beanName);
    }

    /**
     * 获取 ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicationContext has not been initialized yet");
        }
        return applicationContext;
    }
}
