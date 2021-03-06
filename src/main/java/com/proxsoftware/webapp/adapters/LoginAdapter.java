package com.proxsoftware.webapp.adapters;

import com.proxsoftware.webapp.service.AccountServiceIml1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity
public class LoginAdapter implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    @Autowired
    private AccountServiceIml1 accountService;

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent
                                           event) {
//        accountService.updateLastLogin(event.getAuthentication().getName());
    }


}
