package com.proxsoftware.webapp.adapters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class MvcAdapter extends WebMvcConfigurerAdapter {

    @Bean
    public ViewInterceptorAdapter viewInterceptor() {
        return new ViewInterceptorAdapter();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(viewInterceptor());
    }
}
