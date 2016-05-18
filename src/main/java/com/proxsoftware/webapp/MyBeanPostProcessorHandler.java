package com.proxsoftware.webapp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Created by Proxima on 05.05.2016.
 */

public class MyBeanPostProcessorHandler implements BeanPostProcessor {

    @Autowired
    ApplicationContext context;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Environment environment = context.getEnvironment();
        environment.getProperty("");
        System.out.println("<<<<<<<<< FROM BEAN POST PROCESSOR");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
