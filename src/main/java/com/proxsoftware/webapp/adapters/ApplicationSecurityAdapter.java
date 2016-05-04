package com.proxsoftware.webapp.adapters;

import com.proxsoftware.webapp.service.AccountServiceIml1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class ApplicationSecurityAdapter extends WebSecurityConfigurerAdapter {


    @Autowired
    private SecurityProperties security;

    @Autowired
    private AccountServiceIml1 userService;

   /* @Value("${app.secret}")
    private String applicationSecret;*/


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/user/activate").permitAll()
                .antMatchers("/user/autologin").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/delete").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/fonts*").permitAll()
//                .antMatchers("/data/get").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
                .and()
                .logout().logoutRequestMatcher(
                new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .and().csrf().disable();
//                .rememberMe().key("CHANGE_ME")
//                .tokenValiditySeconds(31536000);
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
